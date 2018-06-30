package com.mrgames13.jimdo.bsbz_app.HomescreenWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.mrgames13.jimdo.bsbz_app.App.SplashScreenActivity;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Utils.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Utils.StorageUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Widget_Provider extends AppWidgetProvider {
	
	//Konstanten
	public static boolean RUN_SERVICE;
	String color = "#ea690c";

	//Variablen als Objekte
	private StorageUtils su;
	private AccountUtils au;
    private Resources res;

	//Variablen
	private Account current_account;
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

        //Hintergrundfarbe des Widgets festlegen
		setColor(context);

        //Prozentanzeige des Tages updaten
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

        //Resourcen initialisieren
        res = context.getResources();

        //StorageUtils initialisieren
        su = new StorageUtils(context, res);

        //AccountUtils initialisieren
        au = new AccountUtils(su);

        //Aktuellen Account laden
        current_account = au.getLastUser();

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
		Intent intent = new Intent(context, SplashScreenActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteView.setOnClickPendingIntent(R.id.app_oeffnen, pendingIntent);
        remoteView.setTextViewText(R.id.last_sync, res.getString(R.string.lastSyncronisation_) + su.getString("SyncTime", res.getString(R.string.no_synchronisation)));
		
		ComponentName widgetComponent = new ComponentName(context, Widget_Provider.class);
		AppWidgetManager.getInstance(context).updateAppWidget(widgetComponent, remoteView);
	}
	
	private void setColor(Context context) {
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        String layout = su.getString("Layout", res.getString(R.string.bsbz_layout_orange));
        if(layout.equals("0")) {
            color = "#ea690c";
        } else if(layout.equals("1")) {
            color = "#000000";
        } else if(layout.equals("2")) {
            color = "#3ded25";
        } else if(layout.equals("3")) {
            color = "#ff0000";
        } else if(layout.equals("4")) {
            color = "#0000ff";
        } else if(layout.equals("5")) {
            color = "#00007f";
        }
		//Farbe setzen
		remoteView.setInt(R.id.widget, "setBackgroundColor", Color.parseColor(color));
		updateWidget(context, remoteView);
	}
	
	private void updateProgress(Context context) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		
		String weekString;
        String daycode;

        TimeTable tt = su.getTimeTable(current_account.getForm());

		if(weekday == 2) {
			weekString = "Mo";
            daycode = tt.getMo();
		} else if (weekday == 3) {
			weekString = "Di";
            daycode = tt.getDi();
		} else if (weekday == 4) {
			weekString = "Mi";
            daycode = tt.getMi();
		} else if (weekday == 5) {
			weekString = "Do";
            daycode = tt.getDo();
		} else if (weekday == 6) {
			weekString = "Fr";
            daycode = tt.getFr();
		} else {
            return;
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
		
		if(hour1.equals("")) {
            hour1 = "-";
		}
		if(hour2.equals("")) {
            hour2 = "-";
		}
		if(hour3.equals("")) {
            hour3 = "-";
		}
		if(hour4.equals("")) {
            hour4 = "-";
		}
		if(hour5.equals("")) {
            hour5 = "-";
		}
		if(hour6.equals("")) {
            hour6 = "-";
		}
		if(hour7.equals("")) {
            hour7 = "-";
		}
		if(hour8.equals("")) {
            hour8 = "-";
		}
		if(hour9.equals("")) {
            hour9 = "-";
		}
		if(hour10.equals("")) {
            hour10 = "-";
		}
		
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
		now = (Integer.parseInt(zeit.substring(0, zeit.indexOf(":"))) * 60 + Integer.parseInt(zeit.substring(zeit.indexOf(":") + 1))) * 60000;
		long percent = 0;
		try { percent = ((now - start) * 100) / (end - start); } catch (Exception e) {}
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