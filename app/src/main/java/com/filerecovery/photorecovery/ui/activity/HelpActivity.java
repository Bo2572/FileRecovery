package com.filerecovery.photorecovery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;

public class HelpActivity extends AppCompatActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_help);
    }

    public void onStartClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
