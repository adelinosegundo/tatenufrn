package com.android_dev.tatenuufrn.managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.activities.EventDetail;

/**
 * Created by adelinosegundo on 11/25/15.
 */
public class TatenUFRNNotificationManager {
    public static void buildEventNotification(Context context, String eventId){
        Intent resultIntent = new Intent(context, EventDetail.class);
        resultIntent.putExtra("event_id", eventId);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.i_tatenu_w100)
                        .setContentTitle("RATE TE EVENT")
                        .setContentText("LOOKS LIKE YOU CAME TO THE EVENT! PLEASE, TELL US HOW MUCH ARE YOU ENJOYING IT.")
                        .setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
