package com.example.aiw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.aiw.R;

import java.util.HashMap;

public class ExchangeThreeActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    public static Activity activity;
    private final String[] SPINNER_LIST = {"선택", "공항 수령", "배달 수령"};

    private EditText editTextAddress1, editTextAddress2;
    private SpinnerAdapter spinnerAdapter;
    private Spinner spinner;
    private HashMap<String, Object> payment;
    private String deliveryOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_three);

        activity = ExchangeThreeActivity.this;

        editTextAddress1 = findViewById(R.id.exchange_three_address1);
        editTextAddress2= findViewById(R.id.exchange_three_address2);

        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, SPINNER_LIST);
        spinner = findViewById(R.id.exchange_three_option);
        spinner.setAdapter(spinnerAdapter);

        Intent intent = getIntent();
        payment = (HashMap<String, Object>) intent.getSerializableExtra("payment");

        findViewById(R.id.exchange_three_button).setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        //findViewById(R.id.exchange_three_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchange_three_button:
                payment.put("Receiver_Method", deliveryOption);
                if(deliveryOption.equals("배달 수령")) {
                    payment.put("Receiver_Address1", editTextAddress1.getText().toString());
                    payment.put("Receiver_Address2", editTextAddress2.getText().toString());
                }
                Intent intent = new Intent(this, ExchangeFourthActivity.class);
                intent.putExtra("payment", payment);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 2) {
                editTextAddress1.setText(data.getStringExtra("address"));
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deliveryOption = SPINNER_LIST[position];
        if (SPINNER_LIST[position].equals("배달 수령")) {
            startActivityForResult(new Intent(getApplicationContext(), AddressActivity.class), 1);
            editTextAddress1.setVisibility(View.VISIBLE);
            editTextAddress2.setVisibility(View.VISIBLE);
        }
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
