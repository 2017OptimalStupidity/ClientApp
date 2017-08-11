package com.machine.i2max.i2max.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class ForecastDataTable extends RealmObject {

    public int progressId;
    public RealmList<RealmDouble> forecastedData;
    public RealmList<RealmString> forecastedDate;

    public RealmList<RealmDouble> getForecastedData() {
        return forecastedData;
    }

    public void setForecastedData(RealmList<RealmDouble> forecastedData) {
        this.forecastedData = forecastedData;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }
}
