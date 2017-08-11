package com.machine.i2max.i2max.Model;

import io.realm.RealmObject;

/**
 * Created by stories2 on 2017. 8. 11..
 */

public class RealmString extends RealmObject {

    public String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
