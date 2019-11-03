package my.projects.habittracker.view.broadcast;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import my.projects.habittracker.R;
import my.projects.habittracker.view.main.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_NAME = "HabitTrackerChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = notificationBuilder(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        notificationManager.notify(0, builder.build());
    }

    private NotificationCompat.Builder notificationBuilder(Context context) {
        PendingIntent pendingIntent = buildMainActivityIntent(context);

        return new NotificationCompat.Builder(context, CHANNEL_NAME)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Habit Tracker")
                .setContentText("Have you checked on your habits today?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);
    }

    private PendingIntent buildMainActivityIntent(Context context) {
        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        return pendingIntent;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_NAME, "HabitTracker channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
