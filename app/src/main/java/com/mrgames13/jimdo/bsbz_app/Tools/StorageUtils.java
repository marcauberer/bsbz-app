package com.mrgames13.jimdo.bsbz_app.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mrgames13.jimdo.bsbz_app.ComponentClasses.New;

import java.util.ArrayList;

public class StorageUtils {
    //Konstanten
    private final String DEFAULT_STRING_VALUE = "";
    private final int DEFAULT_INT_VALUE = 0;
    private final boolean DEFAULT_BOOLEAN_VALUE = false;

    //Variablen als Objekte
    private Context context;
    public SharedPreferences prefs;
    private SharedPreferences.Editor e;

    //Variablen

    //Konstruktor
    public StorageUtils(Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putString(String name, String value) {
        e = prefs.edit();
        e.putString(name, value);
        e.commit();
    }

    public void putInt(String name, int value) {
        e = prefs.edit();
        e.putInt(name, value);
        e.commit();
    }

    public void putBoolean(String name, boolean value) {
        e = prefs.edit();
        e.putBoolean(name, value);
        e.commit();
    }

    public String getString(String name) {
        return prefs.getString("name", DEFAULT_STRING_VALUE);
    }

    public int getInt(String name) {
        return prefs.getInt(name, DEFAULT_INT_VALUE);
    }

    public boolean getBoolean(String name) {
        return prefs.getBoolean(name, DEFAULT_BOOLEAN_VALUE);
    }

    public String getString(String name, String default_value) {
        return prefs.getString("name", default_value);
    }

    public int getInt(String name, int default_value) {
        return prefs.getInt(name, default_value);
    }

    public boolean getBoolean(String name, boolean default_value) {
        return prefs.getBoolean(name, default_value);
    }

    //-----------------------------------------News-Funktionen--------------------------------------

    public void addNew(int id, String subject, String description, String receiver, String writer, String activation_date, String expitration_date) {
        //New-Daten in die SharedPreferences speichern
        String complete_new_string = String.valueOf(id) + "," + subject + "," + description + "," + receiver + "," + writer + "," + activation_date + "," + expitration_date;
        putString("N" + String.valueOf(id), complete_new_string);
        //News-Anzahl in den SharedPreferences um eins erhöhen
        putInt("NCount", getInt("NCount") +1);
    }

    public ArrayList<New> parseNews() {
        ArrayList<New> news = new ArrayList<New>();
        for(int i = 0; i <= getInt("NCount"); i++) {
            String current_new = getString("N" + String.valueOf(i), null);
            if(current_new != null) {
                //Aktuelle Nachricht zerteilen
                int index1 = current_new.indexOf(",");
                int index2 = current_new.indexOf(",", index1 +1);
                int index3 = current_new.indexOf(",", index2 +1);
                int index4 = current_new.indexOf(",", index3 +1);
                int index5 = current_new.indexOf(",", index4 +1);
                int index6 = current_new.indexOf(",", index5 +1);
                //Unterteilen
                int current_new_id = Integer.parseInt(current_new.substring(0, index1));
                String current_new_subject = current_new.substring(index1 +1, index2);
                String current_new_description = current_new.substring(index2 +1, index3);
                String current_new_receiver = current_new.substring(index3 +1, index4);
                String current_new_writer = current_new.substring(index4 +1, index5);
                String current_new_activation_date = current_new.substring(index5 +1, index6);
                String current_new_expiration_date = current_new.substring(index6 +1);
                //New-Objekt erstellen und der ArrayList hinzufügen
                news.add(new New(current_new_id, current_new_subject, current_new_description, current_new_receiver, current_new_writer, current_new_activation_date, current_new_expiration_date));
            } else {
                putInt("NCount", i - 1);
                break;
            }
        }
        return news;
    }
}