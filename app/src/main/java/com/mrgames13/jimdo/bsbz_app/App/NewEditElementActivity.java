package com.mrgames13.jimdo.bsbz_app.App;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

import java.net.URLEncoder;
import java.util.Calendar;

public class NewEditElementActivity extends AppCompatActivity {
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
    private Resources res;
    private ConnectivityManager cm;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Handler h = new Handler();
    private ProgressDialog pd;
    private StorageUtils su;
    private AccountUtils au;

    //Variablen
    private boolean pressedOnce;
    private int mode;
    private String activity_title;
    private String old_title;
    private String old_description;
    private String old_writer;
    private String old_receiver;
    private String old_date;
    private boolean result;
    private Account current_account;
    private String subject;
    private String description;
    private String date;
    private String receiver;
    private String from;

    @Override
    protected void onStart() {
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

        //Calendar initialisieren
        calendar = Calendar.getInstance();

        //StorageUtils initialisieren
        su = new StorageUtils(this, res);

        //AccountUtils initialisieren
        au = new AccountUtils(su);

        //Account laden
        current_account = au.getLastUser();

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
        old_description = getIntent().getStringExtra("old_description");
        old_writer = getIntent().getStringExtra("old_writer");
        old_receiver = getIntent().getStringExtra("old_receiver");
        old_date = getIntent().getStringExtra("old_date");

        //ActivityTitle festlegen
        if(mode == MODE_CREATE_CLASSTEST) {
            activity_title = res.getString(R.string.new_classtest);
        } else if(mode == MODE_CREATE_HOMEWORK) {
            activity_title = res.getString(R.string.new_homework);
        } else if(mode == MODE_CREATE_EVENT) {
            activity_title = res.getString(R.string.new_event);
        } else if(mode == MODE_EDIT_CLASSTEST) {
            activity_title = res.getString(R.string.edit_classtest);
        } else if(mode == MODE_EDIT_HOMEWORK) {
            activity_title = res.getString(R.string.edit_homework);
        } else if(mode == MODE_EDIT_EVENT) {
            activity_title = res.getString(R.string.edit_event);
        }

        //Writer-Textfeld initialisieren
        final EditText writer = (EditText) findViewById(R.id.new_element_writer);
        writer.setText(current_account.getUsername());
        if(old_writer != null) writer.setText(old_writer);

        final EditText betreff = (EditText) findViewById(R.id.new_element_betreff);
        final EditText beschreibung = (EditText) findViewById(R.id.new_element_description);
        if(old_title != null) betreff.setText(old_title);
        if(old_description != null) beschreibung.setText(old_description);

        //ChooseReceiver-Button initialisieren
        final Button choose_receiver = (Button) findViewById(R.id.new_element_choose_receiver);
        if(current_account.getRights() == Account.RIGHTS_CLASSSPEAKER) {
            choose_receiver.setText(current_account.getForm());
            choose_receiver.setEnabled(false);
        } else {
            if(old_receiver != null) choose_receiver.setText(old_receiver);
            choose_receiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert;
                    if(MainActivity.AppTheme == 0) {
                        alert = new AlertDialog.Builder(NewEditElementActivity.this, R.style.FirstTheme_Dialog);
                    } else {
                        alert = new AlertDialog.Builder(NewEditElementActivity.this, R.style.SecondTheme_Dialog);
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

        final Button choose_date = (Button) findViewById(R.id.new_element_choose_date);
        if(old_date != null) choose_date.setText(old_date);
        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.AppTheme == 0) {
                    datePickerDialog = new DatePickerDialog(NewEditElementActivity.this, R.style.FirstTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //Datum formatieren
                            String year_string = String.valueOf(year);
                            String month_string = String.valueOf(monthOfYear +1);
                            String day_string = String.valueOf(dayOfMonth);
                            if(month_string.length() < 2) month_string = "0" + month_string;
                            if(day_string.length() < 2) day_string = "0" + day_string;
                            //Button Aufschrift setzen
                            choose_date.setText(day_string + "." + month_string + "." + year_string);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                } else {
                    datePickerDialog = new DatePickerDialog(NewEditElementActivity.this, R.style.SecondTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //Datum formatieren
                            String year_string = String.valueOf(year);
                            String month_string = String.valueOf(monthOfYear +1);
                            String day_string = String.valueOf(dayOfMonth);
                            if(month_string.length() < 2) month_string = "0" + month_string;
                            if(day_string.length() < 2) day_string = "0" + day_string;
                            //Button Aufschrift setzen
                            choose_date.setText(day_string + "." + month_string + "." + year_string);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });

        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.new_element_create);
        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject = betreff.getText().toString().replace("~", "").replace("|", "").trim();
                description = beschreibung.getText().toString().replace("~", "").replace("|", "").trim();
                date = choose_date.getText().toString().replace("~", "").replace("|", "").trim();
                receiver = choose_receiver.getText().toString().replace("~", "").replace("|", "").trim();
                from = writer.getText().toString().trim();
                if(subject.equals("") || description.equals("") || date.equals(res.getString(R.string.choose_date)) || receiver.equals(res.getString(R.string.choose_receiver_)) || from.equals("")) {
                    Toast.makeText(NewEditElementActivity.this, res.getString(R.string.not_all_fields_filled), Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ProgressDialog anzeigen
                                    pd = new ProgressDialog(NewEditElementActivity.this);
                                    pd.setTitle(res.getString(R.string.please_wait_));
                                    if(mode == MODE_CREATE_CLASSTEST)pd.setMessage(res.getString(R.string.classtest_creating_));
                                    if(mode == MODE_CREATE_HOMEWORK)pd.setMessage(res.getString(R.string.homework_creating_));
                                    if(mode == MODE_CREATE_EVENT)pd.setMessage(res.getString(R.string.event_creating_));
                                    if(mode == MODE_EDIT_CLASSTEST || mode == MODE_EDIT_HOMEWORK || mode == MODE_EDIT_EVENT)pd.setMessage(res.getString(R.string.changings_uploading_));
                                    pd.setIndeterminate(true);
                                    pd.show();
                                }
                            });
                            //Name aus den SharedPreferences auslesen
                            String name = current_account.getUsername();
                            //Je nach Modus Element hochladen
                            result = false;
                            if(mode == MODE_CREATE_CLASSTEST || mode == MODE_CREATE_HOMEWORK || mode == MODE_CREATE_EVENT) {
                                if(receiver.equals(res.getString(R.string.all_classes))) receiver = "Alle";
                                result = createElement(subject, date, subject, description, receiver, from);
                            } else if(mode == MODE_EDIT_CLASSTEST || mode == MODE_EDIT_HOMEWORK || mode == MODE_EDIT_EVENT) {
                                if(receiver.equals(res.getString(R.string.all_classes))) receiver = "Alle";
                                result = editElement(old_title, name, date, subject, description, receiver, from);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Je nach Result handeln
                                    if(result) {
                                        pd.dismiss();
                                        Toast.makeText(NewEditElementActivity.this, res.getString(R.string.action_successful), Toast.LENGTH_SHORT).show();
                                        startService(new Intent(NewEditElementActivity.this, SyncronisationService.class));
                                        finish();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(NewEditElementActivity.this, res.getString(R.string.error_occured_try_again), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!pressedOnce) {
                pressedOnce = true;
                Toast.makeText(NewEditElementActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewEditElementActivity.this, R.string.press_again_to_go_back_delete_entry, Toast.LENGTH_SHORT).show();
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

    private boolean createElement(final String name, final String date, final String title, final String text, final String receiver, final String from) {
        if(!title.equals("") && !text.equals("") && !receiver.equals(res.getString(R.string.choose_receiver_)) && !date.equals(res.getString(R.string.choose_date))) {
            try{
                String element = "newclasstest";
                if(mode == MODE_CREATE_HOMEWORK) element = "newhomework";
                if(mode == MODE_CREATE_EVENT) element = "newevent";
                String result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(name, "UTF-8")+"&command="+element+"&date="+URLEncoder.encode(date, "UTF-8")+"&title="+URLEncoder.encode(title, "UTF-8")+"&text="+URLEncoder.encode(text, "UTF-8")+"&class="+URLEncoder.encode(receiver, "UTF-8")+"&from="+URLEncoder.encode(from, "UTF-8"));
                if(result.equals("Action Successful")) return true;
            } catch(Exception e) {}
        }
        return false;
    }

    private boolean editElement(final String old_title, final String name, final String date, final String title, final String text, final String receiver, final String from) {
        try{
            String element = "editclasstest";
            if(mode == MODE_EDIT_HOMEWORK) element = "edithomework";
            if(mode == MODE_EDIT_EVENT) element = "editevent";
            String result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(name, "UTF-8")+"&command="+element+"&old_title="+URLEncoder.encode(old_title, "UTF-8")+"&new_date="+URLEncoder.encode(date, "UTF-8")+"&new_title="+URLEncoder.encode(title, "UTF-8")+"&new_text="+URLEncoder.encode(text, "UTF-8")+"&new_class="+URLEncoder.encode(receiver, "UTF-8")+"&new_from="+URLEncoder.encode(from, "UTF-8"));
            if(result.equals("Action Successful")) return true;
        } catch(Exception e) {}
        return false;
    }
}
