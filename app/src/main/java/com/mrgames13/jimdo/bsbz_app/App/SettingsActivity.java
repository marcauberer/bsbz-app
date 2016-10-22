package com.mrgames13.jimdo.bsbz_app.App;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.PercentService;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.AccountUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.StorageUtils;
import com.mrgames13.jimdo.bsbz_app.Tools.ThemeUtils;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
@SuppressLint("InlinedApi")
public class SettingsActivity extends PreferenceActivity {

    //Variablen als Objekte
    private ConnectivityManager cm;
	private ServerMessagingUtils serverMessagingUtils;
	private Toolbar toolbar;
	private Resources res;
    private ProgressDialog pd_Progress;
	private StorageUtils su;
	private AccountUtils au;

    //Variablen
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	private static String result;
	private static Account current_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Resourcen initialisieren
        res = getResources();

        //StorageUtils initialisieren
        su = new StorageUtils(this, res);

        //AccountUtils initialisieren
        au = new AccountUtils(su);

        //Account laden
        current_account = au.getLastUser();

		//Theme setzen
		if(MainActivity.AppTheme == 0) {
			setTheme(R.style.FirstTheme);
			if(Build.VERSION.SDK_INT >= 21) {
				Window window = getWindow();
				window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor("#FFFFFF")));
			}
		} else if(MainActivity.AppTheme == 1) {
			setTheme(R.style.SecondTheme);
			View view = this.getWindow().getDecorView();
		    view.setBackgroundColor(res.getColor(R.color.background_gray));
		    if (Build.VERSION.SDK_INT >= 16) this.getListView().setBackgroundColor(res.getColor(R.color.background_gray));
		}

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, SettingsActivity.this);

		// Daten von den SharedPreferences abrufen
		String layout = su.getString("Layout", res.getString(R.string.bsbz_layout_orange));
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

		//Toolbar initialisieren
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
			toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
			toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
			toolbar.setTitleTextColor(res.getColor(R.color.white));

			Drawable upArrow = res.getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(res.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
			toolbar.setNavigationIcon(upArrow);

			root.addView(toolbar, 0);
		} else {
			ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
			ListView content = (ListView) root.getChildAt(0);

			root.removeAllViews();

			toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);

			toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
			toolbar.setTitleTextColor(res.getColor(R.color.white));

			Drawable upArrow = res.getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(res.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
			toolbar.setNavigationIcon(upArrow);

			int height;
			TypedValue tv = new TypedValue();
			if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
				height = TypedValue.complexToDimensionPixelSize(tv.data, res.getDisplayMetrics());
			} else{
				height = toolbar.getHeight();
			}

			content.setPadding(0, height, 0, 0);

			root.addView(content);
			root.addView(toolbar);
		}

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}
	
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) return;

		addPreferencesFromResource(R.xml.pref_general);
		addPreferencesFromResource(R.xml.pref_account);
		addPreferencesFromResource(R.xml.pref_notifications);
		if(current_account.getRights() == Account.RIGHTS_TEAM) {
            addPreferencesFromResource(R.xml.pref_infos_team);
        } else {
            addPreferencesFromResource(R.xml.pref_infos);
        }

		Preference delete_account_Pref = findPreference("delete_account");

		if(current_account.getUsername().equals(res.getString(R.string.guest))) delete_account_Pref.setEnabled(false);
		
		delete_account_Pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
			    //EitTextDialog anzeigen
                int style;
                if(MainActivity.AppTheme == 0) {
                    style = R.style.FirstTheme_Dialog;
                } else {
                    style = R.style.SecondTheme_Dialog;
                }
                final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this, style);
				builder1.setMessage(res.getString(R.string.please_enter_your_password_));
				//Eingabefeld zeichnen
				final EditText password = new EditText(SettingsActivity.this);
				password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				builder1.setView(password);
				
				builder1.setPositiveButton(res.getString(R.string.delete_account), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String right_password = current_account.getPassword();
						
						if(!right_password.equals(password.getText().toString())) {
							Toast.makeText(builder1.getContext(), res.getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
						} else {
							//AlertDialog anzeigen
                            int style;
                            if(MainActivity.AppTheme == 0) {
                                style = R.style.FirstTheme_Dialog;
                            } else {
                                style = R.style.SecondTheme_Dialog;
                            }
                            android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this ,style);
							builder2.setMessage(res.getString(R.string.do_you_want_to_delete_your_account));
							builder2.setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(final DialogInterface dialog, int which) {
									//Vom Server löschen
									new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+URLEncoder.encode(current_account.getUsername(), "UTF-8")+"&command=deleteaccount");
                                                //Activity starten
                                                Intent i = new Intent(SettingsActivity.this,LogInActivity.class);
                                                if(result.equals("Action Successful")) {
													su.putBoolean("Angemeldet bleiben", false);
                                                    //Extra erstellen
                                                    i.putExtra("Action", "deleted account");
                                                } else {
                                                    //Extra erstellen
                                                    i.putExtra("Action", "not deleted account");
                                                }
                                                startActivity(i);
                                                //Dialog schließen
                                                dialog.cancel();
                                                finish();
                                            } catch(Exception e) {}
                                        }
                                    }).start();
								}
							});
							builder2.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
							builder2.create().show();
						}
					}
				});
				builder1.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder1.create().show();
				
			    return false;
		    }
		});
		
		Preference change_password_Pref = findPreference("change_password");
		
		if(current_account.getUsername().equals(res.getString(R.string.guest))) change_password_Pref.setEnabled(false);
		
		change_password_Pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				//EitTextDialog anzeigen
                int style;
                if(MainActivity.AppTheme == 0) {
                    style = R.style.FirstTheme_Dialog;
                } else {
                    style = R.style.SecondTheme_Dialog;
                }
				final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this, style);
				builder1.setMessage(res.getString(R.string.change_password));
				
				LinearLayout layout = new LinearLayout(SettingsActivity.this);
		        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		        layout.setOrientation(LinearLayout.VERTICAL);
		        layout.setLayoutParams(parms);

		        layout.setGravity(Gravity.CLIP_VERTICAL);
		        layout.setPadding(6, 6, 6, 6);
				
				//Eingabefelder zeichnen
		        final TextView tv1 = new TextView(SettingsActivity.this);
				tv1.setText("Altes Passwort:");
				final EditText password_old = new EditText(SettingsActivity.this);
				password_old.setTransformationMethod(PasswordTransformationMethod.getInstance());
				final TextView tv2 = new TextView(SettingsActivity.this);
				tv2.setText("Neues Passwort:");
				final EditText password_new = new EditText(SettingsActivity.this);
				password_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
				final TextView tv3 = new TextView(SettingsActivity.this);
				tv3.setText("Neues Passwort wiederholen:");
				final EditText repassword_new = new EditText(SettingsActivity.this);
				repassword_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
				
				layout.addView(tv1);
				layout.addView(password_old);
				layout.addView(tv2);
				layout.addView(password_new);
				layout.addView(tv3);
				layout.addView(repassword_new);
				
				builder1.setView(layout);
				
				builder1.setPositiveButton(res.getString(R.string.change_password), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String right_password = current_account.getPassword();
						
						final String new_password = password_new.getText().toString();
						final String new_repassword = repassword_new.getText().toString();
						
						if(new_password.equals("") || new_repassword.equals("") || password_old.getText().toString().equals("")) {
							Toast.makeText(builder1.getContext(), res.getString(R.string.not_all_fields_filled), Toast.LENGTH_SHORT).show();
						} else if(!right_password.equals(password_old.getText().toString())) {
							Toast.makeText(builder1.getContext(), res.getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
						} else if(!new_password.equals(new_repassword)) {
							Toast.makeText(builder1.getContext(), res.getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
						} else {
							//AlertDialog anzeigen
                            int style;
                            if(MainActivity.AppTheme == 0) {
                                style = R.style.FirstTheme_Dialog;
                            } else {
                                style = R.style.SecondTheme_Dialog;
                            }
                            android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this, style);
							builder2.setMessage(res.getString(R.string.do_you_really_want_to_change_password));
							builder2.setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(final DialogInterface dialog, int which) {
                                    Log.d("BSBZ-App", current_account.getUsername());
                                    Log.d("BSBZ-App", new_password);
                                    Log.d("BSBZ-App", current_account.getForm());
									//Vom Server löschen
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+URLEncoder.encode(current_account.getUsername(), "UTF-8")+"&command=editaccount&new_password="+URLEncoder.encode(new_password, "UTF-8")+"&new_class="+URLEncoder.encode(current_account.getForm(), "UTF-8"));
                                                //Activity starten
                                                Intent i = new Intent(SettingsActivity.this, LogInActivity.class);
                                                if(result.equals("Action Successful")) {
													su.putBoolean("Angemeldet bleiben", false);
													su.putString("Password", new_password);
                                                    //Extra erstellen
                                                    i.putExtra("Action", "changed password");
                                                } else {
                                                    i.putExtra("Action", "not changed password");
                                                }
                                                startActivity(i);
                                                //Dialog schließen
                                                dialog.cancel();
                                                finish();
                                            } catch(Exception e) {}
                                        }
                                    }).start();
								}
							});
							builder2.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
							builder2.create().show();
						}
					}
				});
				builder1.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder1.create().show();
				
				return false;
			}
		});
		
		Preference show_percent = findPreference("send_percent_notification");
		show_percent.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				newValue = newValue.toString();
				
				if(newValue.equals("true")) {
					//Alarmmanager aufsetzen
					@SuppressWarnings("static-access")
					AlarmManager alarmmanager2 = (AlarmManager) SettingsActivity.this.getSystemService(SettingsActivity.this.ALARM_SERVICE);
					
					Intent startServiceIntent2 = new Intent(SettingsActivity.this, PercentService.class);
					PendingIntent startServicePendingIntent2 = PendingIntent.getService(SettingsActivity.this,0,startServiceIntent2, 0);
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					
					alarmmanager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, startServicePendingIntent2);
					
					//Service starten
					startService(new Intent(getApplicationContext(), PercentService.class));
				} else if(newValue.equals("false")) {
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					nm.cancel(4);
					@SuppressWarnings("static-access")
					AlarmManager alarmmanager1 = (AlarmManager) SettingsActivity.this.getSystemService(SettingsActivity.this.ALARM_SERVICE);
					Intent startServiceIntent2 = new Intent(SettingsActivity.this, PercentService.class);
					PendingIntent startServicePendingIntent2 = PendingIntent.getService(SettingsActivity.this,0,startServiceIntent2, 0);
					alarmmanager1.cancel(startServicePendingIntent2);
				}
				
				return true;
			}
		});
		
		Preference version = findPreference("version");
		PackageInfo pinfo;
		try {
			pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version.setSummary("Version " + pinfo.versionName);
		} catch (NameNotFoundException e) {}
		version.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				final String appPackageName = getPackageName();
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
				return false;
			}
		});
		
		Preference developers = findPreference("developers");
		developers.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(SettingsActivity.this, WebActivity.class);
				i.putExtra("Webside", "http://mrgames13.jimdo.com/");
				i.putExtra("Title", "Unsere Homepage");
				startActivity(i);
				return false;
			}
		});
		
		Preference syncfrequency = findPreference("SyncFreq");
		syncfrequency.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				//Alarmmanager für Hintergrundprozess aufsetzen
				@SuppressWarnings("static-access")
				AlarmManager alarmmanager = (AlarmManager) SettingsActivity.this.getSystemService(SettingsActivity.this.ALARM_SERVICE);
				
				Intent startServiceIntent = new Intent(SettingsActivity.this, SyncronisationService.class);
				PendingIntent startServicePendingIntent = PendingIntent.getService(SettingsActivity.this,0,startServiceIntent,0);
				
				if (alarmmanager!= null) {
				    alarmmanager.cancel(startServicePendingIntent);
				}
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis() + 10);
				
				alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Integer.parseInt(newValue.toString()), startServicePendingIntent);

				return true;
			}
		});

		Preference applayout = findPreference("Layout");
        applayout.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Daten von den SharedPreferences abrufen
                String layout = newValue.toString();
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
                return true;
            }
        });

		Preference apptheme = findPreference("AppTheme");
		apptheme.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, final Object newValue) {
				//Dialog anzeigen
				if(!su.getString("AppTheme", "Helles Schema (Standard)").equals(newValue)) {
					android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this);
					builder.setCancelable(true);
					builder.setTitle(res.getString(R.string.restart));
					builder.setMessage(res.getString(R.string.changings_will_be_visible_after_restart));
					builder.setPositiveButton(res.getString(R.string.pref_reboot_title), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						}
					});
					builder.setNegativeButton(res.getString(R.string.pref_later), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if(newValue.toString().equals("0")) {
								MainActivity.AppTheme = 0;
								ThemeUtils.changeToTheme(SettingsActivity.this, 0);
							} else if(newValue.toString().equals("1")) {
								MainActivity.AppTheme = 1;
								ThemeUtils.changeToTheme(SettingsActivity.this, 1);
							} else if(newValue.toString().equals("2")) {
								MainActivity.AppTheme = 2;
								ThemeUtils.changeToTheme(SettingsActivity.this, 2);
							}
						}
					});
					builder.create().show();
				}
				return true;
			}
		});
		
		Preference serverinfo = findPreference("serverinfo");
		serverinfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(serverMessagingUtils.isInternetAvailable()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getServerInfo(true, true);
                        }
                    }).start();
                } else {
                    Toast.makeText(SettingsActivity.this, res.getString(R.string.internet_is_not_available), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //Als Summary, die wichtigen Daten eintragen
        if(serverMessagingUtils.isInternetAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //Info vom Server holen
                        final String serverinfo = getServerInfo(false, false);
                        //Result auseinandernehmen
                        int index1 = result.indexOf(",");
                        int index2 = result.indexOf(",", index1 +1);
                        int index3 = result.indexOf(",", index2 +1);
                        int index4 = result.indexOf(",", index3 +1);
						int index5 = result.indexOf(",", index4 +1);
                        String client_name = result.substring(0, index1);
                        String server_state = result.substring(index1 +1, index2);
                        String app_version = result.substring(index2 +1, index3);
                        String adminconsole_version = result.substring(index3 +1, index4);
						String supporturl = result.substring(index4 +1, index5);
                        String owners = result.substring(index5 +1);
                        //ServerState überschreiben
                        if(server_state.equals("1")) server_state = res.getString(R.string.serverstate_1);
                        if(server_state.equals("2")) server_state = res.getString(R.string.serverstate_2);
                        if(server_state.equals("3")) server_state = res.getString(R.string.serverstate_3);
                        if(server_state.equals("4")) server_state = res.getString(R.string.serverstate_4);
                        final String summary = server_state;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Preference serverinfo = findPreference("serverinfo");
                                serverinfo.setSummary(summary);
                            }
                        });
                    } catch(Exception e) {}
                }
            }).start();
        } else {
            serverinfo.setSummary(res.getString(R.string.internet_is_not_available));
        }

		final Preference opensouce = findPreference("opensoucelicenses");
        opensouce.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SpannableString s = new SpannableString(res.getString(R.string.openSourceLicense));
                Linkify.addLinks(s, Linkify.ALL);
                AlertDialog d = new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle(opensouce.getTitle())
                        .setMessage(Html.fromHtml(s.toString()))
                        .setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            }
        });

		final Preference custom_startpage = findPreference("CustomStartPage");
        String startpage = su.getString("CustomStartPage", "0");
		if(startpage.equals("0")) startpage = "Mein Profil";
        custom_startpage.setSummary(startpage.replace(" (Standard)", ""));
        custom_startpage.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                custom_startpage.setSummary(newValue.toString().replace(" (Standard)", ""));
                return true;
            }
        });

		final Preference deletestorage = findPreference("DeleteStorage");
        deletestorage.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder d;
                if(MainActivity.AppTheme == 0) {
                    d = new AlertDialog.Builder(SettingsActivity.this, R.style.FirstTheme_Dialog);
                } else {
                    d = new AlertDialog.Builder(SettingsActivity.this, R.style.SecondTheme_Dialog);
                }
                d.setTitle(res.getString(R.string.delete_storage_t))
                        .setMessage(res.getString(R.string.delete_storage_m))
                        .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(res.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                su.clear();
                                dialog.dismiss();
                                deletestorage.setSummary(res.getString(R.string.delete_storage_s));
                                Toast.makeText(SettingsActivity.this, res.getString(R.string.delete_storage_s), Toast.LENGTH_LONG).show();
								startActivity(new Intent(SettingsActivity.this, LogoActivity.class));
                            }
                        })
                        .create();
                d.create().show();
                return true;
            }
        });

		if(current_account.getRights() == Account.RIGHTS_TEAM) {
			SwitchPreference selectedserver = (SwitchPreference) findPreference("selectedserver");
			selectedserver.setSummaryOff(res.getString(R.string.normal_server));
			selectedserver.setSummaryOn(res.getString(R.string.test_server));
			selectedserver.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					AlertDialog.Builder d;
					if(MainActivity.AppTheme == 0) {
						d = new AlertDialog.Builder(SettingsActivity.this, R.style.FirstTheme_Dialog);
					} else {
						d = new AlertDialog.Builder(SettingsActivity.this, R.style.SecondTheme_Dialog);
					}
					d.setTitle(res.getString(R.string.restart))
							.setMessage(res.getString(R.string.changings_will_be_visible_after_restart))
							.setNegativeButton(res.getString(R.string.pref_later), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
							.setPositiveButton(res.getString(R.string.pref_reboot_title), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
									i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(i);
								}
							})
							.create();
					d.create().show();
					return true;
				}
			});
		}
	}
	
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	private static OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager
				.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);

			bindPreferenceSummaryToValue(findPreference("example_text"));
			bindPreferenceSummaryToValue(findPreference("example_list"));
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class AccountPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_account);
			
			bindPreferenceSummaryToValue(findPreference("example_text"));
			bindPreferenceSummaryToValue(findPreference("example_list"));
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NotificationPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_notifications);
			
			bindPreferenceSummaryToValue(findPreference("example_text"));
			bindPreferenceSummaryToValue(findPreference("example_list"));
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class InfosPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if(current_account.getRights() == Account.RIGHTS_TEAM) {
				addPreferencesFromResource(R.xml.pref_infos_team);
			} else {
				addPreferencesFromResource(R.xml.pref_infos);
			}
			
			bindPreferenceSummaryToValue(findPreference("example_text"));
			bindPreferenceSummaryToValue(findPreference("example_list"));
		}
	}

    private String getServerInfo(final boolean showProgressDialog, final boolean showResultDialog) {
		try {
			if(showProgressDialog) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//Dialog für den Fortschritt anzeigen
						if(MainActivity.AppTheme == 0) {
							pd_Progress = new ProgressDialog(SettingsActivity.this, R.style.FirstTheme_Dialog_Progress);
						} else {
							pd_Progress = new ProgressDialog(SettingsActivity.this, R.style.SecondTheme_Dialog_Progress);
						}
						pd_Progress.setMessage(res.getString(R.string.download_serverinfo));
						pd_Progress.setIndeterminate(true);
						pd_Progress.setTitle(res.getString(R.string.serverinfo));
						pd_Progress.show();
					}
				});
			}
			//Abfrage an den Server senden
			result = serverMessagingUtils.sendRequest(null, "name="+URLEncoder.encode(current_account.getUsername(), "UTF-8")+"&command=getserverinfo");
			//Result auseinandernehmen
			int index1 = result.indexOf(",");
			int index2 = result.indexOf(",", index1 +1);
			int index3 = result.indexOf(",", index2 +1);
			int index4 = result.indexOf(",", index3 +1);
			int index5 = result.indexOf(",", index4 +1);
			final String client_name = result.substring(0, index1);
			final String server_state = result.substring(index1 +1, index2);
			final String app_version = result.substring(index2 +1, index3);
			final String adminconsole_version = result.substring(index3 +1, index4);
			final String supporturl = result.substring(index4 +1, index5);
			final String owners = result.substring(index5 +1);
			//Dialog für das Ergebnis anzeigen
			if(showResultDialog) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
                        if(showProgressDialog) pd_Progress.dismiss();
                        //Serverinfo überschreiben
                        String server_state_display = null;
                        if(server_state.equals("1")) server_state_display = res.getString(R.string.server_state) + ": " + res.getString(R.string.serverstate_1_short);
                        if(server_state.equals("2")) server_state_display = res.getString(R.string.server_state) + ": " + res.getString(R.string.serverstate_2_short);
                        if(server_state.equals("3")) server_state_display = res.getString(R.string.server_state) + ": " + res.getString(R.string.serverstate_3_short);
                        if(server_state.equals("4")) server_state_display = res.getString(R.string.server_state) + ": " + res.getString(R.string.serverstate_4_short);
						//String einzeln zusammensetzen
                        String client_name_display = res.getString(R.string.client_name) + ": " + client_name;
						String app_version_display = res.getString(R.string.app_version) + ": " + app_version;
						String adminconsole_version_display = res.getString(R.string.adminconsole_version) + ": " + adminconsole_version;
                        String support_display = res.getString(R.string.support_url) + ": " + supporturl;
						String owners_display = res.getString(R.string.owners) + ": " + owners;
                        //String zusammensetzen und Dialog anzeigen
                        final SpannableString info = new SpannableString(client_name_display + "\n" + server_state_display + "\n" + app_version_display + "\n" + adminconsole_version_display + "\n" + support_display + "\n" + owners_display);
                        Linkify.addLinks(info, Linkify.WEB_URLS);
                        android.support.v7.app.AlertDialog.Builder d_Result;
						if(MainActivity.AppTheme == 0) {
							d_Result = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this, R.style.FirstTheme_Dialog);
						} else {
							d_Result = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this, R.style.SecondTheme_Dialog);
						}
						d_Result.setTitle(res.getString(R.string.serverinfo))
								.setMessage(info)
								.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								})
								.create();
						AlertDialog d = d_Result.show();
                        ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
					}
				});
			}
		} catch (Exception e) {}
		return result;
    }
}