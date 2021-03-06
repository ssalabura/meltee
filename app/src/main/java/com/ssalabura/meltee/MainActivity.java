package com.ssalabura.meltee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ssalabura.meltee.database.MelteeRealm;
import com.ssalabura.meltee.ui.login.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Field;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        setContentView(R.layout.activity_main);
        username = getIntent().getExtras().getString("username");
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

        Context context = this;
        ContextCompat.getMainExecutor(context).execute(() -> MelteeRealm.startListener(context));

        //notification channel
        NotificationChannel channel = new NotificationChannel("new_photos", "New Photos", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
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