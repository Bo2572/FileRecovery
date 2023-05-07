package com.filerecovery.photorecovery.model.modul.recoveryphoto;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.adapter.AlbumsPhotoAdapter;
import com.filerecovery.photorecovery.ui.activity.MainActivity;

public class AlbumPhotoActivity extends AppCompatActivity implements AlbumsPhotoAdapter.OnClickItemListener {
    AlbumsPhotoAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

    public void onClickItem(int i) {
        Intent intent = new Intent(getApplicationContext(), PhotosActivity.class);
        intent.putExtra("value", i);
        startActivity(intent);
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_album);
        intView();
        intData();
    }

    public void intView() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle((CharSequence) getString(R.string.photo_recovery));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.recyclerView = (RecyclerView) findViewById(R.id.gv_folder);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void intData() {
        this.adapter = new AlbumsPhotoAdapter(this, MainActivity.mAlbumPhoto, this);
        this.recyclerView.setAdapter(this.adapter);
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

    private int dpToPx(int i) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, getResources().getDisplayMetrics()));
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onResume() {
        super.onResume();
        this.adapter.notifyDataSetChanged();
    }
}
