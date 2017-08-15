package com.machine.i2max.i2max.Core;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.machine.i2max.i2max.Model.DownloadForecastDataRequest;
import com.machine.i2max.i2max.Model.DownloadForecastDataResponse;
import com.machine.i2max.i2max.Model.UploadDataRequest;
import com.machine.i2max.i2max.Model.UploadDataResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_DATE;
import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_RESULT;
import static com.machine.i2max.i2max.Settings.DefineManager.BUNDLE_STATUS;
import static com.machine.i2max.i2max.Settings.DefineManager.COMMUNICATE_CONTENT_TYPE;
import static com.machine.i2max.i2max.Settings.DefineManager.CONNECTION_SUCCESSFULL;
import static com.machine.i2max.i2max.Settings.DefineManager.DISABLE_PULLING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.FORECAST_DATA_RECEIVED;
import static com.machine.i2max.i2max.Settings.DefineManager.INVISIBLE_LOADING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_ERROR;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_WARN;
import static com.machine.i2max.i2max.Settings.DefineManager.SERVER_DOMAIN_NAME;
import static com.machine.i2max.i2max.Settings.DefineManager.VISIBLE_LOADING_PROGRESS;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class NetworkManager{

    Handler handlingWithController;

    public NetworkManager(Handler handlingWithController) {
        this.handlingWithController = handlingWithController;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        //builder.addInterceptor(receivedCookiesInterceptor);
        return builder.build();
    }

    public Retrofit CreateRetrofitConnector() {
        return new Retrofit.Builder()
                .baseUrl("http://" + SERVER_DOMAIN_NAME + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    public void UploadDataProcess(double data[], String date[], int day){

        handlingWithController.sendEmptyMessage(VISIBLE_LOADING_PROGRESS);

        NetworkManagerRoute retrofitInterface = CreateRetrofitConnector().create(NetworkManagerRoute.class);
        UploadDataRequest uploadDataRequest = new UploadDataRequest();
        uploadDataRequest.setData(data);
        uploadDataRequest.setDay(day);
        uploadDataRequest.setDate(date);

        Call<UploadDataResponse> calling = retrofitInterface.UploadDataProcess(COMMUNICATE_CONTENT_TYPE, uploadDataRequest);
        calling.enqueue(new Callback<UploadDataResponse>() {
            @Override
            public void onResponse(Call<UploadDataResponse> call, Response<UploadDataResponse> response) {
                handlingWithController.sendEmptyMessage(INVISIBLE_LOADING_PROGRESS);
                if(response.code() == CONNECTION_SUCCESSFULL) {
                    try {
                        PrintLog("NetworkManager", "UploadDataProcess", "ok response is: " + response.body().getResult(), LOG_LEVEL_INFO);
                    }
                    catch (Exception err) {
                        PrintLog("NetworkManager", "UploadDataProcess", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                else {
                    PrintLog("NetworkManager", "UploadDataProcess", "wrong response is: " + response.raw().message(), LOG_LEVEL_WARN);
                }
            }

            @Override
            public void onFailure(Call<UploadDataResponse> call, Throwable t) {
                PrintLog("NetworkManager", "UploadDataProcess", "fail response is: " + t.getMessage(), LOG_LEVEL_WARN);
                handlingWithController.sendEmptyMessage(INVISIBLE_LOADING_PROGRESS);
            }
        });
    }

    public void DownloadForecastProcess(int processId) {
        NetworkManagerRoute retrofitInterface = CreateRetrofitConnector().create(NetworkManagerRoute.class);

        DownloadForecastDataRequest downloadForecastDataRequest = new DownloadForecastDataRequest();
        downloadForecastDataRequest.setProcessId(processId);

        Call<DownloadForecastDataResponse> calling = retrofitInterface.DownloadForecastProcess(COMMUNICATE_CONTENT_TYPE, downloadForecastDataRequest);
        calling.enqueue(new Callback<DownloadForecastDataResponse>() {
            @Override
            public void onResponse(Call<DownloadForecastDataResponse> call, Response<DownloadForecastDataResponse> response) {
                handlingWithController.sendEmptyMessage(DISABLE_PULLING_PROGRESS);
                if(response.code() == CONNECTION_SUCCESSFULL) {
                    try {
                        PrintLog("NetworkManager", "DownloadForecastProcess", "ok response is: " + response.body().getStatus(), LOG_LEVEL_INFO);

                        Bundle recvDataBundle = new Bundle();
                        recvDataBundle.putStringArray(BUNDLE_DATE, response.body().getDate());
                        recvDataBundle.putDoubleArray(BUNDLE_RESULT, response.body().getResult());
                        recvDataBundle.putString(BUNDLE_STATUS, response.body().getStatus());

//                        recvDataBundle.putStringArray(BUNDLE_DATE, new String[]{"2017-08-14", "2017-08-15"});
//                        recvDataBundle.putDoubleArray(BUNDLE_RESULT, new double[]{2.5, 4.5});
//                        recvDataBundle.putString(BUNDLE_STATUS, "Done");

                        Message forecastMessage = new Message();
                        forecastMessage.what = FORECAST_DATA_RECEIVED;
                        forecastMessage.setData(recvDataBundle);

                        handlingWithController.sendMessage(forecastMessage);
                        PrintLog("NetworkManager", "DownloadForecastProcess", "received data sended", LOG_LEVEL_INFO);
                    } catch (Exception err) {
                        PrintLog("NetworkManager", "DownloadForecastProcess", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                else {
                    PrintLog("NetworkManager", "DownloadForecastProcess", "wrong response is: " + response.raw().message(), LOG_LEVEL_WARN);
                }
            }

            @Override
            public void onFailure(Call<DownloadForecastDataResponse> call, Throwable t) {
                handlingWithController.sendEmptyMessage(DISABLE_PULLING_PROGRESS);
                PrintLog("NetworkManager", "DownloadForecastProcess", "fail response is: " + t.getMessage(), LOG_LEVEL_WARN);
            }
        });
    }
}
