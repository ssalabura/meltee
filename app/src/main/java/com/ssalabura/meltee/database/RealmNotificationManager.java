package com.ssalabura.meltee.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.util.BitmapTools;

import java.util.List;

class RealmNotificationManager {
    static void reactToChanges(Context context) {
        Log.d("Meltee", "RealmNotificationManager reacting to Realm changes.");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        long lastSeen;
        lastSeen = preferences.getLong("lastSeen", 0);
        List<PhotoCard> photoCards = MelteeRealm.getPhotos();
        Log.d("Meltee", "RealmNotificationManager loaded " + photoCards.size() + " photos.");
        if(photoCards.size() > 0) {
            Log.d("Meltee", "lastSeen:  " + lastSeen);
            Log.d("Meltee", "lastPhoto: " + photoCards.get(0).timestamp);
        }
        for(PhotoCard photoCard : photoCards) {
            if(photoCard.timestamp > lastSeen) showNotification(context, photoCard);
        }
        preferences.edit().putLong("lastSeen", photoCards.get(0).timestamp).apply();
    }

    private static void showNotification(Context context, PhotoCard photoCard) {
        Log.d("Meltee", "RealmNotificationManager showing notification for " + photoCard._id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "new_photos")
                .setSmallIcon(R.drawable.meltee_foreground)
                .setContentTitle("New photo from " + photoCard.sender)
                .setContentText(photoCard.message)
                .setLargeIcon(BitmapTools.fromByteArray(photoCard.photo))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0,builder.build());
    }
}
