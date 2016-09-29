package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.R;

import java.util.ArrayList;

public class ViewPagerAdapterEditTimeTable extends FragmentPagerAdapter {

    //Konstanten

    //Variablen als Objekte
    public static Resources res;
    private ArrayList<String> tabTitles = new ArrayList<>();

    //Variablen
    public static String current_date;

    //Konstruktor
    public ViewPagerAdapterEditTimeTable(FragmentManager manager, Resources res) {
        super(manager);
        ViewPagerAdapterEditTimeTable.res = res;
        tabTitles.add(res.getString(R.string.monday));
        tabTitles.add(res.getString(R.string.tuesday));
        tabTitles.add(res.getString(R.string.wednesday));
        tabTitles.add(res.getString(R.string.thursday));
        tabTitles.add(res.getString(R.string.friday));
        ViewPagerAdapterEditTimeTable.current_date = current_date;
        //Daten laden
        MainActivity.classtests = MainActivity.su.parseClasstests(null, current_date);
        MainActivity.homeworks = MainActivity.su.parseHomeworks(null, current_date);
        MainActivity.events = MainActivity.su.parseEvents(null, current_date);
    }

    @Override
    public Fragment getItem(int pos) {
        if(pos == 0) return new MondayFragment();
        if(pos == 1) return new MondayFragment();
        if(pos == 2) return new MondayFragment();
        if(pos == 3) return new MondayFragment();
        if(pos == 4) return new MondayFragment();
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    //-------------------------------------------Fragmente------------------------------------------

    public static class MondayFragment extends Fragment {
        //Konstanten

        //Variablen als Objekte
        private View contentView;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren

            return contentView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }
}