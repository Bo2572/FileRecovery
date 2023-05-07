package com.filerecovery.photorecovery.model.modul.recoveryvideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo.R;
import com.filerecovery.photorecovery.helpers.LoadingAnimation;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.VideoModel;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.task.RecoverOneVideosAsyncTask;
import com.filerecovery.photorecovery.ui.activity.MainActivity;
import com.filerecovery.photorecovery.ui.activity.RestoreResultActivity;
import com.filerecovery.photorecovery.utilts.Utils;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;

public class FileInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private LoadingAnimation LAC;
    Button btnOpen;
    Button btnRestore;
    Button btnShare;
    ImageView ivVideo;
    RecoverOneVideosAsyncTask mRecoverOneVideosAsyncTask;
    VideoModel mVideoModel;
    private LinearLayout rl;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    TextView tvDate;
    TextView tvSize;
    TextView tvType;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_file_info);
        intView();
        intData();
        intEvent();
    }

    public void intView() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.setTitle((CharSequence) getString(R.string.restore_photo));
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.btnOpen = (Button) findViewById(R.id.btnOpen);
        this.btnShare = (Button) findViewById(R.id.btnShare);
        this.btnRestore = (Button) findViewById(R.id.btnRestore);
        this.tvDate = (TextView) findViewById(R.id.tvDate);
        this.tvSize = (TextView) findViewById(R.id.tvSize);
        this.tvType = (TextView) findViewById(R.id.tvType);
        this.ivVideo = (ImageView) findViewById(R.id.ivVideo);
        this.rl = (LinearLayout) findViewById(R.id.activity_file_info_cont);
    }

    public void intData() {
        this.mVideoModel = (VideoModel) getIntent().getSerializableExtra("ojectVideo");
        TextView textView = this.tvDate;
        textView.setText(DateFormat.getDateInstance().format(Long.valueOf(this.mVideoModel.getLastModified())) + "  " + this.mVideoModel.getTimeDuration());
        this.tvSize.setText(Utils.formatSize(this.mVideoModel.getSizePhoto()));
        this.tvType.setText(this.mVideoModel.getTypeFile());
        RequestManager with = Glide.with((FragmentActivity) this);
        ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) with.load("file://" + this.mVideoModel.getPathPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL)).priority(Priority.HIGH)).centerCrop()).error((int) R.drawable.ic_error)).into(this.ivVideo);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void intEvent() {
        this.btnOpen.setOnClickListener(this);
        this.btnShare.setOnClickListener(this);
        this.btnRestore.setOnClickListener(this);
        this.ivVideo.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpen:
                openFile(this.mVideoModel.getPathPhoto());
                return;
            case R.id.btnRestore:
                ArrayList arrayList = new ArrayList();
                arrayList.add("X");


                Log.e("ddddd", "dddddd");



                mRecoverOneVideosAsyncTask = new RecoverOneVideosAsyncTask(FileInfoActivity.this, mVideoModel, new RecoverOneVideosAsyncTask.OnRestoreListener() {
                    public void onComplete(String str) {
                        if (str.isEmpty()) {
                            Intent intent = new Intent(FileInfoActivity.this.getApplicationContext(), RestoreResultActivity.class);
                            intent.putExtra("value", 1);
                            FileInfoActivity.this.startActivity(intent);
                            FileInfoActivity.this.finish();
                        } else if (str.equals("Er1")) {
                            FileInfoActivity fileInfoActivity = FileInfoActivity.this;
                            Toast.makeText(fileInfoActivity, fileInfoActivity.getString(R.string.FileDeletedBeforeScan), Toast.LENGTH_LONG).show();
                            Intent intent2 = new Intent(FileInfoActivity.this.getApplicationContext(), MainActivity.class);
                            intent2.addFlags(67108864);
                            FileInfoActivity.this.startActivity(intent2);
                        }
                    }
                });

                Log.e("Recover Videos", "yes");
                mRecoverOneVideosAsyncTask.execute(new String[0]);




                return;
            case R.id.btnShare:
                shareVideo(this.mVideoModel.getPathPhoto());
                return;
            case R.id.ivVideo:
                openFile(this.mVideoModel.getPathPhoto());
                return;
            default:
                return;
        }

    }


    public void cancleUIUPdate() {
        RecoverOneVideosAsyncTask recoverOneVideosAsyncTask = this.mRecoverOneVideosAsyncTask;
        if (recoverOneVideosAsyncTask != null && recoverOneVideosAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            this.mRecoverOneVideosAsyncTask.cancel(true);
            this.mRecoverOneVideosAsyncTask = null;
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean SDCardCheck() {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(this, (String) null);
        return (externalFilesDirs.length <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) ? false : true;
    }

    public void fileSearch() {
        startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 100);
    }

    public void openFile(String str) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 24) {
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setDataAndType(Uri.fromFile(new File(str)), "video/*");
            intent = Intent.createChooser(intent2, "Complete action using");
        } else {
            File file = new File(str);
            Intent intent3 = new Intent("android.intent.action.VIEW");
            try {
                Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                grantUriPermission(getPackageName(), uriForFile, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent3.setType("*/*");
                if (Build.VERSION.SDK_INT < 24) {
                    uriForFile = Uri.fromFile(file);
                }
                intent3.setData(uriForFile);
                intent3.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent = Intent.createChooser(intent3, "Complete action using");
            } catch (Exception unused) {
                return;
            }
        }
        startActivity(intent);
    }

    private void shareVideo(String str) {
        try {
            startActivity(Intent.createChooser(new Intent().setAction("android.intent.action.SEND").setType("video/*").setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(str))), ""));
        } catch (Exception unused) {
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        cancleUIUPdate();
    }

    private static boolean checkIfSDCardRoot(Uri uri) {
        return isExternalStorageDocument(uri) && isRootUri(uri) && !isInternalStorage(uri);
    }

    private static boolean isRootUri(Uri uri) {
        return DocumentsContract.getTreeDocumentId(uri).endsWith(":");
    }

    public static boolean isInternalStorage(Uri uri) {
        return isExternalStorageDocument(uri) && DocumentsContract.getTreeDocumentId(uri).contains("primary");
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public void onActivityResult(int r10, int r11, Intent r12) {

        throw new UnsupportedOperationException("Method not decompiled: com.narmx.photosrecovery.model.modul.recoveryvideo.FileInfoActivity.onActivityResult(int, int, android.content.Intent):void");
    }
}
