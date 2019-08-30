package de.thm.noah.ruben.kantyapp.notificationes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Date;

import de.thm.noah.ruben.kantyapp.R;


/**
 * Handles all things regarding Notifications
 */
public class NotificationHandler extends BroadcastReceiver {


    private static final String NOTIFICATION_ID = "notification-id";
    private static final String NOTIFICATION = "notification";

    public static final String CHANNEL_ID = "kanty";
    public static final String CHANNEL_DESCRIPTION = "Channel for the KANTYApp";

    /**
     * @param context      Die sendende Aktivität
     * @param notification Die "Notification" die eingeplant werden soll
     */
    public static void scheduleNotification(Context context, Notification notification, Date dateTime) {

        // new Intend saving the information to start a new Notification
        Intent notificationIntent = new Intent(context, NotificationHandler.class);
        notificationIntent.putExtra(NotificationHandler.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationHandler.NOTIFICATION, notification);

        // new PendingIntent to wake this class up
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis =System.currentTimeMillis() - (new Date()).getTime();
//        long futureInMillis = (new Date()).getTime() - dateTime.getTime();
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
        System.out.println("dateTime.getTime() = " + dateTime.getTime());
        System.out.println("Millis till notefication: " + futureInMillis);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC, dateTime.getTime(), pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (3 * 1000), pendingIntent);
    }

    public static Notification getNotification(Context context, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle("Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.cover);
        return builder.build();
    }


    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
