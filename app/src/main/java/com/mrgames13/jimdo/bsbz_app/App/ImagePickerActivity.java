package com.mrgames13.jimdo.bsbz_app.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mrgames13.jimdo.bsbz_app.R;

public class ImagePickerActivity extends AppCompatActivity {

    //Konstanten
    private final int REQ_CODE_PICK_IMAGE = 10001;

    //Variablen als Objekte
    private Resources res;
    private SharedPreferences prefs;
    private Toolbar toolbar;
    private ImageView imageView;
    private Button pickImage;
    private Button uploadImage;

    //Variablen
    Uri imageUri;

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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        if(Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(ImagePickerActivity.this);

        //Theme aus den Shared Preferences auslesen
        String theme = prefs.getString("AppTheme", "0");
        if(theme.equals("0")) {
            MainActivity.AppTheme = 0;
            setTheme(R.style.FirstTheme);
        } else if(theme.equals("1")) {
            MainActivity.AppTheme = 1;
            setTheme(R.style.SecondTheme);
        }

        setContentView(R.layout.activity_image_picker);

        //Resourcen initialisieren
        res = getResources();

        //Toolbar initialisieren
        toolbar = (Toolbar) findViewById(R.id.toolbar_image_picker_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(res.getString(R.string.new_image));

        //Benutzeroberfläche initialisieren
        imageView = (ImageView) findViewById(R.id.image_picker_image_view);

        pickImage = (Button) findViewById(R.id.pick_image);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        uploadImage = (Button) findViewById(R.id.upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog d = new AlertDialog.Builder(ImagePickerActivity.this)
                        .setTitle(res.getString(R.string.new_image))
                        .setMessage(res.getString(R.string.image_upload_m))
                        .setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadImage();
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_picker, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/jpg");
        startActivityForResult(pickImageIntent, REQ_CODE_PICK_IMAGE);
    }

    private void uploadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity.serverMessagingUtils.uploadImage(imageUri, ImageFolderActivity.folderName, );

                } catch(Exception e) {}
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQ_CODE_PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            pickImage.setEnabled(false);
            uploadImage.setEnabled(true);
        }
    }
}