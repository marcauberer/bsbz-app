package com.mrgames13.jimdo.bsbz_app.FirebaseMessaging;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

public class FCM_Instance_ID_Service extends FirebaseInstanceIdService {

    //Konstanten
    public static final String token_preference_key = "fcm_token";
    public static final String topic_all = "all";

    //Variablen als Objekte
    private StorageUtils su;

    //Variablen

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Token in den SharedPreferences speichern
        su = new StorageUtils(this, getResources());
        su.putString(token_preference_key, FirebaseInstanceId.getInstance().getToken());

        //Topic 'all' abonnieren
        FirebaseMessaging.getInstance().subscribeToTopic(topic_all);

        //Topic '<Token>' abonnieren
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
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
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            FirebaseMessaging.getInstance().subscribeToTopic(tm.getDeviceId());
        }
    }
}