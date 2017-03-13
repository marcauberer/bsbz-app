package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgames13.jimdo.bsbz_app.App.EditNewActivity;
import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.App.NewEditElementActivity;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Account;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Classtest;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Event;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.Homework;
import com.mrgames13.jimdo.bsbz_app.CommonObjects.New;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.SimpleAnimationListener;

import java.net.URLEncoder;

public class ElementViewAdapter extends RecyclerView.Adapter<ElementViewAdapter.ViewHolderClass> {
    //Konstanten
    public static final int MODE_CLASSTEST = 1;
    public static final int MODE_HOMEWORK = 2;
    public static final int MODE_EVENT = 3;
    public static final int MODE_NEW = 4;
    public static final int MODE_NEW_INVISIBLE = 5;
    public static final int MODE_CLASSTEST_HOMEWORK_EVENTS = 6;

    //Variablen als Objekte
    private Context context;
    private Resources res;
    private Handler h;
    private ProgressDialog pd;

    //Variablen
    private String result = "";
    private int mode;

    public ElementViewAdapter(Context context, int mode) {
        this.context = context;
        this.mode = mode;
        this.res = MainActivity.res;
        this.h = new Handler();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        //Variablen als Objekte
        private TextView item_icon;
        private TextView item_subject;
        private TextView item_description;
        private TextView item_receiver;
        private TextView item_writer;
        private TextView item_date;
        private RelativeLayout item_button_container;
        private RelativeLayout item_area_description;
        private ImageView item_dropdown_arrow;
        private FloatingActionButton item_edit;
        private FloatingActionButton item_delete;
        private boolean item_expanded = false;
        private int item_mode;

        public ViewHolderClass(View itemView) {
            super(itemView);
            item_icon = (TextView) itemView.findViewById(R.id.item_icon);
            item_dropdown_arrow = (ImageView) itemView.findViewById(R.id.item_dropdown_arrow);
            item_subject = (TextView) itemView.findViewById(R.id.item_subject);
            item_description = (TextView) itemView.findViewById(R.id.item_description);
            item_date = (TextView) itemView.findViewById(R.id.item_date);
            item_receiver = (TextView) itemView.findViewById(R.id.item_receiver);
            item_writer = (TextView) itemView.findViewById(R.id.item_writer);
            item_edit = (FloatingActionButton) itemView.findViewById(R.id.item_edit);
            item_delete = (FloatingActionButton) itemView.findViewById(R.id.item_delete);
            item_area_description = (RelativeLayout) itemView.findViewById(R.id.item_area_description);
            item_area_description.setVisibility(View.GONE);
            item_button_container = (RelativeLayout) itemView.findViewById(R.id.item_button_container);
            item_button_container.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_element, null);
        return new ViewHolderClass(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderClass holder, final int pos) {
        //ItemMode festlegen
        if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
            if(MainActivity.all.get(pos) instanceof Classtest) holder.item_mode = MODE_CLASSTEST;
            if(MainActivity.all.get(pos) instanceof Homework) holder.item_mode = MODE_HOMEWORK;
            if(MainActivity.all.get(pos) instanceof Event) holder.item_mode = MODE_EVENT;
        } else if(mode == MODE_CLASSTEST) {
            holder.item_mode = MODE_CLASSTEST;
        } else if(mode == MODE_HOMEWORK) {
            holder.item_mode = MODE_HOMEWORK;
        } else if(mode == MODE_EVENT) {
            holder.item_mode = MODE_EVENT;
        } else if(mode == MODE_NEW || mode == MODE_NEW_INVISIBLE) {
            holder.item_mode = MODE_NEW;
        }
        //Icon festlegen
        if(holder.item_mode == MODE_CLASSTEST) {
            holder.item_icon.setText(res.getString(R.string.classtest_short_short));
            holder.item_icon.setBackgroundResource(R.drawable.icon_k);
        } else if(holder.item_mode == MODE_HOMEWORK) {
            holder.item_icon.setText(res.getString(R.string.homework_short_short));
            holder.item_icon.setBackgroundResource(R.drawable.icon_h);
        } else if(holder.item_mode == MODE_EVENT) {
            holder.item_icon.setText(res.getString(R.string.event_short_short));
            holder.item_icon.setBackgroundResource(R.drawable.icon_t);
        } else if(holder.item_mode == MODE_NEW) {
            holder.item_icon.setText(res.getString(R.string.new_short_short));
            holder.item_icon.setBackgroundResource(R.drawable.icon_n);
        }
        //Daten befüllen
        if(holder.item_mode == MODE_CLASSTEST) {
            Classtest c;
            if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                c = (Classtest) MainActivity.all.get(pos);
            } else {
                c = MainActivity.classtests.get(pos);
            }
            holder.item_subject.setText(c.getSubject());
            holder.item_description.setText(c.getDescription());
            holder.item_date.setText(c.getDate());
            holder.item_receiver.setText(c.getReceiver());
            holder.item_writer.setText(c.getWriter());
        } else if(holder.item_mode == MODE_HOMEWORK) {
            Homework h;
            if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                h = (Homework) MainActivity.all.get(pos);
            } else {
                h = MainActivity.homeworks.get(pos);
            }
            holder.item_subject.setText(h.getSubject());
            holder.item_description.setText(h.getDescription());
            holder.item_date.setText(h.getDate());
            holder.item_receiver.setText(h.getReceiver());
            holder.item_writer.setText(h.getWriter());
        } else if(holder.item_mode == MODE_EVENT) {
            Event e;
            if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                e = (Event) MainActivity.all.get(pos);
            } else {
                e = MainActivity.events.get(pos);
            }
            holder.item_subject.setText(e.getSubject());
            holder.item_description.setText(e.getDescription());
            holder.item_date.setText(e.getDate());
            holder.item_receiver.setText(e.getReceiver());
            holder.item_writer.setText(e.getWriter());
        } else if(holder.item_mode == MODE_NEW) {
            New n = MainActivity.news.get(pos);
            holder.item_subject.setText(n.getSubject());
            holder.item_description.setText(n.getDescription());
            holder.item_date.setText(n.getActivationDate());
            holder.item_receiver.setText(n.getReceiver());
            holder.item_writer.setText(n.getWriter());
        }

        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == MODE_NEW || mode == MODE_NEW_INVISIBLE) {
                    New n = MainActivity.news.get(pos);
                    //Activity starten und Daten übergeben
                    Intent i = new Intent(context, EditNewActivity.class);
                    i.putExtra("Subject", n.getSubject());
                    i.putExtra("Description", n.getDescription());
                    i.putExtra("Writer", n.getWriter());
                    i.putExtra("Activation Date", n.getActivationDate());
                    i.putExtra("Expiration Date", n.getExpirationDate());
                    i.putExtra("Receiver", n.getReceiver());
                    context.startActivity(i);
                } else if(holder.item_mode == MODE_CLASSTEST || holder.item_mode == MODE_HOMEWORK || holder.item_mode == MODE_EVENT) {
                    String subject = "";
                    String description = "";
                    String writer = "";
                    String receiver = "";
                    String date = "";
                    if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                        if(holder.item_mode == MODE_CLASSTEST) {
                            Classtest c = (Classtest) MainActivity.all.get(pos);
                            subject = c.getSubject();
                            description = c.getDescription();
                            writer = c.getWriter();
                            receiver = c.getReceiver();
                            date = c.getDate();
                        } else if(holder.item_mode == MODE_HOMEWORK) {
                            Homework h = (Homework) MainActivity.all.get(pos);
                            subject = h.getSubject();
                            description = h.getDescription();
                            writer = h.getWriter();
                            receiver = h.getReceiver();
                            date = h.getDate();
                        } else if(holder.item_mode == MODE_EVENT) {
                            Event e = (Event) MainActivity.all.get(pos);
                            subject = e.getSubject();
                            description = e.getDescription();
                            writer = e.getWriter();
                            receiver = e.getReceiver();
                            date = e.getDate();
                        }
                    } else {
                        if(holder.item_mode == MODE_CLASSTEST) {
                            Classtest c = MainActivity.classtests.get(pos);
                            subject = c.getSubject();
                            description = c.getDescription();
                            writer = c.getWriter();
                            receiver = c.getReceiver();
                            date = c.getDate();
                        } else if(holder.item_mode == MODE_HOMEWORK) {
                            Homework h = MainActivity.homeworks.get(pos);
                            subject = h.getSubject();
                            description = h.getDescription();
                            writer = h.getWriter();
                            receiver = h.getReceiver();
                            date = h.getDate();
                        } else if(holder.item_mode == MODE_EVENT) {
                            Event e = MainActivity.events.get(pos);
                            subject = e.getSubject();
                            description = e.getDescription();
                            writer = e.getWriter();
                            receiver = e.getReceiver();
                            date = e.getDate();
                        }
                    }
                    //Activity starten und Daten übergeben
                    Intent i = new Intent(context, NewEditElementActivity.class);
                    i.putExtra("old_title", subject);
                    i.putExtra("old_date", date);
                    i.putExtra("old_description", description);
                    i.putExtra("old_writer", writer);
                    i.putExtra("old_receiver", receiver);
                    if(holder.item_mode == MODE_CLASSTEST) i.putExtra("mode", NewEditElementActivity.MODE_EDIT_CLASSTEST);
                    if(holder.item_mode == MODE_HOMEWORK) i.putExtra("mode", NewEditElementActivity.MODE_EDIT_HOMEWORK);
                    if(holder.item_mode == MODE_EVENT) i.putExtra("mode", NewEditElementActivity.MODE_EDIT_EVENT);
                    context.startActivity(i);
                }
            }
        });

        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog d = new AlertDialog.Builder(context)
                        .setTitle(res.getString(R.string.delete_element))
                        .setMessage(res.getString(R.string.really_delete_element))
                        .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pd = new ProgressDialog(context);
                                                pd.setIndeterminate(true);
                                                pd.setTitle(res.getString(R.string.please_wait_));
                                                pd.setMessage(res.getString(R.string.element_is_deleting_));
                                                pd.show();
                                            }
                                        });
                                        try{
                                            String username = MainActivity.current_account.getUsername();
                                            if(holder.item_mode == MODE_CLASSTEST) {
                                                Classtest c;
                                                if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                                                    c = (Classtest) MainActivity.all.get(pos);
                                                } else {
                                                    c = MainActivity.classtests.get(pos);
                                                }
                                                result = MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=deleteclasstest&title="+URLEncoder.encode(c.getSubject().trim(), "UTF-8"));
                                            } else if(holder.item_mode == MODE_HOMEWORK) {
                                                Homework h;
                                                if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                                                    h = (Homework) MainActivity.all.get(pos);
                                                } else {
                                                    h = MainActivity.homeworks.get(pos);
                                                }
                                                result = MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=deletehomework&title="+URLEncoder.encode(h.getSubject().trim(), "UTF-8"));
                                            } else if(holder.item_mode == MODE_EVENT) {
                                                Event e;
                                                if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) {
                                                    e = (Event) MainActivity.all.get(pos);
                                                } else {
                                                    e = MainActivity.events.get(pos);
                                                }
                                                result = MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=deleteevent&title="+URLEncoder.encode(e.getSubject().trim(), "UTF-8"));
                                            } else if(holder.item_mode == MODE_NEW) {
                                                result = MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=deletenew&subject="+URLEncoder.encode(MainActivity.news.get(pos).getSubject().trim(), "UTF-8"));
                                            }
                                            if(result.equals("Action Successful")) {
                                                result = res.getString(R.string.action_successful);
                                                context.startService(new Intent(context, SyncronisationService.class));
                                            } else {
                                                result = res.getString(R.string.error_try_again);
                                            }
                                            h.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pd.dismiss();
                                                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch(Exception e) {}
                                    }
                                }).start();
                            }
                        })
                        .create();
                d.show();
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.item_edit.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.item_edit.setLayoutParams(p);
            p = (ViewGroup.MarginLayoutParams) holder.item_delete.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.item_delete.setLayoutParams(p);
        }

        //OnClickListener setzen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.item_expanded) {
                    //Expand item
                    holder.itemView.findViewById(R.id.item_area_description).setVisibility(View.VISIBLE);
                    if(mode == MODE_NEW && ((MainActivity.current_account.getRights() == Account.RIGHTS_TEACHER || MainActivity.current_account.getRights() == Account.RIGHTS_ADMIN || MainActivity.current_account.getRights() == Account.RIGHTS_TEAM)) || (MainActivity.current_account.getRights() == Account.RIGHTS_TEACHER || MainActivity.current_account.getRights() == Account.RIGHTS_ADMIN || MainActivity.current_account.getRights() == Account.RIGHTS_TEAM)) {
                        holder.itemView.findViewById(R.id.item_button_container).setVisibility(View.VISIBLE);
                        holder.item_description.setMinLines(4);
                    }
                    //Rotations-Animation für DropDown-Arrow starten
                    holder.item_dropdown_arrow.setImageResource(R.drawable.ic_arrow_drop_up);
                    Animation rot = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_element_rotation);
                    holder.item_dropdown_arrow.startAnimation(rot);
                    //Expand-Animation für Item starten
                    Animation exp = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_element_expand);
                    holder.item_area_description.startAnimation(exp);
                    holder.item_button_container.startAnimation(exp);
                } else {
                    //Rotations-Animation für DropDown-Arrow starten
                    holder.item_dropdown_arrow.setImageResource(R.drawable.ic_arrow_drop_down);
                    Animation rot = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_element_rotation);
                    holder.item_dropdown_arrow.startAnimation(rot);
                    //Expand-Animation für Item starten
                    Animation col = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_element_collapse);
                    col.setAnimationListener(new SimpleAnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //Collapse item
                            holder.itemView.findViewById(R.id.item_area_description).setVisibility(View.GONE);
                            holder.itemView.findViewById(R.id.item_button_container).setVisibility(View.GONE);
                        }
                    });
                    holder.item_area_description.startAnimation(col);
                    holder.item_button_container.startAnimation(col);
                }
                holder.item_expanded = !holder.item_expanded;
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(mode == MODE_CLASSTEST) size =  MainActivity.classtests.size();
        if(mode == MODE_HOMEWORK) size = MainActivity.homeworks.size();
        if(mode == MODE_EVENT) size = MainActivity.events.size();
        if(mode == MODE_CLASSTEST_HOMEWORK_EVENTS) size = MainActivity.classtests.size() + MainActivity.homeworks.size() + MainActivity.events.size();
        if(mode == MODE_NEW || mode == MODE_NEW_INVISIBLE) size = MainActivity.news.size();
        return size;
    }
}