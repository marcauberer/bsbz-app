package com.mrgames13.jimdo.bsbz_app.App;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.SimpleAnimationListener;

public class LogoActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte
    private VideoView video;
    private TextView app_name;
    private RelativeLayout logo_container;
    private Handler h;

    //Variablen
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        h = new Handler();

        app_name = (TextView) findViewById(R.id.logo_app_title);
        logo_container = (RelativeLayout) findViewById(R.id.logo_container);

        Uri video_uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo_animation);
        video = (VideoView) findViewById(R.id.logo_video_view);
        video.setVideoURI(video_uri);
        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.seekTo(0);
                video.start();
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Schriftzug langsam einblenden
                Animation fade_in = AnimationUtils.loadAnimation(LogoActivity.this, android.R.anim.fade_in);
                fade_in.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LogoActivity.this, LogInActivity.class));
                                overridePendingTransition(R.anim.logo_container_animation, R.anim.logo_container_animation);
                                finish();
                            }
                        }, 750);
                    }
                });
                app_name.setAnimation(fade_in);
                app_name.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("pos", position);
        video.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("pos");
        video.seekTo(position);
    }
}