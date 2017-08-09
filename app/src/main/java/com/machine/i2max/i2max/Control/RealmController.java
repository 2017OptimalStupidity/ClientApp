package com.machine.i2max.i2max.Control;

import android.content.Context;

import com.machine.i2max.i2max.Model.SellingDataTable;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_ERROR;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class RealmController {

    Context context;
    Realm realmInstance;

    public RealmController(Context context) {
        try {
            this.context = context;

            Realm.init(context);

            PrintLog("RealmController", "RealmController", "Init realm", LOG_LEVEL_INFO);

            realmInstance = Realm.getDefaultInstance();

            PrintLog("RealmController", "RealmController", "Getting realm instance", LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            PrintLog("RealmController", "RealmController", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public void AddNewTodayData(int todaySellingData) {
        Date date = new Date();
        SellingDataTable sellingDataTable = new SellingDataTable();
        sellingDataTable.setSellingData(todaySellingData);
        sellingDataTable.setTodaysDate(date);
        PrintLog("RealmController", "AddNewTodayData", "date: " + date + " today: " + todaySellingData, LOG_LEVEL_INFO);
        realmInstance.beginTransaction();
        realmInstance.copyToRealm(sellingDataTable);
        realmInstance.commitTransaction();
        PrintLog("RealmController", "AddNewTodayData", "data saved", LOG_LEVEL_INFO);
    }

    public ArrayList<SellingDataTable> GetSellingDatas() {
        ArrayList<SellingDataTable> storedSellingDatas = new ArrayList(realmInstance.where(SellingDataTable.class).findAll());
        PrintLog("RealmController", "GetSellingDatas", "Data len: " + storedSellingDatas.size(), LOG_LEVEL_INFO);
        return storedSellingDatas;
    }
}
