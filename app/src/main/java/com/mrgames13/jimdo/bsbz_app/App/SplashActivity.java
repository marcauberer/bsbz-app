package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mrgames13.jimdo.bsbz_app.R;

@SuppressWarnings("deprecation")
public class SplashActivity extends AppCompatActivity {

	//Variablen
	private Toolbar toolbar;
	private Resources res;

	@Override
	public void onStart() {
		super.onStart();
		
		//Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
		String layout = prefs.getString("Layout", res.getString(R.string.bsbz_layout_orange));
		String color = "#ea690c";
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
		toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		
		if(Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Theme setzen
		if(MainActivity.AppTheme == 0) {
			setTheme(R.style.FirstTheme);
		} else if(MainActivity.AppTheme == 1) {
			setTheme(R.style.SecondTheme);
		}
		
		setContentView(R.layout.activity_splash_screen);

		//Resourcen initialisieren
		res = getResources();

		//Toolbar aufsetzen
		toolbar = (Toolbar) findViewById(R.id.toolbar_splash);
		setSupportActionBar(toolbar);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl3);
		ImageView iv = (ImageView) findViewById(R.id.BSBZ_Logo);
		if(MainActivity.AppTheme == 1) {
			rl.setBackgroundColor(Color.BLACK);
			iv.setImageResource(R.drawable.bsbz_logo_gross_mrgames_black);
		}
		
        //Warten
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this,MainActivity.class));
				//Animierter Activitywechsel starten
				overridePendingTransition(R.anim.in_login, R.anim.out_login);
				finish();
			}
		}, 3000);
	}
}