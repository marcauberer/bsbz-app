package com.mrgames13.jimdo.bsbz_app.App;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;

public class DayDetailsActivity extends AppCompatActivity {

	//Variablen
	public static String date1 = "";
	public static String date2 = "";
	Toolbar toolbar;
	
	@Override
	public void onStart() {
		super.onStart();

		// Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DayDetailsActivity.this);
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

		// ActionBar Titel festlegen
		String day = getIntent().getStringExtra("Day").toString();
		date1 = "";
		date1 = getIntent().getStringExtra("Date").toString();
		//date1 = date1.substring(0,2);
		getSupportActionBar().setTitle(day+" der "+date1);
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
		
		setContentView(R.layout.activity_day_details);

		//Toolbar aufsetzen
		toolbar = (Toolbar) findViewById(R.id.toolbar_day_details);
		setSupportActionBar(toolbar);

		LinearLayout ll = (LinearLayout) findViewById(R.id.ll1);
		if(MainActivity.AppTheme == 1) {
			ll.setBackgroundColor(Color.BLACK);
		}
		
		date1 = "";
		date1 = getIntent().getStringExtra("Date").toString();

		//Fragmente aufsetzen
        KlassenArbeitenFragment f1 = new KlassenArbeitenFragment();
        HausaufgabenFragment f2 = new HausaufgabenFragment();
        TermineFragment f3 = new TermineFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.klassenarbeiten_day_details_container, f1);
            ft.replace(R.id.hausaufgaben_day_details_container, f2);
            ft.replace(R.id.termine_day_details_container, f3);
        ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.day_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("InlinedApi")
	public static class KlassenArbeitenFragment extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			//SharedPreferences Instanz erhalten
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			// Liste aus String-Array befüllen
			ArrayList<String> arraylist = new ArrayList<String>();
			
			for(int i = 0;i < 101;i++) {
				String classtest = prefs.getString("Classtests_"+String.valueOf(i), "-");
				if(!classtest.equals("-")) {
					int index1 = classtest.indexOf(",");
					int index2 = classtest.indexOf(",", index1 +1);
					int index3 = classtest.indexOf(",", index2 +1);
					String item_date = classtest.substring(0, index1);
					String item_title = classtest.substring(index1 +1, index2);
					if(!item_title.equals("-") && item_date.equals(date1) && !item_title.equals("") &&!item_date.equals("")) {
						arraylist.add(item_date +": "+ item_title);
					}
				}
			}
			
			if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_KLASSENARBEITEN_TAG);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
			setListAdapter(adapter);
		}
		
		@Override
		public void onListItemClick(ListView listView, View v, int position, long id) {
			super.onListItemClick(listView, v, position, id);
			
			if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_KLASSENARBEITEN_TAG)) {
				Toast.makeText(getActivity(), MainActivity.KEINE_KLASSENARBEITEN_TAG, Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(getActivity(), K_DetailsActivity.class);
				String titel = getListView().getAdapter().getItem(position).toString();
				i.putExtra("Titel", titel);
				i.putExtra("Text", Integer.toString(position+1));
				startActivity(i);
			}
		}
	}
	
	@SuppressLint("InlinedApi")
	public static class HausaufgabenFragment extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			//SharedPreferences Instanz erhalten
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			// Liste aus String-Array befüllen
			ArrayList<String> arraylist = new ArrayList<String>();
			
			for(int i = 0;i < 101;i++) {
				String homework = prefs.getString("Homeworks_"+String.valueOf(i), "-");
				if(!homework.equals("-")) {
					int index1 = homework.indexOf(",");
					int index2 = homework.indexOf(",", index1 +1);
					int index3 = homework.indexOf(",", index2 +1);
					String item_date = homework.substring(0, index1);
					String item_title = homework.substring(index1 +1, index2);
					if(!item_title.equals("-") && item_date.equals(date1) && !item_title.equals("") &&!item_date.equals("")) {
						arraylist.add(item_date +": "+ item_title);
					}
				}
			}
			
			if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_HAUSAUFGABEN_TAG);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
			setListAdapter(adapter);
		}
		
		@Override
		public void onListItemClick(ListView listView, View v, int position, long id) {
			super.onListItemClick(listView, v, position, id);
			
			if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_HAUSAUFGABEN_TAG)) {
				Toast.makeText(getActivity(), MainActivity.KEINE_HAUSAUFGABEN_TAG, Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(getActivity(), H_DetailsActivity.class);
				String titel = getListView().getAdapter().getItem(position).toString();
				i.putExtra("Titel", titel);
				i.putExtra("Text", Integer.toString(position+1));
				startActivity(i);
			}
		}
	}
	
	@SuppressLint("InlinedApi")
	public static class TermineFragment extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			//SharedPreferences Instanz erhalten
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			// Liste aus String-Array befüllen
			ArrayList<String> arraylist = new ArrayList<String>();
			
			for(int i = 0;i < 101;i++) {
				String event = prefs.getString("Events_"+String.valueOf(i), "-");
				if(!event.equals("-")) {
					int index1 = event.indexOf(",");
					int index2 = event.indexOf(",", index1 +1);
					int index3 = event.indexOf(",", index2 +1);
					String item_date = event.substring(0, index1);
					String item_title = event.substring(index1 +1, index2);
					if(!item_title.equals("-") && item_date.equals(date1) && !item_title.equals("") &&!item_date.equals("")) {
						arraylist.add(item_date +": "+ item_title);
					}
				}
			}
			
			if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_TERMINE_TAG);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
			setListAdapter(adapter);
		}
		
		@Override
		public void onListItemClick(ListView listView, View v, int position, long id) {
			super.onListItemClick(listView, v, position, id);
			
			if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_TERMINE_TAG)) {
				Toast.makeText(getActivity(), MainActivity.KEINE_TERMINE_TAG, Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(getActivity(), T_DetailsActivity.class);
				String titel = getListView().getAdapter().getItem(position).toString();
				i.putExtra("Titel", titel);
				i.putExtra("Text", Integer.toString(position+1));
				startActivity(i);
			}
		}
	}
}