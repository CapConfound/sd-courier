package ru.simdelivery.sdcourier.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.LauncherActivity;


public class MessagingService extends FirebaseMessagingService {

    public static final String ANDROID_CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";
    private static final String TAG = "tag";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(LauncherActivity.FIREBASE_TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(LauncherActivity.FIREBASE_TAG, "Message data payload: " + remoteMessage.getData());
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (remoteMessage.getNotification() != null) {
            String title = notification.getTitle(), body = notification.getBody();
            Log.d(LauncherActivity.FIREBASE_TAG, "Message Notification title: " + title);
            Log.d(LauncherActivity.FIREBASE_TAG, "Message Notification Body: " + body);
            sendNotification(title, body);
        }
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LauncherActivity.class), 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getClass().getName())
            .setSmallIcon(R.drawable.ic_launcher_24dp)
            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_24dp))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(contentIntent);

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
