package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrgames13.jimdo.bsbz_app.R;

import java.util.ArrayList;

public class ViewPagerAdapterDayDetails extends FragmentPagerAdapter {

    //Konstanten

    //Variablen als Objekte
    private Resources res;
    private ArrayList<String> tabTitles;

    //Variablen

    //Konstruktor
    public ViewPagerAdapterDayDetails(FragmentManager manager, Context context) {
        super(manager);
        this.res = context.getResources();
        tabTitles.add(res.getString(R.string.day_details_tab_1));
        tabTitles.add(res.getString(R.string.day_details_tab_2));
        tabTitles.add(res.getString(R.string.day_details_tab_3));
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new ClasstestFragment();
            case 1: return new HomeworkFragment();
            case 2: return new EventFragment();
        }
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

    public static class ClasstestFragment extends Fragment {
        //Konstanten

        //Variablen als Objekte
        private View contentView;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.day_details_classtests, null);


            return contentView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

    public static class HomeworkFragment extends Fragment {
        //Konstanten

        //Variablen als Objekte
        private View contentView;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.day_details_homework, null);


            return contentView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

    public static class EventFragment extends Fragment {
        //Konstanten

        //Variablen als Objekte
        private View contentView;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.day_details_events, null);


            return contentView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }
}