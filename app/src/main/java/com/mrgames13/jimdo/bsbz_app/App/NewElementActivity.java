package com.mrgames13.jimdo.bsbz_app.App;

import android.content.DialogInterface;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;

public class NewElementActivity extends AppCompatActivity {
    //Konstanten
    public static final int MODE_CREATE_CLASSTEST = 1;
    public static final int MODE_CREATE_HOMEWORK = 2;
    public static final int MODE_CREATE_EVENT = 3;
    public static final int MODE_EDIT_CLASSTEST = 4;
    public static final int MODE_EDIT_HOMEWORK = 5;
    public static final int MODE_EDIT_EVENT = 6;

    //Variablen als Objekte
    private ServerMessagingUtils serverMessagingUtils;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private Resources res;
    private ConnectivityManager cm;

    //Variablen
    private boolean pressedOnce;
    private int mode;
    private String activity_title;
    private String old_title;
    private String old_discription;
    private String old_receiver;

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

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
        }

        // ToolBar Titel festlegen
        getSupportActionBar().setTitle(activity_title);
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

        //Mode aus dem Intent auslesen
        mode = getIntent().getIntExtra("mode", MODE_CREATE_CLASSTEST);

        //Beim Editieren Daten aus dem Intent lesen
        old_title = getIntent().getStringExtra("old_title");
        old_discription = getIntent().getStringExtra("old_description");
        old_receiver = getIntent().getStringExtra("old_receiver");

        //ActivityTitle aus dem Intent auslesen
        activity_title = getIntent().getStringExtra("title");

        //Writer-Textfeld initialisieren
        EditText writer = (EditText) findViewById(R.id.new_element_writer);
        writer.setText(prefs.getString("Name", res.getString(R.string.guest)));

        //ChooseReceiver-Button initialisieren
        final Button choose_receiver = (Button) findViewById(R.id.new_element_choose_receiver);
        String rights = prefs.getString("Rights", "student");
        if(rights.equals("classspeaker")) {
            choose_receiver.setText(prefs.getString("Klasse", "no_class"));
            choose_receiver.setEnabled(false);
        } else {
            choose_receiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert;
                    if(MainActivity.AppTheme == 0) {
                        alert = new AlertDialog.Builder(NewElementActivity.this, R.style.FirstTheme_Dialog);
                    } else {
                        alert = new AlertDialog.Builder(NewElementActivity.this, R.style.SecondTheme_Dialog);
                    }

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialogview_class_chooser_admin, null);
                    alert.setView(dialogView);

                    final TextView schulart = (TextView) dialogView.findViewById(R.id.schulart);
                    final TextView klassenstufe = (TextView) dialogView.findViewById(R.id.klassenstufe);
                    final TextView klassenart = (TextView) dialogView.findViewById(R.id.klassenart);

                    final TextView klasse1 = (TextView) dialogView.findViewById(R.id.klasse);

                    final TextView schulart_lbl = (TextView) dialogView.findViewById(R.id.l_Rechte);
                    final TextView klassenstufe_lbl = (TextView) dialogView.findViewById(R.id.textView2);
                    final TextView klassenart_lbl = (TextView) dialogView.findViewById(R.id.textView3);

                    final SeekBar s1 = (SeekBar) dialogView.findViewById(R.id.seekBar1);
                    final SeekBar s2 = (SeekBar) dialogView.findViewById(R.id.seekBar2);
                    final SeekBar s3 = (SeekBar) dialogView.findViewById(R.id.seekBar3);

                    s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser) {
                                if(progress == 0) schulart.setText("W");
                                if(progress == 1) schulart.setText("R");
                                if(progress == 2) schulart.setText("G");
                                klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
                            }
                        }
                    });
                    s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser) {
                                if(progress == 0) klassenstufe.setText("5");
                                if(progress == 1) klassenstufe.setText("6");
                                if(progress == 2) klassenstufe.setText("7");
                                if(progress == 3) klassenstufe.setText("8");
                                if(progress == 4) klassenstufe.setText("9");
                                if(progress == 5) klassenstufe.setText("10");
                                if(progress == 6) klassenstufe.setText("11");
                                if(progress == 7) klassenstufe.setText("12");
                                klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
                            }
                        }
                    });
                    s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser) {
                                if(progress == 0) klassenart.setText("a");
                                if(progress == 1) klassenart.setText("b");
                                klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
                            }
                        }
                    });

                    final SwitchCompat all_classes = (SwitchCompat) dialogView.findViewById(R.id.all_classes);
                    if(mode != MODE_CREATE_EVENT && mode != MODE_EDIT_EVENT) {
                        all_classes.setVisibility(View.GONE);
                        all_classes.setChecked(false);
                    } else {
                        all_classes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                s1.setEnabled(!isChecked);
                                s2.setEnabled(!isChecked);
                                s3.setEnabled(!isChecked);
                                klassenstufe.setEnabled(!isChecked);
                                schulart.setEnabled(!isChecked);
                                klassenart.setEnabled(!isChecked);
                                klassenstufe_lbl.setEnabled(!isChecked);
                                schulart_lbl.setEnabled(!isChecked);
                                klassenart_lbl.setEnabled(!isChecked);
                                klasse1.setText(res.getString(R.string.all_classes));
                                if(isChecked) {
                                    klasse1.setText(res.getString(R.string.all_classes));
                                } else {
                                    klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
                                }
                            }
                        });

                        s1.setEnabled(false);
                        s2.setEnabled(false);
                        s3.setEnabled(false);
                        klassenstufe.setEnabled(false);
                        schulart.setEnabled(false);
                        klassenart.setEnabled(false);
                        klassenstufe_lbl.setEnabled(false);
                        schulart_lbl.setEnabled(false);
                        klassenart_lbl.setEnabled(false);
                    }

                    if(all_classes.isChecked()) klasse1.setText(res.getString(R.string.all_classes));

                    if(old_receiver != null) choose_receiver.setText(old_receiver);

                    alert.setTitle(res.getString(R.string.please_coose_your_class_));

                    alert.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            choose_receiver.setText(klasse1.getText().toString());
                            dialog.cancel();
                        }
                    });
                    alert.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alert.create().show();
                }
            });
        }

        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.new_element_create);
        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == MODE_CREATE_CLASSTEST) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_element, menu);
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
