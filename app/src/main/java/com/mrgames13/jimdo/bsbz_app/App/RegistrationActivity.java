package com.mrgames13.jimdo.bsbz_app.App;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Tools.ServerMessagingUtils;

import java.net.URLEncoder;

@SuppressWarnings("deprecation")
public class RegistrationActivity extends AppCompatActivity {

    //Konstanten


    //Variablen als Objekte
    private Toolbar toolbar;
    private ConnectivityManager cm;
    private ServerMessagingUtils serverMessagingUtils;
    private Resources res;
    private SharedPreferences prefs;
    private EditText username;
    private Button klasse;
    private EditText password;
    private EditText repassword;
    private CheckBox auto_login;
    private CheckBox keep_logged_in;

    //Veriablen
	public static String id = "";
    private String result = "";
    private boolean pressedOnce;
    private String rights = "student";

	@Override
	public void onStart() {
		super.onStart();
		
		//Daten von den SharedPreferences abrufen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
		String layout = prefs.getString("Layout", res.getString(R.string.bsbz_layout_orange));
		String color = "#ea690c";
		if(layout.equals("0")) {
			color = "#ea690c";
		} else if(layout.equals("1")) {
			color = "#000000";
		} else if(layout.equals("2")) {
			color = "#3ded25";
		} else if(layout.equals("3")) {
			color = "#ff0000";
		} else if(layout.equals("4")) {
			color = "#0000ff";
		} else if(layout.equals("5")) {
			color = "#00007f";
		}
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(res.getString(R.string.title_activity_registration));
		
		if(Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.setStatusBarColor(MainActivity.darkenColor(Color.parseColor(color)));
		}
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
		
		setContentView(R.layout.activity_registration);

        //Toolbar aufsetzen
        toolbar = (Toolbar) findViewById(R.id.toolbar_registration);
        setSupportActionBar(toolbar);

        //SharedPreferences initialisieren
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl2);
		if(MainActivity.AppTheme == 1) {
			rl.setBackgroundColor(Color.BLACK);
		}

        //Resourcen initialisieren
        res = getResources();

		//Android ID ermitteln
		try{
			TelephonyManager tm = (TelephonyManager) RegistrationActivity.this.getSystemService(TELEPHONY_SERVICE);
			id = tm.getDeviceId();
		} catch(Exception e) {
            e.printStackTrace();
        }

		//Ids herausfinden
		//Button
		final Button Registrieren = (Button) findViewById(R.id.Registration_Registrieren);
		//Textfelder
		username = (EditText) findViewById(R.id.Registration_User_name);
		klasse = (Button) findViewById(R.id.Registration_klasse);
		//final EditText email = (EditText) findViewById(R.id.Registration_Email);
		password = (EditText) findViewById(R.id.Registration_Password);
		repassword = (EditText) findViewById(R.id.Registration_Remember_Password);
		
		klasse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert;
                if(MainActivity.AppTheme == 0) {
                    alert = new AlertDialog.Builder(RegistrationActivity.this, R.style.FirstTheme_Dialog);
                } else {
                    alert = new AlertDialog.Builder(RegistrationActivity.this, R.style.SecondTheme_Dialog);
                }
				
				LayoutInflater inflater = getLayoutInflater();
				View dialogView = inflater.inflate(R.layout.dialogview_class_chooser_registration, null);
				alert.setView(dialogView);
				
				final TextView schulart = (TextView) dialogView.findViewById(R.id.schulart);
				final TextView klassenstufe = (TextView) dialogView.findViewById(R.id.klassenstufe);
				final TextView klassenart = (TextView) dialogView.findViewById(R.id.klassenart);
				
				final TextView klasse1 = (TextView) dialogView.findViewById(R.id.klasse);
				
				final SeekBar s1 = (SeekBar) dialogView.findViewById(R.id.seekBar1);
				final SeekBar s2 = (SeekBar) dialogView.findViewById(R.id.seekBar2);
				final SeekBar s3 = (SeekBar) dialogView.findViewById(R.id.seekBar3);
                final SwitchCompat sw1 = (SwitchCompat) dialogView.findViewById(R.id.student);
				final SwitchCompat sw2 = (SwitchCompat) dialogView.findViewById(R.id.classspeaker);
                final SwitchCompat sw3 = (SwitchCompat) dialogView.findViewById(R.id.classteacher);
                final SwitchCompat sw4 = (SwitchCompat) dialogView.findViewById(R.id.teacher);
                final SwitchCompat sw5 = (SwitchCompat) dialogView.findViewById(R.id.parent);
                final ImageView warning_iv = (ImageView) findViewById(R.id.ic_warning) ;
                final TextView warning_tv = (TextView) findViewById(R.id.warning);
				
				s1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						if(fromUser) {
							if(progress == 0) schulart.setText("W");
							if(progress == 1) schulart.setText("R");
							if(progress == 2) schulart.setText("G");
							klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
						}
					}
				});
				s2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						if(fromUser) {
							if(progress == 0) klassenstufe.setText("5");
							if(progress == 1) klassenstufe.setText("6");
							if(progress == 2) klassenstufe.setText("7");
							if(progress == 3) klassenstufe.setText("8");
							if(progress == 4) klassenstufe.setText("9");
							if(progress == 5) klassenstufe.setText("10");
							if(progress == 6) klassenstufe.setText("11");
							if(progress == 7) klassenstufe.setText("12");
							klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
						}
					}
				});
				s3.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						if(fromUser) {
							if(progress == 0) klassenart.setText("a");
							if(progress == 1) klassenart.setText("b");
							klasse1.setText(schulart.getText().toString() + klassenstufe.getText().toString() + klassenart.getText().toString());
						}
					}
				});
				sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw2.setChecked(false);
                            sw3.setChecked(false);
                            sw4.setChecked(false);
                            sw5.setChecked(false);
                            //SeekBars einblenden
                            s1.setEnabled(true);
                            s2.setEnabled(true);
                            s3.setEnabled(true);
                            //Rights anpassen
                            rights = "student";
                        }
                    }
                });
                sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw3.setChecked(false);
                            sw4.setChecked(false);
                            sw5.setChecked(false);
                            //SeekBars einblenden
                            s1.setEnabled(true);
                            s2.setEnabled(true);
                            s3.setEnabled(true);
                            //Rights anpassen
                            rights = "wants_to_be_a_classspeaker";
                        }
                    }
                });
                sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(false);
                            sw4.setChecked(false);
                            sw5.setChecked(false);
                            //SeekBars einblenden
                            s1.setEnabled(true);
                            s2.setEnabled(true);
                            s3.setEnabled(true);
                            //Rights anpassen
                            rights = "wants_to_be_a_teacher";
                        }
                    }
                });
                sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(false);
                            sw3.setChecked(false);
                            sw5.setChecked(false);
                            //SeekBars einblenden
                            s1.setEnabled(false);
                            s2.setEnabled(false);
                            s3.setEnabled(false);
                            //Rights anpassen
                            rights = "wants_to_be_a_teacher";
                        }
                    }
                });
                sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            sw1.setChecked(false);
                            sw2.setChecked(false);
                            sw3.setChecked(false);
                            sw4.setChecked(false);
                            //SeekBars einblenden
                            s1.setEnabled(true);
                            s2.setEnabled(true);
                            s3.setEnabled(true);
                            //Rights anpassen
                            rights = "parent";
                        }
                    }
                });

				alert.setTitle(res.getString(R.string.please_coose_your_class_));
				
				alert.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						klasse.setText(klasse1.getText().toString());
                        if(sw2.isChecked() || sw3.isChecked() || sw4.isChecked()) {
                            warning_iv.setVisibility(View.VISIBLE);
                            warning_tv.setVisibility(View.VISIBLE);
                        } else {
                            warning_iv.setVisibility(View.GONE);
                            warning_tv.setVisibility(View.GONE);
                        }
                        if(sw4.isChecked()) klasse.setText(res.getString(R.string.no_class));
						dialog.cancel();
					}
				});
				alert.setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				alert.create().show();
			}
		});
		
		//Button-Ausgrau-Mechanismus aufsetzten
		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				Registrieren.setEnabled(s.length() > 0 && password.getText().length() > 0 && repassword.getText().length() > 0);
			}
		});
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				Registrieren.setEnabled(s.length() > 0 && username.getText().length() > 0 && repassword.getText().length() > 0);
			}
		});
		repassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				Registrieren.setEnabled(s.length() > 0 && username.getText().length() > 0 && password.getText().length() > 0);
			}
		});
		repassword.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//Inhalte von Textfeldern ermitteln
				String name_string = username.getText().toString();
				//Klasse auslesen
				String klasse_string = klasse.getText().toString();
				//Passwort auslesen
				String password_string = password.getText().toString();
				//Passwort wiederholung auslesen
				String repassword_string = repassword.getText().toString();
				
				//Wenn die Passwörter übereinstimmen
                if(password_string.equals(repassword_string)) {
                    Registrieren(name_string, klasse_string, password_string);
                    SharedPreferences.Editor e = prefs.edit();
                        e.putString("Name", name_string);
                        e.putString("Klasse", klasse_string);
                        e.putString("Password", password_string);
                    e.commit();
                } else {
                    Toast.makeText(RegistrationActivity.this, res.getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                }
				return true;
			}
		});
		
		Registrieren.setEnabled(false);
		
		//Komponenten abfragen
		Registrieren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				//Inhalte von Textfeldern ermitteln
				String name1 = username.getText().toString();
				//Klasse auslesen
				String klasse1 = klasse.getText().toString();
				//Passwort auslesen
				String password1 = password.getText().toString();
				//Leerzeichen durch '+' erstzen
				password1 = password1.replace(" ","+");
				//Passwort wiederholung auslesen
				String repassword1 = repassword.getText().toString();
				
				//Wenn die Passwörter übereinstimmen
				if(name1.contains(" ")) {
					if(klasse1.equalsIgnoreCase(res.getString(R.string.tap_to_coose_class))) {
						Toast.makeText(RegistrationActivity.this, res.getString(R.string.please_coose_your_class), Toast.LENGTH_SHORT).show();
					} else if(name1.startsWith(" ") || name1.endsWith(" ")) {
						Toast.makeText(RegistrationActivity.this, res.getString(R.string.username_spaces_front_and_back), Toast.LENGTH_SHORT).show();
					} else {
						if(password1.equals(repassword1)) {
							Registrieren(name1, klasse1, password1);
						} else {
							Toast.makeText(RegistrationActivity.this, res.getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(RegistrationActivity.this, res.getString(R.string.username_must_contain_min_1_space), Toast.LENGTH_SHORT).show();
				}
			}
		});

        keep_logged_in = (CheckBox) findViewById(R.id.angemeldet_bleiben);

		auto_login = (CheckBox) findViewById(R.id.automatisch_einloggen);
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                keep_logged_in.setEnabled(isChecked);
            }
        });

        //ServerMessagingUtils initialisieren
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        serverMessagingUtils = new ServerMessagingUtils(cm, RegistrationActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else if(id == android.R.id.home) {
            if(username.getText().toString().equals("") && klasse.getText().toString().equals(res.getString(R.string.tap_to_coose_class)) && password.getText().toString().equals("") && repassword.getText().toString().equals("")) {
                finish();
            } else {
                if (!pressedOnce) {
                    pressedOnce = true;
                    Toast.makeText(RegistrationActivity.this, R.string.press_again_to_exit_registration, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pressedOnce = false;
                        }
                    }, 2500);
                } else {
                    pressedOnce = false;
                    finish();
                }
            }
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(username.getText().toString().equals("") && klasse.getText().toString().equals(res.getString(R.string.tap_to_coose_class)) && password.getText().toString().equals("") && repassword.getText().toString().equals("")) {
                onBackPressed();
            } else {
                if (!pressedOnce) {
                    pressedOnce = true;
                    Toast.makeText(RegistrationActivity.this, R.string.press_again_to_exit_registration, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pressedOnce = false;
                        }
                    }, 2500);
                } else {
                    pressedOnce = false;
                    onBackPressed();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Registrieren(final String username, final String klasse, final String password) {
        if(id == null) id = "no id";
        new Thread(new Runnable() {
			@Override
			public void run() {
				try {
                    String klasse1 = klasse;
					if(klasse1.equals(res.getString(R.string.no_class))) klasse1 = "no_class";
					result = serverMessagingUtils.sendRequest(findViewById(R.id.container), "name="+URLEncoder.encode(username.trim(), "UTF-8")+"&command=newaccount&password="+URLEncoder.encode(password.trim(), "UTF-8")+"&class="+URLEncoder.encode(klasse1, "UTF-8")+"&rights="+URLEncoder.encode(rights, "UTF-8")+"&androidid="+URLEncoder.encode(id, "UTF-8"));
					//Result auswerten
					if(result.contains("Action Successful")) {
						result = res.getString(R.string.account_creation_successful);
					} else {
						result = res.getString(R.string.account_creation_failed);
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(RegistrationActivity.this, result, Toast.LENGTH_SHORT).show();
							if(result.equals(res.getString(R.string.account_creation_successful))) {
								if(auto_login.isChecked()) {
                                    SharedPreferences.Editor e = prefs.edit();
										e.putString("Name", username);
                                        e.putString("Password", password);
                                        e.putBoolean("Angemeldet bleiben", keep_logged_in.isChecked());
                                    e.commit();
									LogInActivity.autologin = username + "," + password;
								}
								finish();
							}
						}
					});
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}