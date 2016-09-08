package com.mrgames13.jimdo.bsbz_app.App;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.PercentService;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;

import java.net.URLEncoder;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class BootCompletedReceiver extends BroadcastReceiver {
	
	//Konstanten

    //Variablen als Objekte
    ConnectivityManager cm;
    ServerMessagingUtils serverMessagingUtils;
    SharedPreferences prefs;
    Resources res;
    Context context;
    NotificationManager nm;

    //Variablen
	private String CURRENTVERSION;
	private final String androidversion = android.os.Build.VERSION.RELEASE;
    private String result;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //Kontext initialisieren
            this.context = context;

            //Resourcen initialisieren
            res = context.getResources();

			//SharedPreferences initialisieren
            prefs = PreferenceManager.getDefaultSharedPreferences(context);

            //ServerMessagingUtils initialisieren
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            serverMessagingUtils = new ServerMessagingUtils(cm, context);

            //SyncFreq herausfinden
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			String syncfreq = prefs.getString("SyncFreq", "60000");
			
			//Alarmmanager für Hintergrundprozess aufsetzen
			AlarmManager alarmmanager_background_process = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			Intent startServiceIntent1 = new Intent(context, SyncronisationService.class);
			PendingIntent startServicePendingIntent1 = PendingIntent.getService(context,0,startServiceIntent1, 0);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			
			alarmmanager_background_process.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Integer.parseInt(syncfreq), startServicePendingIntent1);
			
			//Prozentanzeige in der Statusleiste anzeigen
			boolean percent = prefs.getBoolean("send_percent_notification", false);
			if(percent == true) {
				//Alarmmanager aufsetzen
				AlarmManager alarmmanager_percent_display = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				
				Intent startServiceIntent2 = new Intent(context, PercentService.class);
				PendingIntent startServicePendingIntent2 = PendingIntent.getService(context,0,startServiceIntent2, 0);

                //Alle 30 Sekunden ausführen
				alarmmanager_percent_display.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 30000, startServicePendingIntent2);
            }

            if(serverMessagingUtils.isInternetAvailable()) {
                //Version und Serverstatus prüfen und ggf. Nachrichten in die Statusleiste senden
                String username = prefs.getString("Name", res.getString(R.string.guest));
                checkVersionAndAccountState(username);
            }
		}
	}
	
	public void checkVersion(final Context context) {
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			CURRENTVERSION = pinfo.versionName;
		} catch (NameNotFoundException e) {}

		new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Nutzernamen ermitteln
                    String username = prefs.getString("Name", res.getString(R.string.guest));
                    //Anfrage an Server senden
                    result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=getserverinfo");
                    //Result auseinandernehmen
                    int index1 = result.indexOf(",");
                    int index2 = result.indexOf(",", index1 +1);
                    int index3 = result.indexOf(",", index2 +1);
                    int index4 = result.indexOf(",", index3 +1);
                    String client_name = result.substring(0, index1);
                    String server_state = result.substring(index1 +1, index2);
                    final String app_version = result.substring(index2 +1, index3);
                    String adminconsole_version = result.substring(index3 +1, index4);
                    String owners = result.substring(index4 +1);
                    //Anfrage bearbeiten
                    if(!app_version.equals(CURRENTVERSION)) {
                        //In SharedPreferences eintragen
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor e = prefs.edit();
                        e.putBoolean("UpdateAvailable", true);
                        e.commit();
                        //Notification anzeigen
                        Intent i = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        i.putExtra("Confirm", "Classtests");
                        PendingIntent pi = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);

                        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification n = new Notification.Builder(context)
                                .setContentTitle(res.getString(R.string.app_name))
                                .setContentText(res.getString(R.string.update_available))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .setContentIntent(pi)
                                .getNotification();
                        nm.notify(6, n);
                    } else {
                        //In SharedPreferences eintragen
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor e = prefs.edit();
                        e.putBoolean("UpdateAvailable", false);
                        e.commit();
                    }
                } catch(Exception e) {}
            }
        }).start();
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
                    String client_name = result.substring(0, index1);
                    String server_state = result.substring(index1 +1, index2);
                    final String app_version = result.substring(index2 +1, index3);
                    String adminconsole_version = result.substring(index3 +1, index4);
                    String owners = result.substring(index4 +1);
                    //AppVersion prüfen
                    if(!app_version.equals(CURRENTVERSION)) {
                        MainActivity.isUpdateAvailable = true;

                        //In SharedPreferences eintragen
                        SharedPreferences.Editor e = prefs.edit();
                            e.putBoolean("UpdateAvailable", true);
                        e.commit();
                    } else {
                        MainActivity.isUpdateAvailable = false;
                        //Nachricht in die Statusleiste senden
                        Intent i = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                        i.putExtra("Confirm", "Classtests");
                        PendingIntent pi = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), i, 0);

                        Notification n = new Notification.Builder(context)
                                .setContentTitle(res.getString(R.string.app_name))
                                .setContentText(res.getString(R.string.update_found_tap_to_download_1) + app_version + res.getString(R.string.update_found_tap_to_download_2))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .setContentIntent(pi)
                                .getNotification();
                        nm.notify(6, n);
                        //In SharedPreferences eintragen
                        SharedPreferences.Editor e = prefs.edit();
                            e.putBoolean("UpdateAvailable", false);
                        e.commit();
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
                        Notification n = new Notification.Builder(context)
                                .setContentTitle(res.getString(R.string.app_name))
                                .setContentText(res.getString(R.string.account_locked))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .getNotification();
                        nm.notify(7, n);
                    } else if(account_state.equals("3")) {
                        SharedPreferences.Editor e = prefs.edit();
                            e.putBoolean("Sync", false);
                        e.commit();
                    } else {
                        SharedPreferences.Editor e = prefs.edit();
                            e.putBoolean("Sync", true);
                        e.commit();
                    }
                } catch(Exception e) {}
            }
        }).start();
    }
}