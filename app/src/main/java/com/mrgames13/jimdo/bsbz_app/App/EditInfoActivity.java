package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

import java.net.URLEncoder;

public class EditInfoActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private ConnectivityManager cm;
    private ServerMessagingUtils serverMessagingUtils;
    private Toolbar toolbar;
    public Resources res;
    private StorageUtils su;
    private AccountUtils au;

    //Variablen
    private String result;
    private Account current_account;

    @Override
    public void onStart() {
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

        //Resourcen initialisieren
        res = getResources();

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

        setContentView(R.layout.activity_edit_info);

        //AccountUtils initialisieren
        au = new AccountUtils(su);

        //Account laden
        current_account = au.getLastUser();

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, EditInfoActivity.this);

        //Toolbar initialisieren
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_bsbz_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(res.getString(R.string.edit_bsbz_info));

        final EditText text = (EditText) findViewById(R.id.edit_bsbz_info_text);
        if(!getIntent().getStringExtra("Text").equals(res.getString(R.string.no_info_entered)))text.setText(getIntent().getStringExtra("Text"));
        text.setSelection(text.getText().toString().length());

        //FloatingActionButton 'Finish' initialisieren
        final FloatingActionButton finish = (FloatingActionButton) findViewById(R.id.edit_bsbz_info_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String edited_info = text.getText().toString().trim();
                        if(edited_info.equals("")) edited_info = res.getString(R.string.no_info_entered);
                        try{
                            result = serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(current_account.getUsername(), "UTF-8")+"&command=setbsbzinfo&description="+URLEncoder.encode(edited_info, "UTF-8"));
                            if(result.equals("Action Successful")) {
                                result = res.getString(R.string.action_successful);
                            } else {
                                result = res.getString(R.string.action_failed);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(EditInfoActivity.this, result, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        } catch(Exception e) {}
                    }
                }).start();
            }
        });
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