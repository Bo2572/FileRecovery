package com.filerecovery.photorecovery.model.modul.recoveryvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.AlbumVideo;

import java.util.ArrayList;

public class AlbumsVideoAdapter extends RecyclerView.Adapter<AlbumsVideoAdapter.MyViewHolder> {
    ArrayList<AlbumVideo> al_menu = new ArrayList<>();
    Context context;
    OnClickItemListener mOnClickItemListener;

    public interface OnClickItemListener {
        void onClickItem(int i);
    }

    public AlbumsVideoAdapter(Context context2, ArrayList<AlbumVideo> arrayList, OnClickItemListener onClickItemListener) {
        this.context = context2;
        this.al_menu = arrayList;
        this.mOnClickItemListener = onClickItemListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnClickItemListener onClickItemListener;
        protected RecyclerView recycler_view_list;
        TextView tv_foldersize;

        public MyViewHolder(View view, OnClickItemListener onClickItemListener2) {
            super(view);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.tv_foldersize = (TextView) view.findViewById(R.id.tv_folder2);
            this.onClickItemListener = onClickItemListener2;
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            this.onClickItemListener.onClickItem(getAdapterPosition());
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_album_new, viewGroup, false), this.mOnClickItemListener);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        TextView textView = myViewHolder.tv_foldersize;
        textView.setText(this.al_menu.get(i).getListPhoto().size() + " Videos");
        SectionListDataAdapter sectionListDataAdapter = new SectionListDataAdapter(this.context, this.al_menu.get(i).getListPhoto(), i);
        myViewHolder.recycler_view_list.setHasFixedSize(true);
        myViewHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false));
        myViewHolder.recycler_view_list.setAdapter(sectionListDataAdapter);
    }

    public int getItemCount() {
        return this.al_menu.size();
    }
}
