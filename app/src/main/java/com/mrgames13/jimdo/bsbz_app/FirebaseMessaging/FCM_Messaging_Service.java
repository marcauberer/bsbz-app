package com.mrgames13.jimdo.bsbz_app.FirebaseMessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
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
    NotificationUtils nu;

    //Variablen


    @Override
    public void onCreate() {
        super.onCreate();
        res = getResources();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        nu = new NotificationUtils(this);
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
                nu.displayNotification(message_title, message_text, 0, null, 0, nu.PRIORITY_HIGH, nu.LIGHT_LONG, new long[0]);
            } else if (command.equals("announce_update")) {
                String version = remoteMessage.getData().get("version");
                String message_text = remoteMessage.getData().get("message");
                Intent i = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName())));
                nu.displayNotification(res.getString(R.string.update_to_version) + version, message_text, nu.ID_ANNOUNCE_UPDATE, i, nu.MODE_ANNOUNCE_UPDATE, nu.PRIORITY_NORMAL, 0, new long[0]);
            } else if (command.equals("initiate_sync")) {
                startService(new Intent(this, SyncronisationService.class));
            } else if (command.equals("start_app")) {
                Intent dialogIntent = new Intent(this, LogInActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            } else if (command.equals("clear_notifications")) {
                nu.clearNotifications();
            }
        } catch (Exception e) {
        }
        stopSelf();
    }
}