package com.mrgames13.jimdo.bsbz_app.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrgames13.jimdo.bsbz_app.App.ImageFolderActivity;
import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.R;

public class GalleryViewAdapter_Folders extends RecyclerView.Adapter<GalleryViewAdapter_Folders.ViewHolderClass> {
    //Konstanten

    //Variablen als Objekte
    private Context context;
    private Handler h = new Handler();
    private Bitmap firstImage;

    //Variablen

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        //Variablen als Objekte
        private TextView item_name;
        private ImageView item_icon;

        public ViewHolderClass(View itemView) {
            super(itemView);
            item_icon = (ImageView) itemView.findViewById(R.id.gallery_folder_icon);
            item_name = (TextView) itemView.findViewById(R.id.gallery_folder_name);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view_item_folder, null);

        return new ViewHolderClass(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderClass holder, final int pos) {
        //Ordner-Titel setzen
        holder.item_name.setText(MainActivity.gallery_view_foldernames.get(pos).replace(".", ""));
        //Bild setzen, wenn vorhanden
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = MainActivity.gallery_view_filenames.get(pos).indexOf(",");
                final String firstImageName;
                if(index != -1) {
                    firstImageName = MainActivity.gallery_view_filenames.get(pos).substring(0, index);
                } else {
                    firstImageName = MainActivity.gallery_view_filenames.get(pos).substring(0);
                }
                if(!firstImageName.equals("")) {
                    firstImage = MainActivity.serverMessagingUtils.downloadImage(MainActivity.gallery_view_foldernames.get(pos), firstImageName);
                } else {
                    firstImage = BitmapFactory.decodeResource(MainActivity.res, R.drawable.ic_image_48pt_2x);
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.item_icon.setImageBitmap(firstImage);
                    }
                });
            }
        }).start();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageFolderActivity.class);
                i.putExtra("foldername", MainActivity.gallery_view_foldernames.get(pos));
                i.putExtra("filenames", MainActivity.gallery_view_filenames.get(pos));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return MainActivity.gallery_view_foldernames.size();
    }
}