package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.ComponentClasses.New;
import com.mrgames13.jimdo.bsbz_app.R;
import com.mrgames13.jimdo.bsbz_app.Services.SyncronisationService;
import com.mrgames13.jimdo.bsbz_app.Tools.SimpleAnimationListener;

import java.net.URLEncoder;

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.ViewHolderClass> {
    //Konstanten

    //Variablen als Objekte
    private Context context;

    //Variablen
    private String result;

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
        //Daten befüllen
        New n = MainActivity.news.get(pos);
        holder.item_subject.setText(n.getSubject());
        holder.item_description.setText(n.getDescription());
        holder.item_date.setText(n.getActivationDate());
        holder.item_receiver.setText(n.getReceiver());
        holder.item_writer.setText(n.getWriter());

        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog d = new AlertDialog.Builder(context)
                        .setTitle(MainActivity.res.getString(R.string.delete_new))
                        .setMessage(MainActivity.res.getString(R.string.really_delete_new))
                        .setNegativeButton(MainActivity.res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(MainActivity.res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            String username = MainActivity.prefs.getString("Name", MainActivity.res.getString(R.string.guest));
                                            result = MainActivity.serverMessagingUtils.sendRequest(null, "name="+ URLEncoder.encode(username, "UTF-8")+"&command=deletenew&subject="+URLEncoder.encode(MainActivity.news.get(pos).getSubject().trim(), "UTF-8"));
                                            if(result.equals("Action Successful")) {
                                                result = MainActivity.res.getString(R.string.new_successfully_created);
                                                context.startService(new Intent(context, SyncronisationService.class));
                                            } else {
                                                result = MainActivity.res.getString(R.string.error_try_again);
                                            }
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
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

        //OnClickListener setzen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.item_expanded) {
                    //Expand item
                    holder.itemView.findViewById(R.id.item_area_description).setVisibility(View.VISIBLE);
                    String rights = MainActivity.su.getString("Rights", MainActivity.res.getString(R.string.guest));
                    if(rights.equals("classspeaker") || rights.equals("teacher") || rights.equals("administrator") || rights.equals("team")) {
                        holder.itemView.findViewById(R.id.item_button_container).setVisibility(View.VISIBLE);
                        holder.item_description.setMinLines(4);
                    }
                    //Rotations-Animation für DropDown-Arrow starten
                    holder.item_dropdown_arrow.setImageResource(R.drawable.ic_arrow_drop_up_36pt);
                    Animation rot = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_element_rotation);
                    holder.item_dropdown_arrow.startAnimation(rot);
                    //Expand-Animation für Item starten
                    Animation exp = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_element_expand);
                    holder.item_area_description.startAnimation(exp);
                    holder.item_button_container.startAnimation(exp);
                } else {
                    //Rotations-Animation für DropDown-Arrow starten
                    holder.item_dropdown_arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_36dp);
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
        return MainActivity.news.size();
    }
}