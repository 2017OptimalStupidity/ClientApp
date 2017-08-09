package com.machine.i2max.i2max;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class I2maxMain extends AppCompatActivity {

    private TextView mTextMessage;
    LayoutInflater dynamicLayoutInflater;
    FrameLayout dynamicAppView;
    RelativeLayout shareView, graphView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationShare:
                    VisibleShareView();
                    return true;
                case R.id.navigationGraph:
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
        dynamicLayoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dynamicAppView = (FrameLayout) findViewById(R.id.content);
        shareView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.share_data_layout, null);
        graphView = (RelativeLayout) dynamicLayoutInflater.inflate(R.layout.graph_data_layout, null);

        dynamicAppView.addView(shareView);
        dynamicAppView.addView(graphView);

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
}
