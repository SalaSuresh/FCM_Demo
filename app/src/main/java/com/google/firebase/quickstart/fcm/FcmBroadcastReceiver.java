package com.google.firebase.quickstart.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.games4playstore.aroundyou.R;

import java.net.URL;

/**
 * Created by Dumadu on 29-Dec-17.
 */

public class FcmBroadcastReceiver extends BroadcastReceiver {
    Bitmap bitmap = null;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context, intent.getExtras().getString("message"), intent.getExtras().getString("image_url"));
    }

    private void sendNotification(Context context, String messageBody, String imageUrl) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder =
                new NotificationCompat.Builder(context, "fcm_default_channel")
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        if (!TextUtils.isEmpty(imageUrl)) {
            getImageBitmap(imageUrl);
        }

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    private Bitmap getImageBitmap(final String shareImageUrl) {
        if (shareImageUrl == null) return null;

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    bitmap = BitmapFactory.decodeStream((new URL(shareImageUrl)).openConnection().getInputStream());

                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmap = null;

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }
            }
        }).start();
        return bitmap;
    }
}
