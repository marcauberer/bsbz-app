package com.mrgames13.jimdo.bsbz_app.App;

import android.app.DatePickerDialog;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;

import java.net.URLEncoder;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class EditNewActivity extends AppCompatActivity {

    //Konstanten


    //Variablen als Objekte
    Resources res;
    FloatingActionButton fab;
    SharedPreferences prefs;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    ConnectivityManager cm;

    //Utils-Pakete
    ServerMessagingUtils serverMessagingUtils;

    //Komponenten
    Toolbar toolbar;
    SwitchCompat switch_timed_activation;
    Button btn_choose_date_activation, btn_choose_date_expiration, btn_choose_receiver;
    EditText etSubject, etDescription, etWriter;


    //Variablen
    private boolean pressedOnce;
    String result;
    String subject;
    String description;
    String writer;
    String activation_date;
    String expiration_date;
    String receiver;
    String old_subject;

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
        toolbar.setTitle(res.getString(R.string.title_activity_edit_new));

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
        }

        // ToolBar Titel festlegen
        getSupportActionBar().setTitle(res.getString(R.string.title_activity_edit_new));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_new);

        //Resourcen initialisieren
        res = getResources();

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(EditNewActivity.this);

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, this);

        //Toolbar aufsetzen
        toolbar = (Toolbar) findViewById(R.id.toolbar_new_new);
        setSupportActionBar(toolbar);

        //Calendar initialisieren
        calendar = Calendar.getInstance();

        //Intent-Extras auslesen
        Intent i = getIntent();
        subject = i.getStringExtra("Subject");
        description = i.getStringExtra("Description");
        writer = i.getStringExtra("Writer");
        activation_date = i.getStringExtra("Activation Date");
        expiration_date = i.getStringExtra("Expiration Date");
        receiver = i.getStringExtra("Receiver");
        //OldSubject-Variable setzen
        old_subject = subject;

        //Komponenten voreinstellen
        //IDs finden
        switch_timed_activation = (SwitchCompat) findViewById(R.id.switch_timed_activation);
        btn_choose_date_activation = (Button) findViewById(R.id.btn_choose_activation_date);
        btn_choose_date_expiration = (Button) findViewById(R.id.btn_choose_expiration_date);
        btn_choose_receiver = (Button) findViewById(R.id.choose_receiver);
        etSubject = (EditText) findViewById(R.id.new_new_betreff);
        etDescription = (EditText) findViewById(R.id.new_new_description);
        etWriter = (EditText) findViewById(R.id.new_new_writer);

        //Daten auf Komponenten übertragen
        etSubject.setText(subject);
        etDescription.setText(description);
        etWriter.setText(writer);
        if(receiver.equals("Alle")) receiver = res.getString(R.string.all_classes);
        btn_choose_receiver.setText(receiver);
        if(!activation_date.equals("0") && !expiration_date.equals("0")) {
            switch_timed_activation.setChecked(true);
            btn_choose_date_activation.setText(activation_date);
            btn_choose_date_expiration.setText(expiration_date);
        }

        //Voreinstellen
        if(!switch_timed_activation.isChecked()) {
            btn_choose_date_activation.setEnabled(false);
            btn_choose_date_expiration.setEnabled(false);
        } else {
            btn_choose_date_activation.setEnabled(true);
            btn_choose_date_expiration.setEnabled(true);
        }
        switch_timed_activation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    btn_choose_date_activation.setEnabled(false);
                    btn_choose_date_expiration.setEnabled(false);
                } else {
                    btn_choose_date_activation.setEnabled(true);
                    btn_choose_date_expiration.setEnabled(true);
                }
            }
        });
        //DatePickerButtons initialisieren
        btn_choose_date_activation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditNewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //Datum formatieren
                        String year_string = String.valueOf(year);
                        String month_string = String.valueOf(monthOfYear +1);
                        String day_string = String.valueOf(dayOfMonth);
                        if(month_string.length() < 2) month_string = "0" + month_string;
                        if(day_string.length() < 2) day_string = "0" + day_string;
                        //Button Aufschrift setzen
                        btn_choose_date_activation.setText(day_string + "." + month_string + "." + year_string);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        btn_choose_date_expiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditNewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //Datum formatieren
                        String year_string = String.valueOf(year);
                        String month_string = String.valueOf(monthOfYear +1);
                        String day_string = String.valueOf(dayOfMonth);
                        if(month_string.length() < 2) month_string = "0" + month_string;
                        if(day_string.length() < 2) day_string = "0" + day_string;
                        //Button Aufschrift setzen
                        btn_choose_date_expiration.setText(day_string + "." + month_string + "." + year_string);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)+7);
                datePickerDialog.show();
            }
        });

        //Empfänger-Button mit Dialog verknüpfen
        btn_choose_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditNewActivity.this);

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

                if(all_classes.isChecked()) klasse1.setText(res.getString(R.string.all_classes));

                alert.setTitle(res.getString(R.string.please_coose_your_class_));

                alert.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_choose_receiver.setText(klasse1.getText().toString());
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

        //FloatingActionButton initialisieren
        fab = (FloatingActionButton) findViewById(R.id.new_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch_timed_activation.isChecked()) {
                    if (btn_choose_date_activation.getText().toString().equals(res.getString(R.string.choose_date))) {
                        Toast.makeText(EditNewActivity.this, res.getString(R.string.no_date_for_activation), Toast.LENGTH_SHORT).show();
                    } else if(btn_choose_date_expiration.getText().toString().equals(res.getString(R.string.choose_date))) {
                        Toast.makeText(EditNewActivity.this, res.getString(R.string.no_date_for_expiration), Toast.LENGTH_SHORT).show();
                    } else {
                        //Daten von den Komponenten abholen
                        String subject = etSubject.getText().toString();
                        String desciption = etDescription.getText().toString();
                        String writer = etWriter.getText().toString();
                        String activation_date = btn_choose_date_activation.getText().toString();
                        String expiration_date = btn_choose_date_expiration.getText().toString();
                        //Nachricht erstellen
                        String klasse = btn_choose_receiver.getText().toString();
                        if(klasse.equals(res.getString(R.string.all_classes))) klasse = "Alle";
                        editNew(old_subject, subject, desciption, writer, activation_date, expiration_date, klasse);
                    }
                }
                if(etSubject.getText().toString().equals("")) {
                    Toast.makeText(EditNewActivity.this, res.getString(R.string.no_subject), Toast.LENGTH_SHORT).show();
                } else if(etDescription.getText().toString().equals("")) {
                    Toast.makeText(EditNewActivity.this, res.getString(R.string.no_description), Toast.LENGTH_SHORT).show();
                } else if(etWriter.getText().toString().equals("")) {
                    Toast.makeText(EditNewActivity.this, res.getString(R.string.no_writer), Toast.LENGTH_SHORT).show();
                } else if(btn_choose_receiver.getText().toString().equals(res.getString(R.string.choose_receiver_))) {
                    Toast.makeText(EditNewActivity.this, res.getString(R.string.no_receiver), Toast.LENGTH_SHORT).show();
                } else {
                    //Daten von den Komponenten abholen
                    String subject = etSubject.getText().toString();
                    String desciption = etDescription.getText().toString();
                    String writer = etWriter.getText().toString();
                    String activation_date = btn_choose_date_activation.getText().toString();
                    String expiration_date = btn_choose_date_expiration.getText().toString();
                    //Nachricht erstellen
                    String klasse = btn_choose_receiver.getText().toString();
                    if(klasse.equals(res.getString(R.string.all_classes))) klasse = "Alle";
                    editNew(old_subject, subject, desciption, writer, activation_date, expiration_date, klasse);
                }
            }
        });
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
                Toast.makeText(EditNewActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditNewActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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

    private void editNew(final String old_subject, final String new_subject, final String new_description, final String new_writer, final String new_activation_date, final String new_expiration_date, final String new_receiver) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+ URLEncoder.encode(new_writer, "UTF-8")+"&command=editnew&old_subject="+URLEncoder.encode(old_subject, "UTF-8")+"&new_subject="+URLEncoder.encode(new_subject, "UTF-8")+"&new_description="+URLEncoder.encode(new_description, "UTF-8")+"&new_activation_date="+URLEncoder.encode(new_activation_date, "UTF-8")+"&new_expiration_date="+URLEncoder.encode(new_expiration_date, "UTF-8")+"&new_class="+URLEncoder.encode(new_receiver, "UTF-8")+"&new_from="+URLEncoder.encode(new_writer, "UTF-8"));
                    if(result.equals("Action Successful")) {
                        result = res.getString(R.string.action_successful);
                    } else {
                        result = res.getString(R.string.error_try_again);
                    }
                } catch (Exception e) {
                    result = res.getString(R.string.error_try_again);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditNewActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
                if(result.equals(res.getString(R.string.action_successful))) {
                    //Activity beenden
                    finish();
                    //Synchronisieren
                    startService(new Intent(EditNewActivity.this, SyncronisationService.class));
                }
            }
        }).start();
    }
}