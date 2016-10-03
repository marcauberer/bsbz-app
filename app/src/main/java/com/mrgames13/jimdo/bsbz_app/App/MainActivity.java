package com.mrgames13.jimdo.bsbz_app.App;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Classtest;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Event;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Homework;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.New;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters.ElementViewAdapter;
import com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters.GalleryViewAdapter_Folders;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.NotificationUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Konstanten
    private final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 489;
    public static String KEINE_TERMINE_MONAT = "";
    public static String KEINE_KLASSENARBEITEN_MONAT = "";
    public static String KEINE_HAUSAUFGABEN_MONAT = "";
    public static String KEINE_TERMINE_TAG = "";
    public static String KEINE_KLASSENARBEITEN_TAG = "";
    public static String KEINE_HAUSAUFGABEN_TAG = "";

    //Variablen als Objekte
    private Toolbar toolbar;
    private DrawerLayout drawer_layout_gesamt;
    private ActionBarDrawerToggle drawer_toggle;
    private NavigationView navView;
    public Menu action_menu;
    public static MenuItem progress_menu_item;
    private ViewGroup container;
    private LayoutInflater layoutInflater;
    public static ArrayAdapter<String> adapter;
    public static ArrayList<String> arraylist = new ArrayList<String>();
    public static ArrayList<String> arraylist_main = new ArrayList<String>();
    public static SharedPreferences prefs;
    private static FragmentManager fragmentManager;
    private static ConnectivityManager cm;
    public static SyncronisationService.onSyncFinishedListener syncFinishedListener;
    public static Resources res;
    private ProgressDialog pd_Progress;
    private RecyclerView gallery_view;
    private RecyclerView news_view;
    private RecyclerView year_view;
    private RecyclerView today_view;
    private RecyclerView.Adapter gallery_view_adapter;
    private RecyclerView.Adapter news_view_adapter;
    private RecyclerView.Adapter year_view_adapter;
    private RecyclerView.Adapter today_view_adapter;
    private RecyclerView.LayoutManager gallery_view_manager;
    private RecyclerView.LayoutManager news_view_manager;
    private RecyclerView.LayoutManager year_view_manager;
    private RecyclerView.LayoutManager today_view_manager;
    private FloatingActionButton new_folder;
    public static ArrayList<Classtest> classtests;
    public static ArrayList<Homework> homeworks;
    public static ArrayList<Event> events;
    public static ArrayList<Object> all;
    public static ArrayList<New> news;

    //UtilsPakete
    public static ServerMessagingUtils serverMessagingUtils;
    public static StorageUtils su;
    private NotificationUtils nu;
    private AccountUtils au;

    //Variablen
    private boolean pressedOnce = false;
    public static int AppTheme = 0;
    public static boolean isUpdateAvailable = false;
    public static boolean isRunning = false;
    private static String mo2 = "";
    private static String di2 = "";
    private static String mi2 = "";
    private static String do2 = "";
    private static String fr2 = "";
    private static int selectedMonth = 1;
    public static String color = "#ea690c";
    private static int Selected = 0;
    private static int selected_Menu_Item = 1;
    public static String date1 = "";
    public static String date2 = "";
    private boolean showInvisibleEntries = false;
    private static String result;
    private String currentAppVersion;
    public static ArrayList<String> gallery_view_foldernames;
    public static ArrayList<String> gallery_view_filenames;

    //--------------------------------------------------------------------- Klassenmethoden ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Theme setzen
        if(AppTheme == 0) {
            setTheme(R.style.FirstTheme);
        } else if(AppTheme == 1) {
            setTheme(R.style.SecondTheme);
        }

        setContentView(R.layout.activity_main);

        //Resourcen initialisieren
        res = getResources();

        //AppVersion ermitteln
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentAppVersion = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e1) {}

        //Toolbar finden
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //Container finden
        container = (ViewGroup) findViewById(R.id.container);

        //LayoutInflater initialisieren
        layoutInflater = getLayoutInflater();

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        //StorageUtils initialisieren
        su = new StorageUtils(MainActivity.this);

        //AccountUtils initialisieren
        au = new AccountUtils(su);

        //NotificationUtils initialisieren
        nu = new NotificationUtils(MainActivity.this);

        //DrawerLayout finden
        drawer_layout_gesamt = (DrawerLayout) findViewById(R.id.drawer_layout_gesamt);
        //DrawerToggle aufsetzen
        drawer_toggle = new ActionBarDrawerToggle(MainActivity.this, drawer_layout_gesamt, R.string.navDrawer_opened, R.string.navDrawer_closed);
        //DrawerToggle setzen
        drawer_layout_gesamt.setDrawerListener(drawer_toggle);
        //NavigationView finden
        navView = (NavigationView) findViewById(R.id.navView);
        if(AppTheme == 1) {
            //Bei dunklem Layout Bild austauschen
            ImageView navView_Image = (ImageView) navView.getHeaderView(0).findViewById(R.id.navView_image);
            navView_Image.setImageResource(R.drawable.bsbz_logo_gross_mrgames_black);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                String item_text = menuItem.getTitle().toString();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_item_account: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 1;
                        toolbar.setTitle(res.getString(R.string.my_profile));
                        launchProfileFragment();
                        break;
                    }
                    case R.id.drawer_item_today: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 2;
                        toolbar.setTitle(res.getString(R.string.today));
                        launchTodayFragment();
                        break;
                    }
                    case R.id.drawer_item_this_week: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 3;
                        toolbar.setTitle(res.getString(R.string.this_week));
                        launchThisWeekFragment();
                        break;
                    }
                    case R.id.drawer_item_plan_of_the_year: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 4;
                        toolbar.setTitle(res.getString(R.string.plan_of_the_year));
                        launchPlanOfTheYearFragment();
                        break;
                    }
                    case R.id.drawer_item_news: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 5;
                        toolbar.setTitle(res.getString(R.string.news));
                        launchNewsFragment();
                        break;
                    }
                    case R.id.drawer_item_food_plan: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 6;
                        toolbar.setTitle(res.getString(R.string.food_plan));
                        launchFoodPlanFragment();
                        break;
                    }
                    case R.id.drawer_item_gallery: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 7;
                        toolbar.setTitle(res.getString(R.string.galery));
                        //Permission 'WRITE_EXTERNAL_STORAGE' abfragen
                        if(!(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
                        } else {
                            launchGalleryFragment();
                        }
                        break;
                    }
                    case R.id.drawer_item_bsbz_infos: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 8;
                        toolbar.setTitle(res.getString(R.string.bsbz_infos));
                        launchBSBZInfoFragment();
                        break;
                    }
                    case R.id.drawer_item_the_developers: {
                        menuItem.setChecked(true);
                        selected_Menu_Item = 9;
                        toolbar.setTitle(res.getString(R.string.the_developers));
                        launchDeveloperFragment();
                        break;
                    }
                    case R.id.drawer_item_settings: {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            }
                        }, 200);
                        break;
                    }
                }
                drawer_layout_gesamt.closeDrawers();
                return false;
            }
        });
        //DrawerToggle aktivieren
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer_toggle.syncState();

        //FragmentManager initialisieren
        fragmentManager = getSupportFragmentManager();

        //String-Konstanten initialisieren
        KEINE_TERMINE_MONAT = res.getString(R.string.keine_termine_monat);
        KEINE_KLASSENARBEITEN_MONAT = res.getString(R.string.keine_klassenarbeiten_monat);
        KEINE_HAUSAUFGABEN_MONAT = res.getString(R.string.keine_hausaufgaben_monat);
        KEINE_TERMINE_TAG = res.getString(R.string.keine_termine_heute);
        KEINE_KLASSENARBEITEN_TAG = res.getString(R.string.keine_klassenarbeiten_heute);
        KEINE_HAUSAUFGABEN_TAG = res.getString(R.string.keine_hausaufgaben_heute);

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, MainActivity.this);

        if(AppTheme != 0) findViewById(R.id.copyright).setBackgroundResource(R.color.background_gray);

        //All erstellen
        all = new ArrayList<>();

        try {
            //Extras auslesen
            String extra = getIntent().getExtras().getString("Open");
            if(extra.equals("Diese Woche")) {
                navView.getMenu().getItem(2).setChecked(true);
            } else if(extra.equals("Jahresplan")) {
                navView.getMenu().getItem(3).setChecked(true);
            } else if(extra.equals("Today")) {
                navView.getMenu().getItem(1).setChecked(true);
            } else {
                Log.d("BSBZ-App", "Nicht verständliches Extra");
            }
        } catch(NullPointerException e) {}

        String custom_startpage = prefs.getString("CustomStartPage", "Mein Profil (Standard)");
        if(custom_startpage.equals("Mein Profil (Standard)")) selected_Menu_Item = 1;
        if(custom_startpage.equals("Heute")) selected_Menu_Item = 2;
        if(custom_startpage.equals("Diese Woche")) selected_Menu_Item = 3;
        if(custom_startpage.equals("Jahresplan")) selected_Menu_Item = 4;
        if(custom_startpage.equals("News")) selected_Menu_Item = 5;
        if(custom_startpage.equals("Speiseplan")) selected_Menu_Item = 6;
        if(custom_startpage.equals("Bildergalerie")) selected_Menu_Item = 7;
        if(custom_startpage.equals("BSBZ-Infos")) selected_Menu_Item = 8;
        if(custom_startpage.equals("Die Entwickler")) selected_Menu_Item = 9;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        action_menu = menu;
        progress_menu_item = action_menu.findItem(R.id.action_refresh);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Abfrage, welches Item selektiert wurde
        if (drawer_toggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor e = prefs.edit();
                e.putBoolean(res.getString(R.string.keepLoggedIn), false);
                e.putString("Name", res.getString(R.string.guest));
            e.commit();
            Toast.makeText(MainActivity.this, res.getString(R.string.logoutInProgress), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            overridePendingTransition(R.anim.in_login, R.anim.out_logout);
            finish();
            return true;
        } else if (id == R.id.action_check_for_update) {
            checkAppVersion(MainActivity.this, true, true);
        } else if (id == R.id.action_finish) {
            finish();
            return true;
        } else if (id == R.id.action_recommend) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, res.getString(R.string.recommend_string));
            i.setType("text/plain");
            startActivity(i);
        } else if (id == R.id.action_refresh) {
            if(serverMessagingUtils.isInternetAvailable()) {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startService(new Intent(MainActivity.this, SyncronisationService.class));
                        }
                    }).start();
                    progress_menu_item.setActionView(R.layout.menu_item_layout);
                    syncFinishedListener = new SyncronisationService.onSyncFinishedListener() {
                        @Override
                        public void onSyncFinished() {
                            progress_menu_item.setActionView(null);
                            //Fragmente refreshen
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(selected_Menu_Item == 1) {
                                        launchProfileFragment();
                                    } else if(selected_Menu_Item == 2) {
                                        launchTodayFragment();
                                    } else if(selected_Menu_Item == 3) {
                                        launchThisWeekFragment();
                                    } else if(selected_Menu_Item == 4) {
                                        launchPlanOfTheYearFragment();
                                    } else if(selected_Menu_Item == 5) {
                                        launchNewsFragment();
                                    } else if(selected_Menu_Item == 6) {
                                        launchFoodPlanFragment();
                                    } else if(selected_Menu_Item == 7) {
                                        launchGalleryFragment();
                                    } else if(selected_Menu_Item == 8) {
                                        launchBSBZInfoFragment();
                                    } else if(selected_Menu_Item == 9) {
                                        launchDeveloperFragment();
                                    }
                                }
                            });
                        }
                    };
                    isRunning = true;
                } catch(Exception e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startService(new Intent(MainActivity.this, SyncronisationService.class));
                        }
                    }).start();
                }
            } else {
                serverMessagingUtils.checkConnection(findViewById(R.id.container));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawer_toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawer_toggle.onConfigurationChanged(new Configuration());
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Daten von den SharedPreferences abrufen
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String layout = prefs.getString("Layout", res.getString(R.string.bsbz_layout_orange));
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(darkenColor(Color.parseColor(color)));
        }

        //SyncFreq herausfinden
        String syncfreq = prefs.getString("SyncFreq", "3600000");
        if(syncfreq.equals("3600000")) {
            SharedPreferences.Editor e = prefs.edit();
            e.putString("SyncFreq",syncfreq);
            e.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Seite  refreshen
        if(selected_Menu_Item == 1) {
            getSupportActionBar().setTitle(res.getString(R.string.my_profile));
            launchProfileFragment();
            navView.getMenu().getItem(0).setChecked(true);
        } else if(selected_Menu_Item == 2) {
            getSupportActionBar().setTitle(res.getString(R.string.today));
            launchTodayFragment();
            navView.getMenu().getItem(1).setChecked(true);
        } else if(selected_Menu_Item == 3) {
            getSupportActionBar().setTitle(res.getString(R.string.this_week));
            launchThisWeekFragment();
            navView.getMenu().getItem(2).setChecked(true);
        } else if(selected_Menu_Item == 4) {
            getSupportActionBar().setTitle(res.getString(R.string.plan_of_the_year));
            launchPlanOfTheYearFragment();
            navView.getMenu().getItem(3).setChecked(true);
        } else if(selected_Menu_Item == 5) {
            getSupportActionBar().setTitle(res.getString(R.string.news));
            launchNewsFragment();
            navView.getMenu().getItem(4).setChecked(true);
        } else if(selected_Menu_Item == 6) {
            getSupportActionBar().setTitle(res.getString(R.string.food_plan));
            launchFoodPlanFragment();
            navView.getMenu().getItem(5).setChecked(true);
        } else if(selected_Menu_Item == 7) {
            getSupportActionBar().setTitle(res.getString(R.string.galery));
            launchGalleryFragment();
            navView.getMenu().getItem(6).setChecked(true);
        } else if(selected_Menu_Item == 8) {
            getSupportActionBar().setTitle(res.getString(R.string.bsbz_infos));
            launchBSBZInfoFragment();
            navView.getMenu().getItem(7).setChecked(true);
        } else if(selected_Menu_Item == 9) {
            getSupportActionBar().setTitle(res.getString(R.string.the_developers));
            launchDeveloperFragment();
            navView.getMenu().getItem(8).setChecked(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!pressedOnce) {
                pressedOnce = true;
                Toast.makeText(MainActivity.this, R.string.press_again_to_exit_app, Toast.LENGTH_SHORT).show();
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

    //--------------------------------------------------------------------- Eigene Methoden ------------------------------------------------------------------------

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    public void launchProfileFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        layoutInflater.inflate(R.layout.fragment_profil, container);
        //Funktionalität einrichten
        //Daten von den SharedPreferences abrufen
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String User_name = prefs.getString("Name", res.getString(R.string.max_musterman)).replace("+", " ");
        String User_klasse = prefs.getString("Klasse", "---");
        String User_rechte = prefs.getString("Rights", "student");
        String last_syncronisation_time = prefs.getString("SyncTime", res.getString(R.string.no_synchronisation));

        if(User_klasse.length() == 3) User_klasse.replace("0", "");

        //IDs herausfinden
        TextView Profil_name = (TextView) findViewById(R.id.Profil_Name);
        TextView Profil_klasse = (TextView) findViewById(R.id.Profil_Klasse);
        TextView Profil_Rechte = (TextView) findViewById(R.id.Profil_Rechte);
        TextView l_Rechte = (TextView) findViewById(R.id.l_Rechte);
        final Button klasse_wahlen = (Button) findViewById(R.id.klasse_wahlen);
        klasse_wahlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alert;
                if(AppTheme == 0) {
                    alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.FirstTheme_Dialog);
                } else {
                    alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.SecondTheme_Dialog);
                }

                View dialogView = layoutInflater.inflate(R.layout.dialogview_class_chooser, null);
                alert.setView(dialogView);

                final TextView schulart = (TextView) dialogView.findViewById(R.id.schulart);
                final TextView klassenstufe = (TextView) dialogView.findViewById(R.id.klassenstufe);
                final TextView klassenart = (TextView) dialogView.findViewById(R.id.klassenart);

                final TextView klasse1 = (TextView) dialogView.findViewById(R.id.klasse);

                SeekBar s1 = (SeekBar) dialogView.findViewById(R.id.seekBar1);
                SeekBar s2 = (SeekBar) dialogView.findViewById(R.id.seekBar2);
                SeekBar s3 = (SeekBar) dialogView.findViewById(R.id.seekBar3);

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

                alert.setTitle(res.getString(R.string.please_coose_your_class_));

                alert.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        klasse_wahlen.setText(res.getString(R.string.choose_class_1_)+klasse1.getText().toString()+")");

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor e = prefs.edit();
                            e.putString("Klasse", klasse1.getText().toString());
                        e.commit();

                        Synchronize(klasse1.getText().toString(), MainActivity.this);
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
        //TextView Profil_email = (TextView) findViewById(R.id.Profil_Email);
        TextView Profil_last_syncronisation_time = (TextView) findViewById(R.id.last_syncronisation_time);

        //Texte setzen
        Profil_name.setText(User_name);
        Profil_klasse.setText(User_klasse);
        if(User_klasse.equals("no_class")) Profil_klasse.setText(res.getString(R.string.several_classes));
        if(User_rechte.equals("classspeaker")) {
            Profil_Rechte.setText(res.getString(R.string.classspeaker));
            klasse_wahlen.setVisibility(View.INVISIBLE);
        } else if(User_rechte.equals("parent")) {
            Profil_Rechte.setText(res.getString(R.string.parent));
            klasse_wahlen.setText(res.getString(R.string.choose_class_1_)+User_klasse+")");
        } else if(User_rechte.equals("teacher")) {
            Profil_Rechte.setText(res.getString(R.string.teacher));
            klasse_wahlen.setText(res.getString(R.string.choose_class_1_)+User_klasse+")");
            if(User_klasse.equals("no_class")) klasse_wahlen.setText(res.getString(R.string.choose_class));
        } else if(User_rechte.equals("administrator")) {
            Profil_Rechte.setText(res.getString(R.string.administrator));
            klasse_wahlen.setText(res.getString(R.string.choose_class_1_)+User_klasse+")");
        } else if(User_rechte.equals("team")) {
            Profil_Rechte.setText(res.getString(R.string.team_mrgames));
            klasse_wahlen.setText(res.getString(R.string.choose_class_1_)+User_klasse+")");
        } else if(User_rechte.equals("guest")) {
            l_Rechte.setVisibility(View.INVISIBLE);
            Profil_Rechte.setVisibility(View.INVISIBLE);
            klasse_wahlen.setText(res.getString(R.string.choose_class_1_)+User_klasse+")");
        } else {
            l_Rechte.setVisibility(View.INVISIBLE);
            Profil_Rechte.setVisibility(View.INVISIBLE);
            klasse_wahlen.setVisibility(View.INVISIBLE);
        }
        //Profil_email.setText(User_email);
        //Date + Time ausgeben
        Profil_last_syncronisation_time.setText("Letzte Syncronisation: "+last_syncronisation_time);

        //Feedback-Button einrichten
        Button feedback = (Button) findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                i.putExtra("Webside", "http://mrgames13.jimdo.com/feedback-kommentare/");
                i.putExtra("Title", "Feedback geben");
                startActivity(i);
            }
        });

        //Ideene zur Weiterentwicklung-Button einrichten
        Button ideen = (Button) findViewById(R.id.ideen);
        ideen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                i.putExtra("Webside", "http://mrgames13.jimdo.com/info/bsbz-app/ideen-zur-weiterentwicklung/");
                i.putExtra("Title", "Ideen zur Weiterentwicklung");
                startActivity(i);
            }
        });

        //Arraylists für die Bildergallery erstellen
        gallery_view_foldernames = new ArrayList<>();
        gallery_view_filenames = new ArrayList<>();
    }

    public void launchTodayFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_heute, container);

        //Aktuelles Datum ermitteln und die Daten für dieses Datum laden
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatierer = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.GERMANY);
        String date_today = formatierer.format(date);

        classtests = su.parseClasstests(null, date_today);
        homeworks = su.parseHomeworks(null, date_today);
        events = su.parseEvents(null, date_today);
        all.clear();
        all.addAll(classtests);
        all.addAll(homeworks);
        all.add(events);

        //NewsRecyclerView anzeigen
        today_view = (RecyclerView) findViewById(R.id.today_recycler_view);
        today_view_manager = new LinearLayoutManager(MainActivity.this);
        today_view.setLayoutManager(today_view_manager);
        today_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
        today_view.setAdapter(today_view_adapter);

        if(today_view_adapter.getItemCount() == 0) findViewById(R.id.no_data).setVisibility(View.VISIBLE);

        //FloatingActionButton
        FloatingActionButton new_element = (FloatingActionButton) findViewById(R.id.today_new_element);
        new_element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(res.getString(R.string.create_))
                        .setView(R.layout.dialogview_chooser_element)
                        .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(res.getString(R.string.next), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SwitchCompat sw1 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_classtest);
                                SwitchCompat sw2 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_homework);
                                SwitchCompat sw3 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_event);
                                if(sw1.isChecked()) {
                                    Intent i = new Intent(MainActivity.this, NewEditElementActivity.class);
                                    i.putExtra("mode", NewEditElementActivity.MODE_CREATE_CLASSTEST);
                                    startActivity(i);
                                } else if(sw2.isChecked()) {
                                    Intent i = new Intent(MainActivity.this, NewEditElementActivity.class);
                                    i.putExtra("mode", NewEditElementActivity.MODE_CREATE_HOMEWORK);
                                    startActivity(i);
                                } else if(sw3.isChecked()) {
                                    Intent i = new Intent(MainActivity.this, NewEditElementActivity.class);
                                    i.putExtra("mode", NewEditElementActivity.MODE_CREATE_EVENT);
                                    startActivity(i);
                                }
                            }
                        })
                        .create();
                d.show();

                final SwitchCompat sw1 = (SwitchCompat) d.findViewById(R.id.chooser_element_classtest);
                final SwitchCompat sw2 = (SwitchCompat) d.findViewById(R.id.chooser_element_homework);
                final SwitchCompat sw3 = (SwitchCompat) d.findViewById(R.id.chooser_element_event);
                //Auf Änderungen reagieren
                sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(isChecked);
                            sw2.setChecked(false);
                            sw3.setChecked(false);
                        }
                    }
                });
                sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(isChecked);
                            sw3.setChecked(false);
                        }
                    }
                });
                sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(false);
                            sw3.setChecked(isChecked);
                        }
                    }
                });
            }
        });

        //Stundenplan zeichnen
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int weekday = cal.get(Calendar.DAY_OF_WEEK);

        String weekString = "";

        if(weekday == 2) {
            weekString = "Mo";
        } else if (weekday == 3) {
            weekString = "Di";
        } else if (weekday == 4) {
            weekString = "Mi";
        } else if (weekday == 5) {
            weekString = "Do";
        } else if (weekday == 6) {
            weekString = "Fr";
        } else if (weekday == 7) {
            weekString = "Mo";
        } else if (weekday == 1) {
            weekString = "Mo";
        }

        TextView tv = (TextView) rootView.findViewById(R.id.SV);
        tv.setText(res.getString(R.string.timetable_from_) + weekString);

        //Daycode herausfinden
        TimeTable tt = su.getTimeTable(prefs.getString("Klasse", "no_class"));
        String daycode = "";
        if(tt != null) {
            if(weekString.equals("Mo")) daycode = tt.getMo();
            if(weekString.equals("Di")) daycode = tt.getDi();
            if(weekString.equals("Mi")) daycode = tt.getMi();
            if(weekString.equals("Do")) daycode = tt.getDo();
            if(weekString.equals("Fr")) daycode = tt.getFr();
        } else {
            daycode = "-,-,-,-,-,-,-,-,-,-";
        }

        //Hourcode herausfinden
        int index1 = daycode.indexOf(",", 0);
        int index2 = daycode.indexOf(",", index1 +1);
        int index3 = daycode.indexOf(",", index2 +1);
        int index4 = daycode.indexOf(",", index3 +1);
        int index5 = daycode.indexOf(",", index4 +1);
        int index6 = daycode.indexOf(",", index5 +1);
        int index7 = daycode.indexOf(",", index6 +1);
        int index8 = daycode.indexOf(",", index7 +1);
        int index9 = daycode.indexOf(",", index8 +1);
        String hour1 = daycode.substring(0, index1);
        String hour2 = daycode.substring(index1 +1, index2);
        String hour3 = daycode.substring(index2 +1, index3);
        String hour4 = daycode.substring(index3 +1, index4);
        String hour5 = daycode.substring(index4 +1, index5);
        String hour6 = daycode.substring(index5 +1, index6);
        String hour7 = daycode.substring(index6 +1, index7);
        String hour8 = daycode.substring(index7 +1, index8);
        String hour9 = daycode.substring(index8 +1, index9);
        String hour10 = daycode.substring(index9 +1);

        TextView tt_1 = (TextView) rootView.findViewById(R.id.tt_1);
        tt_1.setText(hour1);
        TextView tt_2 = (TextView) rootView.findViewById(R.id.tt_2);
        tt_2.setText(hour2);
        TextView tt_3 = (TextView) rootView.findViewById(R.id.tt_3);
        tt_3.setText(hour3);
        TextView tt_4 = (TextView) rootView.findViewById(R.id.tt_4);
        tt_4.setText(hour4);
        TextView tt_5 = (TextView) rootView.findViewById(R.id.tt_5);
        tt_5.setText(hour5);
        TextView tt_6 = (TextView) rootView.findViewById(R.id.tt_6);
        tt_6.setText(hour6);
        TextView tt_7 = (TextView) rootView.findViewById(R.id.tt_7);
        tt_7.setText(hour7);
        TextView tt_8 = (TextView) rootView.findViewById(R.id.tt_8);
        tt_8.setText(hour8);
        TextView tt_9 = (TextView) rootView.findViewById(R.id.tt_9);
        tt_9.setText(hour9);
        TextView tt_10 = (TextView) rootView.findViewById(R.id.tt_10);
        tt_10.setText(hour10);

        //Fortschrittsbalken zeichnen
        long start = 0;
        long end = 0;
        long now = 0;

        if(!hour1.equals("-")) {
            if(start == 0) {
                start = 27000000;
            } else {
                end = 29700000;
            }
        } if(!hour2.equals("-")) {
            if(start == 0) {
                start = 29700000;
            } else {
                end = 32400000;
            }
        } if(!hour3.equals("-")) {
            if(start == 0) {
                start = 32400000;
            } else {
                end = 35400000;
            }
        } if(!hour4.equals("-")) {
            if(start == 0) {
                start = 36600000;
            } else {
                end = 39300000;
            }
        } if(!hour5.equals("-")) {
            if(start == 0) {
                start = 39300000;
            } else {
                end = 42000000;
            }
        } if(!hour6.equals("-")) {
            if(start == 0) {
                start = 42300000;
            } else {
                end = 45000000;
            }
        } if(!hour7.equals("-")) {
            if(start == 0) {
                start = 47700000;
            } else {
                end = 50400000;
            }
        } if(!hour8.equals("-")) {
            if(start == 0) {
                start = 50400000;
            } else {
                end = 53400000;
            }
        } if(!hour9.equals("-")) {
            if(start == 0) {
                start = 53400000;
            } else {
                end = 56400000;
            }
        } if(!hour10.equals("-")) {
            if(start == 0) {
                start = 56400000;
            } else {
                end = 59100000;
            }
        }

        //Aktuelle Uhrzeit ermitteln
        String zeit = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, Locale.GERMANY).format(date);
        zeit = zeit.substring(11,16);
        if(zeit.length() == 4) zeit = "0" + zeit;
        //Fehlerträchtige Berechnung
        now = (Integer.parseInt(zeit.substring(0, zeit.indexOf(":"))) * 60 + Integer.parseInt(zeit.substring(zeit.indexOf(":") + 1))) * 60000;

        ProgressBar progbar = (ProgressBar) rootView.findViewById(R.id.Percent_Bar);
        progbar.getProgressDrawable().setColorFilter(Color.parseColor(color), android.graphics.PorterDuff.Mode.SRC_IN);
        try{
            long percent = ((now - start) * 100) / (end - start);
            //In Ladebalken eintragen

            if(percent > 100) {
                percent = 100;
            } else if(percent < 0) {
                percent = 0;
            }

            TextView prozentanzeige = (TextView) rootView.findViewById(R.id.Prozentanzeige);
            if(weekday != 7 && weekday != 1) {
                progbar.setProgress((int) percent);
                prozentanzeige.setText(String.valueOf(percent) + " %");
            } else {
                progbar.setProgress(0);
                prozentanzeige.setText("0 %");
            }
        } catch(Exception e) {}
    }

    public void launchThisWeekFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        layoutInflater.inflate(R.layout.fragment_diese_woche, container);
        //Wochentag ermitteln
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int weekday = cal.get(Calendar.DAY_OF_WEEK);

        String weekString = "";
        if(weekday == 2) {
            weekString = "Mo";
        } else if (weekday == 3) {
            weekString = "Di";
        } else if (weekday == 4) {
            weekString = "Mi";
        } else if (weekday == 5) {
            weekString = "Do";
        } else if (weekday == 6) {
            weekString = "Fr";
        } else if (weekday == 7) {
            weekString = "Sa";
        } else if (weekday == 1) {
            weekString = "So";
        }
        //Datums ausrechnen
        DateFormat formatierer = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.GERMANY);
        long millis = System.currentTimeMillis();

        if(weekString.equals("Mo")) {
            //Datums ermitteln
            Date mo1 = new Date(millis);
            Date di1 = new Date(millis + 86400000);
            Date mi1 = new Date(millis + 86400000 + 86400000);
            Date do1 = new Date(millis + 86400000 + 86400000 + 86400000);
            Date fr1 = new Date(millis + 86400000 + 86400000 + 86400000 + 86400000);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        } else if(weekString.equals("Di")) {
            //Datums ermitteln
            Date mo1 = new Date(millis - 86400000);
            Date di1 = new Date(millis);
            Date mi1 = new Date(millis + 86400000);
            Date do1 = new Date(millis + 86400000 + 86400000);
            Date fr1 = new Date(millis + 86400000 + 86400000 + 86400000);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        } else if(weekString.equals("Mi")) {
            //Datums ermitteln
            Date mo1 = new Date(millis - 86400000 - 86400000);
            Date di1 = new Date(millis - 86400000);
            Date mi1 = new Date(millis);
            Date do1 = new Date(millis + 86400000);
            Date fr1 = new Date(millis + 86400000 + 86400000);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        } else if(weekString.equals("Do")) {
            //Datums ermitteln
            Date mo1 = new Date(millis - 86400000 - 86400000 - 86400000);
            Date di1 = new Date(millis - 86400000 - 86400000);
            Date mi1 = new Date(millis - 86400000);
            Date do1 = new Date(millis);
            Date fr1 = new Date(millis + 86400000);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        } else if(weekString.equals("Fr")) {
            //Datums ermitteln
            Date mo1 = new Date(millis - 86400000 - 86400000 - 86400000 - 86400000);
            Date di1 = new Date(millis - 86400000 - 86400000 - 86400000);
            Date mi1 = new Date(millis - 86400000 - 86400000);
            Date do1 = new Date(millis - 86400000);
            Date fr1 = new Date(millis);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        } else if(weekString.equals("Sa")) {
            //Datums ermitteln
            Date mo1 = new Date(millis + 86400000 + 86400000);
            Date di1 = new Date(millis + 86400000 + 86400000 + 86400000);
            Date mi1 = new Date(millis + 86400000 + 86400000 + 86400000 + 86400000);
            Date do1 = new Date(millis + 86400000 + 86400000 + 86400000 + 86400000 + 86400000);
            Date fr1 = new Date(millis + 86400000 + 86400000 + 86400000 + 86400000 + 86400000 + 86400000);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        } else if(weekString.equals("So")) {
            //Datums ermitteln
            Date mo1 = new Date(millis + 86400000);
            Date di1 = new Date(millis + 86400000 + 86400000);
            Date mi1 = new Date(millis + 86400000 + 86400000 + 86400000);
            Date do1 = new Date(millis + 86400000 + 86400000 + 86400000 + 86400000);
            Date fr1 = new Date(millis + 86400000 + 86400000 + 86400000 + 86400000 + 86400000);
            //Datums formatieren
            mo2 = formatierer.format(mo1);
            di2 = formatierer.format(di1);
            mi2 = formatierer.format(mi1);
            do2 = formatierer.format(do1);
            fr2 = formatierer.format(fr1);
        }

        //Buttons initialisieren
        //Montag
        Button Mo = (Button) findViewById(R.id.Mo);
        if(weekString.equals("Mo")) Mo.setTextColor(Color.parseColor(color));
        Mo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DayDetailsActivity.class);
                i.putExtra("Day", "Montag");
                i.putExtra("Date", mo2);
                startActivity(i);
            }
        });
        //Dienstag
        Button Di = (Button) findViewById(R.id.Di);
        if(weekString.equals("Di")) Di.setTextColor(Color.parseColor(color));
        Di.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DayDetailsActivity.class);
                i.putExtra("Day", "Dienstag");
                i.putExtra("Date", di2);
                startActivity(i);
            }
        });
        //Mittwoch
        Button Mi = (Button) findViewById(R.id.Mi);
        if(weekString.equals("Mi")) Mi.setTextColor(Color.parseColor(color));
        Mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DayDetailsActivity.class);
                i.putExtra("Day", "Mittwoch");
                i.putExtra("Date", mi2);
                startActivity(i);
            }
        });
        //Donnerstag
        Button Do = (Button) findViewById(R.id.Do);
        if(weekString.equals("Do")) Do.setTextColor(Color.parseColor(color));
        Do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DayDetailsActivity.class);
                i.putExtra("Day", "Donnerstag");
                i.putExtra("Date", do2);
                startActivity(i);
            }
        });
        //Freitag
        Button Fr = (Button) findViewById(R.id.Fr);
        if(weekString.equals("Fr")) Fr.setTextColor(Color.parseColor(color));
        Fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DayDetailsActivity.class);
                i.putExtra("Day", "Freitag");
                i.putExtra("Date", fr2);
                startActivity(i);
            }
        });
        //Hint-Items verstecken
        findViewById(R.id.Mo_K).setVisibility(View.GONE);
        findViewById(R.id.Mo_K_Text).setVisibility(View.GONE);
        findViewById(R.id.Mo_H).setVisibility(View.GONE);
        findViewById(R.id.Mo_H_Text).setVisibility(View.GONE);
        findViewById(R.id.Mo_T).setVisibility(View.GONE);
        findViewById(R.id.Mo_T_Text).setVisibility(View.GONE);
        findViewById(R.id.Di_K).setVisibility(View.GONE);
        findViewById(R.id.Di_K_Text).setVisibility(View.GONE);
        findViewById(R.id.Di_H).setVisibility(View.GONE);
        findViewById(R.id.Di_H_Text).setVisibility(View.GONE);
        findViewById(R.id.Di_T).setVisibility(View.GONE);
        findViewById(R.id.Di_T_Text).setVisibility(View.GONE);
        findViewById(R.id.Mi_K).setVisibility(View.GONE);
        findViewById(R.id.Mi_K_Text).setVisibility(View.GONE);
        findViewById(R.id.Mi_H).setVisibility(View.GONE);
        findViewById(R.id.Mi_H_Text).setVisibility(View.GONE);
        findViewById(R.id.Mi_T).setVisibility(View.GONE);
        findViewById(R.id.Mi_T_Text).setVisibility(View.GONE);
        findViewById(R.id.Do_K).setVisibility(View.GONE);
        findViewById(R.id.Do_K_Text).setVisibility(View.GONE);
        findViewById(R.id.Do_H).setVisibility(View.GONE);
        findViewById(R.id.Do_H_Text).setVisibility(View.GONE);
        findViewById(R.id.Do_T).setVisibility(View.GONE);
        findViewById(R.id.Do_T_Text).setVisibility(View.GONE);
        findViewById(R.id.Fr_K).setVisibility(View.GONE);
        findViewById(R.id.Fr_K_Text).setVisibility(View.GONE);
        findViewById(R.id.Fr_H).setVisibility(View.GONE);
        findViewById(R.id.Fr_H_Text).setVisibility(View.GONE);
        findViewById(R.id.Fr_T).setVisibility(View.GONE);
        findViewById(R.id.Fr_T_Text).setVisibility(View.GONE);

        //Hint-Items befüllen
        //Montag
            //Klassenarbeiten
            classtests = su.parseClasstests(null, mo2);
            if(classtests.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Mo_K);
                TextView hintitem_tv = (TextView) findViewById(R.id.Mo_K_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("K"+String.valueOf(classtests.size()));
            }
            //Hausaufgaben
            homeworks = su.parseHomeworks(null, mo2);
            if(homeworks.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Mo_H);
                TextView hintitem_tv = (TextView) findViewById(R.id.Mo_H_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("H"+String.valueOf(homeworks.size()));
            }
            //Termine
            events = su.parseEvents(null, mo2);
            if(events.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Mo_T);
                TextView hintitem_tv = (TextView) findViewById(R.id.Mo_T_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("T"+String.valueOf(events.size()));
            }
        //Dienstag
            //Klassenarbeiten
            classtests = su.parseClasstests(null, di2);
            if(classtests.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Di_K);
                TextView hintitem_tv = (TextView) findViewById(R.id.Di_K_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("K"+String.valueOf(classtests.size()));
            }
            //Hausaufgaben
            homeworks = su.parseHomeworks(null, di2);
            if(homeworks.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Di_H);
                TextView hintitem_tv = (TextView) findViewById(R.id.Di_H_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("H"+String.valueOf(homeworks.size()));
            }
            //Termine
            events = su.parseEvents(null, di2);
            if(events.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Di_T);
                TextView hintitem_tv = (TextView) findViewById(R.id.Di_T_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("T"+String.valueOf(events.size()));
            }
        //Mittwoch
            //Klassenarbeiten
            classtests = su.parseClasstests(null, mi2);
            if(classtests.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Mi_K);
                TextView hintitem_tv = (TextView) findViewById(R.id.Mi_K_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("K"+String.valueOf(classtests.size()));
            }
            //Hausaufgaben
            homeworks = su.parseHomeworks(null, mi2);
            if(homeworks.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Mi_H);
                TextView hintitem_tv = (TextView) findViewById(R.id.Mi_H_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("H"+String.valueOf(homeworks.size()));
            }
            //Termine
            events = su.parseEvents(null, mi2);
            if(events.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Mi_T);
                TextView hintitem_tv = (TextView) findViewById(R.id.Mi_T_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("T"+String.valueOf(events.size()));
            }
        //Donnerstag
            //Klassenarbeiten
            classtests = su.parseClasstests(null, do2);
            if(classtests.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Do_K);
                TextView hintitem_tv = (TextView) findViewById(R.id.Do_K_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("K"+String.valueOf(classtests.size()));
            }
            //Hausaufgaben
            homeworks = su.parseHomeworks(null, do2);
            if(homeworks.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Do_H);
                TextView hintitem_tv = (TextView) findViewById(R.id.Do_H_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("H"+String.valueOf(homeworks.size()));
            }
            //Termine
            events = su.parseEvents(null, do2);
            if(events.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Do_T);
                TextView hintitem_tv = (TextView) findViewById(R.id.Do_T_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("T"+String.valueOf(events.size()));
            }
        //Freitag
            //Klassenarbeiten
            classtests = su.parseClasstests(null, fr2);
            if(classtests.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Fr_K);
                TextView hintitem_tv = (TextView) findViewById(R.id.Fr_K_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("K"+String.valueOf(classtests.size()));
            }
            //Hausaufgaben
            homeworks = su.parseHomeworks(null, fr2);
            if(homeworks.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Fr_H);
                TextView hintitem_tv = (TextView) findViewById(R.id.Fr_H_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("H"+String.valueOf(homeworks.size()));
            }
            //Termine
            events = su.parseEvents(null, fr2);
            if(events.size() > 0) {
                ImageView hintitem_iv = (ImageView) findViewById(R.id.Fr_T);
                TextView hintitem_tv = (TextView) findViewById(R.id.Fr_T_Text);
                hintitem_iv.setVisibility(View.VISIBLE);
                hintitem_tv.setVisibility(View.VISIBLE);
                hintitem_tv.setText("T"+String.valueOf(events.size()));
            }

        //Stundenplan
        Button Stundenplan = (Button) findViewById(R.id.Stundenplan);
        Stundenplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!su.getString("Klasse", "no_class").equals("no_class")) {
                    startActivity(new Intent(MainActivity.this,TimeTableActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, res.getString(R.string.no_class_selected), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Speicher für Textfield einrichten
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        final EditText et = (EditText) findViewById(R.id.TextSpeicher);
        //Text setzen
        et.setText(prefs.getString("TextSpeicher", ""));
        //Textchange in Speicher eintragen
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                SharedPreferences.Editor e = prefs.edit();
                e.putString("TextSpeicher", et.getText().toString());
                e.commit();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public void launchNewsFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        String rights = prefs.getString("Rights", "student");
        if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            layoutInflater.inflate(R.layout.fragment_news_admin, container);
        } else {
            layoutInflater.inflate(R.layout.fragment_news, container);
        }

        if(showInvisibleEntries) {
            news = su.parseNewsAndInvisibleNews();
        } else {
            news = su.parseNews();
        }

        //NewsRecyclerView anzeigen
        news_view = (RecyclerView) findViewById(R.id.news_view);
        news_view_manager = new LinearLayoutManager(MainActivity.this);
        news_view.setLayoutManager(news_view_manager);
        if(showInvisibleEntries) {
            news_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_NEW_INVISIBLE);
        } else {
            news_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_NEW);
        }
        news_view.setAdapter(news_view_adapter);

        if(news_view_adapter.getItemCount() == 0) findViewById(R.id.no_active_news).setVisibility(View.VISIBLE);

        //Aktionen, die nur für Admins oder Team-Mitglieder vorgesehen sind
        if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            //FloatingActionButton Aktion zuweisen
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_new);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NewNewActivity.class));
                }
            });

            //Checkbox aufsetzen
            CheckBox showInvisible = (CheckBox) findViewById(R.id.showInvisible);
            showInvisible.setChecked(showInvisibleEntries);
            showInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    showInvisibleEntries = isChecked;
                    launchNewsFragment();
                }
            });
        }
    }

    public void launchPlanOfTheYearFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        String rights = prefs.getString("Rights", "student");
        if(rights.equals("classspeaker") || rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            layoutInflater.inflate(R.layout.fragment_jahresplan_admin, container);
        } else {
            layoutInflater.inflate(R.layout.fragment_jahresplan, container);
        }

        //Aktuellen Monat ermitteln
        selectedMonth = new Date().getMonth() +1;
        String month = String.valueOf(selectedMonth);
        if(month.length() != 2) month = "0" + month;

        //Daten in die ArrayList holen
        classtests = su.parseClasstests(month, null);
        homeworks = su.parseHomeworks(month, null);
        events = su.parseEvents(month, null);

        all.clear();
        all.addAll(classtests);
        all.addAll(homeworks);
        all.addAll(events);

        //NewsRecyclerView anzeigen
        year_view = (RecyclerView) findViewById(R.id.plan_of_the_year_list);
        year_view_manager = new LinearLayoutManager(MainActivity.this);
        year_view.setLayoutManager(year_view_manager);
        year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
        year_view.setAdapter(year_view_adapter);


        //Wenn keine Daten für diesen Monat vorhanden sind, entsprechende Anzeige sichtbar machen
        final TextView no_active_elements = (TextView) findViewById(R.id.no_active_elements);
        if(year_view_adapter.getItemCount() == 0) {
            no_active_elements.setVisibility(View.VISIBLE);
        } else {
            no_active_elements.setVisibility(View.GONE);
        }

        if(rights.equals("classspeaker") || rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            //FloatingAction Button
            FloatingActionButton new_element = (FloatingActionButton) findViewById(R.id.new_classtest_homework_event);
            new_element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(res.getString(R.string.create_))
                            .setView(R.layout.dialogview_chooser_element)
                            .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(res.getString(R.string.next), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SwitchCompat sw1 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_classtest);
                                    SwitchCompat sw2 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_homework);
                                    SwitchCompat sw3 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_event);
                                    if(sw1.isChecked()) {
                                        Intent i = new Intent(MainActivity.this, NewEditElementActivity.class);
                                        i.putExtra("mode", NewEditElementActivity.MODE_CREATE_CLASSTEST);
                                        startActivity(i);
                                    } else if(sw2.isChecked()) {
                                        Intent i = new Intent(MainActivity.this, NewEditElementActivity.class);
                                        i.putExtra("mode", NewEditElementActivity.MODE_CREATE_HOMEWORK);
                                        startActivity(i);
                                    } else if(sw3.isChecked()) {
                                        Intent i = new Intent(MainActivity.this, NewEditElementActivity.class);
                                        i.putExtra("mode", NewEditElementActivity.MODE_CREATE_EVENT);
                                        startActivity(i);
                                    }
                                }
                            })
                            .create();
                    d.show();

                    final SwitchCompat sw1 = (SwitchCompat) d.findViewById(R.id.chooser_element_classtest);
                    final SwitchCompat sw2 = (SwitchCompat) d.findViewById(R.id.chooser_element_homework);
                    final SwitchCompat sw3 = (SwitchCompat) d.findViewById(R.id.chooser_element_event);
                    sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                sw1.setChecked(isChecked);
                                sw2.setChecked(false);
                                sw3.setChecked(false);
                            }
                        }
                    });
                    sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                sw1.setChecked(false);
                                sw2.setChecked(isChecked);
                                sw3.setChecked(false);
                            }
                        }
                    });
                    sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                sw1.setChecked(false);
                                sw2.setChecked(false);
                                sw3.setChecked(isChecked);
                            }
                        }
                    });
                }
            });
        }

        //Buttons
        final Button jan = (Button) findViewById(R.id.januar);
        final Button feb = (Button) findViewById(R.id.februar);
        final Button ma = (Button) findViewById(R.id.maerz);
        final Button apr = (Button) findViewById(R.id.april);
        final Button mai = (Button) findViewById(R.id.mai);
        final Button jun = (Button) findViewById(R.id.juni);
        final Button jul = (Button) findViewById(R.id.juli);
        final Button aug = (Button) findViewById(R.id.august);
        final Button sep = (Button) findViewById(R.id.september);
        final Button okt = (Button) findViewById(R.id.oktober);
        final Button nov = (Button) findViewById(R.id.november);
        final Button dez = (Button) findViewById(R.id.dezember);

        //Farben setzen
        jan.setTextColor(Color.parseColor("#000000"));
        feb.setTextColor(Color.parseColor("#000000"));
        ma.setTextColor(Color.parseColor("#000000"));
        apr.setTextColor(Color.parseColor("#000000"));
        mai.setTextColor(Color.parseColor("#000000"));
        jun.setTextColor(Color.parseColor("#000000"));
        jul.setTextColor(Color.parseColor("#000000"));
        aug.setTextColor(Color.parseColor("#000000"));
        sep.setTextColor(Color.parseColor("#000000"));
        okt.setTextColor(Color.parseColor("#000000"));
        nov.setTextColor(Color.parseColor("#000000"));
        dez.setTextColor(Color.parseColor("#000000"));

        //Monatsinfos
        final TextView monatsinfos = (TextView) findViewById(R.id.monatsinfos);
        final HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);

        if(selectedMonth == 1) {
            monatsinfos.setText(res.getString(R.string.january) + ":");
            jan.setTextColor(Color.parseColor(color));
            Selected = 0;
        } else if(selectedMonth == 2) {
            monatsinfos.setText(res.getString(R.string.february) + ":");
            feb.setTextColor(Color.parseColor(color));
            Selected = 250;
        } else if(selectedMonth == 3) {
            monatsinfos.setText(res.getString(R.string.march) + ":");
            ma.setTextColor(Color.parseColor(color));
            Selected = 500;
        } else if(selectedMonth == 4) {
            monatsinfos.setText(res.getString(R.string.april) + ":");
            apr.setTextColor(Color.parseColor(color));
            Selected = 750;
        } else if(selectedMonth == 5) {
            monatsinfos.setText(res.getString(R.string.may) + ":");
            mai.setTextColor(Color.parseColor(color));
            Selected = 1000;
        } else if(selectedMonth == 6) {
            monatsinfos.setText(res.getString(R.string.june) + ":");
            jun.setTextColor(Color.parseColor(color));
            Selected = 1250;
        } else if(selectedMonth == 7) {
            monatsinfos.setText(res.getString(R.string.july) + ":");
            jul.setTextColor(Color.parseColor(color));
            Selected = 1500;
        } else if(selectedMonth == 8) {
            monatsinfos.setText(res.getString(R.string.august) + ":");
            aug.setTextColor(Color.parseColor(color));
            Selected = 1750;
        } else if(selectedMonth == 9) {
            monatsinfos.setText(res.getString(R.string.september) + ":");
            sep.setTextColor(Color.parseColor(color));
            Selected = 2000;
        } else if(selectedMonth == 10) {
            monatsinfos.setText(res.getString(R.string.october) + ":");
            okt.setTextColor(Color.parseColor(color));
            Selected = 2250;
        } else if(selectedMonth == 11) {
            monatsinfos.setText(res.getString(R.string.november) + ":");
            nov.setTextColor(Color.parseColor(color));
            Selected = 2500;
        } else if(selectedMonth == 12) {
            monatsinfos.setText(res.getString(R.string.december) + ":");
            dez.setTextColor(Color.parseColor(color));
            Selected = 2750;
        }

        //Auf selektierten Monat scrollen
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(Selected, 0);
            }
        }, 0);


        //Button Januar
        jan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View v) {
                monatsinfos.setText(jan.getText()+":");
                selectedMonth = 1;
                //Farben setzen
                jan.setTextColor(Color.parseColor(color));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(0,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("01", null);
                homeworks = su.parseHomeworks("01", null);
                events = su.parseEvents("01", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button Februar
        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(feb.getText()+":");
                selectedMonth = 2;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor(color));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(250,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("02", null);
                homeworks = su.parseHomeworks("02", null);
                events = su.parseEvents("02", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button März
        ma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(ma.getText()+":");
                selectedMonth = 3;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor(color));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(500,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("03", null);
                homeworks = su.parseHomeworks("03", null);
                events = su.parseEvents("03", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button April
        apr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(apr.getText()+":");
                selectedMonth = 4;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor(color));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(750,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("04", null);
                homeworks = su.parseHomeworks("04", null);
                events = su.parseEvents("04", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button Mai
        mai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(mai.getText()+":");
                selectedMonth = 5;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor(color));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(1000,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("05", null);
                homeworks = su.parseHomeworks("05", null);
                events = su.parseEvents("05", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button Juni
        jun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(jun.getText()+":");
                selectedMonth = 6;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor(color));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(1250,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("06", null);
                homeworks = su.parseHomeworks("06", null);
                events = su.parseEvents("06", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button Juli
        jul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(jul.getText()+":");
                selectedMonth = 7;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor(color));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(1500,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("07", null);
                homeworks = su.parseHomeworks("07", null);
                events = su.parseEvents("07", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button August
        aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(aug.getText()+":");
                selectedMonth = 8;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor(color));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(1750,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("08", null);
                homeworks = su.parseHomeworks("08", null);
                events = su.parseEvents("08", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button September
        sep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(sep.getText()+":");
                selectedMonth = 9;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor(color));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(2000,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("09", null);
                homeworks = su.parseHomeworks("09", null);
                events = su.parseEvents("09", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button Oktober
        okt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(okt.getText()+":");
                selectedMonth = 10;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor(color));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(2250,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("10", null);
                homeworks = su.parseHomeworks("10", null);
                events = su.parseEvents("10", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button November
        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(nov.getText()+":");
                selectedMonth = 11;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor(color));
                dez.setTextColor(Color.parseColor("#000000"));
                scrollView.smoothScrollTo(2500,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("11", null);
                homeworks = su.parseHomeworks("11", null);
                events = su.parseEvents("11", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
                if(year_view_adapter.getItemCount() == 0) {
                    no_active_elements.setVisibility(View.VISIBLE);
                } else {
                    no_active_elements.setVisibility(View.GONE);
                }
            }
        });
        //Button Dezember
        dez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monatsinfos.setText(dez.getText()+":");
                selectedMonth = 12;
                //Farben setzen
                jan.setTextColor(Color.parseColor("#000000"));
                feb.setTextColor(Color.parseColor("#000000"));
                ma.setTextColor(Color.parseColor("#000000"));
                apr.setTextColor(Color.parseColor("#000000"));
                mai.setTextColor(Color.parseColor("#000000"));
                jun.setTextColor(Color.parseColor("#000000"));
                jul.setTextColor(Color.parseColor("#000000"));
                aug.setTextColor(Color.parseColor("#000000"));
                sep.setTextColor(Color.parseColor("#000000"));
                okt.setTextColor(Color.parseColor("#000000"));
                nov.setTextColor(Color.parseColor("#000000"));
                dez.setTextColor(Color.parseColor(color));
                scrollView.smoothScrollTo(2750,0);
                //Daten aktualisieren
                classtests = su.parseClasstests("12", null);
                homeworks = su.parseHomeworks("12", null);
                events = su.parseEvents("12", null);
                all.clear();
                all.addAll(classtests);
                all.addAll(homeworks);
                all.addAll(events);
                year_view_adapter = new ElementViewAdapter(MainActivity.this, ElementViewAdapter.MODE_CLASSTEST_HOMEWORK_EVENTS);
                year_view.setAdapter(year_view_adapter);
            }
        });
    }

    public void launchGalleryFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        String rights = prefs.getString("Rights", res.getString(R.string.guest));
        if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            layoutInflater.inflate(R.layout.fragment_gallery_admin, container);
        } else {
            layoutInflater.inflate(R.layout.fragment_gallery, container);
        }

        if(serverMessagingUtils.isInternetAvailable()) {
            if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
                new_folder = (FloatingActionButton) findViewById(R.id.new_folder);
                new_folder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText input = new EditText(MainActivity.this);
                        input.setHint(res.getString(R.string.new_folder_name));
                        AlertDialog d = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(res.getString(R.string.new_folder))
                                .setView(input)
                                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String foldername = input.getText().toString();
                                        if(!foldername.contains(".") && !foldername.contains(",") && !foldername.contains("/") && !foldername.contains("\\")) {
                                            Toast.makeText(MainActivity.this, res.getString(R.string.folder_is_creating_), Toast.LENGTH_LONG).show();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try{
                                                        String name = prefs.getString("Name", res.getString(R.string.guest));
                                                        serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+URLEncoder.encode(name, "UTF-8")+"&command=setimageconfig&foldername="+URLEncoder.encode(foldername, "UTF-8") + "&filenames=");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                launchGalleryFragment();
                                                            }
                                                        });
                                                    } catch (Exception e) {}
                                                }
                                            }).start();
                                        } else {
                                            Toast.makeText(MainActivity.this, res.getString(R.string.invalid_foldername), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .create();
                        d.show();
                    }
                });
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //Username aus den SharedPreferences auslesen
                        String username = prefs.getString("Name", res.getString(R.string.guest));
                        //ImageConfig herunterladen
                        result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+URLEncoder.encode(username, "UTF-8")+"&command=getimageconfig").trim();
                        //Result auseinandernehmen
                        if(gallery_view_foldernames == null) gallery_view_foldernames = new ArrayList<String>();
                        if(gallery_view_filenames == null) gallery_view_filenames = new ArrayList<String>();
                        gallery_view_foldernames.clear();
                        gallery_view_filenames.clear();
                        String rights = prefs.getString("Rights", "student");
                        String klasse = prefs.getString("Klasse", "no_class");
                        if(result.length() > 0) {
                            for(int i = 0; i < 100; i++) {
                                if(result.length() > 0) {
                                    int index1 = result.indexOf(":");
                                    int index2 = result.indexOf(";");
                                    //Auseinandernehmen
                                    String dirname = result.substring(0, index1);
                                    String filenames = result.substring(index1 +1, index2);
                                    //In Arraylists speichern
                                    if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
                                        gallery_view_foldernames.add(dirname);
                                        gallery_view_filenames.add(filenames);
                                    } else {
                                        if(dirname.contains(".")) {
                                            if(dirname.equals("." + klasse)) {
                                                gallery_view_foldernames.add(dirname);
                                                gallery_view_filenames.add(filenames);
                                            }
                                        } else {
                                            gallery_view_foldernames.add(dirname);
                                            gallery_view_filenames.add(filenames);
                                        }
                                    }
                                    //Vorne abzwacken
                                    result = result.substring(index2 +1);
                                }
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ProgressBar und Laden ausblenden
                                    findViewById(R.id.laden).setVisibility(View.GONE);
                                    findViewById(R.id.progressBar1).setVisibility(View.GONE);
                                    //Keine Bilder in der Gallerie einblenden
                                    findViewById(R.id.gallery_empty).setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    //Gallerie anzeigen
                                    gallery_view = (RecyclerView) findViewById(R.id.gallery_view);
                                    gallery_view_manager = new GridLayoutManager(MainActivity.this, 2);
                                    gallery_view.setLayoutManager(gallery_view_manager);
                                    gallery_view_adapter = new GalleryViewAdapter_Folders();
                                    gallery_view.setAdapter(gallery_view_adapter);
                                    if(gallery_view_adapter.getItemCount() > 0) {
                                        //ProgressBar und Laden ausblenden
                                        findViewById(R.id.laden).setVisibility(View.GONE);
                                        findViewById(R.id.progressBar1).setVisibility(View.GONE);
                                        if(new_folder != null) new_folder.setVisibility(View.VISIBLE);
                                    } else {
                                        //ProgressBar und Laden ausblenden
                                        findViewById(R.id.laden).setVisibility(View.GONE);
                                        findViewById(R.id.progressBar1).setVisibility(View.GONE);
                                        //Keine Bilder in der Gallerie einblenden
                                        findViewById(R.id.gallery_empty).setVisibility(View.VISIBLE);
                                    }

                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            //ProgressBar und Laden ausblenden
            findViewById(R.id.laden).setVisibility(View.GONE);
            findViewById(R.id.progressBar1).setVisibility(View.GONE);
            //Keine Bilder in der Gallerie einblenden
            findViewById(R.id.no_internet_gallery).setVisibility(View.VISIBLE);
        }
    }

    public void launchFoodPlanFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        layoutInflater.inflate(R.layout.fragment_speiseplan, container);
        //Speiseplan anzeigen
        //ID herausfinden
        //Lade - TextView
        final TextView laden = (TextView) findViewById(R.id.laden1);
        laden.setVisibility(View.VISIBLE);
        //WebView
        final WebView speiseplanView = (WebView) findViewById(R.id.SpeiseplanView);
        speiseplanView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                laden.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String discreption, String failingUrl) {
                //Code zur Fehlerbehandlung hierher schreiben

            }
        });
        speiseplanView.getSettings().setBuiltInZoomControls(true);
        speiseplanView.getSettings().setLoadWithOverviewMode(true);
        speiseplanView.getSettings().setUseWideViewPort(true);
        speiseplanView.getSettings().setJavaScriptEnabled(true);

        //Speiseplan herunterladen
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Username aus den SharedPreferences auslesen
                    String username = prefs.getString("Name", res.getString(R.string.guest));
                    //Antwort vom Server holen
                    result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+ URLEncoder.encode(username, "UTF-8")+"&command=getfoodplan");
                    //Speiseplan in die SharedPreferences eintragen
                    SharedPreferences.Editor e = prefs.edit();
                        e.putString("Speiseplan", result);
                    e.commit();
                    //WebView befüllen
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String url = prefs.getString("Speiseplan", "www.mrgames13.jimdo.com");
                            if(url.endsWith(".pdf")) {
                                speiseplanView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
                            } else {
                                speiseplanView.loadUrl(url);
                            }
                        }
                    });
                } catch(Exception e) {}
            }
        }).start();

        //FloatingAction Button befüllen
        FloatingActionButton edit_foodplan = (FloatingActionButton) findViewById(R.id.edit_foodplan);
        edit_foodplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = layoutInflater.inflate(R.layout.dialogview_edit_foodplan, null);

                final EditText et_link = (EditText) view.findViewById(R.id.edit_foodplan_link);
                Button btn_autumn = (Button) view.findViewById(R.id.edit_foodplan_autumn);
                Button btn_winter = (Button) view.findViewById(R.id.edit_foodplan_winter);
                Button btn_christmas = (Button) view.findViewById(R.id.edit_foodplan_chrismas);
                Button btn_easter = (Button) view.findViewById(R.id.edit_foodplan_easter);
                Button btn_pentecost = (Button) view.findViewById(R.id.edit_foodplan_pentecost);
                Button btn_summer = (Button) view.findViewById(R.id.edit_foodplan_summer);
                Button btn_open = (Button) view.findViewById(R.id.edit_foodplan_open);
                btn_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.bsbz.de/schwarzes-brett/speiseplan/"));
                        startActivity(i);
                    }
                });
                btn_autumn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_link.setText(res.getString(R.string.link_foodplan_autumn));
                    }
                });
                btn_christmas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_link.setText(res.getString(R.string.link_foodplan_christmas));
                    }
                });
                btn_winter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_link.setText(res.getString(R.string.link_foodplan_winter));
                    }
                });
                btn_easter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_link.setText(res.getString(R.string.link_foodplan_easter));
                    }
                });
                btn_pentecost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_link.setText(res.getString(R.string.link_foodplan_pentecost));
                    }
                });
                btn_summer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_link.setText(res.getString(R.string.link_foodplan_summer));
                    }
                });

                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(res.getString(R.string.edit_foodplan_t))
                        .setView(view)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.publish, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                final String url = et_link.getText().toString().trim();
                                if(url.startsWith("https://www.bsbz.de/") || url.startsWith("http://files.mrgames-server.de/") || url.startsWith("https://goo.gl/")) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Speiseplan-Url hochladen
                                            try{
                                                String name = su.getString("Name", res.getString(R.string.guest));
                                                serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(name, "UTF-8")+"&command=setfoodplan&url="+URLEncoder.encode(url, "UTF-8"));
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //Dialog schließen und Speiseplan-Seite refreshen
                                                        dialog.dismiss();
                                                        launchFoodPlanFragment();
                                                        Toast.makeText(MainActivity.this, res.getString(R.string.action_successful), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } catch(Exception e) {
                                                e.printStackTrace();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(MainActivity.this, res.getString(R.string.action_failed), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).start();
                                } else {
                                    Toast.makeText(MainActivity.this, res.getString(R.string.no_valid_url), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create();
                alert.show();
            }
        });
    }

    public void launchBSBZInfoFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        layoutInflater.inflate(R.layout.fragment_bsbz, container);
        //Funktionalität entfalten
        //BSBZ-Homepage Button einrichten
        Button bsbz_homepage = (Button) findViewById(R.id.bsbz_homepage);
        bsbz_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                i.putExtra("Webside", "https://www.bsbz.de/");
                i.putExtra("Title", "BSBZ Homepage");
                startActivity(i);
            }
        });
        final TextView bsbz_info = (TextView) findViewById(R.id.tv_bsbz_info);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        //Info vom Server oder aus den SharedPreferences holen
        String info = res.getString(R.string.no_info_entered);
        if(serverMessagingUtils.isInternetAvailable()) {
            info = res.getString(R.string.loading);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //Wenn Internet verfügbar ist, vom Server holen
                        String username = prefs.getString("Name", res.getString(R.string.guest));
                        final String info = serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(username, "UTF-8")+"&command=getbsbzinfo");
                        SharedPreferences.Editor e = prefs.edit();
                            e.putString("BSBZ_Info", info);
                        e.commit();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                bsbz_info.setText(info);
                            }
                        });
                    } catch(Exception e) {}
                }
            }).start();
        } else {
            //Wenn kein Internet verfügbar ist, aus den SharedPreferences holen
            info = prefs.getString("BSBZ_Info", res.getString(R.string.no_info_entered));
            progressBar.setVisibility(View.GONE);
        }
        bsbz_info.setText(info);
    }

    public void launchDeveloperFragment() {
        //Container leeren
        container.removeAllViews();
        //Layout-Datei entfalten
        layoutInflater.inflate(R.layout.fragment_entwickler, container);
        //Funktionalität entfalten
        //IDs herausfinden
        Button hpbutton = (Button) findViewById(R.id.Homepage_btn);
        Button hilfe = (Button) findViewById(R.id.Hilfe);

        //Funktionen belegen
        hpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                i.putExtra("Webside", "http://www.mrgames13.jimdo.com");
                i.putExtra("Title", "Unsere Homepage");
                startActivity(i);
            }
        });

        hilfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                i.putExtra("Webside", "http://mrgames13.jimdo.com/info/bsbz-app");
                i.putExtra("Title", "Hilfeseite");
                startActivity(i);
            }
        });
    }

    public static void Synchronize(final String klasse, final Context context) {
        final ProgressDialog pd = ProgressDialog.show(context, res.getString(R.string.please_wait_), res.getString(R.string.sync_is_running_), true);
        pd.setCancelable(false);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.isRunning = true;
                context.startService(new Intent(context, SyncronisationService.class));

                while(isRunning == true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {}
                    Thread.yield();
                }
                pd.dismiss();
            }
        });
        t.start();
    }

    //-------------------------------------------------------------------------- Fragmente -----------------------------------------------------------------------

    @SuppressLint("InlinedApi")
    public static class TermineFragment_Jahresplan extends ListFragment {

        //Variablen
        String item_text;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            arraylist_main.clear();

            // Liste aus String-Array befüllen
            arraylist.clear();

            String tmp = Integer.toString(selectedMonth);
            while(tmp.length() < 2) tmp = "0" + tmp;

            String item_title;
            String item_date;
            for(int i = 0; i < 101; i++) {
                String classtest = prefs.getString("Classtests_"+Integer.toString(i), "-");
                if(!classtest.equals("-")) {
                    int index1 = classtest.indexOf(",");
                    int index2 = classtest.indexOf(",", index1 +1);
                    int index3 = classtest.indexOf(",", index2 +1);
                    item_date = classtest.substring(0, index1);
                    item_title = classtest.substring(index1 +1, index2);
                    if(!item_title.equals("-") && !item_title.equals("")) {
                        if(item_date.substring(3, 5).equals(tmp)) {
                            arraylist.add(item_date +": "+ item_title);
                        }
                        arraylist_main.add(item_date +": "+ item_title);
                    }
                }
            }

            for(int i = 0; i < 101; i++) {
                String homework = prefs.getString("Homeworks_"+Integer.toString(i), "-");
                if(!homework.equals("-")) {
                    int index1 = homework.indexOf(",");
                    int index2 = homework.indexOf(",", index1 +1);
                    int index3 = homework.indexOf(",", index2 +1);
                    item_date = homework.substring(0, index1);
                    item_title = homework.substring(index1 +1, index2);
                    if(!item_title.equals("-") && !item_title.equals("")) {
                        if(item_date.substring(3, 5).equals(tmp)) {
                            arraylist.add(item_date +": "+ item_title);
                        }
                        arraylist_main.add(item_date +": "+ item_title);
                    }
                }
            }

            for(int i = 0; i < 101; i++) {
                String event = prefs.getString("Events_"+Integer.toString(i), "-");
                if(!event.equals("-")) {
                    int index1 = event.indexOf(",");
                    int index2 = event.indexOf(",", index1 +1);
                    int index3 = event.indexOf(",", index2 +1);
                    item_date = event.substring(0, index1);
                    item_title = event.substring(index1 +1, index2);
                    if(!item_title.equals("-") && !item_title.equals("")) {
                        if(item_date.substring(3, 5).equals(tmp)) {
                            arraylist.add(item_date +": "+ item_title);
                        }
                        arraylist_main.add(item_date +": "+ item_title);
                    }
                }
            }

            //Wenn in der ArrayList keine Elemente sind, Element mit KEINE_TERMINE_MONAT anlegen
            if(arraylist.size() == 0) arraylist.add(KEINE_TERMINE_MONAT);
            //ArrayList nach Datum sortieren
            Collections.sort(arraylist);
            //Adapter aufsetzen und der Liste zuweisen
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
            setListAdapter(adapter);

            String rights = prefs.getString("Rights", "student");
            if(rights.equals("classspeaker") || rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
                registerForContextMenu(getListView());
                getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        item_text = getListView().getItemAtPosition(position).toString();
                        if(!item_text.equals(KEINE_TERMINE_MONAT)) {
                            getListView().showContextMenu();
                        }
                        return true;
                    }
                });
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = this.getActivity().getMenuInflater();
            inflater.inflate(R.menu.plan_of_the_year_fragment_context_menu, menu);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            Activity activity = getActivity();
            if(activity instanceof MainActivity) {
                switch (item.getItemId()){
                    case R.id.context_menu_edit_element:
                        //Daten aus den SharedPreferences herausfiltern
                        String item_subject = "No Data";
                        String item_description = "No Data";
                        String item_writer = "No Data";
                        String item_date = "No Data";
                        String item_receiver = "No Data";
                        //Klassenarbeiten filtern
                        int mode = NewEditElementActivity.MODE_EDIT_CLASSTEST;
                        for(int i = 0; i < 101; i++) {
                            String news = prefs.getString("Classtests_"+Integer.toString(i), "-");
                            if(!news.equals("-")) {
                                int index1 = news.indexOf(",");
                                int index2 = news.indexOf(",", index1 +1);
                                int index3 = news.indexOf(",", index2 +1);
                                item_date = news.substring(0, index1);
                                item_subject = news.substring(index1 +1, index2);
                                item_description = news.substring(index2 +1, index3);
                                item_writer = news.substring(index3 +1);
                                if(item_text.equals(item_date + ": " + item_subject)) break;
                            }
                        }
                        //Hausaufgaben filtern
                        if(!item_text.equals(item_date + ": " + item_subject)) {
                            mode = NewEditElementActivity.MODE_EDIT_HOMEWORK;
                            for(int i = 0; i < 101; i++) {
                                String news = prefs.getString("Homeworks_"+Integer.toString(i), "-");
                                if(!news.equals("-")) {
                                    int index1 = news.indexOf(",");
                                    int index2 = news.indexOf(",", index1 +1);
                                    int index3 = news.indexOf(",", index2 +1);
                                    item_date = news.substring(0, index1);
                                    item_subject = news.substring(index1 +1, index2);
                                    item_description = news.substring(index2 +1, index3);
                                    item_writer = news.substring(index3 +1);
                                    if(item_text.equals(item_date + ": " + item_subject)) break;
                                }
                            }
                        }
                        //Termine filtern
                        if(!item_text.equals(item_date + ": " + item_subject)) {
                            mode = NewEditElementActivity.MODE_EDIT_EVENT;
                            for(int i = 0; i < 101; i++) {
                                String news = prefs.getString("Events_"+Integer.toString(i), "-");
                                if(!news.equals("-")) {
                                    int index1 = news.indexOf(",");
                                    int index2 = news.indexOf(",", index1 +1);
                                    int index3 = news.indexOf(",", index2 +1);
                                    item_date = news.substring(0, index1);
                                    item_subject = news.substring(index1 +1, index2);
                                    item_description = news.substring(index2 +1, index3);
                                    item_writer = news.substring(index3 +1);
                                    if(item_text.equals(item_date + ": " + item_subject)) break;
                                }
                            }
                        }

                        //Activity starten und Daten übergeben
                        Intent i = new Intent(getActivity(), NewEditElementActivity.class);
                        i.putExtra("old_title", item_subject);
                        i.putExtra("old_date", item_date);
                        i.putExtra("old_description", item_description);
                        i.putExtra("old_writer", item_writer);
                        i.putExtra("mode", mode);
                        startActivity(i);
                        return true;
                    case R.id.context_menu_delete_element:
                        /*android.support.v7.app.AlertDialog.Builder d = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        d.setTitle(res.getString(R.string.delete_new));
                        d.setMessage(res.getString(R.string.do_you_want_to_delete_new));
                        d.setPositiveButton(res.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            //Daten aus den SharedPreferences herausfiltern
                                            String item_subject = "No Data";
                                            String item_description = "No Data";
                                            String item_from = "No Data";
                                            String item_state = "No Data";
                                            String item_activation_date = "No Data";
                                            String item_expiration_date = "No Data";
                                            String item_receiver = "No Data";
                                            for(int i = 0; i < 101; i++) {
                                                String news = prefs.getString("News_"+Integer.toString(i), "-");
                                                if(!news.equals("-")) {
                                                    int index1 = news.indexOf(",");
                                                    int index2 = news.indexOf(",", index1 +1);
                                                    int index3 = news.indexOf(",", index2 +1);
                                                    int index4 = news.indexOf(",", index3 +1);
                                                    int index5 = news.indexOf(",", index4 +1);
                                                    int index6 = news.indexOf(",", index5 +1);
                                                    item_subject = news.substring(0, index1);
                                                    item_description = news.substring(index1 +1, index2);
                                                    item_from = news.substring(index2 +1, index3);
                                                    item_state = news.substring(index3 +1, index4);
                                                    item_activation_date = news.substring(index4 +1, index5);
                                                    item_expiration_date = news.substring(index5 +1, index6);
                                                    item_receiver = news.substring(index6);

                                                    if(item_text.equals(item_from + ": " + item_subject)) break;
                                                }
                                            }
                                            //Nachricht vom Server löschen
                                            String username = prefs.getString("Name", res.getString(R.string.guest));
                                            result = serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=deletenew&subject="+URLEncoder.encode(item_subject, "UTF-8"));
                                            if(result.equals("Action Successful")) {
                                                result = res.getString(R.string.new_successfully_created);
                                                getActivity().startService(new Intent(getActivity(), SyncronisationService.class));
                                            } else {
                                                result = res.getString(R.string.error_try_again);
                                            }
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (Exception e) {
                                            result = res.getString(R.string.error_try_again);
                                        }
                                    }
                                }).start();
                                dialog.dismiss();
                            }
                        });
                        d.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        d.create().show();*/
                        return true;
                    default:
                        return super.onContextItemSelected(item);
                }
            }
            return super.onContextItemSelected(item);
        }

        @Override
        public void onListItemClick(ListView listView, View v, int position, long id) {
            super.onListItemClick(listView, v, position, id);

            if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_TERMINE_MONAT)) {
                Toast.makeText(getActivity(), MainActivity.KEINE_TERMINE_MONAT, Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getActivity(), JDetailsActivity.class);
                String titel = getListView().getAdapter().getItem(position).toString();
                i.putExtra("Titel", titel.substring(12));
                startActivity(i);
            }
        }
    }

    @SuppressLint("InlinedApi")
    public static class TermineFragment_Heute extends ListFragment {
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            arraylist_main.clear();

            // Liste aus String-Array befüllen
            ArrayList<String> arraylist = new ArrayList<String>();

            Date date = new Date(System.currentTimeMillis());
            String tmp = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY).format(date).substring(0, 10);

            String item_title;
            String item_date;
            for(int i = 0; i < 101; i++) {
                String classtest = prefs.getString("Classtests_"+Integer.toString(i), "-");
                if(!classtest.equals("-")) {
                    int index1 = classtest.indexOf(",");
                    int index2 = classtest.indexOf(",", index1 +1);
                    int index3 = classtest.indexOf(",", index2 +1);
                    item_date = classtest.substring(0, index1);
                    item_title = classtest.substring(index1 +1, index2);
                    if(!item_title.equals("-") && !item_title.equals("")) {
                        if(item_date.substring(3, 5).equals(tmp)) {
                            arraylist.add(item_date +": "+ item_title);
                        }
                        arraylist_main.add(item_date +": "+ item_title);
                    }
                }
            }

            for(int i = 0; i < 101; i++) {
                String homework = prefs.getString("Homeworks_"+Integer.toString(i), "-");
                if(!homework.equals("-")) {
                    int index1 = homework.indexOf(",");
                    int index2 = homework.indexOf(",", index1 +1);
                    int index3 = homework.indexOf(",", index2 +1);
                    item_date = homework.substring(0, index1);
                    item_title = homework.substring(index1 +1, index2);
                    if(!item_title.equals("-") && !item_title.equals("")) {
                        if(item_date.substring(3, 5).equals(tmp)) {
                            arraylist.add(item_date +": "+ item_title);
                        }
                        arraylist_main.add(item_date +": "+ item_title);
                    }
                }
            }

            for(int i = 0; i < 101; i++) {
                String event = prefs.getString("Events_"+Integer.toString(i), "-");
                if(!event.equals("-")) {
                    int index1 = event.indexOf(",");
                    int index2 = event.indexOf(",", index1 +1);
                    int index3 = event.indexOf(",", index2 +1);
                    item_date = event.substring(0, index1);
                    item_title = event.substring(index1 +1, index2);
                    if(!item_title.equals("-") && !item_title.equals("")) {
                        if(item_date.substring(3, 5).equals(tmp)) {
                            arraylist.add(item_date +": "+ item_title);
                        }
                        arraylist_main.add(item_date +": "+ item_title);
                    }
                }
            }

            if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_TERMINE_TAG);

            Collections.sort(arraylist);

            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView listView, View v, int position, long id) {
            super.onListItemClick(listView, v, position, id);
            if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_TERMINE_TAG)) {
                Toast.makeText(getActivity(), MainActivity.KEINE_TERMINE_TAG, Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getActivity(), JDetailsActivity.class);
                String titel = getListView().getAdapter().getItem(position).toString();
                i.putExtra("Titel", titel.substring(12));
                startActivity(i);
            }
        }
    }

    @SuppressLint("InlinedApi")
    public static class KlassenArbeitenFragment extends ListFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //SharedPreferences Instanz erhalten
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            // Liste aus String-Array befüllen
            ArrayList<String> arraylist = new ArrayList<String>();

            for(int i = 0;i < 101;i++) {
                String item = prefs.getString("K_Titel"+Integer.toString(i), "-");
                String date = prefs.getString("K_Date"+Integer.toString(i), "-");
                if(!item.equals("-") && date.equals(date1) && !item.equals("") &&!date.equals("")) {
                    arraylist.add(date+": "+item);
                }
            }

            if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_KLASSENARBEITEN_TAG);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView listView, View v, int position, long id) {
            super.onListItemClick(listView, v, position, id);

            if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_KLASSENARBEITEN_TAG)) {
                Toast.makeText(getActivity(), MainActivity.KEINE_KLASSENARBEITEN_TAG, Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getActivity(), K_DetailsActivity.class);
                String titel = getListView().getAdapter().getItem(position).toString();
                i.putExtra("Titel", titel);
                i.putExtra("Text", Integer.toString(position+1));
                startActivity(i);
            }
        }
    }

    @SuppressLint("InlinedApi")
    public static class HausaufgabenFragment extends ListFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //SharedPreferences Instanz erhalten
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            // Liste aus String-Array befüllen
            ArrayList<String> arraylist = new ArrayList<String>();

            for(int i = 0;i < 101;i++) {
                String item = prefs.getString("H_Titel"+Integer.toString(i), "-");
                String date = prefs.getString("H_Date"+Integer.toString(i), "-");
                if(!item.equals("-") && date.equals(date1) && !item.equals("") &&!date.equals("")) {
                    arraylist.add(date+": "+item);
                }
            }

            if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_HAUSAUFGABEN_TAG);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView listView, View v, int position, long id) {
            super.onListItemClick(listView, v, position, id);

            if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_HAUSAUFGABEN_TAG)) {
                Toast.makeText(getActivity(), MainActivity.KEINE_HAUSAUFGABEN_TAG, Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getActivity(), H_DetailsActivity.class);
                String titel = getListView().getAdapter().getItem(position).toString();
                i.putExtra("Titel", titel);
                i.putExtra("Text", Integer.toString(position+1));
                startActivity(i);
            }
        }
    }

    @SuppressLint("InlinedApi")
    public static class TermineFragment extends ListFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //SharedPreferences Instanz erhalten
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            // Liste aus String-Array befüllen
            ArrayList<String> arraylist = new ArrayList<String>();

            Log.d("Date", date2);

            for(int i = 0;i < 101;i++) {
                String item = prefs.getString("T_Titel"+Integer.toString(i), "-");
                String date = prefs.getString("T_Date"+Integer.toString(i), "-");
                if(!item.equals("-") && date.equals(date1) && !item.equals("") &&!date.equals("")) {
                    arraylist.add(date+": "+item);
                }
            }

            if(arraylist.size() == 0) arraylist.add(MainActivity.KEINE_TERMINE_TAG);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, arraylist);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView listView, View v, int position, long id) {
            super.onListItemClick(listView, v, position, id);

            if(listView.getItemAtPosition(position).equals(MainActivity.KEINE_TERMINE_TAG)) {
                Toast.makeText(getActivity(), MainActivity.KEINE_TERMINE_TAG, Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(getActivity(), T_DetailsActivity.class);
                String titel = getListView().getAdapter().getItem(position).toString();
                i.putExtra("Titel", titel);
                i.putExtra("Text", Integer.toString(position+1));
                startActivity(i);
            }
        }
    }

    private void checkAppVersion(final Context context, final boolean showProgressDialog, final boolean showResultDialog) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(showProgressDialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Dialog für den Fortschritt anzeigen
                                if(AppTheme == 0) {
                                    pd_Progress = new ProgressDialog(context, R.style.FirstTheme_Dialog_Progress);
                                } else {
                                    pd_Progress = new ProgressDialog(context, R.style.SecondTheme_Dialog_Progress);
                                }
                                pd_Progress.setMessage(res.getString(R.string.searching_for_updates));
                                pd_Progress.setIndeterminate(true);
                                pd_Progress.setTitle(res.getString(R.string.check_for_update));
                                pd_Progress.show();
                            }
                        });
                    }
                    //Benutzernamen aus den SharedPreferences auslesen
                    String username = prefs.getString("Name", res.getString(R.string.guest));
                    //Abfrage an den Server senden
                    result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+URLEncoder.encode(username, "UTF-8")+"&command=getserverinfo");
                    //Result auseinandernehmen
                    int index1 = result.indexOf(",");
                    int index2 = result.indexOf(",", index1 +1);
                    int index3 = result.indexOf(",", index2 +1);
                    int index4 = result.indexOf(",", index3 +1);
                    int index5 = result.indexOf(",", index4 +1);
                    String client_name = result.substring(0, index1);
                    String server_state = result.substring(index1 +1, index2);
                    final String app_version = result.substring(index2 +1, index3);
                    String adminconsole_version = result.substring(index3 +1, index4);
                    String supporturl = result.substring(index4 +1, index5);
                    String owners = result.substring(index4 +1);
                    //Dialog für das Ergebnis anzeigen
                    if(showResultDialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd_Progress.dismiss();
                                if(app_version.equals(currentAppVersion)) {
                                    android.support.v7.app.AlertDialog.Builder d_Result;
                                    if(AppTheme == 0) {
                                        d_Result = new android.support.v7.app.AlertDialog.Builder(context, R.style.FirstTheme_Dialog);
                                    } else {
                                        d_Result = new android.support.v7.app.AlertDialog.Builder(context, R.style.SecondTheme_Dialog);
                                    }
                                    d_Result.setTitle(res.getString(R.string.check_for_update))
                                            .setMessage(res.getString(R.string.no_updates_found))
                                            .setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create();
                                    d_Result.show();
                                } else {
                                    android.support.v7.app.AlertDialog.Builder d_Result = new android.support.v7.app.AlertDialog.Builder(context);
                                    d_Result.setTitle(res.getString(R.string.check_for_update))
                                            .setMessage(res.getString(R.string.updates_found))
                                            .setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final String appPackageName = getPackageName();
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                    }
                                                }
                                            })
                                            .setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            })
                                            .create();
                                    d_Result.show();
                                }
                            }
                        });
                    }
                } catch (UnsupportedEncodingException e) {}
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchGalleryFragment();
        }
    }
}