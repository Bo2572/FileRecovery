package com.filerecovery.photorecovery.model.modul.recoveryvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo.R;
import com.filerecovery.photorecovery.model.SquareImageView;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.FileInfoActivity;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.VideoModel;
import com.filerecovery.photorecovery.utilts.Utils;

import java.text.DateFormat;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    Context context;
    ArrayList<VideoModel> listPhoto = new ArrayList<>();

    public VideoAdapter(Context context2, ArrayList<VideoModel> arrayList) {
        this.context = context2;
        this.listPhoto = arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout album_card;
        AppCompatCheckBox cbSelected;
        SquareImageView ivPhoto;
        TextView tvDate;
        TextView tvSize;
        TextView tvType;

        public MyViewHolder(View view) {
            super(view);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.tvSize = (TextView) view.findViewById(R.id.tvSize);
            this.tvType = (TextView) view.findViewById(R.id.tvType);
            this.ivPhoto = (SquareImageView) view.findViewById(R.id.iv_image);
            this.cbSelected = (AppCompatCheckBox) view.findViewById(R.id.cbSelected);
            this.album_card = (RelativeLayout) view.findViewById(R.id.album_card);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_video, viewGroup, false));
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        final VideoModel videoModel = this.listPhoto.get(i);
        TextView textView = myViewHolder.tvDate;
        textView.setText(DateFormat.getDateInstance().format(Long.valueOf(videoModel.getLastModified())) + "  " + videoModel.getTimeDuration());
        myViewHolder.cbSelected.setChecked(videoModel.getIsCheck());
        myViewHolder.tvSize.setText(Utils.formatSize(videoModel.getSizePhoto()));
        myViewHolder.tvType.setText(videoModel.getTypeFile());
        try {
            RequestManager with = Glide.with(this.context);
            ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) with.load("file://" + videoModel.getPathPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL)).priority(Priority.HIGH)).centerCrop()).error((int) R.drawable.ic_error)).into((ImageView) myViewHolder.ivPhoto);
        } catch (Exception e) {
            Context context2 = this.context;
            Toast.makeText(context2, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        myViewHolder.album_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VideoAdapter.this.context, FileInfoActivity.class);
                intent.putExtra("ojectVideo", videoModel);
                VideoAdapter.this.context.startActivity(intent);
            }
        });
        myViewHolder.cbSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (myViewHolder.cbSelected.isChecked()) {
                    videoModel.setIsCheck(true);
                } else {
                    videoModel.setIsCheck(false);
                }
            }
        });
    }

    public int getItemCount() {
        return this.listPhoto.size();
    }

    public ArrayList<VideoModel> getSelectedItem() {
        ArrayList<VideoModel> arrayList = new ArrayList<>();
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
