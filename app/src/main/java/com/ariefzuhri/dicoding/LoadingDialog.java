package com.ariefzuhri.dicoding;

import android.app.Activity;
import android.app.AlertDialog;

import com.ariefzuhri.dicoding.databinding.DialogLoadingBinding;

public class LoadingDialog {

    private final Activity activity;
    private final AlertDialog dialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
        DialogLoadingBinding binding = DialogLoadingBinding.inflate(activity.getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(binding.getRoot());
        builder.setCancelable(true);
        dialog = builder.create();
    }

    public void show(){
        if (activity != null) {
            dialog.show();
        }
    }

    public void dismiss(){
        if (activity != null) {
            dialog.dismiss();
        }
    }
}
