package com.mrgames13.jimdo.bsbz_app.RecyclerViewAdapters;

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
import android.widget.ProgressBar;

import com.mrgames13.jimdo.bsbz_app.App.ImageFolderActivity;
import com.mrgames13.jimdo.bsbz_app.App.ImageFullscreenActivity;
import com.mrgames13.jimdo.bsbz_app.App.MainActivity;
import com.mrgames13.jimdo.bsbz_app.R;

public class GalleryViewAdapter_Files extends RecyclerView.Adapter<GalleryViewAdapter_Files.ViewHolderClass> {
    //Konstanten

    //Variablen als Objekte
    private Context context;
    private Handler h = new Handler();
    private Bitmap image;

    //Variablen

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        //Variablen als Objekte
        private ImageView item_icon;
        private ProgressBar item_progress;

        public ViewHolderClass(View itemView) {
            super(itemView);
            item_icon = (ImageView) itemView.findViewById(R.id.gallery_file_icon);
            item_progress = (ProgressBar) itemView.findViewById(R.id.gallery_file_progress);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view_item_file, null);

        return new ViewHolderClass(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderClass holder, final int pos) {
        //Bild setzen
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String imageName = ImageFolderActivity.filenames.get(pos);
                    if(!imageName.equals("")) {
                        image = MainActivity.serverMessagingUtils.downloadImage(ImageFolderActivity.folderName, imageName.substring(0, 3) + "_preview.jpg");
                    } else {
                        image = BitmapFactory.decodeResource(MainActivity.res, R.drawable.ic_broken_image_48pt_2x);
                    }
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.item_icon.setImageBitmap(image);
                            holder.item_progress.setVisibility(View.GONE);
                        }
                    });
                } catch(Exception e) {}
            }
        }).start();
        //OnClickListener für die Items setzen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = ImageFolderActivity.folderName;
                //Bildnummer generieren
                String number = String.valueOf(pos +1);
                if(number.length() == 1) number = "00" + number;
                if(number.length() == 2) number = "0" + number;
                //FullScreenActivity starten
                Intent i = new Intent(context, ImageFullscreenActivity.class);
                i.putExtra("foldername", folderName);
                i.putExtra("imagename", number);
                i.putExtra("index", pos);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ImageFolderActivity.filenames.size();
    }
}