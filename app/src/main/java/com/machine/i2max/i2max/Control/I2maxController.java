package com.machine.i2max.i2max.Control;

import android.os.Handler;
import android.os.Message;

import com.machine.i2max.i2max.Core.NetworkManager;
import com.machine.i2max.i2max.Model.SellingDataTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.machine.i2max.i2max.Settings.DefineManager.INVISIBLE_LOADING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_WARN;
import static com.machine.i2max.i2max.Settings.DefineManager.VISIBLE_LOADING_PROGRESS;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class I2maxController {

    Handler handlingWithController;
    NetworkManager networkManager;

    public I2maxController(Handler handlingWithController) {
        this.handlingWithController = handlingWithController;

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
                default:
                    break;
            }
        }
    };
}
