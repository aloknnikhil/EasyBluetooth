package com.alok.easybluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Alok on 3/15/2015.
 */
public class EasyBluetooth {

    private boolean connected = false;
    private boolean enabled = false;
    private boolean discoverable = false;

    // The Handler that gets information back from the BluetoothChatService
    private final Handler btHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BTService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BTService.STATE_CONNECTED:
                            break;
                        case BTService.STATE_CONNECTING:
                            break;
                        case BTService.STATE_LISTEN:
                        case BTService.STATE_NONE:
                            break;
                    }
                    break;
                case BTService.MESSAGE_READ:
                    break;
                case BTService.MESSAGE_DEVICE_NAME:
                    break;
            }
        }
    };

    private void sendBTMessage(String message) {
    }
}
