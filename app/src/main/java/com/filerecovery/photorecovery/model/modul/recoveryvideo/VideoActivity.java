package com.filerecovery.photorecovery.model.modul.recoveryvideo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.VideoModel;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.adapter.VideoAdapter;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.task.RecoverVideoAsyncTask;
import com.filerecovery.photorecovery.ui.activity.MainActivity;
import com.filerecovery.photorecovery.ui.activity.RestoreResultActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class VideoActivity extends AppCompatActivity {
    VideoAdapter adapter;
    Button btnDelete;
    Button btnRestore;
    CheckBox btnSelect;
    int int_position;
    ArrayList<VideoModel> mList = new ArrayList<>();
    RecoverVideoAsyncTask mRecoverVideoAsyncTask;
    RecyclerView recyclerView;
    private LinearLayout rl;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_photos);
        intView();
        intData();
    }

    public void intView() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle((CharSequence) getString(R.string.video_recovery));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.btnRestore = (Button) findViewById(R.id.btnRestore);
        this.recyclerView = (RecyclerView) findViewById(R.id.gv_folder);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.btnSelect = (CheckBox) findViewById(R.id.checkboxSelectAll);
        this.btnDelete = (Button) findViewById(R.id.btnDelete);
        this.rl = (LinearLayout) findViewById(R.id.PhotoActivity);
    }

    public void intData() {
        this.int_position = getIntent().getIntExtra("value", 0);
        if (MainActivity.mAlbumVideo != null && MainActivity.mAlbumVideo.size() > this.int_position) {
            this.mList.addAll((ArrayList) MainActivity.mAlbumVideo.get(this.int_position).getListPhoto().clone());
        }
        this.adapter = new VideoAdapter(this, this.mList);
        this.recyclerView.setAdapter(this.adapter);
        this.btnSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    VideoActivity.this.adapter.setAllImagesUnseleted();
                } else {
                    VideoActivity.this.adapter.selectAll();
                }
            }
        });
        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                builder.setTitle((CharSequence) VideoActivity.this.getString(R.string.delete_title));
                builder.setMessage((CharSequence) VideoActivity.this.getString(R.string.delete_bdy));
                builder.setPositiveButton("ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VideoActivity.this.deleteActions();
                    }
                });
                builder.setNegativeButton("Cancle", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog create = builder.create();
                if (!VideoActivity.this.isFinishing()) {
                    create.show();

                }
            }
        });
        this.btnRestore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<VideoModel> selectedItem = VideoActivity.this.adapter.getSelectedItem();
                if (selectedItem.size() == 0) {
                    VideoActivity videoActivity = VideoActivity.this;
                    Toast.makeText(videoActivity, videoActivity.getString(R.string.can_not_process), 1).show();
                    return;
                }


                SResIT(selectedItem);


            }


        });
    }

    public void SResIT(final ArrayList arrayList) {
        this.btnSelect.setChecked(false);
        this.mRecoverVideoAsyncTask = new RecoverVideoAsyncTask(this, this.adapter.getSelectedItem(), new RecoverVideoAsyncTask.OnRestoreListener() {
            public void onComplete(String str) {
                if (str.isEmpty()) {
                    Intent intent = new Intent(VideoActivity.this.getApplicationContext(), RestoreResultActivity.class);
                    intent.putExtra("value", arrayList.size());
                    intent.putExtra("type", 1);
                    VideoActivity.this.startActivity(intent);
                } else if (str.equals("Er1")) {
                    VideoActivity videoActivity = VideoActivity.this;
                    Toast.makeText(videoActivity, videoActivity.getString(R.string.FileDeletedBeforeScan), Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(VideoActivity.this.getApplicationContext(), MainActivity.class);
                    intent2.addFlags(67108864);
                    VideoActivity.this.startActivity(intent2);
                }
                VideoActivity.this.adapter.setAllImagesUnseleted();
                VideoActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.mRecoverVideoAsyncTask.execute(new String[0]);
    }



    public void deleteActions() {
        if (this.adapter.getSelectedItem().size() == 0) {
            Toast.makeText(this, getString(R.string.can_not_process), Toast.LENGTH_LONG).show();
            return;
        }
        this.btnSelect.setChecked(false);
        this.mRecoverVideoAsyncTask = new RecoverVideoAsyncTask(this, this.adapter.getSelectedItem(), true, new RecoverVideoAsyncTask.OnRestoreListener() {
            public void onComplete(String str) {
                Iterator<VideoModel> it = VideoActivity.this.mList.iterator();
                while (it.hasNext()) {
                    VideoModel next = it.next();
                    if (next.getIsCheck()) {
                        it.remove();
                        MainActivity.mAlbumVideo.get(VideoActivity.this.int_position).getListPhoto().remove(next);
                    }
                }
                VideoActivity.this.adapter.setAllImagesUnseleted();
                VideoActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.mRecoverVideoAsyncTask.execute(new String[0]);
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
        return Math.round(TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics()));
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void cancleUIUPdate() {
        RecoverVideoAsyncTask recoverVideoAsyncTask = this.mRecoverVideoAsyncTask;
        if (recoverVideoAsyncTask != null && recoverVideoAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            this.mRecoverVideoAsyncTask.cancel(true);
            this.mRecoverVideoAsyncTask = null;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
