package com.yaqen.pharmacy;

import android.app.Application;

public class Globalv extends Application {

    private String name;

    public void set_name(String name) {
        this.name = name;
    }

    public String getname() {
        return name;
    }

}