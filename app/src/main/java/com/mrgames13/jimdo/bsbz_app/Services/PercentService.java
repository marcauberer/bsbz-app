package com.mrgames13.jimdo.bsbz_app.Services;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;

import com.mrgames13.jimdo.bsbz_app.App.LogoActivity;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.NotificationUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PercentService extends Service {

    //Konstanten

    //Variablen als Objekte
    private Resources res;
    private StorageUtils su;
    private AccountUtils au;
    private NotificationUtils nu;

    //Variablen
	private boolean show_notification;
    private Account current_account;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

        //Resourcen initialisieren
        res = getResources();

        //StorageUtils initialsieren
        su = new StorageUtils(PercentService.this, res);

        //AccountUtils initialsieren
        au = new AccountUtils(su);

        //Aktuellen Account laden
        current_account = au.getLastUser();

		//NotificationUtils initialisieren
		nu = new NotificationUtils(getApplicationContext());

		//Prozentanzahl berechnen
		int percent = (int) computePercent();

		//Nachricht in die Statusleiste senden
		sendNotification(percent);

		//Service stoppen
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void sendNotification(final int progress) {
		
		boolean send = su.getBoolean("send_percent_notification", true);
		show_notification = su.getBoolean("show_notification_for_schoolday", true);

		if(send && progress > 0 && progress < 100) {
			//Notification senden
			Intent i = new Intent(this, LogoActivity.class);
			i.putExtra("Confirm", "Today");
			nu.displayProgressMessage(res.getString(R.string.app_name), res.getString(R.string.so_much_schooltime_is_over_) + Integer.toString(progress) + "%", nu.ID_SHOW_TODAY_PROGRESS, progress, i, nu.PRIORITY_MAX);
            su.putBoolean("show_notification_for_schoolday", true);
        } else if(show_notification && send && progress == 100) {
			//Notification senden
			Intent i = new Intent(this, LogoActivity.class);
			i.putExtra("Confirm", "Today");
            nu.displayNotification(res.getString(R.string.app_name), res.getString(R.string.congradulations_schoolday_is_over), nu.ID_SHOW_TODAY_PROGRESS, i, nu.PRIORITY_HIGH, 0, new long[0]);
            su.putBoolean("show_notification_for_schoolday", false);
        } else {
			nu.clearNotification(nu.ID_SHOW_TODAY_PROGRESS);
		}
	}
	
	private long computePercent() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		int weekday = cal.get(Calendar.DAY_OF_WEEK);

        //Daycode herausfinden
        TimeTable tt = su.getTimeTable(current_account.getForm());
        String daycode = "";
        if(tt != null) {
            if(weekday == 2) daycode = tt.getMo();
            if(weekday == 3) daycode = tt.getDi();
            if(weekday == 4) daycode = tt.getMi();
            if(weekday == 5) daycode = tt.getDo();
            if(weekday == 6) daycode = tt.getFr();
            if(weekday == 7 || weekday == 1) return 0;
        } else {
            daycode = "-,-,-,-,-,-,-,-,-,-";
        }

		//Hourcode herausfinden
		int index1 = daycode.indexOf(",", 0);
		int index2 = daycode.indexOf(",", index1 +1);
		int index3 = daycode.indexOf(",", index2 +1);
		int index4 = daycode.indexOf(",", index3 +1);
		int index5 = daycode.indexOf(",", index4 +1);
		int index6 = daycode.indexOf(",", index5 +1);
		int index7 = daycode.indexOf(",", index6 +1);
		int index8 = daycode.indexOf(",", index7 +1);
		int index9 = daycode.indexOf(",", index8 +1);
		String hour1 = daycode.substring(0, index1);
		String hour2 = daycode.substring(index1 +1, index2);
		String hour3 = daycode.substring(index2 +1, index3);
		String hour4 = daycode.substring(index3 +1, index4);
		String hour5 = daycode.substring(index4 +1, index5);
		String hour6 = daycode.substring(index5 +1, index6);
		String hour7 = daycode.substring(index6 +1, index7);
		String hour8 = daycode.substring(index7 +1, index8);
		String hour9 = daycode.substring(index8 +1, index9);
		String hour10 = daycode.substring(index9 +1);

		//Fortschrittsbalken zeichnen
		long start = 0;
		long end = 0;
		long now = 0;

		if(!hour1.equals("-")) {
			if(start == 0) {
				start = 27000000;
			} else {
				end = 29700000;
			}
		} if(!hour2.equals("-")) {
			if(start == 0) {
				start = 29700000;
			} else {
				end = 32400000;
			}
		} if(!hour3.equals("-")) {
			if(start == 0) {
				start = 32400000;
			} else {
				end = 35400000;
			}
		} if(!hour4.equals("-")) {
			if(start == 0) {
				start = 36600000;
			} else {
				end = 39300000;
			}
		} if(!hour5.equals("-")) {
			if(start == 0) {
				start = 39300000;
			} else {
				end = 42000000;
			}
		} if(!hour6.equals("-")) {
			if(start == 0) {
				start = 42300000;
			} else {
				end = 45000000;
			}
		} if(!hour7.equals("-")) {
			if(start == 0) {
				start = 47700000;
			} else {
				end = 50400000;
			}
		} if(!hour8.equals("-")) {
			if(start == 0) {
				start = 50400000;
			} else {
				end = 53400000;
			}
		} if(!hour9.equals("-")) {
			if(start == 0) {
				start = 53400000;
			} else {
				end = 56400000;
			}
		} if(!hour10.equals("-")) {
			if(start == 0) {
				start = 56400000;
			} else {
				end = 59100000;
			}
		}
		
		//Aktuelle Uhrzeit ermitteln
		Date date = new Date(System.currentTimeMillis());
		String zeit = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, Locale.GERMANY).format(date);
		zeit = zeit.substring(11,16);
		if(zeit.length() == 4) zeit = "0" + zeit;
		//Fehlerträchtige Berechnung
		try{
			now = (Integer.parseInt(zeit.substring(0, zeit.indexOf(":"))) * 60 + Integer.parseInt(zeit.substring(zeit.indexOf(":") + 1))) * 60000;
		} catch(Exception e) {}
		
		try {
			long percent = ((now - start) * 100) / (end - start);
			//Wenn Prozentanzahl großer als 100% wäre, auf 100% setzen
			if(percent > 100) {
				percent = 100;
			} else if(percent < 0) {
				percent = 0;
			}
			return percent;
		} catch(Exception e) {}
		return 0;
	}
}