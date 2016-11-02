package com.mrgames13.jimdo.bsbz_app.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.ViewPagerAdapters.ViewPagerAdapterDayDetails;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;

public class DayDetailsActivity extends AppCompatActivity {

	//Konstanten

    //Variablen als Objekte
    private Toolbar toolbar;
    private ViewPager viewpager;
    private ViewPagerAdapterDayDetails viewpager_adapter;
    private TabLayout tablayout;
    private Resources res;
    private StorageUtils su;

	//Variablen
	public static String current_date = "";
	
	@Override
	public void onStart() {
		super.onStart();

		// Daten von den SharedPreferences abrufen
		String layout = su.getString("Layout", MainActivity.res.getString(R.string.bsbz_layout_orange));
		String color = "#ea690c";
		if (layout.equals("0")) {
			color = "#ea690c";
		} else if (layout.equals("1")) {
			color = "#000000";
		} else if (layout.equals("2")) {
			color = "#3ded25";
		} else if (layout.equals("3")) {
			color = "#ff0000";
		} else if (layout.equals("4")) {
			color = "#0000ff";
		} else if (layout.equals("5")) {
			color = "#00007f";
		}
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		tablayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// ActionBar Titel festlegen
		String day = getIntent().getStringExtra("Day").toString();
		getSupportActionBar().setTitle(day + " der " + current_date);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Theme setzen
		if(MainActivity.AppTheme == 0) {
			setTheme(R.style.FirstTheme);
		} else if(MainActivity.AppTheme == 1) {
			setTheme(R.style.SecondTheme);
		}
		
		setContentView(R.layout.activity_day_details);

		//Toolbar aufsetzen
		toolbar = (Toolbar) findViewById(R.id.toolbar_day_details);
		setSupportActionBar(toolbar);

        //Resourcen initialisieren
        res = getResources();

        //StorageUtils initialisieren
        su = new StorageUtils(this, res);

        //Datum ermitteln
        current_date = getIntent().getStringExtra("Date").toString();

        //ViewPager aufsetzen
        viewpager_adapter = new ViewPagerAdapterDayDetails(getSupportFragmentManager(), getResources(), current_date);
        viewpager = (ViewPager) findViewById(R.id.day_details_view_pager);
        viewpager.setAdapter(viewpager_adapter);

        //TabLayout aufsetzen
        tablayout = (TabLayout) findViewById(R.id.day_details_tab_layout);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        if(MainActivity.classtests.size() > 0) {
            viewpager.setCurrentItem(tablayout.getTabAt(0).getPosition());
        } else if(MainActivity.homeworks.size() > 0) {
            viewpager.setCurrentItem(tablayout.getTabAt(1).getPosition());
        } else if(MainActivity.events.size() > 0) {
            viewpager.setCurrentItem(tablayout.getTabAt(2).getPosition());
        }

        //FloatingAction Button
        FloatingActionButton new_element = (FloatingActionButton) findViewById(R.id.day_details_new_element);
        new_element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog d = new AlertDialog.Builder(DayDetailsActivity.this)
                        .setTitle(res.getString(R.string.create_))
                        .setView(R.layout.dialogview_chooser_element)
                        .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(res.getString(R.string.next), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SwitchCompat sw1 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_classtest);
                                SwitchCompat sw2 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_homework);
                                SwitchCompat sw3 = (SwitchCompat) ((AlertDialog) dialog).findViewById(R.id.chooser_element_event);
                                if(sw1.isChecked()) {
                                    Intent i = new Intent(DayDetailsActivity.this, NewEditElementActivity.class);
                                    i.putExtra("mode", NewEditElementActivity.MODE_CREATE_CLASSTEST);
                                    startActivity(i);
                                } else if(sw2.isChecked()) {
                                    Intent i = new Intent(DayDetailsActivity.this, NewEditElementActivity.class);
                                    i.putExtra("mode", NewEditElementActivity.MODE_CREATE_HOMEWORK);
                                    startActivity(i);
                                } else if(sw3.isChecked()) {
                                    Intent i = new Intent(DayDetailsActivity.this, NewEditElementActivity.class);
                                    i.putExtra("mode", NewEditElementActivity.MODE_CREATE_EVENT);
                                    startActivity(i);
                                }
                            }
                        })
                        .create();
                d.show();

                final SwitchCompat sw1 = (SwitchCompat) d.findViewById(R.id.chooser_element_classtest);
                final SwitchCompat sw2 = (SwitchCompat) d.findViewById(R.id.chooser_element_homework);
                final SwitchCompat sw3 = (SwitchCompat) d.findViewById(R.id.chooser_element_event);
                //Swiches voreinstellen
                int state = tablayout.getSelectedTabPosition();
                sw1.setChecked(state == 0);
                sw2.setChecked(state == 1);
                sw3.setChecked(state == 2);
                //Auf Änderungen reagieren
                sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(isChecked);
                            sw2.setChecked(false);
                            sw3.setChecked(false);
                        }
                    }
                });
                sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(isChecked);
                            sw3.setChecked(false);
                        }
                    }
                });
                sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(false);
                            sw3.setChecked(isChecked);
                        }
                    }
                });
            }
        });

		current_date = getIntent().getStringExtra("Date").toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.day_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}