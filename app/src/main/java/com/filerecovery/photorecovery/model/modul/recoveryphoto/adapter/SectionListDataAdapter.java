package com.filerecovery.photorecovery.model.modul.recoveryphoto.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo.R;
import com.filerecovery.photorecovery.model.SquareImageView;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.Model.PhotoModel;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.PhotosActivity;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {
    private ArrayList<PhotoModel> itemsList;

    public Context mContext;
    int postion;
    int size;

    public SectionListDataAdapter(Context context, ArrayList<PhotoModel> arrayList, int i) {
        this.itemsList = arrayList;
        this.mContext = context;
        this.postion = i;
        if (arrayList.size() >= 5) {
            this.size = 5;
        } else {
            this.size = arrayList.size();
        }
    }

    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleItemRowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, (ViewGroup) null));
    }

    public void onBindViewHolder(SingleItemRowHolder singleItemRowHolder, int i) {
        PhotoModel photoModel = this.itemsList.get(i);
        try {
            RequestManager with = Glide.with(this.mContext);
            ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) with.load("file://" + photoModel.getPathPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL)).priority(Priority.HIGH)).centerCrop()).error((int) R.drawable.ic_error)).into((ImageView) singleItemRowHolder.itemImage);
        } catch (Exception e) {
            Context context = this.mContext;
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        singleItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SectionListDataAdapter.this.mContext, PhotosActivity.class);
                intent.putExtra("value", SectionListDataAdapter.this.postion);
                SectionListDataAdapter.this.mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.size;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected SquareImageView itemImage;

        public SingleItemRowHolder(View view) {
            super(view);
            this.itemImage = (SquareImageView) view.findViewById(R.id.ivImage);
        }
    }
}
