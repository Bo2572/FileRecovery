package com.filerecovery.photorecovery.model.modul.recoveryphoto.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.filerecovery.photorecovery.model.modul.recoveryphoto.Model.PhotoModel;
import com.filerecovery.photorecovery.utilts.Utils;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;

public class ViewPhotoAdapter extends RecyclerView.Adapter<ViewPhotoAdapter.MyViewHolder> {
    Context context;
    ArrayList<PhotoModel> listPhoto = new ArrayList<>();
    BitmapDrawable placeholder;

    public ViewPhotoAdapter(Context context2, ArrayList<PhotoModel> arrayList) {
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
        final PhotoModel photoModel = this.listPhoto.get(i);
        myViewHolder.tvDate.setText(DateFormat.getDateInstance().format(Long.valueOf(photoModel.getLastModified())));
        myViewHolder.tvSize.setText(Utils.formatSize(photoModel.getSizePhoto()));
        try {
            RequestManager with = Glide.with(this.context);
            ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) with.load("file://" + photoModel.getPathPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL)).priority(Priority.HIGH)).centerCrop()).error((int) R.drawable.ic_error)).placeholder((Drawable) this.placeholder)).into(myViewHolder.ivPhoto);
        } catch (Exception e) {
            Context context2 = this.context;
            Toast.makeText(context2, "Exception: " + e.getMessage(), 0).show();
        }
        myViewHolder.album_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                try {
                    Context context = ViewPhotoAdapter.this.context;
                    Uri uriForFile = FileProvider.getUriForFile(context, ViewPhotoAdapter.this.context.getPackageName() + ".fileprovider", new File(photoModel.getPathPhoto()));
                    intent.setAction("android.intent.action.VIEW").addFlags(1).setDataAndType(uriForFile, ViewPhotoAdapter.this.context.getContentResolver().getType(uriForFile));
                    ViewPhotoAdapter.this.context.startActivity(intent);
                } catch (Exception unused) {
                }
            }
        });
    }

    public int getItemCount() {
        return this.listPhoto.size();
    }
}
