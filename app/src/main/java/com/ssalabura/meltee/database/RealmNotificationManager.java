package com.ssalabura.meltee.database;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.ui.login.LoginActivity;
import com.ssalabura.meltee.util.BitmapTools;

import java.util.Collections;
import java.util.List;

class RealmNotificationManager {
    static void reactToChanges(Context context) {
        Log.d("Meltee", "RealmNotificationManager reacting to Realm changes.");
//        showNotificationDebug(context, String.valueOf(System.currentTimeMillis()));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        long lastSeen = preferences.getLong("lastSeen", 0);
        List<PhotoCard> photoCards = MelteeRealm.getPhotos();
        Log.d("Meltee", "RealmNotificationManager loaded " + photoCards.size() + " photos.");

        if(photoCards.size() > 0) {
            Log.d("Meltee", "lastSeen:  " + lastSeen);
            Log.d("Meltee", "lastPhoto: " + photoCards.get(0).timestamp);
            preferences.edit().putLong("lastSeen", photoCards.get(0).timestamp).apply();
        }
        Collections.reverse(photoCards);
        for(PhotoCard photoCard : photoCards) {
            if(photoCard.timestamp > lastSeen) showNotification(context, photoCard);
        }
    }

    private static void showNotification(Context context, PhotoCard photoCard) {
        Log.d("Meltee", "RealmNotificationManager showing notification for " + photoCard._id);

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "new_photos")
                .setSmallIcon(R.drawable.meltee_foreground)
                .setContentTitle(context.getString(R.string.new_photo) + photoCard.sender)
                .setContentText(photoCard.message)
                .setLargeIcon(BitmapTools.fromByteArray(photoCard.photo))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int)photoCard.timestamp,builder.build());
    }

    private static void showNotificationDebug(Context context, String text) {
        Log.d("Meltee", "RealmNotificationManager showing debug notification: " + text);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "new_photos")
                .setSmallIcon(R.drawable.meltee_foreground)
                .setContentTitle("Debug notification")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0,builder.build());
    }
}
