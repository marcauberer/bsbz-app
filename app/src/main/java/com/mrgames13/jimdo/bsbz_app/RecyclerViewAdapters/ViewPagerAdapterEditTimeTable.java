package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;

import java.util.ArrayList;

public class ViewPagerAdapterEditTimeTable extends FragmentPagerAdapter {

    //Konstanten

    //Variablen als Objekte
    public static TimeTable timetable;
    public static Resources res;
    private ArrayList<String> tabTitles = new ArrayList<>();

    //Variablen
    public static String selectedShort;

    //Konstruktor
    public ViewPagerAdapterEditTimeTable(FragmentManager manager, Resources res, TimeTable timetable) {
        super(manager);
        ViewPagerAdapterEditTimeTable.res = res;
        ViewPagerAdapterEditTimeTable.timetable = timetable;
        tabTitles.add(res.getString(R.string.monday));
        tabTitles.add(res.getString(R.string.tuesday));
        tabTitles.add(res.getString(R.string.wednesday));
        tabTitles.add(res.getString(R.string.thursday));
        tabTitles.add(res.getString(R.string.friday));
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

    public static class MondayFragment extends Fragment implements View.OnClickListener {
        //Konstanten

        //Variablen als Objekte
        private View contentView;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren
            Button hour1 = (Button) contentView.findViewById(R.id.edit_timetable_hour_1);
            Button hour2 = (Button) contentView.findViewById(R.id.edit_timetable_hour_2);
            Button hour3 = (Button) contentView.findViewById(R.id.edit_timetable_hour_3);
            Button hour4 = (Button) contentView.findViewById(R.id.edit_timetable_hour_4);
            Button hour5 = (Button) contentView.findViewById(R.id.edit_timetable_hour_5);
            Button hour6 = (Button) contentView.findViewById(R.id.edit_timetable_hour_6);
            Button hour7 = (Button) contentView.findViewById(R.id.edit_timetable_hour_7);
            Button hour8 = (Button) contentView.findViewById(R.id.edit_timetable_hour_8);
            Button hour9 = (Button) contentView.findViewById(R.id.edit_timetable_hour_9);
            Button hour10 = (Button) contentView.findViewById(R.id.edit_timetable_hour_10);
            //OnClickListener setzen
            hour1.setOnClickListener(this);
            hour2.setOnClickListener(this);
            hour3.setOnClickListener(this);
            hour4.setOnClickListener(this);
            hour5.setOnClickListener(this);
            hour6.setOnClickListener(this);
            hour7.setOnClickListener(this);
            hour8.setOnClickListener(this);
            hour9.setOnClickListener(this);
            hour10.setOnClickListener(this);
            //Texte setzen
            String daycode = timetable.getMo();
            //Daycode auseinandernehmen
            int index1 = daycode.indexOf(",", 0);
            int index2 = daycode.indexOf(",", index1 +1);
            int index3 = daycode.indexOf(",", index2 +1);
            int index4 = daycode.indexOf(",", index3 +1);
            int index5 = daycode.indexOf(",", index4 +1);
            int index6 = daycode.indexOf(",", index5 +1);
            int index7 = daycode.indexOf(",", index6 +1);
            int index8 = daycode.indexOf(",", index7 +1);
            int index9 = daycode.indexOf(",", index8 +1);
            hour1.setText(daycode.substring(0, index1));
            hour2.setText(daycode.substring(index1 +1, index2));
            hour3.setText(daycode.substring(index2 +1, index3));
            hour4.setText(daycode.substring(index3 +1, index4));
            hour5.setText(daycode.substring(index4 +1, index5));
            hour6.setText(daycode.substring(index5 +1, index6));
            hour7.setText(daycode.substring(index6 +1, index7));
            hour8.setText(daycode.substring(index7 +1, index8));
            hour9.setText(daycode.substring(index8 +1, index9));
            hour10.setText(daycode.substring(index9 +1));
            return contentView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onClick(final View v) {
            AlertDialog.Builder alert;
            if(MainActivity.AppTheme == 0) {
                alert = new AlertDialog.Builder(getActivity(), R.style.FirstTheme_Dialog);
            } else {
                alert = new AlertDialog.Builder(getActivity(), R.style.SecondTheme_Dialog);
            }

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialogview_chooser_school_subject, null);

            final RadioButton rd_specific = (RadioButton) dialogView.findViewById(R.id.rb_specific_subject);
            final RadioButton rd_other = (RadioButton) dialogView.findViewById(R.id.rb_other_subject);

            final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spnr_specific_subject);

            final EditText et_text = (EditText) dialogView.findViewById(R.id.et_other_subject_text);
            final EditText et_short = (EditText) dialogView.findViewById(R.id.et_other_subject_short);

            rd_specific.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    spinner.setEnabled(isChecked);
                }
            });
            rd_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    et_text.setEnabled(isChecked);
                    et_short.setEnabled(isChecked);
                }
            });

            alert.setView(dialogView);
            alert.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(rd_specific.isChecked()) {
                        String selectedItem = spinner.getSelectedItem().toString();
                        selectedShort = selectedItem.substring(selectedItem.indexOf("(")+1, selectedItem.indexOf(")"));
                    } else if(rd_other.isChecked()) {
                        selectedShort = et_short.getText().toString();
                        //Neues Fach in den SharedPreferences anlegen

                    }
                    Button btn = (Button) v;
                    btn.setText(selectedShort);
                }
            });
            alert.show();
        }
    }
}