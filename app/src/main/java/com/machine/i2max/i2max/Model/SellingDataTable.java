package com.machine.i2max.i2max.Model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class SellingDataTable extends RealmObject {

    public Date todaysDate;
    public double sellingData;

    public double getSellingData() {
        return sellingData;
    }

    public void setSellingData(double sellingData) {
        this.sellingData = sellingData;
    }

    public Date getTodaysDate() {
        return todaysDate;
    }

    public void setTodaysDate(Date todaysDate) {
        this.todaysDate = todaysDate;
    }

}
