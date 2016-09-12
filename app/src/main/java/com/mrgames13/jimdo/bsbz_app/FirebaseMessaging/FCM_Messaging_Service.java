package com.mrgames13.jimdo.bsbz_app.FirebaseMessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mrgames13.jimdo.bsbz_app.App.LogInActivity;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.NotificationUtils;

public class FCM_Messaging_Service extends FirebaseMessagingService {
    //Konstanten

    //Variablen als Objekte
    Resources res;
    SharedPreferences prefs;
    NotificationUtils notificationUtils;

    //Variablen


    @Override
    public void onCreate() {
        super.onCreate();
        res = getResources();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        notificationUtils = new NotificationUtils(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            //Command ermitteln
            String command = remoteMessage.getData().get("command");
            if (command.equals("display_notification")) {
                String message_title = remoteMessage.getData().get("title");
                String message_text = remoteMessage.getData().get("message");
                notificationUtils.displayNotification(message_title, message_text, 0, 0, notificationUtils.PRIORITY_HIGH);
            } else if (command.equals("announce_update")) {
                String version = remoteMessage.getData().get("version");
                String message_text = remoteMessage.getData().get("message");
                notificationUtils.displayNotification(res.getString(R.string.update_to_version) + version, message_text, notificationUtils.ID_ANNOUNCE_UPDATE, notificationUtils.MODE_ANNOUNCE_UPDATE, notificationUtils.PRIORITY_NORMAL);
            } else if (command.equals("initiate_sync")) {
                startService(new Intent(this, SyncronisationService.class));
            } else if (command.equals("start_app")) {
                Intent dialogIntent = new Intent(this, LogInActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            } else if (command.equals("clear_notifications")) {
                notificationUtils.clearNotifications();
            }
        } catch (Exception e) {
        }
        stopSelf();
    }
}