package com.mrgames13.jimdo.bsbz_app.Tools;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import com.mrgames13.jimdo.bsbz_app.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {
    //Konstanten
        //IDs
        public final int ID_ANNOUNCE_UPDATE = 10001;
        //Modes
        public final int MODE_ANNOUNCE_UPDATE = 101;
        //Priorities
        public final int PRIORITY_HIGH = 1;
        public final int PRIORITY_NORMAL = 0;
        public final int PRIORITY_LOW = -1;
        //Vibrations
        public final int VIBRATION_SHORT = 300;
        public final int VIBRATION_LONG = 600;


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

    public void displayNotification(String title, String message, int id, int mode, int priority) {
        //ID ermitteln
        if(id == 0) id = (int) ((Math.random()) * Integer.MAX_VALUE + 1);
        //Notification aufbauen
        NotificationCompat.Builder n = buildNotification(title, message);
        if(mode == MODE_ANNOUNCE_UPDATE) {

        }
        if(priority == PRIORITY_HIGH) {
            n.setPriority(NotificationCompat.PRIORITY_HIGH);
        } else if(priority == PRIORITY_NORMAL) {
            n.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else if(priority == PRIORITY_LOW) {
            n.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        nm.notify(id, n.build());
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