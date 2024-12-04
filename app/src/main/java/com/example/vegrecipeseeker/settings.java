package com.example.vegrecipeseeker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class settings extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    // Constants for SharedPreferences
    private static final String PREFS_NAME = "VegRecipeSettings";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";
    private static final String CHANNEL_ID = "DailyRecipeNotificationChannel";
    private static final String KEY_FIRST_TIME_NOTIFICATION = "first_time_notification";

    private ActivityResultLauncher<String> notificationPermissionLauncher;
    private SharedPreferences sharedPreferences;
    private Switch notificationSwitch;
    private Switch darkModeSwitch;
    private boolean isFirstTimeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_settings_land);
        } else {
            setContentView(R.layout.activity_settings);
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isFirstTimeNotification = sharedPreferences.getBoolean(KEY_FIRST_TIME_NOTIFICATION, true);

        // Create notification channel
        createNotificationChannel();

        // Initialize permission launcher
        initializeNotificationPermissionLauncher();

        // Set up toolbar
        setupToolbar();

        // Initialize and set up settings controls
        initializeControls();

        // Load saved settings
        loadSavedSettings();

        // Set up listeners
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Daily Recipe Notifications";
            String description = "Notifications for daily recipe suggestions";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initializeControls() {
        notificationSwitch = findViewById(R.id.notification_switch);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);
    }

    private void loadSavedSettings() {
        // Load notifications setting - default to false
        boolean notificationsEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false);
        notificationSwitch.setChecked(notificationsEnabled);

        // Load dark mode setting
        boolean darkModeEnabled = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        darkModeSwitch.setChecked(darkModeEnabled);
    }

    private void initializeNotificationPermissionLauncher() {
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted, enable notifications
                        enableNotifications();
                    } else {
                        // Permission denied, keep switch off
                        notificationSwitch.setChecked(false);
                        Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupListeners() {
        // Notification switch listener
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (isFirstTimeNotification) {
                    // First time enabling notifications, request permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestNotificationPermission();
                    } else {
                        // For older versions, enable directly
                        enableNotifications();
                    }
                } else {
                    // Not first time, check if permission is granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (checkNotificationPermission()) {
                            enableNotifications();
                        } else {
                            requestNotificationPermission();
                        }
                    } else {
                        enableNotifications();
                    }
                }
            } else {
                // Disable notifications
                disableNotifications();
            }
        });

        // Dark mode switch listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveDarkModeSetting(isChecked);
            updateDarkMode(isChecked);
        });
    }

    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void enableNotifications() {
        // Save that it's no longer first time
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_TIME_NOTIFICATION, false);
        editor.putBoolean(KEY_NOTIFICATIONS, true);
        editor.apply();

        // Schedule notifications
        scheduleDaily();
        Toast.makeText(this, "Daily recipe notifications enabled", Toast.LENGTH_SHORT).show();
    }

    private void disableNotifications() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATIONS, false);
        editor.apply();

        cancelDailyNotification();
        Toast.makeText(this, "Daily recipe notifications disabled", Toast.LENGTH_SHORT).show();
    }

    private void scheduleDaily() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DailyRecipeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Set time for notification (e.g., 10:00 AM daily)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // If the time is in the past, add a day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Set repeating alarm
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private void cancelDailyNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DailyRecipeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.cancel(pendingIntent);
    }

    private void saveDarkModeSetting(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_DARK_MODE, enabled);
        editor.apply();
    }

    private void updateDarkMode(boolean enabled) {
        int mode = enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
        String message = enabled ? "Dark mode enabled" : "Dark mode disabled";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        recreate(); // Recreate the activity to apply the theme change
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}