package com.example.aiw.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.aiw.Activity.BaseActivity;
import com.example.aiw.Activity.SignInActivity;
import com.example.aiw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SignInActivity.class.getSimpleName(); //디버깅용
    private EditText editTextEmail, editTextPassword, editTextPasswordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.signUp_email);
        editTextPassword = findViewById(R.id.signUp_password);
        editTextPasswordCheck = findViewById(R.id.signUp_password_check);

        findViewById(R.id.signUp_signUp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_signUp:
                signUp();
                break;
        }
    }

    // signUp() - 사용자가 입력한 이메일과 비밀번호로 파이어베이스에 회원가입하는 메소드이다.

    public void signUp() {
        if(!isValid()) {
            return;
        }

        showProgress("회원가입 중");

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgress("회원가입 성공");
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                hideProgress("이미 가입되어있는 이메일입니다.");
                            } catch (Exception e) {
                                hideProgress("회원가입 실패");
                                Log.e(TAG, e.getLocalizedMessage());
                            }
                        }
                    }
                });
    }

    //  isValid() - 사용자가 입력한 정보가 유효한지 검사하고 유효하면 참을, 유효하지 않으면 거짓을 반환한다.

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
        if (!TextUtils.isEmpty(editTextPassword.getText().toString()) && TextUtils.isEmpty(editTextPasswordCheck.getText().toString())) {
            editTextPasswordCheck.setError("비밀번호가 동일하지 않습니다.");
            valid = false;
        }

        return valid;
    }

    //화면 전환 효과 및 특정 이벤트 발생을 위해서 아래 메소드들을 오버라이드했다.

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
