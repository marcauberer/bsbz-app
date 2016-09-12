package com.mrgames13.jimdo.bsbz_app.Tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.mrgames13.jimdo.bsbz_app.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {
    //Konstanten
    public final int ID_ANNOUNCE_UPDATE = 10001;


    //Variablen als Objekte
    private Context context;
    private NotificationManager nm;

    //Variablen

    //Konstruktor
    public NotificationUtils(Context context) {
        this.context = context;
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public void displayNotification(String title, String message, int id) {
        //Notification aufbauen
        Notification n = buildNotification(title, message);
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, n);
    }

    public void displayNotification(String title, String message) {
        //ID zufällig aussuchen
        int id = (int) ((Math.random()) * Integer.MAX_VALUE + 1);
        //Notification aufbauen
        Notification n = buildNotification(title, message);
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, n);
    }

    private Notification buildNotification(String title, String message) {
        return new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    public void clearNotification(int id) {
        nm.cancel(id);
    }

    public void clearNotifications() {
        nm.cancelAll();
    }
}