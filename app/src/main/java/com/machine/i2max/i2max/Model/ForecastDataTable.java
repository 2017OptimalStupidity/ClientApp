package com.machine.i2max.i2max.Model;

import io.realm.RealmObject;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class ForecastDataTable extends RealmObject {

    public int progressId;
    public double forecastedData;

    public double getForecastedData() {
        return forecastedData;
    }

    public void setForecastedData(double forecastedData) {
        this.forecastedData = forecastedData;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }
}
