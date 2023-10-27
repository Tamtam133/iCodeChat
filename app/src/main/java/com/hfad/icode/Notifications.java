package com.hfad.icode;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class Notifications {

    Intent notifications_intent;
    PendingIntent notifications_pending_intent;
    NotificationManager notifications_manager;
    NotificationChannel notifications_channel;
    NotificationCompat.Builder notifications_builder;
    Uri notifications_sound;

    ChatActivity activity;

    public void init(){
        notifications_intent = new Intent(activity.getApplicationContext(), ChatActivity.class);
        notifications_pending_intent = PendingIntent.getActivity(activity.getApplicationContext(),0,notifications_intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE);
        notifications_builder = new NotificationCompat.Builder(activity.getApplicationContext());
        notifications_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        notifications_channel = new NotificationChannel("test", "test", NotificationManager.IMPORTANCE_HIGH);
        //notifications_sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + activity.getApplicationContext().getPackageName() + "/" + R.raw.icq);

        notifications_channel.setSound(notifications_sound, audioAttributes);
        notifications_manager = (NotificationManager) activity.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifications_manager.createNotificationChannel(notifications_channel);
    }

    public void send(String title, String text){
        notifications_builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setTicker("Hearty365")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(notifications_pending_intent)
                .setContentInfo("Info")
                .setChannelId("test");

        notifications_manager.notify(1, notifications_builder.build());
    }

    public void setActivity(ChatActivity activity) {
        this.activity = activity;
    }
}
