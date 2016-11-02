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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.TimeTable;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerAdapterEditTimeTable extends FragmentPagerAdapter {

    //Konstanten

    //Variablen als Objekte
    private static TimeTable timetable;
    private static Resources res;
    private ArrayList<String> tabTitles = new ArrayList<>();
    private static ArrayList<String> savedSubjects = new ArrayList<>();
    private static StorageUtils su;
    public static MondayFragment mondayFragment = null;
    public static TuesdayFragment tuesdayFragment = null;
    public static WednesdayFragment wednesdayFragment = null;
    public static ThursdayFragment thursdayFragment = null;
    public static FridayFragment fridayFragment = null;

    //Variablen
    public static String selectedName;
    public static String selectedShort;
    public static String mondayDayCodeTmp = null;
    public static String tuesdayDayCodeTmp = null;
    public static String wednesdayDayCodeTmp = null;
    public static String thursdayCodeTmp = null;
    public static String fridayDayCodeTmp = null;

    //Konstruktor
    public ViewPagerAdapterEditTimeTable(FragmentManager manager, Resources res, StorageUtils su, TimeTable timetable) {
        super(manager);
        ViewPagerAdapterEditTimeTable.res = res;
        ViewPagerAdapterEditTimeTable.su = su;
        ViewPagerAdapterEditTimeTable.timetable = timetable;
        tabTitles.add(res.getString(R.string.monday));
        tabTitles.add(res.getString(R.string.tuesday));
        tabTitles.add(res.getString(R.string.wednesday));
        tabTitles.add(res.getString(R.string.thursday));
        tabTitles.add(res.getString(R.string.friday));
    }

    @Override
    public Fragment getItem(int pos) {
        if(pos == 0) return mondayFragment = new MondayFragment();
        if(pos == 1) return tuesdayFragment = new TuesdayFragment();
        if(pos == 2) return wednesdayFragment = new WednesdayFragment();
        if(pos == 3) return thursdayFragment = new ThursdayFragment();
        if(pos == 4) return fridayFragment = new FridayFragment();
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
        public static View contentView;
        public static Button hour1;
        public static Button hour2;
        public static Button hour3;
        public static Button hour4;
        public static Button hour5;
        public static Button hour6;
        public static Button hour7;
        public static Button hour8;
        public static Button hour9;
        public static Button hour10;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren
            hour1 = (Button) contentView.findViewById(R.id.edit_timetable_hour_1);
            hour2 = (Button) contentView.findViewById(R.id.edit_timetable_hour_2);
            hour3 = (Button) contentView.findViewById(R.id.edit_timetable_hour_3);
            hour4 = (Button) contentView.findViewById(R.id.edit_timetable_hour_4);
            hour5 = (Button) contentView.findViewById(R.id.edit_timetable_hour_5);
            hour6 = (Button) contentView.findViewById(R.id.edit_timetable_hour_6);
            hour7 = (Button) contentView.findViewById(R.id.edit_timetable_hour_7);
            hour8 = (Button) contentView.findViewById(R.id.edit_timetable_hour_8);
            hour9 = (Button) contentView.findViewById(R.id.edit_timetable_hour_9);
            hour10 = (Button) contentView.findViewById(R.id.edit_timetable_hour_10);
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
            //SavedSubjects laden
            savedSubjects = su.getSavedSubjects();
            //Dialog anzeigen
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

            String[] subject_res = getResources().getStringArray(R.array.subject_items);
            List<String> subject_res_list = new ArrayList<>();
            subject_res_list.addAll(Arrays.asList(subject_res));
            subject_res_list.addAll(su.getSavedSubjects());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject_res_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

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
                        selectedName = et_text.getText().toString().trim();
                        selectedShort = et_short.getText().toString().trim();
                        //Neues Fach anlegen
                        su.addNewSavedSubject(selectedName, selectedShort);
                    }
                    Button btn = (Button) v;
                    btn.setText(selectedShort);
                }
            });
            alert.show();
        }

        public String getHourCodes() {
            return hour1.getText().toString() + "," + hour2.getText().toString() + "," + hour3.getText().toString() + "," + hour4.getText().toString() + "," + hour5.getText().toString() + "," + hour6.getText().toString() + "," + hour7.getText().toString() + "," + hour8.getText().toString() + "," + hour9.getText().toString() + "," + hour10.getText().toString();
        }
    }

    public static class TuesdayFragment extends Fragment implements View.OnClickListener {
        //Konstanten

        //Variablen als Objekte
        private View contentView;
        public static Button hour1;
        public static Button hour2;
        public static Button hour3;
        public static Button hour4;
        public static Button hour5;
        public static Button hour6;
        public static Button hour7;
        public static Button hour8;
        public static Button hour9;
        public static Button hour10;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren
            hour1 = (Button) contentView.findViewById(R.id.edit_timetable_hour_1);
            hour2 = (Button) contentView.findViewById(R.id.edit_timetable_hour_2);
            hour3 = (Button) contentView.findViewById(R.id.edit_timetable_hour_3);
            hour4 = (Button) contentView.findViewById(R.id.edit_timetable_hour_4);
            hour5 = (Button) contentView.findViewById(R.id.edit_timetable_hour_5);
            hour6 = (Button) contentView.findViewById(R.id.edit_timetable_hour_6);
            hour7 = (Button) contentView.findViewById(R.id.edit_timetable_hour_7);
            hour8 = (Button) contentView.findViewById(R.id.edit_timetable_hour_8);
            hour9 = (Button) contentView.findViewById(R.id.edit_timetable_hour_9);
            hour10 = (Button) contentView.findViewById(R.id.edit_timetable_hour_10);
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
            String daycode = timetable.getDi();
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
            //SavedSubjects laden
            savedSubjects = su.getSavedSubjects();
            //Dialog anzeigen
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

            String[] subject_res = getResources().getStringArray(R.array.subject_items);
            List<String> subject_res_list = new ArrayList<>();
            subject_res_list.addAll(Arrays.asList(subject_res));
            subject_res_list.addAll(su.getSavedSubjects());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject_res_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

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
                        //Neues Fach anlegen
                        su.addNewSavedSubject(selectedName, selectedShort);
                    }
                    Button btn = (Button) v;
                    btn.setText(selectedShort);
                }
            });
            alert.show();
        }

        public String getHourCodes() {
            return hour1.getText().toString() + "," + hour2.getText().toString() + "," + hour3.getText().toString() + "," + hour4.getText().toString() + "," + hour5.getText().toString() + "," + hour6.getText().toString() + "," + hour7.getText().toString() + "," + hour8.getText().toString() + "," + hour9.getText().toString() + "," + hour10.getText().toString();
        }
    }

    public static class WednesdayFragment extends Fragment implements View.OnClickListener {
        //Konstanten

        //Variablen als Objekte
        private View contentView;
        public static Button hour1;
        public static Button hour2;
        public static Button hour3;
        public static Button hour4;
        public static Button hour5;
        public static Button hour6;
        public static Button hour7;
        public static Button hour8;
        public static Button hour9;
        public static Button hour10;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren
            hour1 = (Button) contentView.findViewById(R.id.edit_timetable_hour_1);
            hour2 = (Button) contentView.findViewById(R.id.edit_timetable_hour_2);
            hour3 = (Button) contentView.findViewById(R.id.edit_timetable_hour_3);
            hour4 = (Button) contentView.findViewById(R.id.edit_timetable_hour_4);
            hour5 = (Button) contentView.findViewById(R.id.edit_timetable_hour_5);
            hour6 = (Button) contentView.findViewById(R.id.edit_timetable_hour_6);
            hour7 = (Button) contentView.findViewById(R.id.edit_timetable_hour_7);
            hour8 = (Button) contentView.findViewById(R.id.edit_timetable_hour_8);
            hour9 = (Button) contentView.findViewById(R.id.edit_timetable_hour_9);
            hour10 = (Button) contentView.findViewById(R.id.edit_timetable_hour_10);
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
            String daycode = timetable.getMi();
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
            //SavedSubjects laden
            savedSubjects = su.getSavedSubjects();
            //Dialog anzeigen
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

            String[] subject_res = getResources().getStringArray(R.array.subject_items);
            List<String> subject_res_list = new ArrayList<>();
            subject_res_list.addAll(Arrays.asList(subject_res));
            subject_res_list.addAll(su.getSavedSubjects());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject_res_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

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
                        //Neues Fach anlegen
                        su.addNewSavedSubject(selectedName, selectedShort);
                    }
                    Button btn = (Button) v;
                    btn.setText(selectedShort);
                }
            });
            alert.show();
        }

        public String getHourCodes() {
            return hour1.getText().toString() + "," + hour2.getText().toString() + "," + hour3.getText().toString() + "," + hour4.getText().toString() + "," + hour5.getText().toString() + "," + hour6.getText().toString() + "," + hour7.getText().toString() + "," + hour8.getText().toString() + "," + hour9.getText().toString() + "," + hour10.getText().toString();
        }
    }

    public static class ThursdayFragment extends Fragment implements View.OnClickListener {
        //Konstanten

        //Variablen als Objekte
        private View contentView;
        public static Button hour1;
        public static Button hour2;
        public static Button hour3;
        public static Button hour4;
        public static Button hour5;
        public static Button hour6;
        public static Button hour7;
        public static Button hour8;
        public static Button hour9;
        public static Button hour10;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren
            hour1 = (Button) contentView.findViewById(R.id.edit_timetable_hour_1);
            hour2 = (Button) contentView.findViewById(R.id.edit_timetable_hour_2);
            hour3 = (Button) contentView.findViewById(R.id.edit_timetable_hour_3);
            hour4 = (Button) contentView.findViewById(R.id.edit_timetable_hour_4);
            hour5 = (Button) contentView.findViewById(R.id.edit_timetable_hour_5);
            hour6 = (Button) contentView.findViewById(R.id.edit_timetable_hour_6);
            hour7 = (Button) contentView.findViewById(R.id.edit_timetable_hour_7);
            hour8 = (Button) contentView.findViewById(R.id.edit_timetable_hour_8);
            hour9 = (Button) contentView.findViewById(R.id.edit_timetable_hour_9);
            hour10 = (Button) contentView.findViewById(R.id.edit_timetable_hour_10);
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
            String daycode = timetable.getDo();
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
            //SavedSubjects laden
            savedSubjects = su.getSavedSubjects();
            //Dialog anzeigen
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

            String[] subject_res = getResources().getStringArray(R.array.subject_items);
            List<String> subject_res_list = new ArrayList<>();
            subject_res_list.addAll(Arrays.asList(subject_res));
            subject_res_list.addAll(su.getSavedSubjects());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject_res_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

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
                        //Neues Fach anlegen
                        su.addNewSavedSubject(selectedName, selectedShort);
                    }
                    Button btn = (Button) v;
                    btn.setText(selectedShort);
                }
            });
            alert.show();
        }

        public String getHourCodes() {
            return hour1.getText().toString() + "," + hour2.getText().toString() + "," + hour3.getText().toString() + "," + hour4.getText().toString() + "," + hour5.getText().toString() + "," + hour6.getText().toString() + "," + hour7.getText().toString() + "," + hour8.getText().toString() + "," + hour9.getText().toString() + "," + hour10.getText().toString();
        }
    }

    public static class FridayFragment extends Fragment implements View.OnClickListener {
        //Konstanten

        //Variablen als Objekte
        private View contentView;
        public static Button hour1;
        public static Button hour2;
        public static Button hour3;
        public static Button hour4;
        public static Button hour5;
        public static Button hour6;
        public static Button hour7;
        public static Button hour8;
        public static Button hour9;
        public static Button hour10;

        //Variablen

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            contentView = inflater.inflate(R.layout.edit_timetable_day, null);
            //LayoutElemente initialisieren
            hour1 = (Button) contentView.findViewById(R.id.edit_timetable_hour_1);
            hour2 = (Button) contentView.findViewById(R.id.edit_timetable_hour_2);
            hour3 = (Button) contentView.findViewById(R.id.edit_timetable_hour_3);
            hour4 = (Button) contentView.findViewById(R.id.edit_timetable_hour_4);
            hour5 = (Button) contentView.findViewById(R.id.edit_timetable_hour_5);
            hour6 = (Button) contentView.findViewById(R.id.edit_timetable_hour_6);
            hour7 = (Button) contentView.findViewById(R.id.edit_timetable_hour_7);
            hour8 = (Button) contentView.findViewById(R.id.edit_timetable_hour_8);
            hour9 = (Button) contentView.findViewById(R.id.edit_timetable_hour_9);
            hour10 = (Button) contentView.findViewById(R.id.edit_timetable_hour_10);
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
            String daycode = timetable.getFr();
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
            //SavedSubjects laden
            savedSubjects = su.getSavedSubjects();
            //Dialog anzeigen
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

            String[] subject_res = getResources().getStringArray(R.array.subject_items);
            List<String> subject_res_list = new ArrayList<>();
            subject_res_list.addAll(Arrays.asList(subject_res));
            subject_res_list.addAll(su.getSavedSubjects());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subject_res_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

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
                        //Neues Fach anlegen
                        su.addNewSavedSubject(selectedName, selectedShort);
                    }
                    Button btn = (Button) v;
                    btn.setText(selectedShort);
                }
            });
            alert.show();
        }

        public String getHourCodes() {
            return hour1.getText().toString() + "," + hour2.getText().toString() + "," + hour3.getText().toString() + "," + hour4.getText().toString() + "," + hour5.getText().toString() + "," + hour6.getText().toString() + "," + hour7.getText().toString() + "," + hour8.getText().toString() + "," + hour9.getText().toString() + "," + hour10.getText().toString();
        }
    }

    //----------------------------------------------Methoden------------------------------------------------

    public void saveTmp() {
        if(mondayFragment != null) mondayDayCodeTmp = mondayFragment.getHourCodes();
        if(tuesdayFragment != null) tuesdayDayCodeTmp = tuesdayFragment.getHourCodes();
        if(wednesdayFragment != null) wednesdayDayCodeTmp = wednesdayFragment.getHourCodes();
        if(thursdayFragment != null) thursdayCodeTmp = thursdayFragment.getHourCodes();
        if(fridayFragment != null) fridayDayCodeTmp = fridayFragment.getHourCodes();
    }

    public String getTimeTableCode() {
        String mondayCode;
        String tuesdayCode;
        String wednesdayCode;
        String thursdayCode;
        String fridayCode;
        if(mondayDayCodeTmp != null) {
            mondayCode = mondayDayCodeTmp;
        } else {
            mondayCode = mondayFragment.getHourCodes();
        }
        if(tuesdayDayCodeTmp != null) {
            tuesdayCode = tuesdayDayCodeTmp;
        } else {
            tuesdayCode = tuesdayFragment.getHourCodes();
        }
        if(wednesdayDayCodeTmp != null) {
            wednesdayCode = wednesdayDayCodeTmp;
        } else {
            wednesdayCode = wednesdayFragment.getHourCodes();
        }
        if(thursdayCodeTmp != null) {
            thursdayCode = thursdayCodeTmp;
        } else {
            thursdayCode = thursdayFragment.getHourCodes();
        }
        if(fridayDayCodeTmp != null) {
            fridayCode = fridayDayCodeTmp;
        } else {
            fridayCode = fridayFragment.getHourCodes();
        }
        String timetableCode = mondayCode + ";" + tuesdayCode + ";" + wednesdayCode + ";" + thursdayCode + ";" + fridayCode;
        return timetableCode;
    }
}