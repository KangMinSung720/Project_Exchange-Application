package com.example.aiw.Data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by Hooni on 2018-11-18
 */
public class CurrencyList {
    private static final CurrencyList currencyList = new CurrencyList();
    private List<Currency> list;

    public static CurrencyList getInstance() {
        return currencyList;
    }

    private CurrencyList() {
        list = new ArrayList<>();
    }

    public List<Currency> getList() {
        return list;
    }

    public void setList(Response<List<Currency>> response) {
        list = response.body();
        list.get(0).setName("위안");
        list.get(1).setName("유로");
        list.get(2).setName("엔");
        list.get(3).setName("미국 달러");
    }
}
