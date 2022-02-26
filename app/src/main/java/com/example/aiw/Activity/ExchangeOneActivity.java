package com.example.aiw.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.aiw.Data.CurrencyList;
import com.example.aiw.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class ExchangeOneActivity extends BaseActivity implements View.OnClickListener, Spinner.OnItemSelectedListener, TextView.OnEditorActionListener {
    private static final String TAG = CurrencyActivity.class.getSimpleName(); //현재 액티비티 디버깅 전용 문자열
    private static final String[] SPINNER_LIST = {"선택", "미국 달러", "유로", "엔", "위안"};  //스피너 목록

    public static Activity activity;

    private EditText editTextInput;
    private TextView textViewResult;
    private Button buttonNext;
    private SpinnerAdapter spinnerAdapter;
    private Spinner spinner;

    private double money_to_pay = 0;
    private String selected_currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_one);

        activity = ExchangeOneActivity.this;

        editTextInput = findViewById(R.id.exchange_one_input);
        editTextInput.setInputType(EditorInfo.IME_ACTION_DONE);
        textViewResult = findViewById(R.id.exchange_one_result);
        buttonNext = findViewById(R.id.exchange_one_button);
        buttonNext.setEnabled(false);
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, SPINNER_LIST);
        spinner = findViewById(R.id.exchange_one_currency);
        spinner.setAdapter(spinnerAdapter);

        buttonNext.setOnClickListener(this);
        editTextInput.setOnEditorActionListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchange_one_button:
                if (isValid()) {
                    HashMap<String, Object> payment = new HashMap<>();
                    payment.put("Receiver_Email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    payment.put("Exchange_Unit", selected_currency);
                    payment.put("Exchange_Amount", Integer.parseInt(editTextInput.getText().toString()));
                    payment.put("Exchange_Price", money_to_pay);

                    Intent intent = new Intent(this, ExchangeTwoActivity.class);
                    intent.putExtra("payment", payment);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
        }
    }

    public boolean isValid() {
        if (money_to_pay == 0 || String.valueOf(money_to_pay) == null) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림")
                .setMessage("환율 정보가 갱신됩니다. 괜찮습니까?")
                .setCancelable(false)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExchangeOneActivity.this.finish();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_currency = SPINNER_LIST[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                if (selected_currency.equals("선택")) {
                    break;
                }
                for (int i = 0; i < CurrencyList.getInstance().getList().size(); i++) {
                    if (CurrencyList.getInstance().getList().get(i).getName().equals(selected_currency)) {
                        if (!editTextInput.getText().toString().isEmpty()) {
                            money_to_pay = CurrencyList.getInstance().getList().get(i).getRate() * Integer.parseInt(editTextInput.getText().toString());
                            textViewResult.setText(String.format("결제해야할 금액은 %.2f원입니다", money_to_pay));
                            buttonNext.setVisibility(View.VISIBLE);
                            buttonNext.setEnabled(true);
                        } else {
                            editTextInput.setError("올바른 금액을 입력해주세요");
                            textViewResult.setText("");
                            buttonNext.setVisibility(View.INVISIBLE);
                            buttonNext.setEnabled(false);
                        }
                    }
                }
                break;
        }

        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
