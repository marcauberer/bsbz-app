package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.ComponentClasses.New;
import com.mrgames13.jimdo.bsbz_app.R;

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.ViewHolderClass> {
    //Konstanten

    //Variablen als Objekte
    private Context context;

    //Variablen

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        //Variablen als Objekte
        private TextView item_icon;
        private TextView item_subject;
        private TextView item_description;
        private TextView item_receiver;
        private TextView item_writer;
        private TextView item_date;
        private ImageView item_dropdown_arrow;

        public ViewHolderClass(View itemView) {
            super(itemView);
            item_icon = (TextView) itemView.findViewById(R.id.item_icon);
            item_dropdown_arrow = (ImageView) itemView.findViewById(R.id.item_dropdown_arrow);
            item_subject = (TextView) itemView.findViewById(R.id.item_subject);
            item_description = (TextView) itemView.findViewById(R.id.item_description);
            item_date = (TextView) itemView.findViewById(R.id.item_date);
            item_receiver = (TextView) itemView.findViewById(R.id.item_receiver);
            item_writer = (TextView) itemView.findViewById(R.id.item_writer);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_element_small, null);
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

        //OnClickListener setzen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.news.size();
    }
}