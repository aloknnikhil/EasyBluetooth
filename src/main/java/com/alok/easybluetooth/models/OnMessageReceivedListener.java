package com.alok.easybluetooth.models;

/**
 * Created by Alok on 3/15/2015.
 */
public interface OnMessageReceivedListener {

    public void onReceived(String btMessage);

    public void onPaired();
}
