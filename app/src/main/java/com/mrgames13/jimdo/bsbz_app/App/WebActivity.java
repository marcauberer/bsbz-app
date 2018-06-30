package com.mrgames13.jimdo.bsbz_app.App;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
import com.mrgames13.jimdo.bsbz_app.Utils.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Utils.StorageUtils;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends AppCompatActivity {

    //Konstanten
    private String NO_DATA_URL;

    //Variablen als Objekte
    private Toolbar toolbar;
    private Resources res;
    private WebView website;
	private ConnectivityManager cm;
	private ServerMessagingUtils serverMessagingUtils;
	private StorageUtils su;

	//Variablen
    private String title;
    private String website_url = NO_DATA_URL;

	@Override
	protected void onStart() {
		super.onStart();
		
		//Daten von den SharedPreferences abrufen
		String layout = su.getString("Layout", res.getString(R.string.bsbz_layout_orange));
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

        //Resourcen initialsieren
        res = getResources();
		NO_DATA_URL = res.getString(R.string.link_homepage);

        //StorageUtils initialisieren
        su = new StorageUtils(this, res);

        //Theme aus den Shared Preferences auslesen
        String theme = su.getString("AppTheme", "0");
        if(theme.equals("0")) {
            MainActivity.AppTheme = 0;
            setTheme(R.style.FirstTheme);
        } else if(theme.equals("1")) {
            MainActivity.AppTheme = 1;
            setTheme(R.style.SecondTheme);
        }

		setContentView(R.layout.activity_web);

		//Toolbar aufsetzen
		toolbar = findViewById(R.id.toolbar_web);
		setSupportActionBar(toolbar);

		//ServerMessagingUtils initialisieren
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		serverMessagingUtils = new ServerMessagingUtils(cm, WebActivity.this);
        serverMessagingUtils.checkConnection(findViewById(R.id.container));

		//Lade-TextView
		final TextView laden = findViewById(R.id.laden);
		final ProgressBar laden_progress_bar = findViewById(R.id.progressBar1);
		laden.setVisibility(View.VISIBLE);
		laden_progress_bar.setVisibility(View.VISIBLE);
		//WebView
		website = findViewById(R.id.Website);
		website.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
                if(title.equals(res.getString(R.string.loading))) getSupportActionBar().setTitle(website.getTitle());
				laden.setVisibility(View.GONE);
				laden_progress_bar.setVisibility(View.GONE);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//Code zur Fehlerbehandlung hier schreiben
				
			}
		});
		website.getSettings().setBuiltInZoomControls(true);
		website.getSettings().setLoadWithOverviewMode(true);
		website.getSettings().setUseWideViewPort(true);
		website.getSettings().setJavaScriptEnabled(true);
		
		//Extras im Intent auslesen
		website_url = getIntent().getStringExtra("Website");
		//SeitenTitel aus dem Intent auslesen
		title = getIntent().getStringExtra("Title");

        if(title == null && website_url == null) {
            website_url = getIntent().getData().toString();
            title = res.getString(R.string.loading);
        }

        if(title == null && website_url == null) {
            website_url = getIntent().getData().toString();
            title = res.getString(R.string.loading);
        }

        website.loadUrl(website_url);
		getSupportActionBar().setTitle(title);
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
            if(website.canGoBack()) {
                website.goBack();
            } else {
                finish();
            }
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		super.finish();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (website.canGoBack()) {
                website.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}