package com.mrgames13.jimdo.bsbz_app.App;

import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;

public class EditInfoActivity extends AppCompatActivity {
    //Konstanten

    //Variablen als Objekte


    //Variablen

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

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1);
        nm.cancel(2);
        nm.cancel(3);

        //Aktionen ermitteln und ausführen
        try {
            String action = getIntent().getExtras().getString("Action");
            if(action.equals("deleted account")) {
                Toast.makeText(LogInActivity.this, res.getString(R.string.account_deleted_successfully), Toast.LENGTH_LONG).show();
                getIntent().removeExtra("Action");
            } else if(action.equals("not deleted account")) {
                Toast.makeText(LogInActivity.this, res.getString(R.string.account_deletion_failed), Toast.LENGTH_LONG).show();
                getIntent().removeExtra("Action");
            } else if(action.equals("changed password")) {
                Toast.makeText(LogInActivity.this, res.getString(R.string.password_changed_successfully), Toast.LENGTH_LONG).show();
                getIntent().removeExtra("Action");
            } else if(action.equals("not changed password")) {
                Toast.makeText(LogInActivity.this, res.getString(R.string.password_changing_failed), Toast.LENGTH_LONG).show();
                getIntent().removeExtra("Action");
            }
        } catch(Exception e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
    }
}