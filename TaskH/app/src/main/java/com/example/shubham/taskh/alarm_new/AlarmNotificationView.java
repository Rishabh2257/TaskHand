package com.example.shubham.taskh.alarm_new;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.shubham.taskh.R;
import com.example.shubham.taskh.utility.AppContext;

public class AlarmNotificationView {

    static final String ALARM_NOTIFICATION = "com.example.shubham.taskh.alarm_new.notification";

    public static void show(String title, String contentText, int notificationId, Uri data, Class destinationClass) {

        Context context = AppContext.getContext();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.pirates_black)
                .setColor(AppContext.getContext().getResources().getColor(R.color.holo_orange_dark) )
                .setContentTitle(title)
                .setContentText(contentText);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(context, AlarmReceiver.class);
        resultIntent.setAction(ALARM_NOTIFICATION);
        resultIntent.setData(data);

        PendingIntent resultPendingIntent = PendingIntent.getBroadcast
                (context,notificationId,resultIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        Notification buildNotification = builder.build();
        buildNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        buildNotification.flags |= Notification.FLAG_NO_CLEAR;

        mNotificationManager.notify(notificationId, buildNotification);

    }

}
