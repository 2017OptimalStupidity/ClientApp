package com.machine.i2max.i2max.Model;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class UploadDataRequest {

    public double Data[];
    public int Day;

    public double[] getData() {
        return Data;
    }

    public void setData(double[] data) {
        Data = data;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }
}
