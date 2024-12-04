package com.example.vegrecipeseeker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class DailyRecipeReceiver extends BroadcastReceiver {
    private static final String TAG = "DailyRecipeNotification";
    private static final String CHANNEL_ID = "DailyRecipeNotificationChannel";
    private static final int NOTIFICATION_ID = 1;

    // Sample recipe list
    private static final String[] SAMPLE_RECIPES = {
            "Upma",
            "Veg Hakka Noodles",
            "Okra Fry Curry",
            "Peanut Chutney",
            "Paneer Butter Masala",
            "Biryani"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        // Detailed logging
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        Log.d(TAG, "Notification Receiver Triggered");
        Log.d(TAG, "Current Time: " + currentTime);

        // Check if notifications are enabled system-wide
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (!notificationManager.areNotificationsEnabled()) {
            Log.e(TAG, "Notifications are disabled system-wide");

            // Optional: Show a toast to inform user (only works if called from UI thread)
            // You might want to remove this or handle it differently
            try {
                Toast.makeText(context, "Please enable notifications in system settings", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error showing toast", e);
            }
            return;
        }

        try {
            // Select a random recipe
            String dailyRecipe = getRandomRecipe();
            Log.d(TAG, "Selected Recipe: " + dailyRecipe);

            // Create an intent that will open the recipe details when notification is clicked
            Intent recipeIntent = new Intent(context, RecipeDetailActivity.class);
            recipeIntent.putExtra("RECIPE_NAME", dailyRecipe);

            // Use FLAG_IMMUTABLE for Android 12+ compatibility
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    recipeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Attempt to load large icon
            Bitmap largeIcon = null;
            try {
                largeIcon = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.baseline_circle_notifications_24 // Make sure this drawable exists
                );
            } catch (Exception e) {
                Log.e(TAG, "Error loading large icon", e);
            }

            // Create notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.cooking) // Ensure this drawable exists
                    .setLargeIcon(largeIcon)
                    .setContentTitle("Today's Vegetarian Recipe")
                    .setContentText("Check out " + dailyRecipe + " today!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Show the notification with additional error handling
            try {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
                Log.d(TAG, "Notification successfully sent");
            } catch (SecurityException se) {
                Log.e(TAG, "Security Exception while sending notification", se);

                // Additional error handling
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Log.e(TAG, "Ensure POST_NOTIFICATIONS permission is granted");
                }
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error sending notification", e);
            }

        } catch (Exception e) {
            Log.e(TAG, "Unexpected error in notification creation", e);
        }
    }

    private String getRandomRecipe() {
        Random random = new Random();
        int index = random.nextInt(SAMPLE_RECIPES.length);
        return SAMPLE_RECIPES[index];
    }
}