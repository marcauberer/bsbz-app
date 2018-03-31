package com.mrgames13.jimdo.bsbz_app.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.App.SplashScreenActivity;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.PercentService;
import com.mrgames13.jimdo.bsbz_app.Services.SyncService;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.NotificationUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

import java.net.URLEncoder;
import java.util.Calendar;

public class BootCompletedReceiver extends BroadcastReceiver {
	
	//Konstanten

    //Variablen als Objekte
    private ConnectivityManager cm;
    private ServerMessagingUtils serverMessagingUtils;
    private Resources res;
    private Context context;
    private StorageUtils su;
    private AccountUtils au;
    private NotificationUtils nu;
    private NotificationManager nm;

    //Variablen
	private String current_version = "";
    private String result;
    private Account current_account;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON") || intent.getAction().equals("com.htc.intent.action.QUICKBOOT_POWERON")) {
            //Kontext initialisieren
            this.context = context;

            //Resourcen initialisieren
            res = context.getResources();

            //StorageUtils initialisieren
            su = new StorageUtils(context, res);
            try { current_version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName; } catch (PackageManager.NameNotFoundException e1) {}

            //AccountUtils initialisieren
            au = new AccountUtils(su);

            //Aktuellen Account laden
            current_account = au.getLastUser();

            //NotificationUtils initialisieren
            nu = new NotificationUtils(context);

            //ServerMessagingUtils initialisieren
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            serverMessagingUtils = new ServerMessagingUtils(cm, context);

            //Alarmmanager aufsetzen
            AlarmManager alarmmanager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent startServiceIntent1 = new Intent(context, SyncService.class);
            PendingIntent startServicePendingIntent1 = PendingIntent.getService(context, 0, startServiceIntent1, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            alarmmanager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Integer.parseInt(su.getString("SyncFreq", "600000")), startServicePendingIntent1);

            //Service starten
            context.startService(new Intent(context, PercentService.class));
			
			//Prozentanzeige in der Statusleiste anzeigen
			if(su.getBoolean("send_percent_notification", true)) {
                //Alarmmanager aufsetzen
                AlarmManager alarmmanager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                Intent startServiceIntent2 = new Intent(context, PercentService.class);
                PendingIntent startServicePendingIntent2 = PendingIntent.getService(context, 0, startServiceIntent2, 0);

                alarmmanager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, startServicePendingIntent2);

                //Service starten
                context.startService(new Intent(context, PercentService.class));
            }

            if(serverMessagingUtils.isInternetAvailable()) checkVersionAndAccountState(current_account.getUsername());
		}
	}

    public void checkVersionAndAccountState(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = username;
                if(username.equals("")) name = res.getString(R.string.guest);
                try {
                    result = serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(username, "UTF-8")+"&command=getserverinfo");
                    //Result auseinandernehmen
                    int index1 = result.indexOf(",");
                    int index2 = result.indexOf(",", index1 +1);
                    int index3 = result.indexOf(",", index2 +1);
                    int index4 = result.indexOf(",", index3 +1);
                    int index5 = result.indexOf(",", index4 +1);
                    String client_name = result.substring(0, index1);
                    String server_state = result.substring(index1 +1, index2);
                    String app_version = result.substring(index2 +1, index3);
                    String adminconsole_version = result.substring(index3 +1, index4);
                    String supporturl = result.substring(index4 +1, index5);
                    String owners = result.substring(index5 +1);
                    //AppVersion prüfen
                    if(!app_version.equals(current_version)) {
                        MainActivity.isUpdateAvailable = true;
                        su.putBoolean("UpdateAvailable", true);
                        su.putString("SupportUrl", supporturl);
                    } else {
                        MainActivity.isUpdateAvailable = false;
                        //Nachricht in die Statusleiste senden
                        Intent i = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        i.putExtra("Confirm", "Classtests");

                        nu.displayNotification(res.getString(R.string.app_name), res.getString(R.string.update_found_tap_to_download_1) + app_version + res.getString(R.string.update_found_tap_to_download_2), nu.ID_UPDATE_FOUND, i, nu.PRIORITY_HIGH, 300, null);

                        //In SharedPreferences eintragen
                        su.putBoolean("UpdateAvailable", false);
                        su.putString("SupportUrl", supporturl);
                    }
                    //Accountstate prüfen
                    result = serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(username, "UTF-8")+"&command=getserverinfo");
                    //Accountdaten auseinandernehmen
                    index1 = result.indexOf(",");
                    index2 = result.indexOf(",", index1 +1);
                    String klasse = result.substring(0, index1);
                    String rights = result.substring(index1 +1, index2);
                    String account_state = result.substring(index2 +1);
                    //Accountstate auswerten
                    if(account_state.equals("2")) {
                        nu.displayNotification(res.getString(R.string.app_name), res.getString(R.string.account_locked), nu.ID_ACCOUNT_LOCKED, new Intent(context, SplashScreenActivity.class), nu.PRIORITY_HIGH, 300, null);
                    } else if(account_state.equals("3")) {
                        su.putBoolean("Sync", false);
                        su.putString("SupportUrl", supporturl);
                    } else {
                        su.putBoolean("Sync", true);
                        su.putString("SupportUrl", supporturl);
                    }
                } catch(Exception e) {}
            }
        }).start();
    }
}