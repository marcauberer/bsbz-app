package com.mrgames13.jimdo.bsbz_app.Tools;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

import com.mrgames13.jimdo.bsbz_app.R;

public class ProgressViewUtils {
    //Konstanten

    //Variablen als Objekte
    private Context context;
    private Resources res;

    //Variablen

    public ProgressViewUtils(Context context, Resources res) {
        this.context = context;
        this.res = res;
    }

    public void startLoading(final VideoView video) {
        video.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.start_animation));
        video.setDrawingCacheEnabled(true);
        video.setZOrderOnTop(true);
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
                video.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.progress_animation));
                video.setOnCompletionListener(null);
            }
        });
    }

    public void stopLoading(final VideoView video) {
        video.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.end_animation));
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.setVisibility(View.GONE);
            }
        });
    }
}