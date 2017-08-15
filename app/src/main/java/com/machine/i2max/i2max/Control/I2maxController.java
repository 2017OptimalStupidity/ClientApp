package com.machine.i2max.i2max.Control;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.machine.i2max.i2max.Core.NetworkManager;
import com.machine.i2max.i2max.Model.SellingDataTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_DATE;
import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_RESULT;
import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_STATUS;
import static com.machine.i2max.i2max.Settings.DefineManager.DISABLE_PULLING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.FORECAST_DATA_RECEIVED;
import static com.machine.i2max.i2max.Settings.DefineManager.INVISIBLE_LOADING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_ERROR;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_WARN;
import static com.machine.i2max.i2max.Settings.DefineManager.STATUS_DONE;
import static com.machine.i2max.i2max.Settings.DefineManager.STATUS_WORKING;
import static com.machine.i2max.i2max.Settings.DefineManager.VISIBLE_LOADING_PROGRESS;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class I2maxController {

    Handler handlingWithController;
    NetworkManager networkManager;
    LineChartView lineChart;

    public I2maxController(Handler handlingWithController) {
        this.handlingWithController = handlingWithController;

        networkManager = new NetworkManager(handlingWithNetworkManager);

        PrintLog("I2maxController", "I2maxController", "Init controller", LOG_LEVEL_INFO);
    }

    public I2maxController(Handler handlingWithController, LineChartView lineChart) {
        this.handlingWithController = handlingWithController;
        this.lineChart = lineChart;

        networkManager = new NetworkManager(handlingWithNetworkManager);

        PrintLog("I2maxController", "I2maxController", "Init controller", LOG_LEVEL_INFO);
    }

    public void UploadData(ArrayList<SellingDataTable> sellingDataList, int forecastDay) {
        int sizeOfSellingData = sellingDataList.size(), i;
        double[] eachDaysOfSellingData = new double[sizeOfSellingData];
        String[] eachDaysOfSellingDate = new String[sizeOfSellingData];
        PrintLog("I2maxController", "UploadData", "size of selling data: " + sizeOfSellingData, LOG_LEVEL_INFO);
        if(forecastDay >= sizeOfSellingData || forecastDay <= 0) {
            PrintLog("I2maxController", "UploadData", "Wrong forcast day accepted", LOG_LEVEL_WARN);
            return;
        }

        PrintLog("I2maxController", "UploadData", "preparing to upload data", LOG_LEVEL_INFO);
        for(i = 0; i < sizeOfSellingData; i += 1) {
            eachDaysOfSellingData[i] = sellingDataList.get(i).getSellingData();
            Date indexOfDate = sellingDataList.get(i).getTodaysDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            eachDaysOfSellingDate[i] = dateFormat.format(indexOfDate).toString();
        }
        PrintLog("I2maxController", "UploadData", "uploading process start", LOG_LEVEL_INFO);
        networkManager.UploadDataProcess(eachDaysOfSellingData, eachDaysOfSellingDate, forecastDay);
    }

    public void PullingData(int processId) {
        networkManager.DownloadForecastProcess(processId);
    }

    Handler handlingWithNetworkManager = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VISIBLE_LOADING_PROGRESS:
                    handlingWithController.sendEmptyMessage(VISIBLE_LOADING_PROGRESS);
                    break;
                case INVISIBLE_LOADING_PROGRESS:
                    handlingWithController.sendEmptyMessage(INVISIBLE_LOADING_PROGRESS);
                    break;
                case DISABLE_PULLING_PROGRESS:
                    handlingWithController.sendEmptyMessage(DISABLE_PULLING_PROGRESS);
                    break;
                case FORECAST_DATA_RECEIVED:
                    UpdateLineChartView(msg.getData());
                default:
                    break;
            }
        }
    };

    public void UpdateLineChartView(Bundle forecastBundleData) {
        String status = forecastBundleData.getString(BUNDLE_STATUS);
        String[] date = forecastBundleData.getStringArray(BUNDLE_DATE);
        double[] result = forecastBundleData.getDoubleArray(BUNDLE_RESULT);
        float[] forecastResult = new float[result.length];

        int i;
        for(i = 0; i < result.length; i += 1) {
            forecastResult[i] = (float)result[i];
        }

        switch (status) {
            case STATUS_WORKING:
                PrintLog("I2maxController", "UpdateLineChartView", "Process still working", LOG_LEVEL_INFO);
                break;
            case STATUS_DONE:
                try {
                    LineSet dataSet = new LineSet(date, forecastResult);
                    lineChart.addData(dataSet);
                    lineChart.show();
                }
                catch (Exception err) {
                    PrintLog("I2maxController", "UpdateLineChartView", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                }

                PrintLog("I2maxController", "UpdateLineChartView", "Process done", LOG_LEVEL_INFO);
                break;
            default:
                PrintLog("I2maxController", "UpdateLineChartView", "Wait a minute. what is it?: " + status, LOG_LEVEL_WARN);
                break;
        }
    }
}
