package com.machine.i2max.i2max.Model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by stories2 on 2017. 8. 11..
 */

public class RealmDate extends RealmObject {

    public Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
