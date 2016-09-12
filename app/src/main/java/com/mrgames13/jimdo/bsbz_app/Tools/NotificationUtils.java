package com.mrgames13.jimdo.bsbz_app.Tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import com.mrgames13.jimdo.bsbz_app.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {
    //Konstanten
        //IDs
        public final int ID_ANNOUNCE_UPDATE = 10001;
        public final int ID_SHOW_TODAY_PROGRESS = 10002;
        public final int ID_SHOW_TODAY_PROGRESS_FINISH = 10003;
        public final int ID_COMP_TIMETABLES = 10004;
        public final int ID_COMP_CLASSTESTS = 10005;
        public final int ID_COMP_HOMEWORKS = 10006;
        public final int ID_COMP_EVENTS = 10007;
        public final int ID_COMP_NEWS = 10008;
        //Modes
        public final int MODE_ANNOUNCE_UPDATE = 101;
        public final int MODE_SHOW_TODAY_PROGRESS_FINISH = 102;
        //Priorities
        public final int PRIORITY_HIGH = 1;
        public final int PRIORITY_NORMAL = 0;
        public final int PRIORITY_LOW = -1;
        //Vibrations
        public final int VIBRATION_SHORT = 300;
        public final int VIBRATION_LONG = 600;
        //Lights
        public final int LIGHT_SHORT = 500;
        public final int LIGHT_LONG = 1000;


    //Variablen als Objekte
    private Context context;
    private NotificationManager nm;
    private Resources res;

    //Variablen

    //Konstruktor
    public NotificationUtils(Context context) {
        this.context = context;
        res = context.getResources();
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public void displayNotification(String title, String message, int id, Intent i, int mode, int priority, int light_lenght, long[] vibration) {
        //ID ermitteln
        if(id == 0) id = (int) ((Math.random()) * Integer.MAX_VALUE + 1);
        //Notification aufbauen
        NotificationCompat.Builder n = buildNotification(title, message);
        if(i != null) {
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            n.setContentIntent(pi);
        }
        if(priority == PRIORITY_HIGH) {
            n.setPriority(NotificationCompat.PRIORITY_HIGH);
            n.setLights(res.getColor(R.color.colorAccent), light_lenght, light_lenght);
            n.setVibrate(vibration);
        } else if(priority == PRIORITY_NORMAL) {
            n.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else if(priority == PRIORITY_LOW) {
            n.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        nm.notify(id, n.build());
    }

    public void displayProgressMessage(String title, String message, int id, int progress, Intent i) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setProgress(100, progress, false)
                .setContentIntent(pi)
                .build();
        nm.notify(id, n);
    }

    public void clearNotification(int id) {
        nm.cancel(id);
    }

    public void clearNotifications() {
        nm.cancelAll();
    }

    private NotificationCompat.Builder buildNotification(String title, String message) {
        return new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(res.getColor(R.color.colorAccent));
    }
}