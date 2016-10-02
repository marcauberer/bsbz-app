package com.mrgames13.jimdo.bsbz_app.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Classtest;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Event;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Homework;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.New;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;

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
        return prefs.getString(name, DEFAULT_STRING_VALUE);
    }

    public int getInt(String name) {
        return prefs.getInt(name, DEFAULT_INT_VALUE);
    }

    public boolean getBoolean(String name) {
        return prefs.getBoolean(name, DEFAULT_BOOLEAN_VALUE);
    }

    public String getString(String name, String default_value) {
        return prefs.getString(name, default_value);
    }

    public int getInt(String name, int default_value) {
        return prefs.getInt(name, default_value);
    }

    public boolean getBoolean(String name, boolean default_value) {
        return prefs.getBoolean(name, default_value);
    }

    //-------------------------------------Stundenplan-Funktionen-----------------------------------

    public void addTimetable(String tt_receiver, String tt_mo, String tt_di, String tt_mi, String tt_do, String tt_fr) {
        //TimeTable-Daten in die SharedPreferences speichern
        String complete_classtest_string = tt_mo + ";" + tt_di + ";" + tt_mi + ";" + tt_do + ";" + tt_fr;
        putString("T" + tt_receiver, complete_classtest_string);
    }

    public TimeTable getTimeTable(String tt_receiver) {
        TimeTable timetable;
        if(tt_receiver.equals("no_class")) {
            timetable = new TimeTable(tt_receiver, "-,-,-,-,-,-,-,-,-,-", "-,-,-,-,-,-,-,-,-,-", "-,-,-,-,-,-,-,-,-,-", "-,-,-,-,-,-,-,-,-,-", "-,-,-,-,-,-,-,-,-,-");
        } else {
            String current_timetable = getString("T" + tt_receiver, null);
            if(current_timetable == null) return null;
            //Aktuellen Stundenplan zerteilen
            int index1 = current_timetable.indexOf(";");
            int index2 = current_timetable.indexOf(";", index1 +1);
            int index3 = current_timetable.indexOf(";", index2 +1);
            int index4 = current_timetable.indexOf(";", index3 +1);
            //Unterteilen
            String current_timetable_mo = current_timetable.substring(0, index1);
            String current_timetable_di = current_timetable.substring(index1 +1, index2);
            String current_timetable_mi = current_timetable.substring(index2 +1, index3);
            String current_timetable_do = current_timetable.substring(index3 +1, index4);
            String current_timetable_fr = current_timetable.substring(index4 +1);
            Log.d("BSBZ-App", "MO: " + current_timetable_mo);
            //Timetable-Objekt erstellen und der ArrayList hinzufügen
            timetable = new TimeTable(tt_receiver, current_timetable_mo, current_timetable_di, current_timetable_mi, current_timetable_do, current_timetable_fr);
        }
        return timetable;
    }

    public int getTimeTableCount() {
        return getInt("TCount");
    }

    public void setTimeTableCount(int count) {
        putInt("TCount", count);
    }

    //-----------------------------------Klassenarbeiten-Funktionen---------------------------------

    public void addClasstest(int id, String subject, String description, String receiver, String writer, String date) {
        //Classtest-Daten in die SharedPreferences speichern
        String complete_classtest_string = String.valueOf(id) + "," + subject + "," + description + "," + receiver + "," + writer + "," + date;
        putString("C" + String.valueOf(id), complete_classtest_string);
        //Classtest-Anzahl in den SharedPreferences um eins erhöhen
        putInt("CCount", getClasstestCount() +1);
    }

    public ArrayList<Classtest> parseClasstests(String month, String date) {
        ArrayList<Classtest> classtests = new ArrayList<>();
        int c_count = getClasstestCount();
        for(int i = 1; i <= c_count; i++) {
            String current_classtest = getString("C" + String.valueOf(i), null);
            if(current_classtest != null) {
                //Aktuelle Nachricht zerteilen
                int index1 = current_classtest.indexOf(",");
                int index2 = current_classtest.indexOf(",", index1 +1);
                int index3 = current_classtest.indexOf(",", index2 +1);
                int index4 = current_classtest.indexOf(",", index3 +1);
                int index5 = current_classtest.indexOf(",", index4 +1);
                //Unterteilen
                int current_classtest_id = Integer.parseInt(current_classtest.substring(0, index1));
                String current_classtest_subject = current_classtest.substring(index1 +1, index2);
                String current_classtest_description = current_classtest.substring(index2 +1, index3);
                String current_classtest_receiver = current_classtest.substring(index3 +1, index4);
                String current_classtest_writer = current_classtest.substring(index4 +1, index5);
                String current_classtest_date = current_classtest.substring(index5 +1);
                //Classtest-Objekt erstellen und der ArrayList hinzufügen
                if(month != null) {
                    if(current_classtest_date.substring(3, 5).equals(month)) {
                        Classtest c = new Classtest(current_classtest_id, current_classtest_subject, current_classtest_description, current_classtest_receiver, current_classtest_writer, current_classtest_date);
                        classtests.add(c);
                    }
                } else if(date != null) {
                    if(current_classtest_date.equals(date)) {
                        Classtest c = new Classtest(current_classtest_id, current_classtest_subject, current_classtest_description, current_classtest_receiver, current_classtest_writer, current_classtest_date);
                        classtests.add(c);
                    }
                } else {
                    Classtest c = new Classtest(current_classtest_id, current_classtest_subject, current_classtest_description, current_classtest_receiver, current_classtest_writer, current_classtest_date);
                    classtests.add(c);
                }
            } else {
                setClasstestCount(i -1);
                break;
            }
        }
        return classtests;
    }

    public void deleteAllClasstests() {
        int c_count = getClasstestCount();
        for(int i = 1; i <= c_count; i++) {
            prefs.edit().remove("C" + String.valueOf(i)).commit();
        }
    }

    public int getClasstestCount() {
        return getInt("CCount");
    }

    public void setClasstestCount(int count) {
        putInt("CCount", count);
    }

    //-------------------------------------Hausaufgaben-Funktionen----------------------------------

    public void addHomework(int id, String subject, String description, String receiver, String writer, String date) {
        //Classtest-Daten in die SharedPreferences speichern
        String complete_homework_string = String.valueOf(id) + "," + subject + "," + description + "," + receiver + "," + writer + "," + date;
        putString("H" + String.valueOf(id), complete_homework_string);
        //Classtest-Anzahl in den SharedPreferences um eins erhöhen
        putInt("HCount", getHomeworkCount() +1);
    }

    public ArrayList<Homework> parseHomeworks(String month, String date) {
        ArrayList<Homework> homeworks = new ArrayList<>();
        int h_count = getHomeworkCount();
        for(int i = 1; i <= h_count; i++) {
            String current_homework = getString("H" + String.valueOf(i), null);
            if(current_homework != null) {
                //Aktuelle Nachricht zerteilen
                int index1 = current_homework.indexOf(",");
                int index2 = current_homework.indexOf(",", index1 +1);
                int index3 = current_homework.indexOf(",", index2 +1);
                int index4 = current_homework.indexOf(",", index3 +1);
                int index5 = current_homework.indexOf(",", index4 +1);
                //Unterteilen
                int current_homework_id = Integer.parseInt(current_homework.substring(0, index1));
                String current_homework_subject = current_homework.substring(index1 +1, index2);
                String current_homework_description = current_homework.substring(index2 +1, index3);
                String current_homework_receiver = current_homework.substring(index3 +1, index4);
                String current_homework_writer = current_homework.substring(index4 +1, index5);
                String current_homework_date = current_homework.substring(index5 +1);
                //Homework-Objekt erstellen und der ArrayList hinzufügen
                if(month != null) {
                    if(current_homework_date.substring(3, 5).equals(month)) {
                        Homework h = new Homework(current_homework_id, current_homework_subject, current_homework_description, current_homework_receiver, current_homework_writer, current_homework_date);
                        homeworks.add(h);
                    }
                } else if(date != null) {
                    if(current_homework_date.equals(date)) {
                        Homework h = new Homework(current_homework_id, current_homework_subject, current_homework_description, current_homework_receiver, current_homework_writer, current_homework_date);
                        homeworks.add(h);
                    }
                } else {
                    Homework h = new Homework(current_homework_id, current_homework_subject, current_homework_description, current_homework_receiver, current_homework_writer, current_homework_date);
                    homeworks.add(h);
                }
            } else {
                setHomeworkCount(i -1);
                break;
            }
        }
        return homeworks;
    }

    public void deleteAllHomeworks() {
        int h_count = getHomeworkCount();
        for(int i = 1; i <= h_count; i++) {
            prefs.edit().remove("H" + String.valueOf(i)).commit();
        }
    }

    public int getHomeworkCount() {
        return getInt("HCount");
    }

    public void setHomeworkCount(int count) {
        putInt("HCount", count);
    }

    //----------------------------------------Termine-Funktionen------------------------------------

    public void addEvent(int id, String subject, String description, String receiver, String writer, String date) {
        //Classtest-Daten in die SharedPreferences speichern
        String complete_event_string = String.valueOf(id) + "," + subject + "," + description + "," + receiver + "," + writer + "," + date;
        putString("E" + String.valueOf(id), complete_event_string);
        //Classtest-Anzahl in den SharedPreferences um eins erhöhen
        putInt("ECount", getEventCount() +1);
    }

    public ArrayList<Event> parseEvents(String month, String date) {
        ArrayList<Event> events = new ArrayList<>();
        int e_count = getEventCount();
        for(int i = 1; i <= e_count; i++) {
            String current_event = getString("E" + String.valueOf(i), null);
            if(current_event != null) {
                //Aktuelle Nachricht zerteilen
                int index1 = current_event.indexOf(",");
                int index2 = current_event.indexOf(",", index1 +1);
                int index3 = current_event.indexOf(",", index2 +1);
                int index4 = current_event.indexOf(",", index3 +1);
                int index5 = current_event.indexOf(",", index4 +1);
                //Unterteilen
                int current_event_id = Integer.parseInt(current_event.substring(0, index1));
                String current_event_subject = current_event.substring(index1 +1, index2);
                String current_event_description = current_event.substring(index2 +1, index3);
                String current_event_receiver = current_event.substring(index3 +1, index4);
                String current_event_writer = current_event.substring(index4 +1, index5);
                String current_event_date = current_event.substring(index5 +1);
                //New-Objekt erstellen und der ArrayList hinzufügen
                if(month != null) {
                    if(current_event_date.substring(3, 5).equals(month)) {
                        Event e = new Event(current_event_id, current_event_subject, current_event_description, current_event_receiver, current_event_writer, current_event_date);
                        events.add(e);
                    }
                } else if(date != null) {
                    if(current_event_date.equals(date)) {
                        Event e = new Event(current_event_id, current_event_subject, current_event_description, current_event_receiver, current_event_writer, current_event_date);
                        events.add(e);
                    }
                } else {
                    Event e = new Event(current_event_id, current_event_subject, current_event_description, current_event_receiver, current_event_writer, current_event_date);
                    events.add(e);
                }
            } else {
                setEventCount(i -1);
                break;
            }
        }
        return events;
    }

    public void deleteAllEvents() {
        int e_count = getEventCount();
        for(int i = 1; i <= e_count; i++) {
            prefs.edit().remove("E" + String.valueOf(i)).commit();
        }
    }

    public int getEventCount() {
        return getInt("ECount");
    }

    public void setEventCount(int count) {
        putInt("ECount", count);
    }

    //-----------------------------------------News-Funktionen--------------------------------------

    public void addNew(int id, int state, String subject, String description, String receiver, String writer, String activation_date, String expitration_date) {
        //New-Daten in die SharedPreferences speichern
        String complete_new_string = String.valueOf(id) + "," + String.valueOf(state) + "," + subject + "," + description + "," + receiver + "," + writer + "," + activation_date + "," + expitration_date;
        putString("N" + String.valueOf(id), complete_new_string);
        //News-Anzahl in den SharedPreferences um eins erhöhen
        putInt("NCount", getNewsCount() +1);
    }

    public ArrayList<New> parseNews() {
        ArrayList<New> news = new ArrayList<>();
        int n_count = getNewsCount();
        for(int i = 1; i <= n_count; i++) {
            String current_new = getString("N" + String.valueOf(i), null);
            if(current_new != null) {
                //Aktuelle Nachricht zerteilen
                int index1 = current_new.indexOf(",");
                int index2 = current_new.indexOf(",", index1 +1);
                int index3 = current_new.indexOf(",", index2 +1);
                int index4 = current_new.indexOf(",", index3 +1);
                int index5 = current_new.indexOf(",", index4 +1);
                int index6 = current_new.indexOf(",", index5 +1);
                int index7 = current_new.indexOf(",", index6 +1);
                //Unterteilen
                int current_new_id = Integer.parseInt(current_new.substring(0, index1));
                int current_new_state = Integer.parseInt(current_new.substring(index1 +1, index2));
                String current_new_subject = current_new.substring(index2 +1, index3);
                String current_new_description = current_new.substring(index3 +1, index4);
                String current_new_receiver = current_new.substring(index4 +1, index5);
                String current_new_writer = current_new.substring(index5 +1, index6);
                String current_new_activation_date = current_new.substring(index6 +1, index7);
                String current_new_expiration_date = current_new.substring(index7 +1);
                if(current_new_state == 1) {
                    //New-Objekt erstellen und der ArrayList hinzufügen
                    New n = new New(current_new_id, current_new_state, current_new_subject, current_new_description, current_new_receiver, current_new_writer, current_new_activation_date, current_new_expiration_date);
                    news.add(n);
                }
            } else {
                setNewsCount(i -1);
                break;
            }
        }
        return news;
    }

    public ArrayList<New> parseNewsAndInvisibleNews() {
        ArrayList<New> news = new ArrayList<>();
        int n_count = getNewsCount();
        for(int i = 1; i <= n_count; i++) {
            String current_new = getString("N" + String.valueOf(i), null);
            if(current_new != null) {
                //Aktuelle Nachricht zerteilen
                int index1 = current_new.indexOf(",");
                int index2 = current_new.indexOf(",", index1 +1);
                int index3 = current_new.indexOf(",", index2 +1);
                int index4 = current_new.indexOf(",", index3 +1);
                int index5 = current_new.indexOf(",", index4 +1);
                int index6 = current_new.indexOf(",", index5 +1);
                int index7 = current_new.indexOf(",", index6 +1);
                //Unterteilen
                int current_new_id = Integer.parseInt(current_new.substring(0, index1));
                int current_new_state = Integer.parseInt(current_new.substring(index1 +1, index2));
                String current_new_subject = current_new.substring(index2 +1, index3);
                String current_new_description = current_new.substring(index3 +1, index4);
                String current_new_receiver = current_new.substring(index4 +1, index5);
                String current_new_writer = current_new.substring(index5 +1, index6);
                String current_new_activation_date = current_new.substring(index6 +1, index7);
                String current_new_expiration_date = current_new.substring(index7 +1);
                //New-Objekt erstellen und der ArrayList hinzufügen
                New n = new New(current_new_id, current_new_state, current_new_subject, current_new_description, current_new_receiver, current_new_writer, current_new_activation_date, current_new_expiration_date);
                news.add(n);
            } else {
                setNewsCount(i -1);
                break;
            }
        }
        return news;
    }

    public void deleteAllNews() {
        int n_count = getNewsCount();
        for(int i = 1; i <= n_count; i++) {
            prefs.edit().remove("N" + String.valueOf(i)).commit();
        }
    }

    public int getNewsCount() {
        return getInt("NCount");
    }

    public void setNewsCount(int count) {
        putInt("NCount", count);
    }
}