package com.example.aiw.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.aiw.R;

public class BaseActivity extends Activity {
    ProgressDialog progressDialog;

    public void showProgress(String message) {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
        }

        progressDialog.show();
    }

    public void hideProgress(String message) {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
