package com.example.jerusalemnewsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public class GlobalHelper {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity activity, String message) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(message);
            progressDialog.show();
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    progressDialog = null;
                }
            });
        }
    }

    public static void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}
