package com.example.skyglass.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.skyglass.MainActivity

class NotificationHelper(private val context: Context) {
    private val channelId = "skyglass_emergency_channel" // Новый уникальный ID

    fun showNotification(title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 1. Создаем канал (для Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Уведомления Skyglass",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Канал для важных оповещений о погоде"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 2. Создаем Intent, чтобы при клике открывалось приложение
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        // 3. Строим само уведомление
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX) // Максимальный приоритет
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent) // Ссылка на приложение
            .setAutoCancel(true) // Удалять после клика
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Звук + Вибрация по дефолту

        // 4. Отправляем (используем уникальный ID по времени)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
