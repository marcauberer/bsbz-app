package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    //Variablen

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

        //Toolbar initialisieren
        toolbar = (Toolbar) findViewById(R.id.toolbar_image_picker_activity);
        setSupportActionBar(toolbar);

        //Resourcen initialisieren
        res = getResources();

        //Benutzeroberfläche initialisieren
        imageView = (ImageView) findViewById(R.id.image_picker_image_view);

        Button pickImage = (Button) findViewById(R.id.pick_image);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        Button uploadImage = (Button) findViewById(R.id.upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void pickImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/jpg");
        startActivityForResult(pickImageIntent, REQ_CODE_PICK_IMAGE);
    }

    private void uploadImage() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQ_CODE_PICK_IMAGE) {

        }
    }
}