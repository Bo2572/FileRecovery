package com.filerecovery.photorecovery.model.modul.recoveryphoto.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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

import java.text.DateFormat;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    Context context;
    ArrayList<PhotoModel> listPhoto = new ArrayList<>();
    BitmapDrawable placeholder;

    public PhotoAdapter(Context context2, ArrayList<PhotoModel> arrayList) {
        this.context = context2;
        this.listPhoto = arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView album_card;
        ImageView ivChecked;
        ImageView ivPhoto;
        TextView tvDate;
        TextView tvSize;

        public MyViewHolder(View view) {
            super(view);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.tvSize = (TextView) view.findViewById(R.id.tvSize);
            this.ivPhoto = (SquareImageView) view.findViewById(R.id.iv_image);
            this.ivChecked = (ImageView) view.findViewById(R.id.isChecked);
            this.album_card = (CardView) view.findViewById(R.id.album_card);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_photo, viewGroup, false));
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        final PhotoModel photoModel = this.listPhoto.get(i);
        myViewHolder.tvDate.setText(DateFormat.getDateInstance().format(Long.valueOf(photoModel.getLastModified())));
        myViewHolder.tvSize.setText(Utils.formatSize(photoModel.getSizePhoto()));
        if (photoModel.getIsCheck()) {
            myViewHolder.ivChecked.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.ivChecked.setVisibility(View.GONE);
        }
        try {
            RequestManager with = Glide.with(this.context);
            ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) with.load("file://" + photoModel.getPathPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL)).priority(Priority.HIGH)).centerCrop()).error((int) R.drawable.ic_error)).placeholder((Drawable) this.placeholder)).into(myViewHolder.ivPhoto);
        } catch (Exception e) {
            Context context2 = this.context;
            Toast.makeText(context2, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        myViewHolder.album_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (photoModel.getIsCheck()) {
                    myViewHolder.ivChecked.setVisibility(View.GONE);
                    photoModel.setIsCheck(false);
                    return;
                }
                myViewHolder.ivChecked.setVisibility(View.VISIBLE);
                photoModel.setIsCheck(true);
            }
        });
    }

    public int getItemCount() {
        return this.listPhoto.size();
    }

    public ArrayList<PhotoModel> getSelectedItem() {
        ArrayList<PhotoModel> arrayList = new ArrayList<>();
        if (this.listPhoto != null) {
            for (int i = 0; i < this.listPhoto.size(); i++) {
                if (this.listPhoto.get(i).getIsCheck()) {
                    arrayList.add(this.listPhoto.get(i));
                }
            }
        }
        return arrayList;
    }

    public void setAllImagesUnseleted() {
        if (this.listPhoto != null) {
            for (int i = 0; i < this.listPhoto.size(); i++) {
                if (this.listPhoto.get(i).getIsCheck()) {
                    this.listPhoto.get(i).setIsCheck(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void selectAll() {
        if (this.listPhoto != null) {
            for (int i = 0; i < this.listPhoto.size(); i++) {
                this.listPhoto.get(i).setIsCheck(true);
            }
            notifyDataSetChanged();
        }
    }
}
