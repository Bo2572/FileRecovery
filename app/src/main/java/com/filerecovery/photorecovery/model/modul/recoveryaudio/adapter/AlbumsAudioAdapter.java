package com.filerecovery.photorecovery.model.modul.recoveryaudio.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AlbumAudio;

import java.util.ArrayList;

public class AlbumsAudioAdapter extends RecyclerView.Adapter<AlbumsAudioAdapter.MyViewHolder> {
    ArrayList<AlbumAudio> al_menu = new ArrayList<>();
    Context context;
    OnClickItemListener mOnClickItemListener;

    public interface OnClickItemListener {
        void onClickItem(int i);
    }

    public AlbumsAudioAdapter(Context context2, ArrayList<AlbumAudio> arrayList, OnClickItemListener onClickItemListener) {
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
            this.tv_foldersize = (TextView) view.findViewById(R.id.tvDate);
            this.onClickItemListener = onClickItemListener2;
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            this.onClickItemListener.onClickItem(getAdapterPosition());
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_audio, viewGroup, false), this.mOnClickItemListener);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        TextView textView = myViewHolder.tv_foldersize;
        textView.setText(this.al_menu.get(i).getListPhoto().size() + " Audio");
        SectionListAudioAdapter sectionListAudioAdapter = new SectionListAudioAdapter(this.context, this.al_menu.get(i).getListPhoto(), i);
        myViewHolder.recycler_view_list.setLayoutManager(new GridLayoutManager(this.context, 1));
        myViewHolder.recycler_view_list.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(this.context, 10), true));
        myViewHolder.recycler_view_list.setAdapter(sectionListAudioAdapter);
    }

    public int getItemCount() {
        return this.al_menu.size();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private boolean includeEdge;
        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int i, int i2, boolean z) {
            this.spanCount = i;
            this.spacing = i2;
            this.includeEdge = z;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            int i = this.spanCount;
            int i2 = childAdapterPosition % i;
            if (this.includeEdge) {
                int i3 = this.spacing;
                rect.left = i3 - ((i2 * i3) / i);
                rect.right = ((i2 + 1) * i3) / i;
                if (childAdapterPosition < i) {
                    rect.top = i3;
                }
                rect.bottom = this.spacing;
                return;
            }
            int i4 = this.spacing;
            rect.left = (i2 * i4) / i;
            rect.right = i4 - (((i2 + 1) * i4) / i);
            if (childAdapterPosition >= i) {
                rect.top = i4;
            }
        }
    }

    private int dpToPx(Context context2, int i) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, context2.getResources().getDisplayMetrics()));
    }
}
