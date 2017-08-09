package com.machine.i2max.i2max;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.machine.i2max.i2max.Control.I2maxController;

import static com.machine.i2max.i2max.Settings.DefineManager.LOG_LEVEL_INFO;
import static com.machine.i2max.i2max.Utils.LogManager.PrintLog;

public class I2maxMain extends AppCompatActivity {

    private TextView mTextMessage;
    LayoutInflater dynamicLayoutInflater;
    FrameLayout dynamicAppView;
    RelativeLayout shareView, graphView;
    I2maxController i2maxController;

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
    }

    public void Init() {

        PrintLog("I2maxMain", "Init", "preparing ui", LOG_LEVEL_INFO);

        dynamicLayoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dynamicAppView = (FrameLayout) findViewById(R.id.content);
        shareView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.share_data_layout, null);
        graphView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.graph_data_layout, null);

        dynamicAppView.addView(shareView);
        dynamicAppView.addView(graphView);

        i2maxController = new I2maxController(handlingWithController);

        VisibleShareView();
    }

    public void VisibleShareView() {
        shareView.setVisibility(View.VISIBLE);
        graphView.setVisibility(View.INVISIBLE);
    }

    public void VisibleGraphView() {
        shareView.setVisibility(View.INVISIBLE);
        graphView.setVisibility(View.VISIBLE);
    }

    Handler handlingWithController = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                default:
                    break;
            }
        }
    };
}
