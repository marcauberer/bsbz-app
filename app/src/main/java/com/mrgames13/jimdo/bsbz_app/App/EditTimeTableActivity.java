package com.mrgames13.jimdo.bsbz_app.App;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters.ViewPagerAdapterEditTimeTable;

public class EditTimeTableActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private ViewPagerAdapterEditTimeTable viewpager_adapter;
    private Resources res;

    //Variablen
    private String klasse;

    @Override
    protected void onStart() {
        super.onStart();
        // Daten von den SharedPreferences abrufen
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditTimeTableActivity.this);
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
        tablayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
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

        setContentView(R.layout.activity_edit_time_table);

        //Resourcen initialisieren
        res = getResources();

        //Toolbar initialisieren
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_timetable);
        setSupportActionBar(toolbar);

        //ViewPager aufsetzen
        viewpager_adapter = new ViewPagerAdapterEditTimeTable(getSupportFragmentManager(), res);
        viewpager = (ViewPager) findViewById(R.id.edit_timetable_view_pager);
        viewpager.setAdapter(viewpager_adapter);

        //TabLayout aufsetzen
        tablayout = (TabLayout) findViewById(R.id.edit_timetable_tab_layout);
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

        klasse = getIntent().getStringExtra("class");
    }
}
