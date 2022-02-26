package com.example.aiw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.aiw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class ExchangeFourthActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    private final String[] SPINNER_LIST = {"선택", "무통장입금", "계좌이체", "삼성페이", "카카오페이", "네이버페이"};

    private SpinnerAdapter spinnerAdapter;
    private Spinner spinner;

    private HashMap<String, Object> payment;
    private String paymentOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_fourth);

        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, SPINNER_LIST);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);

        Intent intent = getIntent();
        payment = (HashMap<String, Object>) intent.getSerializableExtra("payment");

        findViewById(R.id.exchange_fourth_button).setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    public void addOrder() {
        FirebaseFirestore.getInstance().collection("Exchange_List").document().set(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                    ExchangeOneActivity.activity.finish();
                    ExchangeTwoActivity.activity.finish();
                    ExchangeThreeActivity.activity.finish();
                    Toast.makeText(getApplicationContext(), "환전 예약이 완료됐습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!paymentOption.equals("선택")) {
            payment.put("Payment_Method", paymentOption);
            payment.put("Status_Payment", "결제 완료");
            payment.put("Status_Receive", "수령 미완료");
            payment.put("", new Date());
            addOrder();
        } else {
            Toast.makeText(getApplicationContext(), "결제방식을 선택해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        paymentOption = SPINNER_LIST[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
