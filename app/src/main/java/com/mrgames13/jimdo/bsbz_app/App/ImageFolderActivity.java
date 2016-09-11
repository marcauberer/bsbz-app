package com.mrgames13.jimdo.bsbz_app.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.GalleryViewAdapter_Files;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFolderActivity extends AppCompatActivity {

    //Konstanten

    //Varialben als Objekte
    private Toolbar toolbar;
    private Resources res;
    private SharedPreferences prefs;
    private RecyclerView gallery_view;
    private RecyclerView.Adapter gallery_view_adapter;
    private RecyclerView.LayoutManager gallery_view_manager;

    //Varialben
    public static ArrayList<String> filenames;
    public static String folderName;

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

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
        }

        // ToolBar Titel festlegen
        String title = getIntent().getExtras().getString("foldername").toString().replace(".", "");
        if(title.equals("")) title = res.getString(R.string.no_name);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        String rights = prefs.getString("Rights", "student");
        if(rights.equals("classspeaker") || rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            setContentView(R.layout.activity_image_folder_admin);
        } else {
            setContentView(R.layout.activity_image_folder);
        }

        //Toolbar initialisieren
        toolbar = (Toolbar) findViewById(R.id.toolbar_image_folder_activity);
        setSupportActionBar(toolbar);

        //Resourcen initialisieren
        res = getResources();

        //Gallerie anzeigen
        gallery_view = (RecyclerView) findViewById(R.id.gallery_view);
        gallery_view_manager = new GridLayoutManager(ImageFolderActivity.this, 2);
        gallery_view.setLayoutManager(gallery_view_manager);

        gallery_view_adapter = new GalleryViewAdapter_Files();
        gallery_view.setAdapter(gallery_view_adapter);

        if(gallery_view_adapter.getItemCount() == 0) findViewById(R.id.dir_empty).setVisibility(View.VISIBLE);

        //ArrayList Filenames anlegen und befüllen
        folderName = getIntent().getExtras().getString("foldername").toString();
        String filenames_string = getIntent().getExtras().getString("filenames");
        if(filenames_string.contains(",")) {
            filenames = new ArrayList<String>(Arrays.asList(filenames_string.split(",")));
        } else {
            filenames = new ArrayList<>();
            if(!filenames_string.equals("")) filenames.add(filenames_string);
        }

        if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            //FloatingAction-Button initialisieren
            FloatingActionButton new_image = (FloatingActionButton) findViewById(R.id.new_image);
            new_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ImageFolderActivity.this, ImagePickerActivity.class));
                }
            });
        } else if(rights.equals("classspeaker") && folderName.equals("." + prefs.getString("Klasse", "no_class"))) {
            //FloatingAction-Button initialisieren
            FloatingActionButton new_image = (FloatingActionButton) findViewById(R.id.new_image);
            new_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ImageFolderActivity.this, ImagePickerActivity.class));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String rights = MainActivity.prefs.getString("Rights", "student");
        if(rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
            getMenuInflater().inflate(R.menu.image_folder_admin, menu);
        } else if(rights.equals("classspeaker")) {
            getMenuInflater().inflate(R.menu.image_folder_classspeaker, menu);
        } else {
            getMenuInflater().inflate(R.menu.image_folder, menu);
        }
        return super.onCreateOptionsMenu(menu);
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
        } else if(id == R.id.action_download_folder) {
            if(MainActivity.serverMessagingUtils.isInternetAvailable()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.serverMessagingUtils.downloadFolder(ImageFolderActivity.this, folderName, filenames);
                    }
                }).start();
            } else {
                Toast.makeText(getApplicationContext(), res.getString(R.string.internet_is_not_available), Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if(id == R.id.action_delete_folder) {
            AlertDialog d = new AlertDialog.Builder(ImageFolderActivity.this)
                    .setTitle(res.getString(R.string.delete_folder))
                    .setMessage(res.getString(R.string.delete_folder_m))
                    .setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(prefs.getString("Name", res.getString(R.string.guest)), "UTF-8")+"&command=deleteimagefolder&foldername="+URLEncoder.encode(folderName, "UTF-8"));
                                        finish();
                                    } catch(Exception e) {}
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            d.show();
        }
        return super.onOptionsItemSelected(item);
    }
}