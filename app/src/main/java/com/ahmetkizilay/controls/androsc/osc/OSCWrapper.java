package com.ahmetkizilay.controls.androsc.osc;

import android.app.Activity;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Objects;

public class OSCWrapper {
    private Activity mActivity;
    private OSCPortOut oscPortOut = null;

    private OSCWrapper() {}

    private static OSCWrapper singleton = null;

    public static OSCWrapper getInstance() throws Exception {
        if(OSCWrapper.singleton == null) {
            OSCWrapper.singleton = new OSCWrapper();
        }

        if(OSCWrapper.singleton.oscPortOut == null) {
            throw new Exception("OSC Connection is not initialized");
        }

        return OSCWrapper.singleton;
    }

    public static OSCWrapper getInstance(Activity activity, String ipAddress, int port) throws Exception {
        if(OSCWrapper.singleton == null) {
            OSCWrapper.singleton = new OSCWrapper();
        }

        OSCWrapper.singleton.mActivity = activity;
        OSCWrapper.singleton.initializeOSC(ipAddress, port);

        return OSCWrapper.singleton;
    }

    private void initializeOSC(String ipAddress, int port) throws Exception {

        try {
            if (oscPortOut != null) {
                oscPortOut.close();
            }

            oscPortOut = new OSCPortOut(InetAddress.getByName(ipAddress), port);
        }
        catch(SocketException se) {
            oscPortOut = null;
            throw new Exception(se.getMessage());
        }
    }

    public void sendOSC(String message) {
        new AsyncSendOSCTask(this.mActivity, this.oscPortOut).execute(new OSCMessage(message));
    }

    public void sendOSC(String message, List<Object> args) {
        new AsyncSendOSCTask(this.mActivity, this.oscPortOut).execute(new OSCMessage(message, args));
    }
}
