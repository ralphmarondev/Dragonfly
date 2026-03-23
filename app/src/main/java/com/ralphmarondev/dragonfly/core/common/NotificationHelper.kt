package com.ralphmarondev.dragonfly.core.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ralphmarondev.dragonfly.R

@SuppressLint("MissingPermission")
object NotificationHelper {

    private const val CHANNEL_ID = "dragonfly_notification_channel"
    private const val NOTIFICATION_ID = 2003

    private fun hasNotificationPermission(context: Context): Boolean {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED)
    }

    fun createChannel(
        context: Context,
        channelId: String = CHANNEL_ID,
        name: String = "Dragonfly Notification",
        description: String = "Notifications for Dragonfly Application.",
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun sendNotification(
        context: Context,
        title: String,
        content: String,
        id: Int = NOTIFICATION_ID,
        iconRes: Int = R.drawable.ic_launcher_foreground,
        style: NotificationCompat.Style? = null,
        pendingIntent: PendingIntent? = null,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        autoCancel: Boolean = false
    ) {
        if (!hasNotificationPermission(context)) return

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(priority)
            .setAutoCancel(autoCancel)

        style?.let { builder.setStyle(it) }
        pendingIntent?.let { builder.setContentIntent(it) }

        NotificationManagerCompat.from(context).notify(id, builder.build())
    }
}