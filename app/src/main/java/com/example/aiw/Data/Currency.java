package com.example.aiw.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hooni on 2018-11-18
 */
public class Currency {
    @SerializedName("name")
    private String name;

    @SerializedName("rate")
    private double rate;

    @SerializedName("date")
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }
}
