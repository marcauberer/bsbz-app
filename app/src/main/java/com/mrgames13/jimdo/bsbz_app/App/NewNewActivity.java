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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class NewNewActivity extends AppCompatActivity {

    //Konstanten


    //Variablen als Objekte
    private Resources res;
    private FloatingActionButton fab;
    private SharedPreferences prefs;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private ConnectivityManager cm;

    //Utils-Pakete
    private ServerMessagingUtils serverMessagingUtils;

    //Komponenten
    private Toolbar toolbar;
    private SwitchCompat switch_timed_activation;
    private Button btn_choose_date_activation, btn_choose_date_expiration, btn_choose_receiver;
    private EditText etSubject, etDescription, etWriter;


    //Variablen
    private boolean pressedOnce;
    private String result;
    private String current_date;

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
        getSupportActionBar().setTitle(res.getString(R.string.title_activity_new_new));
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

        setContentView(R.layout.activity_new_edit_new);

        //Resourcen initialisieren
        res = getResources();

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(NewNewActivity.this);

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, this);

        //Toolbar aufsetzen
        toolbar = (Toolbar) findViewById(R.id.toolbar_new_new);
        setSupportActionBar(toolbar);

        //Calendar initialisieren
        calendar = Calendar.getInstance();

        //Aktuelles Datum einstellen
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatierer = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY);
        current_date = formatierer.format(date);

        //Komponenten voreinstellen
        //IDs finden
        switch_timed_activation = (SwitchCompat) findViewById(R.id.switch_timed_activation);
        btn_choose_date_activation = (Button) findViewById(R.id.btn_choose_activation_date);
        btn_choose_date_expiration = (Button) findViewById(R.id.btn_choose_expiration_date);
        etSubject = (EditText) findViewById(R.id.new_new_betreff);
        etDescription = (EditText) findViewById(R.id.new_new_description);
        etWriter = (EditText) findViewById(R.id.new_new_writer);

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
                if(MainActivity.AppTheme == 0) {
                    datePickerDialog = new DatePickerDialog(NewNewActivity.this, R.style.FirstTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
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
                } else {
                    datePickerDialog = new DatePickerDialog(NewNewActivity.this, R.style.SecondTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
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
            }
        });
        btn_choose_date_expiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.AppTheme == 0) {
                    datePickerDialog = new DatePickerDialog(NewNewActivity.this, R.style.FirstTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
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
                } else {
                    datePickerDialog = new DatePickerDialog(NewNewActivity.this, R.style.SecondTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
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
            }
        });

        //Textfeld des Verfassers initialisieren
        etWriter.setText(prefs.getString("Name", res.getString(R.string.guest)));

        //Empfänger-Button mit Dialog verknüpfen
        btn_choose_receiver = (Button) findViewById(R.id.choose_receiver);
        btn_choose_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert;
                if(MainActivity.AppTheme == 0) {
                    alert = new AlertDialog.Builder(NewNewActivity.this, R.style.FirstTheme_Dialog);
                } else {
                    alert = new AlertDialog.Builder(NewNewActivity.this, R.style.SecondTheme_Dialog);
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
                        Toast.makeText(NewNewActivity.this, res.getString(R.string.no_date_for_activation), Toast.LENGTH_SHORT).show();
                    } else if(btn_choose_date_expiration.getText().toString().equals(res.getString(R.string.choose_date))) {
                        Toast.makeText(NewNewActivity.this, res.getString(R.string.no_date_for_expiration), Toast.LENGTH_SHORT).show();
                    } else {
                        //Daten von den Komponenten abholen
                        String subject = etSubject.getText().toString().replace("~", "").replace("|", "").trim();
                        String desciption = etDescription.getText().toString().replace("~", "").replace("|", "").trim();
                        String writer = etWriter.getText().toString().replace("~", "").replace("|", "").trim();
                        String activation_date = btn_choose_date_activation.getText().toString().trim();
                        String expiration_date = btn_choose_date_expiration.getText().toString().trim();
                        //Nachricht erstellen
                        String klasse = btn_choose_receiver.getText().toString().trim();
                        if(klasse.equals(res.getString(R.string.all_classes))) klasse = "Alle";
                        createNew(subject, desciption, writer, activation_date, expiration_date, klasse);
                    }
                } else {
                    if(etSubject.getText().toString().equals("")) {
                        Toast.makeText(NewNewActivity.this, res.getString(R.string.no_subject), Toast.LENGTH_SHORT).show();
                    } else if(etDescription.getText().toString().equals("")) {
                        Toast.makeText(NewNewActivity.this, res.getString(R.string.no_description), Toast.LENGTH_SHORT).show();
                    } else if(etWriter.getText().toString().equals("")) {
                        Toast.makeText(NewNewActivity.this, res.getString(R.string.no_writer), Toast.LENGTH_SHORT).show();
                    } else if(btn_choose_receiver.getText().toString().equals(res.getString(R.string.choose_receiver_))) {
                        Toast.makeText(NewNewActivity.this, res.getString(R.string.no_receiver), Toast.LENGTH_SHORT).show();
                    } else {
                        //Daten von den Komponenten abholen
                        String subject = etSubject.getText().toString().replace("~", "").replace("|", "").trim();
                        String desciption = etDescription.getText().toString().replace("~", "").replace("|", "").trim();
                        String writer = etWriter.getText().toString().replace("~", "").replace("|", "").trim();
                        String activation_date = current_date;
                        String expiration_date = "0";
                        //Nachricht erstellen
                        String klasse = btn_choose_receiver.getText().toString().trim();
                        if(klasse.equals(res.getString(R.string.all_classes))) klasse = "Alle";
                        createNew(subject, desciption, writer, activation_date, expiration_date, klasse);
                    }
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
                Toast.makeText(NewNewActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewNewActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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

    private void createNew(final String subject, final String description, final String writer, final String activation_date, final String expiration_date, final String klasse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+ URLEncoder.encode(writer, "UTF-8")+"&command=newnew&subject="+URLEncoder.encode(subject, "UTF-8")+"&description="+URLEncoder.encode(description, "UTF-8")+"&activation_date="+URLEncoder.encode(activation_date, "UTF-8")+"&expiration_date="+URLEncoder.encode(expiration_date, "UTF-8")+"&class="+URLEncoder.encode(klasse, "UTF-8")+"&from="+URLEncoder.encode(writer, "UTF-8"));
                    if(result.equals("Action Successful")) {
                        result = res.getString(R.string.action_successful);
                    } else {
                        result = res.getString(R.string.error_try_again);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = res.getString(R.string.error_try_again);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewNewActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
                if(result.equals(res.getString(R.string.action_successful))) {
                    //Activity beenden
                    finish();
                    //Synchronisieren
                    startService(new Intent(NewNewActivity.this, SyncronisationService.class));
                }
            }
        }).start();
    }
}