package com.ahmetkizilay.controls.androsc.osc;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class AsyncSendOSCTask extends AsyncTask<OSCMessage, Void, Boolean> {

    private OSCPortOut oscPortOut;
    private Activity activity;

    public AsyncSendOSCTask(Activity activity, OSCPortOut oscPortOut) {
        this.activity = activity;
        this.oscPortOut = oscPortOut;
    }

    @Override
    protected Boolean doInBackground(OSCMessage... message) {
        try {
            this.oscPortOut.send(message[0]);
            return Boolean.TRUE;
        } catch (Exception exp) {
            return Boolean.FALSE;
        }
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (!result.booleanValue()) {
            Toast.makeText(this.activity, "Error Sending OSC", Toast.LENGTH_SHORT).show();
        }
    }
}