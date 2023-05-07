package com.filerecovery.photorecovery.ui.activity;

import android.app.Dialog;
import android.content.Context;

import com.example.demo.R;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(1);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_loading_dialog);
    }
}
