package com.filerecovery.photorecovery.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demo.R;
import com.filerecovery.photorecovery.utilts.Utils;

public class RestoreResultActivity extends AppCompatActivity {
    String mName = "";
    String path = "";
    Toolbar toolbar;
    int type = 0;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_restore_result);
        intView();
        intData();
    }

    public void intView() {
        this.type = getIntent().getIntExtra("type", 0);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (this.type == 0) {
            this.mName = getString(R.string.photo_recovery);

            this.path = Utils.getPathSave(RestoreResultActivity.this, getString(R.string.restore_folder_path_photo));


        }
        if (this.type == 1) {
            this.mName = getString(R.string.video_recovery);

            this.path = Utils.getPathSave(RestoreResultActivity.this, getString(R.string.restore_folder_path_video));


        }
        if (this.type == 2) {
            this.mName = getString(R.string.audio_recovery);

            this.path = Utils.getPathSave(RestoreResultActivity.this, getString(R.string.restore_folder_path_audio));


        }
        this.toolbar.setTitle((CharSequence) this.mName);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void intData() {
        ((TextView) findViewById(R.id.tvStatus)).setText(String.valueOf(getIntent().getIntExtra("value", 0)));
        ((TextView) findViewById(R.id.tvPath)).setText("File Restored to\n/" + this.path);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
