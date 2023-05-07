package com.filerecovery.photorecovery.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import com.example.demo.R;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.AlbumAudioActivity;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AlbumAudio;
import com.filerecovery.photorecovery.model.modul.recoveryaudio.Model.AudioModel;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.AlbumPhotoActivity;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.Model.AlbumPhoto;
import com.filerecovery.photorecovery.model.modul.recoveryphoto.Model.PhotoModel;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.AlbumVideoActivity;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.AlbumVideo;
import com.filerecovery.photorecovery.model.modul.recoveryvideo.Model.VideoModel;
import com.filerecovery.photorecovery.utilts.SliderItem;
import com.filerecovery.photorecovery.utilts.Utils;
import com.romainpiel.shimmer.Shimmer;
import com.skyfishjy.library.RippleBackground;


import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 100;
    public static ArrayList<AlbumAudio> mAlbumAudio = new ArrayList<>();
    public static ArrayList<AlbumPhoto> mAlbumPhoto = new ArrayList<>();
    public static ArrayList<AlbumVideo> mAlbumVideo = new ArrayList<>();
    private ArrayList<String> arrPermission;
    ImageButton btnScan;
    CardView cvAudio;
    CardView cvImage;
    CardView cvVideo;
    ScanAsyncTask mScanAsyncTask;
    private ArrayList<SliderItem> mSliderItems = new ArrayList<>();
    RippleBackground rippleBackground;
    TextView tvNumber;

    CardView cvRate;
    CardView cvShare;
    CardView cvPP;

    public void intData() {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        intView();
        intData();
        intEvent();
        checkPermission();

    }


    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            File file = new File(Utils.getPathSave(this, "RestoreAudio"));
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(Utils.getPathSave(this, "RestoreVideo"));
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(Utils.getPathSave(this, "RestorePhoto"));
            if (!file3.exists()) {
                file3.mkdirs();
            }
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || !ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 100);
        }
    }

    public void intView() {
        this.btnScan = (ImageButton) findViewById(R.id.btnScan);
        this.tvNumber = (TextView) findViewById(R.id.tvNumber);
        this.rippleBackground = (RippleBackground) findViewById(R.id.im_scan_bg);
        this.cvImage = (CardView) findViewById(R.id.cvImage);
        this.cvAudio = (CardView) findViewById(R.id.cvAudio);
        this.cvVideo = (CardView) findViewById(R.id.cvVideo);
        this.cvRate = (CardView) findViewById(R.id.rateapp);
        this.cvShare = (CardView) findViewById(R.id.shareapp);
        this.cvPP = (CardView) findViewById(R.id.privacy);

    }

    public void intEvent() {
        this.cvImage.setOnClickListener(this);
        this.cvAudio.setOnClickListener(this);
        this.cvVideo.setOnClickListener(this);
        this.cvRate.setOnClickListener(this);
        this.cvShare.setOnClickListener(this);
        this.cvPP.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cvAudio:
                scanType(2);
                return;
            case R.id.cvImage:
                scanType(0);
                return;

            case R.id.cvVideo:
                scanType(1);
                return;
            case R.id.rateapp:
                rateapp();

                return;
            case R.id.shareapp:
                shareapp();

                return;
            case R.id.privacy:
                privacy();
                return;

            default:
                return;
        }
    }

    void privacy() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.google.com/"));
        startActivity(i);

    }

    void shareapp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            String sAux = "\n Recovery File\n\n";
            sAux = sAux
                    + "https://play.google.com/store/apps/details?id="
                    + getPackageName() + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception back5) {
        }

    }

    void rateapp() {

        Intent rate = new Intent("android.intent.action.VIEW", Uri
                .parse("market://details?id=" + getPackageName()));
        try {
            startActivity(rate);

        } catch (ActivityNotFoundException back5) {
            Toast.makeText(
                    MainActivity.this,
                    "You don't have Google Play installed or Internet connection",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void scanType(int i) {
        ScanAsyncTask scanAsyncTask = this.mScanAsyncTask;
        if (scanAsyncTask == null || scanAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
            mAlbumAudio.clear();
            mAlbumPhoto.clear();
            mAlbumVideo.clear();
            this.tvNumber.setVisibility(View.VISIBLE);
            this.tvNumber.setText(getString(R.string.analyzing));
            this.rippleBackground.startRippleAnimation();
            this.mScanAsyncTask = new ScanAsyncTask(i);
            this.mScanAsyncTask.execute(new Void[0]);
            return;
        }
        Toast.makeText(this, getString(R.string.scan_wait), Toast.LENGTH_LONG).show();
    }

    public class ScanAsyncTask extends AsyncTask<Void, Integer, Void> {
        ArrayList<AudioModel> listAudio = new ArrayList<>();
        ArrayList<PhotoModel> listPhoto = new ArrayList<>();
        ArrayList<VideoModel> listVideo = new ArrayList<>();
        int number = 0;
        int typeScan = 0;

        public ScanAsyncTask(int i) {
            this.typeScan = i;
        }


        public void onPreExecute() {
            super.onPreExecute();
            this.number = 0;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            MainActivity.this.rippleBackground.stopRippleAnimation();
            MainActivity.this.tvNumber.setText("");
            MainActivity.this.tvNumber.setVisibility(View.INVISIBLE);
            if (this.typeScan == 0) {
                if (MainActivity.mAlbumPhoto.size() == 0) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), NoFileActiviy.class));
                } else {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), AlbumPhotoActivity.class));
                }
            }
            if (this.typeScan == 1) {
                if (MainActivity.mAlbumVideo.size() == 0) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), NoFileActiviy.class));
                } else {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), AlbumVideoActivity.class));
                }
            }
            if (this.typeScan != 2) {
                return;
            }
            if (MainActivity.mAlbumAudio.size() == 0) {
                MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), NoFileActiviy.class));
                return;
            }
            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), AlbumAudioActivity.class));
        }


        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            TextView textView = MainActivity.this.tvNumber;
            textView.setText("Files: " + String.valueOf(numArr[0]));
        }

        public Void doInBackground(Void... voidArr) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            File file = new File(absolutePath);
            Log.e("absolutePath =", "" + absolutePath);
            String parentPath = file.getAbsoluteFile().getParent();
            absolutePath = parentPath;
            Log.e("absolutePath 1=", "" + absolutePath);


            StringBuilder sb = new StringBuilder();
            sb.append("root = ");
            sb.append(absolutePath);
            if (this.typeScan == 0) {
                try {
                    getSdCardImage();

                    Log.e("ssss", "" + Utils.getFileList(absolutePath).length);


                    checkFileOfDirectoryImage(absolutePath, Utils.getFileList(absolutePath));
                } catch (Exception unused) {
                }
                Collections.sort(MainActivity.mAlbumPhoto, new Comparator<AlbumPhoto>() {
                    public int compare(AlbumPhoto albumPhoto, AlbumPhoto albumPhoto2) {
                        return Long.valueOf(albumPhoto2.getLastModified()).compareTo(Long.valueOf(albumPhoto.getLastModified()));
                    }
                });
            }
            if (this.typeScan == 1) {
                getSdCardVideo();
                checkFileOfDirectoryVideo(absolutePath, Utils.getFileList(absolutePath));
                Collections.sort(MainActivity.mAlbumVideo, new Comparator<AlbumVideo>() {
                    public int compare(AlbumVideo albumVideo, AlbumVideo albumVideo2) {
                        return Long.valueOf(albumVideo2.getLastModified()).compareTo(Long.valueOf(albumVideo.getLastModified()));
                    }
                });
            }
            if (this.typeScan == 2) {
                try {
                    getSdCardAudio();
                    checkFileOfDirectoryAudio(absolutePath, Utils.getFileList(absolutePath));
                } catch (Exception unused2) {
                }
                Collections.sort(MainActivity.mAlbumAudio, new Comparator<AlbumAudio>() {
                    public int compare(AlbumAudio albumAudio, AlbumAudio albumAudio2) {
                        return Long.valueOf(albumAudio2.getLastModified()).compareTo(Long.valueOf(albumAudio.getLastModified()));
                    }
                });
            }
            try {
                Thread.sleep(3000);
                return null;
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        public void checkFileOfDirectoryImage(String str, File[] fileArr) {
            int i;
            String str2 = str;
            File[] fileArr2 = fileArr;
            MainActivity mainActivity = MainActivity.this;
            boolean contains = str2.contains(Utils.getPathSave(mainActivity, mainActivity.getString(R.string.restore_folder_path_photo)));
            boolean z = false;
            int i2 = 0;
            while (i2 < fileArr2.length) {
                if (fileArr2[i2].isDirectory()) {
                    String path = fileArr2[i2].getPath();
                    File[] fileList = Utils.getFileList(fileArr2[i2].getPath());
                    if (!(path == null || fileList == null || fileList.length <= 0)) {
                        checkFileOfDirectoryImage(path, fileList);
                    }
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inSampleSize = 8;
                    options.inDither = z;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inTempStorage = new byte[32768];
                    BitmapFactory.decodeFile(fileArr2[i2].getPath(), options);
                    if (!(contains || options.outWidth == -1 || options.outHeight == -1)) {
                        File file = new File(fileArr2[i2].getPath());
                        if (isImageFile(file)) {
                            int parseInt = Integer.parseInt(String.valueOf(file.length()));
                            ArrayList<PhotoModel> arrayList = this.listPhoto;
                            String path2 = fileArr2[i2].getPath();
                            long lastModified = file.lastModified();
                            i = i2;
                            PhotoModel photoModel2 = new PhotoModel(path2, lastModified, (long) parseInt);
                            arrayList.add(photoModel2);
                            this.number++;
                            publishProgress(new Integer[]{Integer.valueOf(this.number)});
                        } else {
                            i = i2;
                            int parseInt2 = Integer.parseInt(String.valueOf(file.length()));
                            if (parseInt2 > 1000) {
                                this.listPhoto.add(new PhotoModel(fileArr2[i].getPath(), file.lastModified(), (long) parseInt2));
                                this.number++;
                                publishProgress(new Integer[]{Integer.valueOf(this.number)});
                                i2 = i + 1;
                                z = false;
                            }
                        }
                        i2 = i + 1;
                        z = false;
                    }
                }
                i = i2;
                i2 = i + 1;
                z = false;
            }
            if (this.listPhoto.size() != 0) {
                AlbumPhoto albumPhoto = new AlbumPhoto();
                albumPhoto.setStr_folder(str2);
                albumPhoto.setLastModified(new File(str2).lastModified());
                Collections.sort(this.listPhoto, new Comparator<PhotoModel>() {
                    public int compare(PhotoModel photoModel, PhotoModel photoModel2) {
                        return Long.valueOf(photoModel2.getLastModified()).compareTo(Long.valueOf(photoModel.getLastModified()));
                    }
                });
                albumPhoto.setListPhoto((ArrayList) this.listPhoto.clone());
                MainActivity.mAlbumPhoto.add(albumPhoto);
            }
            this.listPhoto.clear();
        }

        public boolean isImageFile(File file) {
            String mimeType = MainActivity.this.getMimeType(file);
            return mimeType != null && mimeType.startsWith("image");
        }

        public boolean isVideoFile(File file) {
            String mimeType = MainActivity.this.getMimeType(file);
            return mimeType != null && (mimeType.equals("application/x-mpegURL") || mimeType.startsWith("video"));
        }

        public boolean isAudioFile(File file) {
            String mimeType = MainActivity.this.getMimeType(file);
            return mimeType != null && mimeType.startsWith("audio");
        }

        public void getSdCardImage() {
            String[] externalStorageDirectories = MainActivity.this.getExternalStorageDirectories();
            if (externalStorageDirectories != null && externalStorageDirectories.length > 0) {
                for (String str : externalStorageDirectories) {
                    File file = new File(str);
                    if (file.exists()) {
                        checkFileOfDirectoryImage(str, file.listFiles());
                    }
                }
            }
        }

        public void checkFileOfDirectoryVideo(String str, File[] fileArr) {
            String str2 = str;
            File[] fileArr2 = fileArr;
            MainActivity mainActivity = MainActivity.this;
            boolean contains = str2.contains(Utils.getPathSave(mainActivity, mainActivity.getString(R.string.restore_folder_path_video)));
            if (fileArr2 != null) {
                for (int i = 0; i < fileArr2.length; i++) {
                    if (fileArr2[i].isDirectory()) {
                        String path = fileArr2[i].getPath();
                        File[] fileList = Utils.getFileList(fileArr2[i].getPath());
                        if (!(path == null || fileList == null || fileList.length <= 0)) {
                            checkFileOfDirectoryVideo(path, fileList);
                        }
                    } else {
                        File file = new File(fileArr2[i].getPath());
                        if (!contains && isVideoFile(file)) {
                            String substring = fileArr2[i].getPath().substring(fileArr2[i].getPath().lastIndexOf(".") + 1);
                            long j = 0;
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            try {
                                mediaMetadataRetriever.setDataSource(file.getPath());
                                j = Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
                                mediaMetadataRetriever.release();
                            } catch (Exception unused) {
                            }
                            ArrayList<VideoModel> arrayList = this.listVideo;
                            VideoModel videoModel2 = new VideoModel(fileArr2[i].getPath(), file.lastModified(), file.length(), substring, Utils.convertDuration(j));
                            arrayList.add(videoModel2);
                            this.number++;
                            publishProgress(new Integer[]{Integer.valueOf(this.number)});
                        }
                    }
                }
                if (this.listVideo.size() != 0) {
                    AlbumVideo albumVideo = new AlbumVideo();
                    albumVideo.setStr_folder(str2);
                    albumVideo.setLastModified(new File(str2).lastModified());
                    Collections.sort(this.listVideo, new Comparator<VideoModel>() {
                        public int compare(VideoModel videoModel, VideoModel videoModel2) {
                            return Long.valueOf(videoModel2.getLastModified()).compareTo(Long.valueOf(videoModel.getLastModified()));
                        }
                    });
                    albumVideo.setListPhoto((ArrayList) this.listVideo.clone());
                    MainActivity.mAlbumVideo.add(albumVideo);
                }
                this.listVideo.clear();
            }
        }

        public void getSdCardVideo() {
            String[] externalStorageDirectories = MainActivity.this.getExternalStorageDirectories();
            if (externalStorageDirectories != null && externalStorageDirectories.length > 0) {
                for (String str : externalStorageDirectories) {
                    File file = new File(str);
                    if (file.exists()) {
                        checkFileOfDirectoryVideo(str, file.listFiles());
                    }
                }
            }
        }

        public void checkFileOfDirectoryAudio(String str, File[] fileArr) {
            int parseInt;
            MainActivity mainActivity = MainActivity.this;
            boolean contains = str.contains(Utils.getPathSave(mainActivity, mainActivity.getString(R.string.restore_folder_path_audio)));
            for (int i = 0; i < fileArr.length; i++) {
                if (fileArr[i].isDirectory()) {
                    String path = fileArr[i].getPath();
                    File[] fileList = Utils.getFileList(fileArr[i].getPath());
                    if (!(path == null || fileList == null || fileList.length <= 0)) {
                        checkFileOfDirectoryAudio(path, fileList);
                    }
                } else {
                    File file = new File(fileArr[i].getPath());
                    if (!contains && isAudioFile(file) && (parseInt = Integer.parseInt(String.valueOf(file.length()))) > 10000) {
                        this.listAudio.add(new AudioModel(fileArr[i].getPath(), file.lastModified(), (long) parseInt));
                        this.number++;
                        publishProgress(new Integer[]{Integer.valueOf(this.number)});
                    }
                }
            }
            if (this.listAudio.size() != 0) {
                AlbumAudio albumAudio = new AlbumAudio();
                albumAudio.setStr_folder(str);
                albumAudio.setLastModified(new File(str).lastModified());
                Collections.sort(this.listAudio, new Comparator<AudioModel>() {
                    public int compare(AudioModel audioModel, AudioModel audioModel2) {
                        return Long.valueOf(audioModel2.getLastModified()).compareTo(Long.valueOf(audioModel.getLastModified()));
                    }
                });
                albumAudio.setListPhoto((ArrayList) this.listAudio.clone());
                MainActivity.mAlbumAudio.add(albumAudio);
            }
            this.listAudio.clear();
        }

        public void getSdCardAudio() {
            String[] externalStorageDirectories = MainActivity.this.getExternalStorageDirectories();
            if (externalStorageDirectories != null && externalStorageDirectories.length > 0) {
                for (String str : externalStorageDirectories) {
                    File file = new File(str);
                    if (file.exists()) {
                        checkFileOfDirectoryAudio(str, file.listFiles());
                    }
                }
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (iArr.length <= 0 || iArr[i2] != 0) {
                    Toast.makeText(this, getString(R.string.need_perms), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    File file = new File(Utils.getPathSave(this, "RestoreAudio"));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(Utils.getPathSave(this, "RestoreVideo"));
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    File file3 = new File(Utils.getPathSave(this, "RestorePhoto"));
                    if (!file3.exists()) {
                        file3.mkdirs();
                    }

                    MangeFile();

                }
            }
        }
    }

    private void showNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) getString(R.string.not_found_audio));
        builder.setPositiveButton((CharSequence) getString(17039370), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = builder.create();
        if (!isFinishing()) {
            create.show();
        }
    }


    public String[] getExternalStorageDirectories() {
        File[] externalFilesDirs;
        String[] split;
        boolean z;
        ArrayList arrayList = new ArrayList();

        if (Build.VERSION.SDK_INT >= 19 && (externalFilesDirs = getExternalFilesDirs("0")) != null && externalFilesDirs.length > 0) {
            for (File file : externalFilesDirs) {
                if (!(file == null || (split = file.getPath().split("/Android")) == null || split.length <= 0)) {
                    String str = split[0];
                    if (Build.VERSION.SDK_INT >= 21) {
                        z = Environment.isExternalStorageRemovable(file);
                    } else {
                        z = "mounted".equals(EnvironmentCompat.getStorageState(file));
                    }
                    if (z) {
                        arrayList.add(str);
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            String str2 = "";
            try {
                Process start = new ProcessBuilder(new String[0]).command(new String[]{"mount | grep /dev/block/vold"}).redirectErrorStream(true).start();
                start.waitFor();
                InputStream inputStream = start.getInputStream();
                byte[] bArr = new byte[1024];
                while (inputStream.read(bArr) != -1) {
                    str2 = str2 + new String(bArr);
                }
                inputStream.close();
            } catch (Exception unused) {

            }
            if (!str2.trim().isEmpty()) {
                String[] split2 = str2.split(IOUtils.LINE_SEPARATOR_UNIX);
                if (split2.length > 0) {
                    for (String split3 : split2) {
                        arrayList.add(split3.split(" ")[2]);
                    }
                }
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            strArr[i] = (String) arrayList.get(i);
        }
        return strArr;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }

    private void checkPermission() {
        this.arrPermission = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {





            if (!Utils.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                this.arrPermission.add("android.permission.WRITE_EXTERNAL_STORAGE");
            } else if (!Utils.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                this.arrPermission.add("android.permission.READ_EXTERNAL_STORAGE");
            }

            if (!this.arrPermission.isEmpty()) {
                requestPermissions((String[]) this.arrPermission.toArray(new String[0]), 100);
            } else {
                MangeFile();

            }
        }
    }

    AlertDialog dialog;

    Button ok_click;


    void Show_Diloge(Context con) {
        View alertCustomdialog = LayoutInflater.from(con).inflate(R.layout.mange_file_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(con);
        ok_click = alertCustomdialog.findViewById(R.id.ok_btn);
        alert.setView(alertCustomdialog);
        dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();


        ok_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    void MangeFile() {

        String sdk_version_number = android.os.Build.VERSION.SDK;
        Log.e("sdk_version_number", "" + sdk_version_number);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {

            } else {


                Show_Diloge(MainActivity.this);

            }


        } else {


        }


    }

    public void onBackPressed() {
        ScanAsyncTask scanAsyncTask = this.mScanAsyncTask;
        if (scanAsyncTask != null && scanAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            Toast.makeText(this, getString(R.string.scan_wait), Toast.LENGTH_LONG).show();
        }
    }

    public String getMimeType(File file) {
        Uri fromFile = Uri.fromFile(file);
        if (fromFile.getScheme().equals("content")) {
            return getContentResolver().getType(fromFile);
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fromFile.toString()).toLowerCase());
    }


    public void goURL(String str) {
        Matcher matcher = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*").matcher(str);
        if (!matcher.find()) {
            String group = matcher.group();
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("vnd.youtube:" + group));
            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youtube.com/watch?v=" + group));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                startActivity(intent2);
            }
        } else {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        }
    }
}
