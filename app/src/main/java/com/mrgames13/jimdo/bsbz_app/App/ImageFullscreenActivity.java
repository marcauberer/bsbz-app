package com.mrgames13.jimdo.bsbz_app.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;

import java.net.URLEncoder;

public class ImageFullscreenActivity extends AppCompatActivity {

    //Konstanten

    //Varialen als Objekte
    private SharedPreferences prefs;
    private Resources res;
    private Toolbar toolbar;

    //Variablen
    private String folderName;
    private String imageName;
    private Bitmap bitmap = null;
    private int index;
    private int vibrantColor;

    @Override
    protected void onStart() {
        super.onStart();

        if(vibrantColor == 0) {
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
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

            if(Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
        } else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrantColor));
            if(Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(MainActivity.darkenColor(vibrantColor));
        }
        // ToolBar Titel festlegen
        getSupportActionBar().setTitle(folderName + "_" + imageName);
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

        setContentView(R.layout.activity_image_fullscreen);

        //Resourcen initialisieren
        res = getResources();

        //Toolbar aufsetzen
        toolbar = (Toolbar) findViewById(R.id.toolbar_image_fullscreen_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Schwarzer Hintergrund hinzufügen
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl1);
        if(theme.equals("1")) {
            rl.setBackgroundColor(Color.BLACK);
        }

        folderName = getIntent().getExtras().getString("foldername");
        imageName = getIntent().getExtras().getString("imagename");
        index = getIntent().getExtras().getInt("index");

        //Bild in den ImageView laden
        setFullscreenImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_fullscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } if(id == android.R.id.home) {
            finish();
            return true;
        } else if(id == R.id.action_delete_image) {
            AlertDialog d = new AlertDialog.Builder(ImageFullscreenActivity.this)
                    .setTitle(res.getString(R.string.delete_image))
                    .setMessage(res.getString(R.string.delete_image_m))
                    .setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(MainActivity.serverMessagingUtils.isInternetAvailable()) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            imageName = imageName + ".jpg";
                                            String name = prefs.getString("Name", res.getString(R.string.guest));
                                            MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(name, "UTF-8")+"&command=deleteimagefile&foldername="+URLEncoder.encode(folderName, "UTF-8")+"&filename="+URLEncoder.encode(imageName, "UTF-8"));
                                            String filenames = "";
                                            for(String filename : ImageFolderActivity.filenames) {
                                                filenames = filenames + "," + filename;
                                            }
                                            if(!filenames.equals("")) filenames = filenames.substring(1);
                                            if(filenames.contains("," + imageName)) {
                                                filenames = filenames.replace("," + imageName, "");
                                            } else if(filenames.contains(imageName + ",")) {
                                                filenames = filenames.replace(imageName + ",", "");
                                            } else {
                                                filenames = "";
                                            }
                                            MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(name, "UTF-8")+"&command=setimageconfig&foldername="+URLEncoder.encode(folderName, "UTF-8")+"&filenames="+URLEncoder.encode(filenames, "UTF-8"));
                                            finish();
                                        } catch(Exception e) {}
                                    }
                                }).start();
                            } else {
                                Toast.makeText(getApplicationContext(), res.getString(R.string.internet_is_not_available), Toast.LENGTH_SHORT).show();
                            }
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
        } else if(id == R.id.action_download_image) {
            if(MainActivity.serverMessagingUtils.isInternetAvailable()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.serverMessagingUtils.downloadFile(folderName, imageName, null);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), res.getString(R.string.download_finished), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(getApplicationContext(), res.getString(R.string.internet_is_not_available), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFullscreenImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = MainActivity.serverMessagingUtils.downloadImage(folderName, ImageFolderActivity.filenames.get(index));
                if(bitmap == null) bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_image_black_48dp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ImageView iv = (ImageView) findViewById(R.id.image_fullscreen);
                        iv.setImageBitmap(bitmap);
                        //VibrantColor herausfinden und auf Toolbar übertragen
                        Palette palette = Palette.from(bitmap).generate();
                        vibrantColor = palette.getVibrantColor(Color.parseColor("#ea690c"));
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrantColor));
                        if(Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(MainActivity.darkenColor(vibrantColor));
                        //ProgressBar ausblenden
                        findViewById(R.id.progressBar1).setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}