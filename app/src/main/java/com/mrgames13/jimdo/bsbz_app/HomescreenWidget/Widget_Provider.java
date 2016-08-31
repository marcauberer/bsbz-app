package com.mrgames13.jimdo.bsbz_app.HomescreenWidget;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.mrgames13.jimdo.bsbz_app.App.LogInActivity;
import com.mrgames13.jimdo.bsbz_app.R;

public class Widget_Provider extends AppWidgetProvider {
	
	//Konstanten
	public static boolean RUN_SERVICE;
	String color = "#ea690c";
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
		setColor(context);
		updateProgress(context);
		
		//Service starten
		RUN_SERVICE = true;
		Intent i = new Intent(context, Widget_Update_Service.class);
		context.startService(i);
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		
		//Service stoppen
		RUN_SERVICE = false;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(RUN_SERVICE == false) {
			//Service starten
			RUN_SERVICE = true;
			Intent i = new Intent(context, Widget_Update_Service.class);
			context.startService(i);
		}
		
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		setColor(context);
		
		updateProgress(context);
		
		if(intent.hasExtra("Update")) {
			updateWidget(context, remoteView);
		} else if(intent.hasExtra("setColor")) {
			setColor(context);
		} else if(intent.hasExtra("UpdateProgress")) {
			updateProgress(context);
		} else if(intent.hasExtra("UpdateAll")) {
			setColor(context);
			updateProgress(context);
		}
		super.onReceive(context, intent);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		updateWidget(context, remoteView);
	}
	
	public void updateWidget(Context context, RemoteViews remoteView) {
		Intent intent = new Intent(context, LogInActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteView.setOnClickPendingIntent(R.id.app_oeffnen, pendingIntent);
		
		ComponentName widgetComponent = new ComponentName(context, Widget_Provider.class);
		AppWidgetManager.getInstance(context).updateAppWidget(widgetComponent, remoteView);
	}
	
	private void setColor(Context context) {
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String layout = prefs.getString("Layout", "BSBZ Layout (Orange)");
		if(layout.equals("0")) {
			color = "ea690c";
		} else if(layout.equals("1")) {
			color = "000000";
		} else if(layout.equals("2")) {
			color = "3ded25";
		} else if(layout.equals("3")) {
			color = "ff0000";
		} else if(layout.equals("4")) {
			color = "0000ff";
		} else if(layout.equals("5")) {
			color = "00007f";
		}
		//Farbe setzen
		int color1 = Color.parseColor("#"+color);
		remoteView.setInt(R.id.widget, "setBackgroundColor", color1);
		updateWidget(context, remoteView);
	}
	
	private void updateProgress(Context context) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		
		String weekString = "";
		
		if(weekday == 2) {
			weekString = "Mo";
		} else if (weekday == 3) {
			weekString = "Di";
		} else if (weekday == 4) {
			weekString = "Mi";
		} else if (weekday == 5) {
			weekString = "Do";
		} else if (weekday == 6) {
			weekString = "Fr";
		} else if (weekday == 7) {
			weekString = "Mo";
		} else if (weekday == 1) {
			weekString = "Mo";
		}
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Daycode herausfinden
		String daycode= prefs.getString(weekString, "00000,00000,00000,00000,00000,00000,00000,00000,00000,00000").replace(",", "");
		
		//Hourcode herausfinden
		String hourcode1 = daycode.substring(0,5).replace("0", "");
		String hourcode2 = daycode.substring(5,10).replace("0", "");
		String hourcode3 = daycode.substring(10,15).replace("0", "");
		String hourcode4 = daycode.substring(15,20).replace("0", "");
		String hourcode5 = daycode.substring(20,25).replace("0", "");
		String hourcode6 = daycode.substring(25,30).replace("0", "");
		String hourcode7 = daycode.substring(30,35).replace("0", "");
		String hourcode8 = daycode.substring(35,40).replace("0", "");
		String hourcode9 = daycode.substring(40,45).replace("0", "");
		String hourcode10 = daycode.substring(45,50).replace("0", "");
		
		if(hourcode1.equals("")) {
			hourcode1 = "-";
		}
		if(hourcode2.equals("")) {
			hourcode2 = "-";
		}
		if(hourcode3.equals("")) {
			hourcode3 = "-";
		}
		if(hourcode4.equals("")) {
			hourcode4 = "-";
		}
		if(hourcode5.equals("")) {
			hourcode5 = "-";
		}
		if(hourcode6.equals("")) {
			hourcode6 = "-";
		}
		if(hourcode7.equals("")) {
			hourcode7 = "-";
		}
		if(hourcode8.equals("")) {
			hourcode8 = "-";
		}
		if(hourcode9.equals("")) {
			hourcode9 = "-";
		}
		if(hourcode10.equals("")) {
			hourcode10 = "-";
		}
		
		//Fortschrittsbalken zeichnen
		long start = 0;
		long end = 0;
		long now = 0;
		if(!hourcode1.equals("-")) {
			if(start == 0) {
				start = 27000000;
			} else {
				end = 29700000;
			}
		} if(!hourcode2.equals("-")) {
			if(start == 0) {
				start = 29700000;
			} else {
				end = 32400000;
			}
		} if(!hourcode3.equals("-")) {
			if(start == 0) {
				start = 32400000;
			} else {
				end = 35400000;
			}
		} if(!hourcode4.equals("-")) {
			if(start == 0) {
				start = 36600000;
			} else {
				end = 39300000;
			}
		} if(!hourcode5.equals("-")) {
			if(start == 0) {
				start = 39300000;
			} else {
				end = 42000000;
			}
		} if(!hourcode6.equals("-")) {
			if(start == 0) {
				start = 42300000;
			} else {
				end = 45000000;
			}
		} if(!hourcode7.equals("-")) {
			if(start == 0) {
				start = 47700000;
			} else {
				end = 50400000;
			}
		} if(!hourcode8.equals("-")) {
			if(start == 0) {
				start = 50400000;
			} else {
				end = 53400000;
			}
		} if(!hourcode9.equals("-")) {
			if(start == 0) {
				start = 53400000;
			} else {
				end = 56400000;
			}
		} if(!hourcode10.equals("-")) {
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
		now = (Integer.parseInt(zeit.substring(0, zeit.indexOf(":"))) * 60 + Integer.parseInt(zeit.substring(zeit.indexOf(":") + 1))) * 60000;
		long percent = ((now - start) * 100) / (end - start);
		
		//In Ladebalken eintragen
		if(percent > 100) {
			percent = 100;
		} else if(percent < 0) {
			percent = 0;
		}
		
		//In Widget übertragen
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		remoteView.setProgressBar(R.id.percentbar, 100, (int) percent, false);
		remoteView.setTextViewText(R.id.percent, Long.toString(percent)+"%");
		//Datum und Wochentag eintragen
		String date2 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, Locale.GERMANY).format(date).substring(0, 16);
		remoteView.setTextViewText(R.id.tag, weekString + " der "+ date2);
		
		//Widget updaten
		updateWidget(context, remoteView);
	}
}