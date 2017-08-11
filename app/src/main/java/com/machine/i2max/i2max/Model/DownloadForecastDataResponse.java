package com.machine.i2max.i2max.Model;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class DownloadForecastDataResponse {

    public String Status;
    public Double Result[];
    public String Date[];

    public String[] getDate() {
        return Date;
    }

    public void setDate(String[] date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Double[] getResult() {
        return Result;
    }

    public void setResult(Double[] result) {
        Result = result;
    }
}
