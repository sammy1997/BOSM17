package com.bitspilanidvm.bosm2017.Notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.bitspilanidvm.bosm2017.Activity.Main;

/**
 * Created by vaibhav on 30/8/17.
 */

public class Notifications {
    public static void scheduleNotification(Context context, long delay, int notificationId, String title, String content, int res) {//delay is after how much time(in millis) from current time you want to schedule the notification

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(notificationId))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(res)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, Main.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static void cancelNotification(Context context, long delay, int notificationId, String title, String content, int res){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(notificationId))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(res)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, Main.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        try {
            alarmManager.cancel(pendingIntent);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
