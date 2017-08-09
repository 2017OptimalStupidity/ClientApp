package com.machine.i2max.i2max.Control;

import android.os.Handler;
import android.os.Message;

import com.machine.i2max.i2max.Core.NetworkManager;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class I2maxController {

    Handler handlingWithController;
    NetworkManager networkManager;

    public I2maxController(Handler handlingWithController) {
        this.handlingWithController = handlingWithController;

        networkManager = new NetworkManager(handlingWithNetworkManager);
    }

    Handler handlingWithNetworkManager = new Handler() {

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
