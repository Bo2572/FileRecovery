package com.filerecovery.photorecovery.model.modul.recoveryphoto;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.filerecovery.photorecovery.helpers.LoadingAnimation;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.Model.PhotoModel;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.adapter.PhotoAdapter;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.task.RecoverPhotosAsyncTask;
import com.filerecovery.photorecovery.ui.activity.MainActivity;
import com.filerecovery.photorecovery.ui.activity.RestoreResultActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class PhotosActivity extends AppCompatActivity {
    private LoadingAnimation LAC;
    PhotoAdapter adapter;
    Button btnAll;
    Button btnDelete;
    Button btnIcons;
    Button btnLarge;
    Button btnMedium;
    Button btnRestore;
    CheckBox btnSelect;
    int int_position;
    ArrayList<PhotoModel> mList = new ArrayList<>();
    RecoverPhotosAsyncTask mRecoverPhotosAsyncTask;

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
        this.toolbar.setTitle((CharSequence) getString(R.string.photo_recovery));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((LinearLayout) findViewById(R.id.TOPButtonsDIV)).setVisibility(View.VISIBLE);
        this.btnAll = (Button) findViewById(R.id.btnAll);
        this.btnIcons = (Button) findViewById(R.id.btnIcons);
        this.btnMedium = (Button) findViewById(R.id.btnMedium);
        this.btnLarge = (Button) findViewById(R.id.btnLarge);
        this.btnRestore = (Button) findViewById(R.id.btnRestore);
        this.recyclerView = (RecyclerView) findViewById(R.id.gv_folder);
        this.btnSelect = (CheckBox) findViewById(R.id.checkboxSelectAll);
        this.btnDelete = (Button) findViewById(R.id.btnDelete);
        this.rl = (LinearLayout) findViewById(R.id.PhotoActivity);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        this.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void intData() {
        this.int_position = getIntent().getIntExtra("value", 0);
        if (MainActivity.mAlbumPhoto != null && MainActivity.mAlbumPhoto.size() > this.int_position) {
            this.mList.addAll((ArrayList) MainActivity.mAlbumPhoto.get(this.int_position).getListPhoto().clone());
        }
        this.adapter = new PhotoAdapter(this, this.mList);
        this.recyclerView.setAdapter(this.adapter);
        this.btnAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotosActivity.this.SortBySize(1);
            }
        });
        this.btnIcons.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotosActivity.this.SortBySize(2);
            }
        });
        this.btnMedium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotosActivity.this.SortBySize(3);
            }
        });
        this.btnLarge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotosActivity.this.SortBySize(4);
            }
        });
        this.btnSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    PhotosActivity.this.adapter.setAllImagesUnseleted();
                } else {
                    PhotosActivity.this.adapter.selectAll();
                }
            }
        });
        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PhotosActivity.this);
                builder.setTitle((CharSequence) PhotosActivity.this.getString(R.string.delete_title));
                builder.setMessage((CharSequence) PhotosActivity.this.getString(R.string.delete_bdy));
                builder.setPositiveButton("ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhotosActivity.this.deleteActions();
                    }
                });
                builder.setNegativeButton("Cancle", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog create = builder.create();
                if (!PhotosActivity.this.isFinishing()) {
                    create.show();
                }

            }
        });
        this.btnRestore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<PhotoModel> selectedItem = PhotosActivity.this.adapter.getSelectedItem();
                if (selectedItem.size() == 0) {
                    PhotosActivity photosActivity = PhotosActivity.this;
                    Toast.makeText(photosActivity, photosActivity.getString(R.string.can_not_process), Toast.LENGTH_LONG).show();
                    return;
                }
                SResIT(selectedItem);
            }
        });
    }



    public void deleteActions() {
        if (this.adapter.getSelectedItem().size() == 0) {
            Toast.makeText(this, getString(R.string.can_not_process), Toast.LENGTH_LONG).show();
            return;
        }
        this.btnSelect.setChecked(false);
        this.mRecoverPhotosAsyncTask = new RecoverPhotosAsyncTask(this, this.adapter.getSelectedItem(), true, new RecoverPhotosAsyncTask.OnRestoreListener() {
            public void onComplete(String str) {
                Iterator<PhotoModel> it = PhotosActivity.this.mList.iterator();
                while (it.hasNext()) {
                    PhotoModel next = it.next();
                    if (next.getIsCheck()) {
                        it.remove();
                        MainActivity.mAlbumPhoto.get(PhotosActivity.this.int_position).getListPhoto().remove(next);
                        new File(next.getPathPhoto()).exists();
                    }
                }
                PhotosActivity.this.adapter.setAllImagesUnseleted();
                PhotosActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.mRecoverPhotosAsyncTask.execute(new String[0]);
    }


    public void SortBySize(int i) {
        this.btnSelect.setChecked(false);
        ArrayList arrayList = (ArrayList) MainActivity.mAlbumPhoto.get(this.int_position).getListPhoto().clone();
        this.mList.clear();
        if (i == 1) {
            this.mList.addAll(arrayList);
        } else if (i == 2) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                PhotoModel photoModel = (PhotoModel) it.next();
                if (photoModel.getSizePhoto() <= ((long) 10000)) {
                    this.mList.add(photoModel);
                }
            }
        } else if (i == 3) {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                PhotoModel photoModel2 = (PhotoModel) it2.next();
                long sizePhoto = photoModel2.getSizePhoto();
                if (sizePhoto > ((long) 10000) && sizePhoto <= ((long) 2048000)) {
                    this.mList.add(photoModel2);
                }
            }
        } else if (i == 4) {
            Iterator it3 = arrayList.iterator();
            while (it3.hasNext()) {
                PhotoModel photoModel3 = (PhotoModel) it3.next();
                if (photoModel3.getSizePhoto() > ((long) 2048000)) {
                    this.mList.add(photoModel3);
                }
            }
        }
        this.adapter.setAllImagesUnseleted();
        this.adapter.notifyDataSetChanged();
        ((LinearLayoutManager) this.recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
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

    public void SResIT(final ArrayList arrayList) {
        this.btnSelect.setChecked(false);
        this.mRecoverPhotosAsyncTask = new RecoverPhotosAsyncTask(this, this.adapter.getSelectedItem(), new RecoverPhotosAsyncTask.OnRestoreListener() {
            public void onComplete(String str) {
                if (str.isEmpty()) {
                    Intent intent = new Intent(PhotosActivity.this.getApplicationContext(), RestoreResultActivity.class);
                    intent.putExtra("value", arrayList.size());
                    intent.putExtra("type", 0);
                    PhotosActivity.this.startActivity(intent);
                } else if (str.equals("Er1")) {
                    PhotosActivity photosActivity = PhotosActivity.this;
                    Toast.makeText(photosActivity, photosActivity.getString(R.string.FileDeletedBeforeScan), Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(PhotosActivity.this.getApplicationContext(), MainActivity.class);
                    intent2.addFlags(67108864);
                    PhotosActivity.this.startActivity(intent2);
                }
                PhotosActivity.this.adapter.setAllImagesUnseleted();
                PhotosActivity.this.adapter.notifyDataSetChanged();
            }
        });
        this.mRecoverPhotosAsyncTask.execute(new String[0]);
    }
}
