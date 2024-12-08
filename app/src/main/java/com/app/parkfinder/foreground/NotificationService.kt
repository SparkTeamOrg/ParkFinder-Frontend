package com.app.parkfinder.foreground


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo

import android.os.Build

import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.app.parkfinder.BuildConfig

import com.app.parkfinder.R
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.ui.activities.NavigationActivity
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.core.Single


enum class Actions {
    START, STOP
}

class NotificationService : Service() {

    private var hubConnection: HubConnection? = null
    private val channelId = "HubNotificationChannel"

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
        val openAppIntent = Intent(this, NavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("FPM On")
            .setContentText("Listening for free parking spots...")
            .setContentIntent(openAppPendingIntent)
            .setOngoing(true)
            .build()

        ServiceCompat.startForeground(
            this,
            notificationId,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING else 0
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
            val parkingSpots = message.mapNotNull { it as? ParkingSpotDto }
            showNotification("New Notification", parkingSpots.size.toString())
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
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopForegroundService()
    }

}
