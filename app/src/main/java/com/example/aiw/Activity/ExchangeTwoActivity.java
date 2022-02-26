package com.example.aiw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aiw.R;

import java.util.HashMap;

public class ExchangeTwoActivity extends Activity implements View.OnClickListener {
    public static Activity activity;

    private HashMap<String, Object> payment;
    private EditText editTextName, editTextID, editTextPhone;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_two);

        activity = ExchangeTwoActivity.this;

        editTextName = findViewById(R.id.exchange_two_name);
        editTextID = findViewById(R.id.exchange_two_id);
        editTextPhone = findViewById(R.id.exchange_two_phone);
        buttonNext = findViewById(R.id.exchange_two_button);

        buttonNext.setOnClickListener(this);

        Intent intent = getIntent();
        payment = (HashMap<String, Object>) intent.getSerializableExtra("payment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchange_two_button:
                payment.put("Receiver_Name", editTextName.getText().toString());
                payment.put("Receiver_ID", editTextID.getText().toString());
                payment.put("Receiver_Phone", editTextPhone.getText().toString());

                Intent intent = new Intent(getApplicationContext(), ExchangeThreeActivity.class);
                intent.putExtra("payment", payment);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }
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
