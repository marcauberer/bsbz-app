package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;

public class NewElementActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private ServerMessagingUtils serverMessagingUtils;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private Resources res;
    private ConnectivityManager cm;

    //Variablen
    private boolean pressedOnce;

    @Override
    protected void onStart() {
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
        toolbar.setTitle(res.getString(R.string.title_activity_new_new));

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
        }

        // ToolBar Titel festlegen
        toolbar.setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        setContentView(R.layout.activity_new_element);

        //Resourcen initialisieren
        res = getResources();

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(NewElementActivity.this);

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, this);

        //Toolbar aufsetzen
        toolbar = (Toolbar) findViewById(R.id.toolbar_new_element);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if(id == android.R.id.home) {
            if (!pressedOnce) {
                pressedOnce = true;
                Toast.makeText(NewElementActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pressedOnce = false;
                    }
                }, 2500);
            } else {
                pressedOnce = false;
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
