package com.filerecovery.photorecovery.model.modul.recoveryaudio.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AudioModel;
import com.filerecovery.photorecovery.utilts.Utils;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;

public class ViewAudioAdapter extends RecyclerView.Adapter<ViewAudioAdapter.MyViewHolder> {
    Context context;
    ArrayList<AudioModel> listPhoto = new ArrayList<>();
    BitmapDrawable placeholder;

    public ViewAudioAdapter(Context context2, ArrayList<AudioModel> arrayList) {
        this.context = context2;
        this.listPhoto = arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout album_card;
        protected AppCompatCheckBox ivChecked;
        protected TextView tvDate;
        protected TextView tvSize;
        protected TextView tvTitle;

        public MyViewHolder(View view) {
            super(view);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tvSize = (TextView) view.findViewById(R.id.tvSize);
            this.tvDate = (TextView) view.findViewById(R.id.tvType);
            this.ivChecked = (AppCompatCheckBox) view.findViewById(R.id.cbSelect);
            this.ivChecked.setVisibility(View.GONE);
            this.album_card = (RelativeLayout) view.findViewById(R.id.album_card);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final AudioModel audioModel = this.listPhoto.get(i);
        myViewHolder.tvDate.setText(DateFormat.getDateInstance().format(Long.valueOf(audioModel.getLastModified())));
        myViewHolder.tvSize.setText(Utils.formatSize(audioModel.getSizePhoto()));
        myViewHolder.tvTitle.setText(Utils.getFileTitle(audioModel.getPathPhoto()));
        myViewHolder.album_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    ViewAudioAdapter.this.openFile(new File(audioModel.getPathPhoto()));
                } catch (Exception unused) {
                }
            }
        });
    }

    public int getItemCount() {
        return this.listPhoto.size();
    }

    public void openFile(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        if (file.exists()) {
            if (Build.VERSION.SDK_INT < 24) {
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
            } else {
                try {
                    Context context2 = this.context;
                    Uri uriForFile = FileProvider.getUriForFile(context2, this.context.getPackageName() + ".fileprovider", file);
                    this.context.grantUriPermission(this.context.getPackageName(), uriForFile, 1);
                    intent.setDataAndType(uriForFile, "audio/*");
                    intent.setFlags(1);
                } catch (Exception unused) {
                    return;
                }
            }
            this.context.startActivity(Intent.createChooser(intent, "Complete action using"));
        }
    }
}
