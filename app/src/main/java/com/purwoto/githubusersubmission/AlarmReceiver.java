package com.purwoto.githubusersubmission;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        showAlarmNotification(context);
    }

    public void setAlarm(Context context){
        Calendar now = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, i, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void stopAlarm(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public boolean checkAlarm(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_NO_CREATE);
        boolean alarmUp = (pendingIntent != null);

        if (alarmUp)
        {
            Log.d("myTag", "Alarm is already active");
            return true;
        }else{
            return false;
        }
    }

    private void showAlarmNotification(Context context){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_github)
                .setContentTitle("ComeBack To GITHUB!")
                .setContentText("We have new news for you in your github page")
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(101, notification);
        }
    }
}
