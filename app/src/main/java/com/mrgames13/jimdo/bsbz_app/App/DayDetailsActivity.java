package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters.ViewPagerAdapterDayDetails;

public class DayDetailsActivity extends AppCompatActivity {

	//Konstanten

    //Variablen als Objekte
    private Toolbar toolbar;
    private ViewPager viewpager;
    private ViewPagerAdapterDayDetails viewpager_adapter;
    private TabLayout tablayout;

	//Variablen
	public static String current_date = "";
	
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
		current_date = "";
		current_date = getIntent().getStringExtra("Date").toString();
		//current_date = current_date.substring(0,2);
		getSupportActionBar().setTitle(day+" der "+ current_date);
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

        //ViewPager aufsetzen
        viewpager_adapter = new ViewPagerAdapterDayDetails(getSupportFragmentManager(), getResources(), current_date);
        viewpager = (ViewPager) findViewById(R.id.day_details_view_pager);
        viewpager.setAdapter(viewpager_adapter);

        //TabLayout aufsetzen
        tablayout = (TabLayout) findViewById(R.id.day_details_tab_layout);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

		current_date = getIntent().getStringExtra("Date").toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
}