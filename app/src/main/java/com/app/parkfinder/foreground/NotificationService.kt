package com.app.parkfinder.foreground


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo

import android.os.Build

import android.os.IBinder
import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.app.parkfinder.BuildConfig

import com.app.parkfinder.R
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.activities.NavigationActivity
import com.google.gson.JsonParser
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.core.Single
import org.osmdroid.util.GeoPoint


enum class Actions {
    START, STOP
}

class NotificationService : Service() {

    private var hubConnection: HubConnection? = null
    private val channelId = "HubNotificationChannel"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> startForegroundService()
            Actions.STOP.toString() -> stopForegroundService()
        }
        return START_STICKY
    }

    @SuppressLint("InlinedApi")
    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("FPM On")
            .setContentText("Listening for free parking spots...")
            .setOngoing(true)
            .build()

        ServiceCompat.startForeground(
            this,
            notificationId,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
        )

        // Initialize and start the HubConnection
        hubConnection = HubConnectionBuilder.create(BuildConfig.BACKEND_URL + "notificationHub")
            .withTransport(com.microsoft.signalr.TransportEnum.LONG_POLLING)
            .withAccessTokenProvider(Single.defer {
                Single.just((AppPreferences.accessToken) ?: "")
            })
            .build()
        Log.d("Serviceee","Connection with hub started")
        hubConnection?.on("SendNotification", { message ->
            Log.d("Serviceee","Received: ${message}")

            // Convert the raw `message` to a JSON string
            val messageJson = gson.toJson(message)

            // Parse the JSON string to a list of ParkingSpotDto objects
            val parkingSpotsType = object : TypeToken<List<ParkingSpotDto>>() {}.type
            val parkingSpots: List<ParkingSpotDto> = gson.fromJson(messageJson, parkingSpotsType)

            for(spot in parkingSpots)
            {
                //convert to polygon
                val jsonObject = JsonParser.parseString(spot.polygonGeoJson).asJsonObject
                val coordinatesArray = jsonObject.getAsJsonObject("geometry")
                    .getAsJsonArray("coordinates")
                    .get(0) // gets the first one in the file

                val geoPoints = mutableListOf<GeoPoint>()
                for (coordinate in coordinatesArray.asJsonArray) {
                    val lng = coordinate.asJsonArray[0].asDouble
                    val lat = coordinate.asJsonArray[1].asDouble
                    geoPoints.add(GeoPoint(lat, lng))
                }

                val centroid = MapViewModel.calculateCentroid(geoPoints)
                val userDistanceFromSpot = centroid.distanceToAsDouble(userLocation)

                val radius = 0.03 // 5km in degrees

                //user in range, send notification
                if(userDistanceFromSpot < radius)
                    showNotification("New Notification", "Parking spot ${spot.id} is free")
                else
                    Log.d("Serviceee","Spot is not in user range")
            }
        }, List::class.java)

        hubConnection?.start()?.blockingAwait()
    }

    private fun stopForegroundService() {
        Log.d("Serviceee","Stopping foreground service")
        hubConnection?.stop()?.blockingAwait()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher_round) // Use your app's icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        Log.d("Serviceee","Creating notification $message")
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(++notificationId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Service Channel", NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

    }

    companion object {
        var notificationId = 1
        var userLocation: GeoPoint? = null
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopForegroundService()
        Log.d("Serviceee","Stopping foreground service")
    }

}
