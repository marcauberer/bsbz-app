package com.mrgames13.jimdo.bsbz_app.ViewPagerAdapters;

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
import com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters.ElementViewAdapter;

import java.util.ArrayList;

public class ViewPagerAdapterPlanOfTheYear extends FragmentPagerAdapter {

    //Konstanten

    //Variablen als Objekte
    public static Resources res;
    private ArrayList<String> tabTitles = new ArrayList<>();

    //Variablen
    public static String current_month;

    //Konstruktor
    public ViewPagerAdapterPlanOfTheYear(FragmentManager manager, Resources res, String current_month) {
        super(manager);
        ViewPagerAdapterPlanOfTheYear.res = res;
        tabTitles.add(res.getString(R.string.day_details_tab_1));
        tabTitles.add(res.getString(R.string.day_details_tab_2));
        tabTitles.add(res.getString(R.string.day_details_tab_3));
        ViewPagerAdapterPlanOfTheYear.current_month = current_month;
        //Daten laden
        MainActivity.classtests = MainActivity.su.parseClasstests(current_month, null);
        MainActivity.homeworks = MainActivity.su.parseHomeworks(current_month, null);
        MainActivity.events = MainActivity.su.parseEvents(current_month, null);
    }

    @Override
    public Fragment getItem(int pos) {
        if(pos == 0) return new ClasstestFragment();
        if(pos == 1) return new HomeworkFragment();
        if(pos == 2) return new EventFragment();
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
            classtest_view = (RecyclerView) contentView.findViewById(R.id.day_details_recyclerview_classtests);
            classtest_view_manager = new LinearLayoutManager(contentView.getContext());
            classtest_view.setLayoutManager(classtest_view_manager);
            classtest_view_adapter = new ElementViewAdapter(contentView.getContext(), ElementViewAdapter.MODE_CLASSTEST);
            classtest_view.setAdapter(classtest_view_adapter);
            if(classtest_view_adapter.getItemCount() == 0) contentView.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
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
        private RecyclerView homework_view;
        private RecyclerView.Adapter homework_view_adapter;
        private RecyclerView.LayoutManager homework_view_manager;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.day_details_homework, null);
            //HomeworkRecyclerView anzeigen
            homework_view = (RecyclerView) contentView.findViewById(R.id.day_details_recyclerview_homework);
            homework_view_manager = new LinearLayoutManager(contentView.getContext());
            homework_view.setLayoutManager(homework_view_manager);
            homework_view_adapter = new ElementViewAdapter(contentView.getContext(), ElementViewAdapter.MODE_HOMEWORK);
            homework_view.setAdapter(homework_view_adapter);
            if(homework_view_adapter.getItemCount() == 0) contentView.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
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
        private RecyclerView events_view;
        private RecyclerView.Adapter events_view_adapter;
        private RecyclerView.LayoutManager events_view_manager;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.day_details_events, null);
            //EventRecyclerView anzeigen
            events_view = (RecyclerView) contentView.findViewById(R.id.day_details_recyclerview_events);
            events_view_manager = new LinearLayoutManager(contentView.getContext());
            events_view.setLayoutManager(events_view_manager);
            events_view_adapter = new ElementViewAdapter(contentView.getContext(), ElementViewAdapter.MODE_EVENT);
            events_view.setAdapter(events_view_adapter);
            if(events_view_adapter.getItemCount() == 0) contentView.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
            return contentView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }
}