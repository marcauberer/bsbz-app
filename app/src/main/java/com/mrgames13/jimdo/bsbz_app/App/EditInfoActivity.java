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

import com.mrgames13.jimdo.bsbz_app.R;

public class EditInfoActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private SharedPreferences prefs;
    private Toolbar toolbar;
    public Resources res;

    //Variablen

    @Override
    public void onStart() {
        super.onStart();

        //Daten von den SharedPreferences abrufen
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Theme aus den Shared Preferences auslesen
        String theme = prefs.getString("AppTheme", "0");
        if(theme.equals("0")) {
            MainActivity.AppTheme = 0;
            setTheme(R.style.FirstTheme);
        } else if(theme.equals("1")) {
            MainActivity.AppTheme = 1;
            setTheme(R.style.SecondTheme);
        }

        setContentView(R.layout.activity_edit_info);

        //Resourcen initialisieren
        res = getResources();

        //Toolbar initialisieren
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_bsbz_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(res.getString(R.string.edit_bsbz_info));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_info, menu);
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