package com.machine.i2max.i2max;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.db.chart.view.LineChartView;
import com.dd.processbutton.iml.ActionProcessButton;
import com.machine.i2max.i2max.Control.I2maxController;
import com.machine.i2max.i2max.Control.RealmController;

import static com.machine.i2max.i2max.Settings.DefineManager.DATA_SAVE;
import static com.machine.i2max.i2max.Settings.DefineManager.DISABLE_PULLING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.FORECAST_DATA_RECEIVED;
import static com.machine.i2max.i2max.Settings.DefineManager.FORECAST_DATA_RECEIVED_ERROR;
import static com.machine.i2max.i2max.Settings.DefineManager.INVISIBLE_UPLOADING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_ERROR;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_WARN;
import static com.machine.i2max.i2max.Settings.DefineManager.NOT_AVAILABLE;
import static com.machine.i2max.i2max.Settings.DefineManager.PRINT_PROCESS_NOT_READY;
import static com.machine.i2max.i2max.Settings.DefineManager.SERVER_DOMAIN_NAME;
import static com.machine.i2max.i2max.Settings.DefineManager.TOO_SMALL_DATA_ACCEPTED;
import static com.machine.i2max.i2max.Settings.DefineManager.VISIBLE_UPLOADING_PROGRESS;
import static com.machine.i2max.i2max.Settings.DefineManager.WRONG_FORECAST_DAY_ACCEPTED;
import static com.machine.i2max.i2max.Settings.DefineManager.ZERO;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

public class I2maxMain extends AppCompatActivity {

    private TextView mTextMessage;
    LayoutInflater dynamicLayoutInflater;
    FrameLayout dynamicAppView;
    RelativeLayout shareView, graphView, infoView, homePageView;
    I2maxController i2maxController;
    ProgressBar progressUploading;
    EditText etxtSellingData, etxtForecastDay;
    ActionProcessButton btnUploadData;
    RealmController realmController;
    PullRefreshLayout swipeRefreshLayout;
    LineChartView lineChart;
    WebView webView;

    int pullForecastDataProcessId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationShare:
                    PrintLog("I2maxMain", "onNavigationItemSelected", "show share view", LOG_LEVEL_INFO);
                    VisibleShareView();
                    return true;
                case R.id.navigationGraph:
                    PrintLog("I2maxMain", "onNavigationItemSelected", "show graph view", LOG_LEVEL_INFO);
                    VisibleGraphView();
                    return true;
                case R.id.navigationInfo:
                    PrintLog("I2maxMain", "onNavigationItemSelected", "show info view", LOG_LEVEL_INFO);
                    VisibleInfoView();
                    return true;
                case R.id.navigationHomePage:
                    PrintLog("I2maxMain", "onNavigationItemSelected", "show home page view", LOG_LEVEL_INFO);
                    VisibleHomePageView();
                    return true;
                default:
                    return false;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i2max_main_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Init();

        btnUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellingData = etxtSellingData.getText().toString(),
                        forecastDay = etxtForecastDay.getText().toString();

                int todaySellingData = 0, todayForecastDay = 0;

                if(sellingData != null && sellingData != "") {
                    try {
                        todaySellingData = Integer.parseInt(sellingData);
                    }
                    catch (Exception err) {
                        PrintLog("I2maxMain", "onClick", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                else {
                    PrintLog("I2maxMain", "onClick", "No selling data", LOG_LEVEL_WARN);
                }

                if(forecastDay != null && forecastDay != "") {
                    try {
                        todayForecastDay = Integer.parseInt(forecastDay);
                    }
                    catch (Exception err) {
                        PrintLog("I2maxMain", "onClick", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                else {
                    PrintLog("I2maxMain", "onClick", "No forecast day", LOG_LEVEL_WARN);
                }
                realmController.AddNewTodayData(todaySellingData);
                i2maxController.UploadData(realmController.GetSellingDatas(), todayForecastDay);
            }
        });

        etxtSellingData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                PrintLog("I2maxMain", "onEditorAction", "Confirmed text: " + etxtSellingData.getText(), LOG_LEVEL_INFO);
                return true;
            }
        });

        etxtForecastDay.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                PrintLog("I2maxMain", "onEditorAction", "Confirmed text: " + etxtForecastDay.getText(), LOG_LEVEL_INFO);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullForecastDataProcessId = realmController.GetLatestProcessId();
                i2maxController.PullingData(pullForecastDataProcessId);
                PrintLog("I2maxMain", "onRefresh", "Start refresh", LOG_LEVEL_INFO);
            }
        });
    }

    public void Init() {

        PrintLog("I2maxMain", "Init", "preparing ui", LOG_LEVEL_INFO);

        dynamicLayoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dynamicAppView = (FrameLayout) findViewById(R.id.content);
        shareView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.share_data_layout, null);
        graphView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.graph_data_layout, null);
        infoView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.info_layout, null);
        homePageView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.home_page_layout, null);
        progressUploading = (ProgressBar) shareView.findViewById(R.id.progressUploading);
        etxtSellingData = (EditText) shareView.findViewById(R.id.etxtSellingData);
        etxtForecastDay = (EditText) shareView.findViewById(R.id.etxtForecastDay);
        btnUploadData = (ActionProcessButton) shareView.findViewById(R.id.btnUploadData);
        swipeRefreshLayout = (PullRefreshLayout) graphView.findViewById(R.id.swipeRefreshLayout);
        lineChart = (LineChartView)graphView.findViewById(R.id.lineChart);
        webView = (WebView)homePageView.findViewById(R.id.webView);

        btnUploadData.setMode(ActionProcessButton.Mode.ENDLESS);
        swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);

        dynamicAppView.addView(shareView);
        dynamicAppView.addView(graphView);
        dynamicAppView.addView(infoView);
        dynamicAppView.addView(homePageView);

//        LineSet dataset = new LineSet(new String[]{"2017-08-11", "2017-08-12", "2017-08-13"}, new float[]{1.0f, 2.0f, 0.5f});
//        lineChart.addData(dataset);
//        lineChart.show();

        i2maxController = new I2maxController(handlingWithController, lineChart);
        realmController = new RealmController(getApplicationContext());

        VisibleShareView();
        InvisibleProgress();
        DrawForecastGraph();

        pullForecastDataProcessId = NOT_AVAILABLE;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://" + SERVER_DOMAIN_NAME);
    }

    public void VisibleProgress() {
        progressUploading.setVisibility(View.VISIBLE);
        btnUploadData.setProgress(1);
    }

    public void InvisibleProgress() {
        progressUploading.setVisibility(View.INVISIBLE);
        btnUploadData.setProgress(0);
    }

    public void VisibleShareView() {
        shareView.setVisibility(View.VISIBLE);
        graphView.setVisibility(View.INVISIBLE);
        infoView.setVisibility(View.INVISIBLE);
        homePageView.setVisibility(View.INVISIBLE);
    }

    public void VisibleGraphView() {
        shareView.setVisibility(View.INVISIBLE);
        graphView.setVisibility(View.VISIBLE);
        infoView.setVisibility(View.INVISIBLE);
        homePageView.setVisibility(View.INVISIBLE);
    }

    public void VisibleInfoView() {
        shareView.setVisibility(View.INVISIBLE);
        graphView.setVisibility(View.INVISIBLE);
        infoView.setVisibility(View.VISIBLE);
        homePageView.setVisibility(View.INVISIBLE);
    }

    public void VisibleHomePageView() {
        shareView.setVisibility(View.INVISIBLE);
        graphView.setVisibility(View.INVISIBLE);
        infoView.setVisibility(View.INVISIBLE);
        homePageView.setVisibility(View.VISIBLE);
    }

    public void DisablePullingProcess() {
        swipeRefreshLayout.setRefreshing(false);
        PrintLog("I2maxMain", "DisablePullingProcess", "process ui disabled", LOG_LEVEL_INFO);
    }

    Handler handlingWithController = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case VISIBLE_UPLOADING_PROGRESS:
                    VisibleProgress();
                    break;
                case INVISIBLE_UPLOADING_PROGRESS:
                    AddNewProcessData(msg.arg1);
                    InvisibleProgress();
                    ShowSnackBarMessage(getString(R.string.UploadSuccess));
                    break;
                case DISABLE_PULLING_PROGRESS:
                    DisablePullingProcess();
                    break;
                case FORECAST_DATA_RECEIVED:
                    UpdateReceivedFroecastData(msg.getData());
                    break;
                case PRINT_PROCESS_NOT_READY:
                    ShowSnackBarMessage(getString(R.string.ProcessNotReady));
                    break;
                case FORECAST_DATA_RECEIVED_ERROR:
                    ShowSnackBarMessage(getString(R.string.ProcessReturnsError));
                    break;
                case WRONG_FORECAST_DAY_ACCEPTED:
                    ShowSnackBarMessage(getString(R.string.WrongForecastDayAccepted));
                    break;
                case TOO_SMALL_DATA_ACCEPTED:
                    ShowSnackBarMessage(getString(R.string.TooSmallDataAccepted));
                    break;
                case DATA_SAVE:
                    ShowSnackBarMessage(getString(R.string.DataSaved));
                    break;
                default:
                    break;
            }
        }
    };

    public void UpdateReceivedFroecastData(Bundle forecastBundleData) {
//        Bundle testBundle = new Bundle();
//        testBundle.putString(BUNDLE_STATUS, "Done");
//        testBundle.putStringArray(BUNDLE_DATE, new String[]{"2017-08-14", "2017-08-15"});
//        testBundle.putDoubleArray(BUNDLE_RESULT, new double[]{1.5, 0.5});
        realmController.UpdateForecastData(pullForecastDataProcessId, forecastBundleData);
        i2maxController.UpdateLineChartView(forecastBundleData);
    }

    public void AddNewProcessData(int processId) {
        if(processId >= ZERO) {
            PrintLog("I2maxMain", "AddNewProcessData", "add new process id: " + processId, LOG_LEVEL_INFO);
            realmController.AddForecastData(processId);
        }
        else {
            PrintLog("I2maxMain", "AddNewProcessData", "Not available process id: " + processId, LOG_LEVEL_WARN);
        }
    }

    public void DrawForecastGraph() {
        Bundle forecastData = realmController.GetLatestForecastData();
        i2maxController.UpdateLineChartView(forecastData);
    }

    public void ShowSnackBarMessage(String message) {
        Snackbar.make(findViewById(R.id.container), message,
                Snackbar.LENGTH_SHORT)
                .show();
    }
}
