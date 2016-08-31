package com.mrgames13.jimdo.bsbz_app.HomescreenWidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class Widget_Update_Service extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		startForeground(0, null);
		
		sendBroadcast();
		
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendBroadcast() {
		Intent i = new Intent(Widget_Update_Service.this, Widget_Provider.class);
		i.setAction(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
		i.putExtra("UpdateAll", "UpdateAll");
		sendBroadcast(i);
		
		//Handler erstellen
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				sendBroadcast();
			}
		}, 10000);
	}
}