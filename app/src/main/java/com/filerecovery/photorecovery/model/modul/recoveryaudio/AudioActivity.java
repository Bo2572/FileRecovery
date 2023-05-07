package com.filerecovery.photorecovery.model.modul.recoveryaudio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AudioModel;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.adapter.AudioAdapter;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.task.RecoverAudioAsyncTask;
import com.filerecovery.photorecovery.ui.activity.MainActivity;
import com.filerecovery.photorecovery.ui.activity.RestoreResultActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class AudioActivity extends AppCompatActivity {
    AudioAdapter adapter;
    Button btnDelete;
    Button btnRestore;
    CheckBox btnSelect;
    int int_position;
    ArrayList<AudioModel> mList = new ArrayList<>();
    RecoverAudioAsyncTask mRecoverPhotosAsyncTask;

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
        this.toolbar.setTitle((CharSequence) getString(R.string.audio_recovery));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.btnRestore = (Button) findViewById(R.id.btnRestore);
        this.recyclerView = (RecyclerView) findViewById(R.id.gv_folder);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.btnSelect = (CheckBox) findViewById(R.id.checkboxSelectAll);
        this.btnDelete = (Button) findViewById(R.id.btnDelete);
        this.rl = (LinearLayout) findViewById(R.id.PhotoActivity);
    }

    public void intData() {
        this.int_position = getIntent().getIntExtra("value", 0);
        if (MainActivity.mAlbumAudio != null && MainActivity.mAlbumAudio.size() > this.int_position) {
            this.mList.addAll((ArrayList) MainActivity.mAlbumAudio.get(this.int_position).getListPhoto().clone());
        }
        this.adapter = new AudioAdapter(this, this.mList);
        this.recyclerView.setAdapter(this.adapter);
        this.btnSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    AudioActivity.this.adapter.setAllImagesUnseleted();
                } else {
                    AudioActivity.this.adapter.selectAll();
                }
            }
        });
        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AudioActivity.this);
                builder.setTitle((CharSequence) AudioActivity.this.getString(R.string.delete_title));
                builder.setMessage((CharSequence) AudioActivity.this.getString(R.string.delete_bdy));
                builder.setPositiveButton("ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AudioActivity.this.deleteActions();
                    }
                });
                builder.setNegativeButton("Cancle", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            }

        });
        this.btnRestore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<AudioModel> selectedItem = AudioActivity.this.adapter.getSelectedItem();
                if (selectedItem.size() == 0) {
                    AudioActivity audioActivity = AudioActivity.this;
                    Toast.makeText(audioActivity, audioActivity.getString(R.string.can_not_process), Toast.LENGTH_LONG).show();
                    return;
                }

                AudioActivity.this.SResIT(selectedItem);

            }
        });
    }

    public void SResIT(final ArrayList arrayList) {
        this.btnSelect.setChecked(false);
        this.mRecoverPhotosAsyncTask = new RecoverAudioAsyncTask(this, this.adapter.getSelectedItem(), new RecoverAudioAsyncTask.OnRestoreListener() {
            public void onComplete(String str) {
                if (str.isEmpty()) {
                    Intent intent = new Intent(AudioActivity.this.getApplicationContext(), RestoreResultActivity.class);
                    intent.putExtra("value", arrayList.size());
                    intent.putExtra("type", 2);
                    AudioActivity.this.startActivity(intent);
                } else if (str.equals("Er1")) {
                    AudioActivity audioActivity = AudioActivity.this;
                    Toast.makeText(audioActivity, audioActivity.getString(R.string.FileDeletedBeforeScan), Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(AudioActivity.this.getApplicationContext(), MainActivity.class);
                    intent2.addFlags(67108864);
                    AudioActivity.this.startActivity(intent2);
                }
                AudioActivity.this.adapter.setAllImagesUnseleted();
                AudioActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.mRecoverPhotosAsyncTask.execute(new String[0]);
    }



    public void deleteActions() {
        if (this.adapter.getSelectedItem().size() == 0) {
            Toast.makeText(this, getString(R.string.can_not_process), Toast.LENGTH_LONG).show();
            return;
        }
        this.btnSelect.setChecked(false);
        this.mRecoverPhotosAsyncTask = new RecoverAudioAsyncTask(this, this.adapter.getSelectedItem(), true, new RecoverAudioAsyncTask.OnRestoreListener() {
            public void onComplete(String str) {
                Iterator<AudioModel> it = AudioActivity.this.mList.iterator();
                while (it.hasNext()) {
                    AudioModel next = it.next();
                    if (next.getIsCheck()) {
                        it.remove();
                        MainActivity.mAlbumAudio.get(AudioActivity.this.int_position).getListPhoto().remove(next);
                    }
                }
                AudioActivity.this.adapter.setAllImagesUnseleted();
                AudioActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.mRecoverPhotosAsyncTask.execute(new String[0]);
    }

    public boolean SDCardCheck() {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(this, (String) null);
        return (externalFilesDirs.length <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) ? false : true;
    }

    public void fileSearch() {
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 100);
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
}
