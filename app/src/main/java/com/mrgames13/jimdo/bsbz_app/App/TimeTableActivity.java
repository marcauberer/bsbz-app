package com.mrgames13.jimdo.bsbz_app.App;

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
import android.view.Window;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.ComponentClasses.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

@SuppressWarnings("deprecation")
public class TimeTableActivity extends AppCompatActivity {

	//Konstanten

    //Variablen als Objekte
    private StorageUtils su;
    private TimeTable timetable;
    private Resources res;

	//Variablen
	private Toolbar toolbar;

	@Override
	protected void onStart() {
		super.onStart();
		
		// Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TimeTableActivity.this);
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
		
		//Theme setzen
		if(MainActivity.AppTheme == 0) {
			setTheme(R.style.FirstTheme);
		} else if(MainActivity.AppTheme == 1) {
			setTheme(R.style.SecondTheme);
		}
		
		setContentView(R.layout.activity_stundenplan);

		//Toolbar aufsetzen
		toolbar = (Toolbar) findViewById(R.id.toolbar_timetable);
		setSupportActionBar(toolbar);

        //Resourcen initialisieren
        res = getResources();

        //StorageUtils initialisieren
        su = new StorageUtils(TimeTableActivity.this);

		String klasse = su.getString("Klasse", "no_class");
		if(klasse.equals("no_class")) {
            Toast.makeText(TimeTableActivity.this, res.getString(R.string.no_class_selected), Toast.LENGTH_LONG).show();
            finish();
        }

        //TimeTable abrufen
        timetable = su.getTimeTable(klasse);
        if(timetable == null) {
            Toast.makeText(TimeTableActivity.this, res.getString(R.string.timetable_not_synchronized), Toast.LENGTH_LONG).show();
            finish();
        }

        //Klasse eintragen
		TextView tv_klasse = (TextView) findViewById(R.id.tt_klasse);
		tv_klasse.setText(klasse);
		
		//TableRows einfärben
		TableRow tr1 = (TableRow) findViewById(R.id.monate);
		tr1.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BEBEBE")));
		TableRow tr3 = (TableRow) findViewById(R.id.tableRow3);
		tr3.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BEBEBE")));
		TableRow tr5 = (TableRow) findViewById(R.id.tableRow5);
		tr5.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BEBEBE")));
		TableRow tr7 = (TableRow) findViewById(R.id.tableRow7);
		tr7.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BEBEBE")));
		TableRow tr9 = (TableRow) findViewById(R.id.tableRow9);
		tr9.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BEBEBE")));
		TableRow tr11 = (TableRow) findViewById(R.id.tableRow11);
		tr11.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BEBEBE")));
		
		//Stundenplan eintragen
		//Montag
		String daycode = timetable.getMo();
		//Daycode auseinandernehmen
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
		//Codes eintragen
		TextView tv1 = (TextView) findViewById(R.id.tta1);
		tv1.setText(hour1);
		TextView tv2 = (TextView) findViewById(R.id.tta6);
		tv2.setText(hour2);
		TextView tv3 = (TextView) findViewById(R.id.tta11);
		tv3.setText(hour3);
		TextView tv4 = (TextView) findViewById(R.id.tta16);
		tv4.setText(hour4);
		TextView tv5 = (TextView) findViewById(R.id.tta21);
		tv5.setText(hour5);
		TextView tv6 = (TextView) findViewById(R.id.tta26);
		tv6.setText(hour6);
		TextView tv7 = (TextView) findViewById(R.id.tta31);
		tv7.setText(hour7);
		TextView tv8 = (TextView) findViewById(R.id.tta36);
		tv8.setText(hour8);
		TextView tv9 = (TextView) findViewById(R.id.tta41);
		tv9.setText(hour9);
		TextView tv10 = (TextView) findViewById(R.id.tta46);
		tv10.setText(hour10);
		
		//Dienstag
		daycode = timetable.getDi();
		//Daycode auseinandernehmen
		index1 = daycode.indexOf(",", 0);
		index2 = daycode.indexOf(",", index1 +1);
		index3 = daycode.indexOf(",", index2 +1);
		index4 = daycode.indexOf(",", index3 +1);
		index5 = daycode.indexOf(",", index4 +1);
		index6 = daycode.indexOf(",", index5 +1);
		index7 = daycode.indexOf(",", index6 +1);
		index8 = daycode.indexOf(",", index7 +1);
		index9 = daycode.indexOf(",", index8 +1);
		hour1 = daycode.substring(0, index1);
		hour2 = daycode.substring(index1 +1, index2);
		hour3 = daycode.substring(index2 +1, index3);
		hour4 = daycode.substring(index3 +1, index4);
		hour5 = daycode.substring(index4 +1, index5);
		hour6 = daycode.substring(index5 +1, index6);
		hour7 = daycode.substring(index6 +1, index7);
		hour8 = daycode.substring(index7 +1, index8);
		hour9 = daycode.substring(index8 +1, index9);
		hour10 = daycode.substring(index9 +1);
		//Codes eintragen
		tv1 = (TextView) findViewById(R.id.tta2);
		tv1.setText(hour1);
		tv2 = (TextView) findViewById(R.id.tta7);
		tv2.setText(hour2);
		tv3 = (TextView) findViewById(R.id.tta12);
		tv3.setText(hour3);
		tv4 = (TextView) findViewById(R.id.tta17);
		tv4.setText(hour4);
		tv5 = (TextView) findViewById(R.id.tta22);
		tv5.setText(hour5);
		tv6 = (TextView) findViewById(R.id.tta27);
		tv6.setText(hour6);
		tv7 = (TextView) findViewById(R.id.tta32);
		tv7.setText(hour7);
		tv8 = (TextView) findViewById(R.id.tta37);
		tv8.setText(hour8);
		tv9 = (TextView) findViewById(R.id.tta42);
		tv9.setText(hour9);
		tv10 = (TextView) findViewById(R.id.tta47);
		tv10.setText(hour10);
		
		//Mittwoch
		daycode = timetable.getMi();
		//Daycode auseinandernehmen
		index1 = daycode.indexOf(",", 0);
		index2 = daycode.indexOf(",", index1 +1);
		index3 = daycode.indexOf(",", index2 +1);
		index4 = daycode.indexOf(",", index3 +1);
		index5 = daycode.indexOf(",", index4 +1);
		index6 = daycode.indexOf(",", index5 +1);
		index7 = daycode.indexOf(",", index6 +1);
		index8 = daycode.indexOf(",", index7 +1);
		index9 = daycode.indexOf(",", index8 +1);
		hour1 = daycode.substring(0, index1);
		hour2 = daycode.substring(index1 +1, index2);
		hour3 = daycode.substring(index2 +1, index3);
		hour4 = daycode.substring(index3 +1, index4);
		hour5 = daycode.substring(index4 +1, index5);
		hour6 = daycode.substring(index5 +1, index6);
		hour7 = daycode.substring(index6 +1, index7);
		hour8 = daycode.substring(index7 +1, index8);
		hour9 = daycode.substring(index8 +1, index9);
		hour10 = daycode.substring(index9 +1);
		//Codes eintragen
		tv1 = (TextView) findViewById(R.id.tta3);
		tv1.setText(hour1);
		tv2 = (TextView) findViewById(R.id.tta8);
		tv2.setText(hour2);
		tv3 = (TextView) findViewById(R.id.tta13);
		tv3.setText(hour3);
		tv4 = (TextView) findViewById(R.id.tta18);
		tv4.setText(hour4);
		tv5 = (TextView) findViewById(R.id.tta23);
		tv5.setText(hour5);
		tv6 = (TextView) findViewById(R.id.tta28);
		tv6.setText(hour6);
		tv7 = (TextView) findViewById(R.id.tta33);
		tv7.setText(hour7);
		tv8 = (TextView) findViewById(R.id.tta38);
		tv8.setText(hour8);
		tv9 = (TextView) findViewById(R.id.tta43);
		tv9.setText(hour9);
		tv10 = (TextView) findViewById(R.id.tta48);
		tv10.setText(hour10);
		
		//Donnerstag
		daycode = timetable.getDo();
		//Daycode auseinandernehmen
		index1 = daycode.indexOf(",", 0);
		index2 = daycode.indexOf(",", index1 +1);
		index3 = daycode.indexOf(",", index2 +1);
		index4 = daycode.indexOf(",", index3 +1);
		index5 = daycode.indexOf(",", index4 +1);
		index6 = daycode.indexOf(",", index5 +1);
		index7 = daycode.indexOf(",", index6 +1);
		index8 = daycode.indexOf(",", index7 +1);
		index9 = daycode.indexOf(",", index8 +1);
		hour1 = daycode.substring(0, index1);
		hour2 = daycode.substring(index1 +1, index2);
		hour3 = daycode.substring(index2 +1, index3);
		hour4 = daycode.substring(index3 +1, index4);
		hour5 = daycode.substring(index4 +1, index5);
		hour6 = daycode.substring(index5 +1, index6);
		hour7 = daycode.substring(index6 +1, index7);
		hour8 = daycode.substring(index7 +1, index8);
		hour9 = daycode.substring(index8 +1, index9);
		hour10 = daycode.substring(index9 +1);
		//Codes eintragen
		tv1 = (TextView) findViewById(R.id.tta4);
		tv1.setText(hour1);
		tv2 = (TextView) findViewById(R.id.tta9);
		tv2.setText(hour2);
		tv3 = (TextView) findViewById(R.id.tta14);
		tv3.setText(hour3);
		tv4 = (TextView) findViewById(R.id.tta19);
		tv4.setText(hour4);
		tv5 = (TextView) findViewById(R.id.tta24);
		tv5.setText(hour5);
		tv6 = (TextView) findViewById(R.id.tta29);
		tv6.setText(hour6);
		tv7 = (TextView) findViewById(R.id.tta34);
		tv7.setText(hour7);
		tv8 = (TextView) findViewById(R.id.tta39);
		tv8.setText(hour8);
		tv9 = (TextView) findViewById(R.id.tta44);
		tv9.setText(hour9);
		tv10 = (TextView) findViewById(R.id.tta49);
		tv10.setText(hour10);
		
		//Freitag
		daycode = timetable.getFr();
		//Daycode auseinandernehmen
		index1 = daycode.indexOf(",", 0);
		index2 = daycode.indexOf(",", index1 +1);
		index3 = daycode.indexOf(",", index2 +1);
		index4 = daycode.indexOf(",", index3 +1);
		index5 = daycode.indexOf(",", index4 +1);
		index6 = daycode.indexOf(",", index5 +1);
		index7 = daycode.indexOf(",", index6 +1);
		index8 = daycode.indexOf(",", index7 +1);
		index9 = daycode.indexOf(",", index8 +1);
		hour1 = daycode.substring(0, index1);
		hour2 = daycode.substring(index1 +1, index2);
		hour3 = daycode.substring(index2 +1, index3);
		hour4 = daycode.substring(index3 +1, index4);
		hour5 = daycode.substring(index4 +1, index5);
		hour6 = daycode.substring(index5 +1, index6);
		hour7 = daycode.substring(index6 +1, index7);
		hour8 = daycode.substring(index7 +1, index8);
		hour9 = daycode.substring(index8 +1, index9);
		hour10 = daycode.substring(index9 +1);
		//Codes eintragen
		tv1 = (TextView) findViewById(R.id.tta5);
		tv1.setText(hour1);
		tv2 = (TextView) findViewById(R.id.tta10);
		tv2.setText(hour2);
		tv3 = (TextView) findViewById(R.id.tta15);
		tv3.setText(hour3);
		tv4 = (TextView) findViewById(R.id.tta20);
		tv4.setText(hour4);
		tv5 = (TextView) findViewById(R.id.tta25);
		tv5.setText(hour5);
		tv6 = (TextView) findViewById(R.id.tta30);
		tv6.setText(hour6);
		tv7 = (TextView) findViewById(R.id.tta35);
		tv7.setText(hour7);
		tv8 = (TextView) findViewById(R.id.tta40);
		tv8.setText(hour8);
		tv9 = (TextView) findViewById(R.id.tta45);
		tv9.setText(hour9);
		tv10 = (TextView) findViewById(R.id.tta50);
		tv10.setText(hour10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.stundenplan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else if(id == R.id.action_edit_timetable) {
			startActivity(new Intent(this, EditTimeTableActivity.class));
			return true;
		} else if(id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}