package com.example.aiw.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.aiw.APIService;
import com.example.aiw.Data.Currency;
import com.example.aiw.Data.CurrencyList;
import com.example.aiw.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = CurrencyActivity.class.getSimpleName(); //디버깅용
    private TextView textViewDollar, textViewEuro, textViewYen, textViewYuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        textViewDollar = findViewById(R.id.currency_dollar_rate);
        textViewEuro = findViewById(R.id.currency_euro_rate);
        textViewYen = findViewById(R.id.currency_yen_rate);
        textViewYuan = findViewById(R.id.currency_yuan_rate);

        findViewById(R.id.currency_reservation).setOnClickListener(this);
        findViewById(R.id.currency_my_menu).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        getCurrency();
        super.onResume();
    }

    // getCurrency() - Retrofit을 이용해 지정한 API로부터 환율 정보를 받아오는 메소드이다.

    public void getCurrency() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.manana.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<Currency>> call = apiService.call();

        call.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                if (response.isSuccessful()) {
                    CurrencyList.getInstance().setList(response);
                    textViewYuan.setText(String.format("%.2f원", response.body().get(0).getRate()));
                    textViewEuro.setText(String.format("%.2f원", response.body().get(1).getRate()));
                    textViewYen.setText(String.format("%.2f원", response.body().get(2).getRate() * 100));
                    textViewDollar.setText(String.format("%.2f원", response.body().get(3).getRate()));
                }
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currency_reservation:
                startActivity(new Intent(getApplicationContext(), ExchangeOneActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
            case R.id.currency_my_menu:
                startActivity(new Intent(this, MyMenuActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;
        }
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
