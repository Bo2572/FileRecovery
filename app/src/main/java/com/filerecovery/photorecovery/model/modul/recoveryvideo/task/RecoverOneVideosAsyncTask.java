package com.filerecovery.photorecovery.model.modul.recoveryvideo.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.VideoModel;
import com.filerecovery.photorecovery.utilts.MediaScanner;
import com.filerecovery.photorecovery.utilts.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class RecoverOneVideosAsyncTask extends AsyncTask<String, Integer, String> {
    private final String TAG = getClass().getName();
    int count = 0;
    private Context mContext;
    private VideoModel mVideo;
    private OnRestoreListener onRestoreListener;

    AlertDialog dialog;

    TextView tvNumber;


    void Show_Diloge(Context con) {
        View alertCustomdialog = LayoutInflater.from(con).inflate(R.layout.custom_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(con);
        tvNumber = alertCustomdialog.findViewById(R.id.tvNumber);
        alert.setView(alertCustomdialog);
        dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public interface OnRestoreListener {
        void onComplete(String str);
    }

    public RecoverOneVideosAsyncTask(Context context, VideoModel videoModel, OnRestoreListener onRestoreListener2) {
        this.mContext = context;
        this.mVideo = videoModel;
        this.onRestoreListener = onRestoreListener2;
    }


    public void onPreExecute() {
        super.onPreExecute();
        if (!((Activity) this.mContext).isFinishing()) {



            Show_Diloge(this.mContext);


        }

    }


    public String doInBackground(String... strArr) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Log.e("doInBack", "1");
        File file = new File(this.mVideo.getPathPhoto());
        Context context = this.mContext;
        File file2 = new File(Utils.getPathSave(context, context.getString(R.string.restore_folder_path_video)));
        StringBuilder sb = new StringBuilder();
        Context context2 = this.mContext;
        sb.append(Utils.getPathSave(context2, context2.getString(R.string.restore_folder_path_video)));
        sb.append(File.separator);
        Log.e("doInBack", "2");

        if (!file.exists()) {
            return "Er1";
        }
        sb.append(getFileName(this.mVideo.getPathPhoto()));
        File file3 = new File(sb.toString());
        try {
            if (!file3.exists()) {
                file2.mkdirs();
            }
            copy(file, file3);
            if (Build.VERSION.SDK_INT >= 19) {
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(file3));
                this.mContext.sendBroadcast(intent);
            }
            new MediaScanner(this.mContext, file3);
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("doInBack", "e2" + e2.getMessage());

        }

        Log.e("doInBack", "3");

        try {
            Thread.sleep(2000);
            return null;
        } catch (InterruptedException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public void copy(File file, File file2) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        FileChannel channel2 = new FileOutputStream(file2).getChannel();
        channel.transferTo(0, channel.size(), channel2);
        if (channel != null) {
            channel.close();
        }
        if (channel2 != null) {
            channel2.close();
        }
    }

    public String getFileName(String str) {
        String substring = str.substring(str.lastIndexOf("/") + 1);
        if (substring.endsWith(".3gp") || substring.endsWith(".mp4") || substring.endsWith(".mkv") || substring.endsWith(".flv")) {
            return substring;
        }
        return substring + ".mp4";
    }


    public void onPostExecute(String str) {
        super.onPostExecute(str);
        try {






            if (this.dialog != null && this.dialog.isShowing()) {
                this.dialog.dismiss();
                this.dialog = null;
            }
        } catch (Exception unused) {
        }
        if (this.onRestoreListener != null) {
            if (str == null) {
                str = "";
            }
            this.onRestoreListener.onComplete(str);
        }
    }


    public void onProgressUpdate(Integer... numArr) {
        super.onProgressUpdate(numArr);
        this.tvNumber.setText(this.mContext.getString(R.string.restoring_video));
    }
}
