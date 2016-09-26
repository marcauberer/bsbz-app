package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.R;

import java.util.ArrayList;

public class ViewPagerAdapterDayDetails extends FragmentPagerAdapter {

    //Konstanten

    //Variablen als Objekte
    private Resources res;
    private ArrayList<String> tabTitles = new ArrayList<>();

    //Variablen

    //Konstruktor
    public ViewPagerAdapterDayDetails(FragmentManager manager, Resources res) {
        super(manager);
        this.res = res;
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
        private RecyclerView classtest_view;
        private RecyclerView.Adapter classtest_view_adapter;
        private RecyclerView.LayoutManager classtest_view_manager;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.day_details_classtests, null);
            //ClasstestRecyclerView anzeigen
            classtest_view = (RecyclerView) findViewById(R.id.news_view);
            classtest_view_manager = new LinearLayoutManager(getContext());
            classtest_view.setLayoutManager(classtest_view_manager);
            classtest_view_adapter = new ElementViewAdapter(getContext(), ElementViewAdapter.MODE_NEW);
            classtest_view.setAdapter(classtest_view_adapter);

            if(classtest_view_adapter.getItemCount() == 0) findViewById(R.id.no_active_news).setVisibility(View.VISIBLE);
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