package com.mrgames13.jimdo.bsbz_app.App;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.SyncService;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;
import com.mrgames13.jimdo.bsbz_app.ViewPagerAdapters.ViewPagerAdapterEditTimeTable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EditTimeTableActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private ViewPagerAdapterEditTimeTable viewpager_adapter;
    private Resources res;
    private StorageUtils su;
    private static ConnectivityManager cm;
    private ServerMessagingUtils serverMessagingUtils;
    private AccountUtils au;
    private TimeTable timetable;
    private ProgressDialog pd;

    //Variablen
    private String klasse;
    private Account current_user;
    private String result;
    private boolean pressedOnce;

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
        getSupportActionBar().setTitle(res.getString(R.string.edit_timetable) + ": " + klasse);
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

        //StorageUtils initialisieren
        su = new StorageUtils(EditTimeTableActivity.this, res);

        //AccountUtils initialisieren
        au = new AccountUtils(su);

        //Aktuellen Account laden
        current_user = au.getLastUser();

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, this);

        //Extras aus dem Intent auslesen
        klasse = getIntent().getStringExtra("class");
        timetable = su.getTimeTable(klasse);

        //Toolbar initialisieren
        toolbar = findViewById(R.id.toolbar_edit_timetable);
        setSupportActionBar(toolbar);

        //ViewPager aufsetzen
        viewpager_adapter = new ViewPagerAdapterEditTimeTable(getSupportFragmentManager(), res, su, timetable);
        viewpager = findViewById(R.id.edit_timetable_view_pager);
        viewpager.setAdapter(viewpager_adapter);

        //TabLayout aufsetzen
        tablayout = findViewById(R.id.edit_timetable_tab_layout);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if(id == R.id.action_commit_timetable_changings) {

            AlertDialog d = new AlertDialog.Builder(EditTimeTableActivity.this)
                    .setTitle(res.getString(R.string.publish))
                    .setMessage(res.getString(R.string.do_you_want_to_publish_timetable))
                    .setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(res.getString(R.string.publish), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Aktionen durchführen
                            viewpager_adapter.saveTmp();
                            int current_position = viewpager.getCurrentItem();
                            viewpager.setCurrentItem(0);
                            viewpager.setCurrentItem(1);
                            viewpager.setCurrentItem(2);
                            viewpager.setCurrentItem(3);
                            viewpager.setCurrentItem(4);
                            viewpager.setCurrentItem(current_position);
                            uploadTimeTableChangings(viewpager_adapter.getTimeTableCode());
                            //Dialog schließen
                            dialog.dismiss();
                        }
                    })
                    .create();
            d.show();
            return true;
        } else if (id == android.R.id.home) {
            if (!pressedOnce) {
                pressedOnce = true;
                Toast.makeText(EditTimeTableActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!pressedOnce) {
                pressedOnce = true;
                Toast.makeText(EditTimeTableActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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
        return super.onKeyDown(keyCode, event);
    }

    private void uploadTimeTableChangings(final String changings) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //ProgressDialog anzeigen
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd = new ProgressDialog(EditTimeTableActivity.this);
                        pd.setIndeterminate(true);
                        pd.setTitle(res.getString(R.string.upload_changes));
                        pd.setMessage(res.getString(R.string.changings_uploading_));
                        pd.setCancelable(false);
                        pd.show();
                    }
                });
                //Änderungen hochladen
                try {
                    result = serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(current_user.getUsername(), "UTF-8")+"&command=settimetable&class="+URLEncoder.encode(klasse, "UTF-8")+"&code="+URLEncoder.encode(changings, "UTF-8"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.equals("Action Successful")) {
                                Toast.makeText(EditTimeTableActivity.this, res.getString(R.string.action_successful), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditTimeTableActivity.this, res.getString(R.string.action_failed), Toast.LENGTH_SHORT).show();
                            }
                            pd.dismiss();
                            startService(new Intent(EditTimeTableActivity.this, SyncService.class));
                            finish();
                        }
                    });
                } catch (UnsupportedEncodingException e) {}

            }
        }).start();
    }
}
