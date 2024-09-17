package com.example.expensemanager.views.activities;

import static android.content.Context.ACTIVITY_SERVICE;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.expensemanager.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = false;
        String string = intent.getExtras().getString("extra");

        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Music.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
            }
        }
        Intent mIntent = new Intent(context, Music.class);
        if (string.equals("on") && !isRunning) {
            context.startService(mIntent);
            ReminderActivity.activeAlarm = intent.getExtras().getString("active");
            showNotification(context, ReminderActivity.activeAlarm);
        } else if (string.equals("off")) {
            context.stopService(mIntent);
            ReminderActivity.activeAlarm = "";
        }
    }

    private void showNotification(Context context, String activeAlarm) {
        // Create a notification channel (required for Android 8.0 and above)
        createNotificationChannel(context);

        // Create an intent to open the ReminderActivity when the notification is clicked
        Intent intent = new Intent(context, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Reminder")
                .setContentText(activeAlarm)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(123, builder.build());
    }

    private void stopNotification(Context context) {
        // Cancel the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(123);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Reminder Channel";
            String channelDescription = "Channel for reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("notify", channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

