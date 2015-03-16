package com.alok.easybluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alok.easybluetooth.models.OnMessageReceivedListener;
import com.alok.easybluetooth.utils.BTDeviceListActivity;

import java.lang.reflect.Method;

/**
 * Created by Alok on 3/15/2015.
 */
public class EasyBluetooth {

    public final static int REQUEST_ENABLE_BT = 1515;
    public final static int REQUEST_CONNECT_DEVICE = 5151;
    public final static String EXTRA_DEVICE_ADDRESS = "device_address";

    private boolean connected = false;
    private boolean enabled = false;
    private boolean discoverable = false;
    private OnMessageReceivedListener onMessageReceivedListener = null;
    private Activity activity;
    private BluetoothAdapter btAdapter;
    private BTService btService;

    public EasyBluetooth(Activity activity, OnMessageReceivedListener onMessageReceivedListener) {
        this.activity = activity;
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        registerReceiveListener(onMessageReceivedListener);
        btService = new BTService(activity, btHandler);
    }

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
                    EasyBluetooth.this.onMessageReceivedListener.onReceived((String) msg.obj);
                    break;
                case BTService.MESSAGE_DEVICE_NAME:
                    break;
            }
        }
    };

    public void enableBluetooth()   {
        if(btAdapter == null)   {
            Toast.makeText(activity, "Your device does not support Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }

        if (!btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            enabled = true;
        }
    }


    public void sendMessage(String btMessage) {

        // Check that we're actually connected before trying anything
        if (btService.getState() != BTService.STATE_CONNECTED) {
            Toast.makeText(activity, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (btMessage.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = btMessage.getBytes();
            btService.write(send);
        }
    }

    public void connectDevice(String address)   {

        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        //device.createBond();

        // Attempt to connect to the device
        btService.connect(device);
    }

    //For Pairing
    private void pairDevice(BluetoothDevice device) {
        try {
            Log.d("pairDevice()", "Start Pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d("pairDevice()", "Pairing finished.");
        } catch (Exception e) {
            Log.e("pairDevice()", e.getMessage());
        }
    }

    public void registerReceiveListener(OnMessageReceivedListener listener)    {
        this.onMessageReceivedListener = listener;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDiscoverable() {
        return discoverable;
    }

    public void scanForDevices()    {
        Intent serverIntent = new Intent(activity, BTDeviceListActivity.class);
        activity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }
}
