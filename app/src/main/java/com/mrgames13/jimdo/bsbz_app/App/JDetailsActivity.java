package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mrgames13.jimdo.bsbz_app.R;

@SuppressWarnings("deprecation")
public class JDetailsActivity extends ActionBarActivity {

	//Variablen
	Toolbar toolbar;
	Resources res;

	@Override
	public void onStart() {
		super.onStart();

		// Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(JDetailsActivity.this);
		String layout = prefs.getString("Layout", MainActivity.res.getString(R.string.bsbz_layout_orange));
		String color = "#ea690c";
		if (layout.equals("0")) {
			color = "#ea690c";
		} else if (layout.equals("1")) {
			color = "#000000";
		} else if (layout.equals("2")) {
			color = "#3ded25";
		} else if (layout.equals("3")) {
			color = "#ff0000";
		} else if (layout.equals("4")) {
			color = "#0000ff";
		} else if (layout.equals("5")) {
			color = "#00007f";
		}
		toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		// Toolbar Titel festlegen
		String day = getIntent().getStringExtra("Titel").toString();
		getSupportActionBar().setTitle(day);
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

		setContentView(R.layout.activity_jdetails);

		//Resourcen aufsetzen
		res = getResources();

        //Toolbar aufsetzen
        toolbar = (Toolbar) findViewById(R.id.toolbar_jdetails);
        setSupportActionBar(toolbar);

		//Id des TextViews ermitteln
		TextView details = (TextView) findViewById(R.id.details);
		TextView from = (TextView) findViewById(R.id.from_user);

		//Titel auslesen
		String title = getIntent().getExtras().getString("Titel");
		
		//Instanz der SharedPreferences ermitteln
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		String title1 = "";
		String text = "";
		String from_user = "";

		for(int i = 0;i < 101;i++) {
			String classtest = prefs.getString("Classtests_"+String.valueOf(i), "-");
			int index1 = classtest.indexOf(",");
			int index2 = classtest.indexOf(",", index1 +1);
			int index3 = classtest.indexOf(",", index2 +1);
			if(!classtest.equals("-")) {
				title1 = classtest.substring(index1 +1, index2);
				if(title1.equals(title)) {
					text = classtest.substring(index2 +1, index3);
					from_user = classtest.substring(index3 +1);
				}
			}
		}


		for(int i = 0;i < 101;i++) {
			String homework = prefs.getString("Homeworks_"+String.valueOf(i), "-");
			int index1 = homework.indexOf(",");
			int index2 = homework.indexOf(",", index1 +1);
			int index3 = homework.indexOf(",", index2 +1);
			if(!homework.equals("-")) {
				title1 = homework.substring(index1 +1, index2);
				if(title1.equals(title)) {
					text = homework.substring(index2 +1, index3);
					from_user = homework.substring(index3 +1);
				}
			}
		}

		for(int i = 0;i < 101;i++) {
			String event = prefs.getString("Events_"+String.valueOf(i), "-");
			int index1 = event.indexOf(",");
			int index2 = event.indexOf(",", index1 +1);
			int index3 = event.indexOf(",", index2 +1);
			if(!event.equals("-")) {
				title1 = event.substring(index1 +1, index2);
				if(title1.equals(title)) {
					text = event.substring(index2 +1, index3);
					from_user = event.substring(index3 +1);
				}
			}
		}

		if(text.equals("")) text = MainActivity.res.getString(R.string.no_description_found);
		//In Feld eintragen
		details.setText(text);
		from.setText(res.getString(R.string.last_edited_1) + from_user + res.getString(R.string.last_edited_2));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.jdetails, menu);
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}