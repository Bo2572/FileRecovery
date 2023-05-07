package com.filerecovery.photorecovery.model.modul.recoveryaudio.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AudioModel;
import com.filerecovery.photorecovery.utilts.MediaScanner;
import com.filerecovery.photorecovery.utilts.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class RecoverAudioAsyncTask extends AsyncTask<String, Integer, String> {
    private final String TAG = getClass().getName();
    int count = 0;
    boolean delete = false;
    private ArrayList<AudioModel> listPhoto;
    private Context mContext;
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

    public RecoverAudioAsyncTask(Context context, ArrayList<AudioModel> arrayList, OnRestoreListener onRestoreListener2) {
        this.mContext = context;
        this.listPhoto = arrayList;
        this.onRestoreListener = onRestoreListener2;
    }

    public RecoverAudioAsyncTask(Context context, ArrayList<AudioModel> arrayList, boolean z, OnRestoreListener onRestoreListener2) {
        this.delete = z;
        this.mContext = context;
        this.listPhoto = arrayList;
        this.onRestoreListener = onRestoreListener2;
    }


    public void onPreExecute() {
        super.onPreExecute();
        if (!((Activity) this.mContext).isFinishing()) {




            Show_Diloge(this.mContext);
        }
    }


    public String doInBackground(String... strArr) {
        if (this.delete) {
            for (int i = 0; i < this.listPhoto.size(); i++) {
                File file = new File(this.listPhoto.get(i).getPathPhoto());
                if (file.exists()) {
                    int i2 = 0;
                    while (i2 < 2) {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                            fileOutputStream.write("0".getBytes());
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            i2++;
                        } catch (Exception unused) {
                        }
                    }
                    file.delete();
                    if (Build.VERSION.SDK_INT >= 19) {
                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                        intent.setData(Uri.fromFile(file));
                        this.mContext.sendBroadcast(intent);
                    }
                    new MediaScanner(this.mContext, file);
                    this.count = i + 1;
                    publishProgress(new Integer[]{Integer.valueOf(this.count)});
                }
            }
        } else {
            for (int i3 = 0; i3 < this.listPhoto.size(); i3++) {
                File file2 = new File(this.listPhoto.get(i3).getPathPhoto());
                Context context = this.mContext;
                File file3 = new File(Utils.getPathSave(context, context.getString(R.string.restore_folder_path_audio)));
                StringBuilder sb = new StringBuilder();
                Context context2 = this.mContext;
                sb.append(Utils.getPathSave(context2, context2.getString(R.string.restore_folder_path_audio)));
                sb.append(File.separator);
                if (!file2.exists()) {
                    return "Er1";
                }
                sb.append(getFileName(this.listPhoto.get(i3).getPathPhoto()));
                File file4 = new File(sb.toString());
                try {
                    if (!file4.exists()) {
                        file3.mkdirs();
                    }
                    copy(file2, file4);
                    if (Build.VERSION.SDK_INT >= 19) {
                        Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                        intent2.setData(Uri.fromFile(file4));
                        this.mContext.sendBroadcast(intent2);
                    }
                    new MediaScanner(this.mContext, file4);
                    this.count = i3 + 1;
                    publishProgress(new Integer[]{Integer.valueOf(this.count)});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Thread.sleep(2000);
            return null;
        } catch (InterruptedException e2) {
            e2.printStackTrace();
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
        return str.substring(str.lastIndexOf("/") + 1);
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

        if (!this.delete) {
            this.tvNumber.setText(String.format(this.mContext.getString(R.string.restoring_number_format), new Object[]{numArr[0]}));
            return;
        }
        this.tvNumber.setText(String.format(this.mContext.getString(R.string.deleted_number_format), new Object[]{numArr[0]}));
    }
}
