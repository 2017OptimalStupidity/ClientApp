package com.machine.i2max.i2max.Core;

import com.machine.i2max.i2max.Model.DownloadForecastDataRequest;
import com.machine.i2max.i2max.Model.DownloadForecastDataResponse;
import com.machine.i2max.i2max.Model.UploadDataRequest;
import com.machine.i2max.i2max.Model.UploadDataResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static com.machine.i2max.i2max.Settings.DefineManager.SERVER_DOMAIN_NAME;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public interface NetworkManagerRoute {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://" + SERVER_DOMAIN_NAME + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("forecast")
    Call<DownloadForecastDataResponse>  DownloadForecastProcess(@Header("Content-Type") String contentType, @Body DownloadForecastDataRequest downloadForecastDataRequest);


    @POST("upload")
    Call<UploadDataResponse> UploadDataProcess(@Header("Content-Type") String contentType, @Body UploadDataRequest uploadDataRequest);

}
