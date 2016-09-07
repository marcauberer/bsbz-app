package com.mrgames13.jimdo.bsbz_app.FirebaseMessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mrgames13.jimdo.bsbz_app.App.LogInActivity;
import com.mrgames13.jimdo.bsbz_app.App.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.R;

import java.net.URLDecoder;

public class FCM_Messaging_Service extends FirebaseMessagingService {
    //Konstanten

    //Variablen als Objekte
    Resources res;
    SharedPreferences prefs;

    //Variablen


    @Override
    public void onCreate() {
        super.onCreate();
        res = getResources();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try{
            //Command ermitteln
            String command = remoteMessage.getData().get("command");
            if(command.equals("display_notification")) {
                String message_title = URLDecoder.decode(remoteMessage.getData().get("title"), "UTF-8");
                String message_text = URLDecoder.decode(remoteMessage.getData().get("message"), "UTF-8");
                displayNitification(message_title, message_text, (int)((Math.random()) * 1000000 + 1));
            } else if(command.equals("announce_update")) {
                String version = remoteMessage.getData().get("version");
                String message_text = URLDecoder.decode(remoteMessage.getData().get("message"), "UTF-8");
                displayNitification(res.getString(R.string.update_to_version) + version, message_text, 106);
            } else if(command.equals("initiate_sync")) {
                startService(new Intent(this, SyncronisationService.class));
            } else if(command.equals("start_app")) {
                Intent dialogIntent = new Intent(this, LogInActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        } catch(Exception e) {}
        stopSelf();
    }

    private void displayNitification(String title, String message, int id) {
        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, n);
    }
}