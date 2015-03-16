package com.alok.easybluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alok.easybluetooth.models.OnMessageReceivedListener;


public class MainActivity extends ActionBarActivity implements OnMessageReceivedListener{

    Button enableBluetooth;
    Button useBluetooth;
    EasyBluetooth easyBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        easyBluetooth = new EasyBluetooth(this, this);

        enableBluetooth = (Button) findViewById(R.id.enable_bluetooth);
        enableBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyBluetooth.enableBluetooth();
            }
        });

        useBluetooth = (Button) findViewById(R.id.use_bluetooth);
        useBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyBluetooth.scanForDevices();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceived(String btMessage) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EasyBluetooth.REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(EasyBluetooth.EXTRA_DEVICE_ADDRESS);
                    easyBluetooth.connectDevice(address);
                }
                break;
            case EasyBluetooth.REQUEST_ENABLE_BT:
                break;
        }
    }
}
