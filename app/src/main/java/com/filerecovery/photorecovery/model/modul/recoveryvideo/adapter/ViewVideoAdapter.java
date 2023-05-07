package com.filerecovery.photorecovery.model.modul.recoveryvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo.R;
import com.filerecovery.photorecovery.model.SquareImageView;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.VideoModel;
import com.filerecovery.photorecovery.utilts.Utils;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;

public class ViewVideoAdapter extends RecyclerView.Adapter<ViewVideoAdapter.MyViewHolder> {
    Context context;
    ArrayList<VideoModel> listPhoto = new ArrayList<>();
    BitmapDrawable placeholder;

    public ViewVideoAdapter(Context context2, ArrayList<VideoModel> arrayList) {
        this.context = context2;
        this.listPhoto = arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView album_card;
        ImageView ivPhoto;
        TextView tvDate;
        TextView tvSize;

        public MyViewHolder(View view) {
            super(view);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.tvSize = (TextView) view.findViewById(R.id.tvSize);
            this.ivPhoto = (SquareImageView) view.findViewById(R.id.iv_image);
            this.album_card = (CardView) view.findViewById(R.id.album_card);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_photo, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final VideoModel videoModel = this.listPhoto.get(i);
        myViewHolder.tvDate.setText(DateFormat.getDateInstance().format(Long.valueOf(videoModel.getLastModified())));
        myViewHolder.tvSize.setText(Utils.formatSize(videoModel.getSizePhoto()));
        try {
            RequestManager with = Glide.with(this.context);
            ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) with.load("file://" + videoModel.getPathPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL)).priority(Priority.HIGH)).centerCrop()).error((int) R.drawable.ic_error)).placeholder((Drawable) this.placeholder)).into(myViewHolder.ivPhoto);
        } catch (Exception e) {
            Context context2 = this.context;
            Toast.makeText(context2, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        myViewHolder.album_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ViewVideoAdapter.this.openFile(videoModel.getPathPhoto());
            }
        });
    }

    public void openFile(String str) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 24) {
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setDataAndType(Uri.fromFile(new File(str)), "video/*");
            intent = Intent.createChooser(intent2, "Complete action using");
        } else {
            File file = new File(str);
            Intent intent3 = new Intent("android.intent.action.VIEW");
            try {
                Context context2 = this.context;
                this.context.grantUriPermission(this.context.getPackageName(), FileProvider.getUriForFile(context2, this.context.getPackageName() + ".fileprovider", file), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent3.setType("video/*");
                if (Build.VERSION.SDK_INT < 24) {
                    Uri.fromFile(file);
                }
                intent3.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent = Intent.createChooser(intent3, "Complete action using");
            } catch (Exception unused) {
                return;
            }
        }
        this.context.startActivity(intent);
    }

    public int getItemCount() {
        return this.listPhoto.size();
    }
}
