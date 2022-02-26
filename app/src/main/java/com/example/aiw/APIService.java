package com.example.aiw;

import com.example.aiw.Data.Currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hooni on 2018-11-18
 */
public interface APIService {
    @GET("exchange/rate/KRW/USD,EUR,JPY,CNY.json")
    Call<List<Currency>> call();
}
