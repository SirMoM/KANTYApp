package de.thm.noah.ruben.kantyapp.notifications;

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
     * Plant eine Benachrichtigung zum angegebenen Datum ein.
     *
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

    /**
     * Erstellt eine neue Notifikation
     *
     * @param context der Kontext der Notifikation
     * @param content Die Mitteilungen der Notifikation
     *
     * @return die Notifikation
     */
    public static Notification getNotification(Context context, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle("Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.cover);
        return builder.build();
    }


    /**
     * Empfängt den Intend der vom AlarmManager gesendet wird. Und erstellt eine Notifikation daraus.// TODO auch nach nem restart.
     *
     * @param context der Kontext
     * @param intent der Intend der empfangen wird
     */
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }

    /**
     * Erstellt den Notifications-Kanal für die Anwendung
     *
     * @param context Der Kontext
     */
    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        // Just to be sure
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
