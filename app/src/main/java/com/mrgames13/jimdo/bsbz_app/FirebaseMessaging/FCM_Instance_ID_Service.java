package com.mrgames13.jimdo.bsbz_app.FirebaseMessaging;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCM_Instance_ID_Service extends FirebaseInstanceIdService {

    //Konstanten
    public static final String token_preference_key = "fcm_token";
    public static final String topic_all = "all";

    //Variablen als Objekte
    SharedPreferences prefs;

    //Variablen

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Token in den SharedPreferences speichern
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor e = prefs.edit();
            e.putString(token_preference_key, FirebaseInstanceId.getInstance().getToken());
        e.commit();

        //Topic 'all' abonnieren
        FirebaseMessaging.getInstance().subscribeToTopic(topic_all);

        //Topic '<Token>' abonnieren
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
            FirebaseMessaging.getInstance().subscribeToTopic(tm.getDeviceId());
        }

        //Stoppen
        stopSelf();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //Topic '<Token>' abonnieren
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
            FirebaseMessaging.getInstance().subscribeToTopic(tm.getDeviceId());
        }
        Log.d("BSBZ-App", "Topic abbonniert");
    }
}