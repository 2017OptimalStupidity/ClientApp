package com.machine.i2max.i2max.Control;

import android.content.Context;
import android.os.Bundle;

import com.machine.i2max.i2max.Model.ForecastDataTable;
import com.machine.i2max.i2max.Model.RealmDouble;
import com.machine.i2max.i2max.Model.RealmString;
import com.machine.i2max.i2max.Model.SellingDataTable;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_DATE;
import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_RESULT;
import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_STATUS;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_ERROR;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Settings.DefineManager.NOT_AVAILABLE;
import static com.machine.i2max.i2max.Settings.DefineManager.STATUS_WORKING;
import static com.machine.i2max.i2max.Settings.DefineManager.ZERO;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class RealmController {

    Context context;
    Realm realmInstance;
    RealmConfiguration realmConfiguration;

    public RealmController(Context context) {
        try {
            this.context = context;

            Realm.init(context);

            PrintLog("RealmController", "RealmController", "Init realm", LOG_LEVEL_INFO);

            realmConfiguration = new RealmConfiguration.Builder()
                    .schemaVersion(1)
                    .deleteRealmIfMigrationNeeded()
                    .build();

            Realm.setDefaultConfiguration(realmConfiguration);
            realmInstance = Realm.getInstance(realmConfiguration);
//            realmInstance = Realm.getDefaultInstance();

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

    public void AddForecastData(int processId) {
        PrintLog("RealmController", "AddForecastData", "Add new forecast data: " + processId, LOG_LEVEL_INFO);
        try {
            ForecastDataTable newForecastTable = new ForecastDataTable();
            newForecastTable.setProcessId(processId);
            newForecastTable.setStatus(STATUS_WORKING);

            realmInstance.beginTransaction();
            realmInstance.copyToRealm(newForecastTable);
            realmInstance.commitTransaction();

            PrintLog("RealmController", "AddForecastData", "new forecast table added: " + processId, LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            PrintLog("RealmController", "AddForecastData", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public void UpdateForecastData(int processId, Bundle recivedBundleData) {
        PrintLog("RealmController", "UpdateForecastData", "Update saved forecast data: " + processId, LOG_LEVEL_INFO);
    }

    public int GetLatestProcessId() {
        PrintLog("RealmController", "GetLatestProcessId", "Getting latest process id", LOG_LEVEL_INFO);
        try {
            RealmResults<ForecastDataTable> forecastDataTables = realmInstance.where(ForecastDataTable.class).findAllSorted("processId", Sort.DESCENDING);
            for(ForecastDataTable indexOfForecastData : forecastDataTables) {
                PrintLog("RealmController", "GetLatestProcessId", "saved request process id: " + indexOfForecastData.getProcessId(), LOG_LEVEL_INFO);
                return indexOfForecastData.getProcessId();
            }
        }
        catch (Exception err) {
            PrintLog("RealmController", "GetLatestProcessId", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
        return NOT_AVAILABLE;
    }

    public Bundle GetLatestForecastData() {
        PrintLog("RealmController", "GetLatestForecastData", "Getting latest forecast data", LOG_LEVEL_INFO);

        int latestProcessId = NOT_AVAILABLE, i;
        latestProcessId = GetLatestProcessId();

        try {
            PrintLog("RealmController", "GetLatestForecastData", "Getting forecast bundle data: " + latestProcessId, LOG_LEVEL_INFO);
            RealmResults<ForecastDataTable> forecastDataTables = realmInstance.where(ForecastDataTable.class).equalTo("processId", latestProcessId).findAll();

            for(ForecastDataTable indexOfForecastData : forecastDataTables) {

                Bundle forecastBundleData = new Bundle();
                forecastBundleData.putString(BUNDLE_STATUS, indexOfForecastData.getStatus());

                RealmList<RealmDouble> customRealmDoubleArray = indexOfForecastData.getForecastedData();
                double[] forecastResult = new double[customRealmDoubleArray.size()];

                for(i = ZERO; i < customRealmDoubleArray.size(); i += 1) {
                    forecastResult[i] = customRealmDoubleArray.get(i).getRealmDouble();
                }

                forecastBundleData.putDoubleArray(BUNDLE_RESULT, forecastResult);

                RealmList<RealmString> customRealmStringArray = indexOfForecastData.getForecastedDate();
                String[] forecastDate = new String[customRealmStringArray.size()];

                for(i = ZERO; i < customRealmStringArray.size(); i += 1) {
                    forecastDate[i] = customRealmStringArray.get(i).getDate();
                }

                forecastBundleData.putStringArray(BUNDLE_DATE, forecastDate);

                PrintLog("RealmController", "GetLatestForecastData", "forecast data package rdy", LOG_LEVEL_INFO);

                return forecastBundleData;
            }
        }
        catch (Exception err) {
            PrintLog("RealmController", "GetLatestForecastData", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
        return null;
    }
}
