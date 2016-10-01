package com.mrgames13.jimdo.bsbz_app.App;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.mrgames13.jimdo.bsbz_app.R;

public class LogoActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private VideoView video;

    //Variablen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Uri video_uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo_animation);

        video = (VideoView) findViewById(R.id.logo_video_view);
        video.setVideoURI(video_uri);
    }
}