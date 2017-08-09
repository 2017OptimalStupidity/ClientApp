package com.machine.i2max.i2max.Core;

import android.os.Handler;

import com.machine.i2max.i2max.Model.UploadDataRequest;
import com.machine.i2max.i2max.Model.UploadDataResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.machine.i2max.i2max.Settings.DefineManager.COMMUNICATE_CONTENT_TYPE;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Settings.DefineManager.SERVER_DOMAIN_NAME;
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

    public void UploadDataProcess(double data[], int day){
        NetworkManagerRoute retrofitInterface = CreateRetrofitConnector().create(NetworkManagerRoute.class);
        UploadDataRequest uploadDataRequest = new UploadDataRequest();
        uploadDataRequest.setData(data);
        uploadDataRequest.setDay(day);

        Call<UploadDataResponse> calling = retrofitInterface.UploadDataProcess(COMMUNICATE_CONTENT_TYPE, uploadDataRequest);
        calling.enqueue(new Callback<UploadDataResponse>() {
            @Override
            public void onResponse(Call<UploadDataResponse> call, Response<UploadDataResponse> response) {
                PrintLog("NetworkManager", "UploadDataProcess", "ok response is: " + response.raw().message(), LOG_LEVEL_INFO);
            }

            @Override
            public void onFailure(Call<UploadDataResponse> call, Throwable t) {
                PrintLog("NetworkManager", "UploadDataProcess", "fail response is: " + t.getMessage(), LOG_LEVEL_INFO);
            }
        });
    }
}
