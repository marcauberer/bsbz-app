package com.mrgames13.jimdo.bsbz_app.App;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrgames13.jimdo.bsbz_app.R;

@SuppressLint("SetJavaScriptEnabled")
@SuppressWarnings("deprecation")
public class WebActivity extends AppCompatActivity {

	//Variablen
	private Toolbar toolbar;
    private Resources res;


	@Override
	protected void onStart() {
		super.onStart();
		
		//Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WebActivity.this);
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
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		if(Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		//Toolbar aufsetzen
		toolbar = (Toolbar) findViewById(R.id.toolbar_web);
		setSupportActionBar(toolbar);

        //Resourcen initialsieren
        res = getResources();

		//Lade-TextView
		final TextView laden = (TextView) findViewById(R.id.laden);
		final ProgressBar laden_progress_bar = (ProgressBar) findViewById(R.id.progressBar1);
		laden.setVisibility(View.VISIBLE);
		laden_progress_bar.setVisibility(View.VISIBLE);
		//WebView
		WebView webside = (WebView) findViewById(R.id.Webside);
		webside.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				laden.setVisibility(View.INVISIBLE);
				laden_progress_bar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//Code zur Fehlerbehandlung hier schreiben
				
			}
		});
		webside.getSettings().setBuiltInZoomControls(true);
		webside.getSettings().setLoadWithOverviewMode(true);
		webside.getSettings().setUseWideViewPort(true);
		webside.getSettings().setJavaScriptEnabled(true);
		
		//Extras im Intent auslesen
		final String stringExtraWebside = getIntent().getStringExtra("Webside");
		webside.loadUrl(stringExtraWebside);
		//SeitenTitel aus dem Intent auslesen
		final String stringExtraTitle = getIntent().getStringExtra("Title");
		getSupportActionBar().setTitle(stringExtraTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else if(id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		super.finish();
	}
}
