package com.filerecovery.photorecovery.model.modul.recoveryaudio.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.AudioActivity;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AudioModel;
import com.filerecovery.photorecovery.utilts.Utils;

import java.util.ArrayList;

public class SectionListAudioAdapter extends RecyclerView.Adapter<SectionListAudioAdapter.SingleItemRowHolder> {
    private ArrayList<AudioModel> itemsList;

    public Context mContext;
    int postion;
    int size;

    public SectionListAudioAdapter(Context context, ArrayList<AudioModel> arrayList, int i) {
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
        return new SingleItemRowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio_album, (ViewGroup) null));
    }

    public void onBindViewHolder(SingleItemRowHolder singleItemRowHolder, int i) {
        singleItemRowHolder.tvTitle.setText(Utils.getFileTitle(this.itemsList.get(i).getPathPhoto()));
        singleItemRowHolder.album_card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SectionListAudioAdapter.this.mContext, AudioActivity.class);
                intent.putExtra("value", SectionListAudioAdapter.this.postion);
                SectionListAudioAdapter.this.mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.size;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        CardView album_card;
        TextView tvTitle;

        public SingleItemRowHolder(View view) {
            super(view);
            this.album_card = (CardView) view.findViewById(R.id.album_card);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }
}
