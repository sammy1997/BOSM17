package com.bitspilanidvm.bosm2017.Firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bitspilanidvm.bosm2017.Activity.Main;
import com.bitspilanidvm.bosm2017.R;
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                handleNow();
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }

    }


    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    private void sendNotification(String title, String messageBody) {
/*        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);*/
       /* NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_atheletics)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);*/

       NotificationCompat.Builder notificationBuilder = getBuilder(title, messageBody);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }

    private NotificationCompat.Builder getBuilder(String title, String messageBody){

        int icon = R.drawable.bosmlogolarge;

        if (GLOBAL_DATA.INSTANCE.getSportsImageIconRes().get(title) != null)
            icon = GLOBAL_DATA.INSTANCE.getSportsImageIconRes().get(title);

        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            return new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setSmallIcon(icon)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
    }
}
