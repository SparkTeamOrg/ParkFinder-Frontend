package com.app.parkfinder.foreground


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.app.parkfinder.BuildConfig
import com.app.parkfinder.R
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.view_models.MapViewModel
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
            .setContentTitle("Find Parking Mode On")
            .setContentText("Listening for free parking spots...")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
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

        hubConnection?.on("SendNotification", { message ->

            // Convert the raw `message` to a JSON string
            val messageJson = gson.toJson(message)

            // Parse the JSON string to a list of ParkingSpotDto objects
            val parkingSpotsType = object : TypeToken<List<ParkingSpotDto>>() {}.type
            val parkingSpots: List<ParkingSpotDto> = gson.fromJson(messageJson, parkingSpotsType)

            var freeSpotsAroundUser = 0
            for(spot in parkingSpots) {
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

                val viewRadius = 5 // in kilometers
                val kmValue = 0.006 // 1km in degrees

                //user in range, send notification
                if(userDistanceFromSpot < viewRadius * kmValue * 111000) {
                    freeSpotsAroundUser += 1
                }
            }

            if (freeSpotsAroundUser > 0) {
                if (freeSpotsAroundUser == 1)
                    showNotification("Free parking spots found", "There is $freeSpotsAroundUser free parking spot around you. Check it out!")
                else
                    showNotification("Free parking spots found", "There are $freeSpotsAroundUser free parking spots around you. Check them out!")
            }

        }, List::class.java)

        hubConnection?.start()?.blockingAwait()
    }

    private fun stopForegroundService() {
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
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setFullScreenIntent(null, true)    // Ensure heads-up notification
            .build()

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
    }
}