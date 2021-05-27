package com.ssalabura.meltee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ssalabura.meltee.database.MelteeRealm;
import com.ssalabura.meltee.ui.login.LoginActivity;
import com.ssalabura.meltee.work.SyncWorker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getExtras().getString("username");
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_add_photo, R.id.navigation_friends)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("new_photos", "New Photos", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // I was playing with WorkManager for a bit
        // might be useful in the future

//        WorkManager.getInstance(this).cancelAllWorkByTag("MelteeSync");
//
//        WorkRequest workRequest =
//                new PeriodicWorkRequest.Builder(SyncWorker.class,
//                        15, TimeUnit.MINUTES)
//                        .addTag("MelteeSync")
//                        .build();
//
//        WorkManager.getInstance(this).enqueue(workRequest);

        //test by id
//        String id = "c0afe124-3e61-44c1-bad6-b4615bac22db";
//
//        WorkManager.getInstance(this).getWorkInfoByIdLiveData(UUID.fromString(id)).observe(this, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(WorkInfo workInfo) {
//                Log.d("Meltee", "id-test: " + workInfo.getState());
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_kebab_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sign_out) {
            MelteeRealm.logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("autologin",false);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}