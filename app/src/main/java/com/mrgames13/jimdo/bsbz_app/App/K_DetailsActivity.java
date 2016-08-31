package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrgames13.jimdo.bsbz_app.R;

@SuppressWarnings("deprecation")
public class K_DetailsActivity extends ActionBarActivity {

	//Variablen
	Toolbar toolbar;

	@Override
	protected void onStart() {
		super.onStart();
		
		// Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(K_DetailsActivity.this);
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
		
		setContentView(R.layout.activity_details);

		//Toolbar aufsetzen
		toolbar = (Toolbar) findViewById(R.id.toolbar_details);
		setSupportActionBar(toolbar);

		RelativeLayout dl = (RelativeLayout) findViewById(R.id.rl5);
		if(MainActivity.AppTheme == 1) {
			dl.setBackgroundColor(Color.BLACK);
		}
		
		//Id der TextViews ermitteln
		TextView beschreibung = (TextView) findViewById(R.id.Beschreibung);
		TextView from_user = (TextView) findViewById(R.id.from_user);
		
		// ActionBar Titel festlegen
		String title = getIntent().getExtras().getString("Titel").substring(12);
		getSupportActionBar().setTitle(title);
		
		//Instanz der SharedPreferences ermitteln
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		String title1 = "";
		String text = "";
		String from = "";
		for(int i = 0;i < 101;i++) {
			String classtest = prefs.getString("Classtests_"+String.valueOf(i), "-");
			if(!classtest.equals("-")) {
				int index1 = classtest.indexOf(",");
				int index2 = classtest.indexOf(",", index1 +1);
				int index3 = classtest.indexOf(",", index2 +1);
				String item_title = classtest.substring(index1 +1, index2);
				String item_text = classtest.substring(index2 +1, index3);
				String item_from = classtest.substring(index3 +1);
				if(item_title.equals(title) && !item_title.equals("-")) {
					text = item_text;
					from = item_from;
				}
			}
		}
		if(text.equals("")) {
			text = MainActivity.res.getString(R.string.no_description_found);
		}
		//In Feld eintragen
		beschreibung.setText(text);
		from_user.setText(getResources().getString(R.string.last_edited_1) + from + getResources().getString(R.string.last_edited_2));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.details, menu);
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