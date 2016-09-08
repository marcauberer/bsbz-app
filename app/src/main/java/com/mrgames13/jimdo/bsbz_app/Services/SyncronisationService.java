package com.mrgames13.jimdo.bsbz_app.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.App.LogInActivity;
import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class SyncronisationService extends Service {
	
	//Konstanten
	private final int NOTIFICATION_ID_TIMETABLES = 101;
	private final int NOTIFICATION_ID_CLASSTESTS = 102;
	private final int NOTIFICATION_ID_HOMEWORKS = 103;
	private final int NOTIFICATION_ID_EVENTS = 104;
	private final int NOTIFICATION_ID_NEWS = 105;

	//Variablen als Objekte
	ConnectivityManager cm;
	ServerMessagingUtils serverMessagingUtils;
	SharedPreferences prefs;
	Context context;
	Handler handler;
	NotificationManager nm;
	Resources res;

	//Variablen
	private String klasse = "";
	private String username = "";
	private boolean update = false;
	private boolean sync;
	private String result;
	boolean show_notifications = true;

	//Interfaces
	public interface onSyncronisationFinishedListener {
		void onSyncFinished();
	}

	@SuppressLint("UseValueOf")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Kontext initialisieren
		context = getApplicationContext();

		//Resourcen initilisieren
		res = getResources();

		//SharedPreferences initialisieren
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		//ServerMessagingUtils initialisieren
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		serverMessagingUtils = new ServerMessagingUtils(cm, context);

		//NotificationManager initialisieren
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		//Handler initialisieren
		handler = new Handler();

		show_notifications = prefs.getBoolean("send_notifications", true);
		klasse = prefs.getString("Klasse", "---");
		username = prefs.getString("Name", res.getString(R.string.guest));
		update = prefs.getBoolean("UpdateAvailable", false);
		sync = prefs.getBoolean("Sync", true);

		if(!sync) Toast.makeText(context, getResources().getString(R.string.account_sync_blocked), Toast.LENGTH_LONG).show();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if(klasse.substring(0, 1).equals("R") && update == false && sync && serverMessagingUtils.isInternetAvailable()) {
					//Stundenplan von Server herunterladen
					downloadTimetable();
					//Klassenarbeiten vom Server herunterladen
					downloadClasstests();
					//Hausaufgaben vom Server herunterladen
					downloadHomeworks();
					//Termine vom Server herunterladen
					downloadEvents();
					//News vom Server herunterladen
					downloadNews();
					//SyncTime eintragen
					EnterLastSyncTime();
					//Fertig
				}
				//Menu Item der MainActivity auf null setzen
				MainActivity.isRunning = false;
				handler.post(new Runnable() {
					@Override
					public void run() {
						try{ MainActivity.syncFinishedListener.onSyncFinished(); } catch(Exception e) {}
					}
				});
				stopSelf();
			}
		});
		t.start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent i) {
		return null;
	}
	
	private void downloadTimetable() {
		try{
			result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=gettimetable&class="+URLEncoder.encode(klasse, "UTF-8"));
			//Stundenplan vergleichen und ggf. eine Nachricht in der Statusleiste anzeigen
			compareTimetables(result);
			//in SharedPreferences eintragen
			SharedPreferences.Editor e = prefs.edit();
				e.putString("Timetables", result);
			e.commit();
			//Daten auseinandernehmen
			int index1 = result.indexOf(";", 0);
			int index2 = result.indexOf(";", index1 +1);
			int index3 = result.indexOf(";", index2 +1);
			int index4 = result.indexOf(";", index3 +1);
			String MO = result.substring(0, index1);
			String DI = result.substring(index1 +1, index2);
			String MI = result.substring(index2 +1, index3);
			String DO = result.substring(index3 +1, index4);
			String FR = result.substring(index4 +1);
			//in SharedPreferences eintragen
			e.putString("Mo", MO);
			e.putString("Di", DI);
			e.putString("Mi", MI);
			e.putString("Do", DO);
			e.putString("Fr", FR);
			e.commit();
		} catch(Exception e){}
	}
	
	private void downloadClasstests() {
		try {
			result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=getclasstests&class="+URLEncoder.encode(klasse, "UTF-8"));
			//Classtests vergleichen und ggf. eine Nachricht in die Statusleiste senden
			compareClasstests(result);
			//Alte Klasstests in den SharedPreferences durch neue ersetzen
			SharedPreferences.Editor e = prefs.edit();
			e.putString("Classtests", result);
			e.commit();
			//Daten auseinandernehmen
			ArrayList<String> arraylist = new ArrayList<String>();
			//In einzelne Klassenarbeiten unterteilen
			while(result.contains(";")) {
				int index = result.indexOf(";");
				arraylist.add(result.substring(0, index));
				result = result.substring(index +1);
			}
			//Klassenarbeiten in die SharedPreferences eintragen
			for(int i = 0; i < 101; i++) {
				String classtest = "-";
				try{ classtest = arraylist.get(i); } catch(Exception e1){}
				e.putString("Classtests_"+String.valueOf(i), classtest);
			}
			e.commit();
		} catch(Exception e) {}
	}
	
	private void downloadHomeworks() {
		try {
			result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=gethomeworks&class="+URLEncoder.encode(klasse, "UTF-8"));
			//Homeworks vergleichen und ggf. eine Nachricht in die Statusleiste senden
			compareHomeworks(result);
			//Alte Homeworks in den SharedPreferences durch neue ersetzen
			SharedPreferences.Editor e = prefs.edit();
			e.putString("Homeworks", result);
			e.commit();
			//Daten auseinandernehmen
			ArrayList<String> arraylist = new ArrayList<String>();
			//In einzelne Hausaufgaben unterteilen
			while(result.contains(";")) {
				int index = result.indexOf(";");
				arraylist.add(result.substring(0, index));
				result = result.substring(index +1);
			}
			//Hausaufgaben in die SharedPreferences eintragen
			for(int i = 0; i < 101; i++) {
				String classtest = "-";
				try{ classtest = arraylist.get(i); } catch(Exception e1){}
				e.putString("Homeworks_"+String.valueOf(i), classtest);
			}
			e.commit();
		} catch(Exception e) {}
	}
	
	private void downloadEvents() {
		try {
			result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=getevents&class="+URLEncoder.encode(klasse, "UTF-8"));
			//Events vergleichen und ggf. eine Nachricht in die Statusleiste senden
			compareEvents(result);
			//Alte Events in den SharedPreferences durch neue ersetzen
			SharedPreferences.Editor e = prefs.edit();
			e.putString("Events", result);
			e.commit();
			//Daten auseinandernehmen
			ArrayList<String> arraylist = new ArrayList<String>();
			//In einzelne Events unterteilen
			while(result.contains(";")) {
				int index = result.indexOf(";");
				arraylist.add(result.substring(0, index));
				result = result.substring(index +1);
			}
			//Events in die SharedPreferences eintragen
			for(int i = 0; i < 101; i++) {
				String classtest = "-";
				try{ classtest = arraylist.get(i); } catch(Exception e1){}
				e.putString("Events_"+String.valueOf(i), classtest);
			}
			e.commit();
		} catch(Exception e) {}
	}
	
	private void downloadNews() {
		try {
			result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=getnews&class="+URLEncoder.encode(klasse, "UTF-8"));
			//News vergleichen und ggf. eine Nachricht in die Statusleiste senden
			compareNews(result);
			//Alte News in den SharedPreferences durch neue ersetzen
			SharedPreferences.Editor e = prefs.edit();
				e.putString("News", result);
			e.commit();
			//Daten auseinandernehmen
			ArrayList<String> arraylist = new ArrayList<String>();
			//In einzelne News unterteilen
			while(result.contains(";")) {
				int index = result.indexOf(";");
				arraylist.add(result.substring(0, index));
				result = result.substring(index +1);
			}
			//News in die SharedPreferences eintragen
			for(int i = 0; i < 101; i++) {
				String news = "-";
				try{ news = arraylist.get(i); } catch(Exception e1){}
				e.putString("News_"+String.valueOf(i), news);
			}
			e.commit();
		} catch(Exception e) {}
	}

	private void compareTimetables(String timetable) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String timetable_old = prefs.getString("Timetables", timetable);

		timetable_old = timetable_old.trim();
		timetable = timetable.trim();
		
		if(!timetable_old.equals(timetable) && show_notifications) {
			//Notification senden
			Intent i = new Intent(this, LogInActivity.class);
			i.putExtra("Confirm", "Timetable");
			PendingIntent pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);

			Notification n = new Notification.Builder(this)
					.setContentTitle(res.getString(R.string.app_name))
					.setContentText(res.getString(R.string.timetable_changings))
					.setSmallIcon(R.mipmap.ic_launcher)
					.setAutoCancel(true)
					.setContentIntent(pi)
					.getNotification();
			nm.notify(NOTIFICATION_ID_TIMETABLES, n);
		}
	}

	private void compareClasstests(String classtests) {
		String classtests_old = prefs.getString("Classtests", classtests);

		classtests_old = classtests_old.trim();
		classtests = classtests.trim();
		
		if(!classtests_old.equals(classtests) && show_notifications) {
			//Notification senden
			Intent i = new Intent(this, LogInActivity.class);
			i.putExtra("Confirm", "Classtests");
			PendingIntent pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);

			Notification n = new Notification.Builder(this)
					.setContentTitle(res.getString(R.string.app_name))
					.setContentText(res.getString(R.string.classtest_changings))
					.setSmallIcon(R.mipmap.ic_launcher)
					.setAutoCancel(true)
					.setContentIntent(pi)
					.getNotification();
			nm.notify(NOTIFICATION_ID_CLASSTESTS, n);
		}
	}

	private void compareHomeworks(String homeworks) {
		String homeworks_old = prefs.getString("Homeworks", homeworks);

		homeworks_old = homeworks_old.trim();
		homeworks = homeworks.trim();
		
		if(!homeworks_old.equals(homeworks) && show_notifications) {
			//Notification senden
			Intent i = new Intent(this, LogInActivity.class);
			i.putExtra("Confirm", "Homework");
			PendingIntent pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);

			Notification n = new Notification.Builder(this)
					.setContentTitle(res.getString(R.string.app_name))
					.setContentText(res.getString(R.string.homework_changings))
					.setSmallIcon(R.mipmap.ic_launcher)
					.setAutoCancel(true)
					.setContentIntent(pi)
					.getNotification();
			nm.notify(NOTIFICATION_ID_HOMEWORKS, n);
		}
	}
	
	private void compareEvents(String events) {
		String events_old = prefs.getString("Events", events);

		events_old = events_old.trim();
		events = events.trim();
		
		if(!events_old.equals(events) && show_notifications) {
			//Notification senden
			Intent i = new Intent(this, LogInActivity.class);
			i.putExtra("Confirm", "Events");
			PendingIntent pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);

			Notification n = new Notification.Builder(this)
					.setContentTitle(res.getString(R.string.app_name))
					.setContentText(res.getString(R.string.event_changings))
					.setSmallIcon(R.mipmap.ic_launcher)
					.setAutoCancel(true)
					.setContentIntent(pi)
					.getNotification();
			nm.notify(NOTIFICATION_ID_EVENTS, n);
		}
	}

	private void compareNews(String news) {
		String news_old = prefs.getString("News", news);

		news_old = news_old.trim();
		news = news.trim();

		if(!news_old.equals(news) && show_notifications) {
			//Notification senden
			Intent i = new Intent(this, LogInActivity.class);
			i.putExtra("Confirm", "News");
			PendingIntent pi = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);

			Notification n = new Notification.Builder(this)
					.setContentTitle(res.getString(R.string.app_name))
					.setContentText(res.getString(R.string.news_changings))
					.setSmallIcon(R.mipmap.ic_launcher)
					.setAutoCancel(true)
					.setContentIntent(pi)
					.getNotification();
			nm.notify(NOTIFICATION_ID_NEWS, n);
		}
	}
	
	private void EnterLastSyncTime() {
		//Letzte Syncronisationszeit eintragen
		//Time in Timestamp umwandeln
		Date date = new Date(System.currentTimeMillis());
		String sync_time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, Locale.GERMANY).format(date).substring(0, 16);
		//In Shared Preferences eintragen
		SharedPreferences.Editor e = prefs.edit();
			e.putString("SyncTime", sync_time);
		e.commit();
	}
}