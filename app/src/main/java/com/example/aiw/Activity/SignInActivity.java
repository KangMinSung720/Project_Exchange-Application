package com.example.aiw.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.aiw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = findViewById(R.id.signIn_email);
        editTextPassword = findViewById(R.id.signIn_password);

        findViewById(R.id.signIn_signIn).setOnClickListener(this);
        findViewById(R.id.signIn_signUp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn_signIn:
                signIn();
                break;
            case R.id.signIn_signUp:
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }
    }

    public void signIn() {
        if (!isValid()) {
            return;
        }

        showProgress("로그인 중");

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgress("로그인 성공");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                hideProgress("로그인 실패");
                                Log.e(TAG, e.getLocalizedMessage());
                            }
                        }
                    }
                });
    }

    public boolean isValid() {
        Pattern emailPattern = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
        Matcher matcher = emailPattern.matcher(editTextEmail.getText().toString());

        boolean valid = true;

        if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            editTextEmail.setError("이메일을 입력해주세요.");
            valid = false;
        }
        if (!editTextEmail.getText().toString().isEmpty() && !matcher.matches()) {
            editTextEmail.setError("유효한 이메일을 입력해주세요.");
            valid = false;
        }
        if (editTextPassword.getText().toString().length() < 6) {
            editTextPassword.setError("비밀번호는 6자리 이상 입력해주세요.");
            valid = false;
        }
        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError("비밀번호를 입력해주세요.");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림")
                .setMessage("애플리케이션을 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignInActivity.this.finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
