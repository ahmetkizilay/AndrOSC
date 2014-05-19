package com.ahmetkizilay.controls.androsc.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;

public class NetworkSettingsDialogFragment extends DialogFragment {

    private OnNetworkSettingsChangedListener mNetworkSettingsCallback;

    public static NetworkSettingsDialogFragment getInstance(String ipAddress, int port, boolean connectOnStartUp) {
        NetworkSettingsDialogFragment dlgFrag = new NetworkSettingsDialogFragment();

        Bundle args = new Bundle();
        args.putString("ipAddress", ipAddress);
        args.putInt("port", port);
        args.putBoolean("connectOnStartUp", connectOnStartUp);
        dlgFrag.setArguments(args);

        return dlgFrag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.mNetworkSettingsCallback = (OnNetworkSettingsChangedListener) activity;
        }
        catch(ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnSaveFileNameSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String ipAddress = getArguments().containsKey("ipAddress") ? getArguments().getString("ipAddress") : "192.168.2.1";
        int port = getArguments().getInt("port", 8080);
        boolean connectOnStartUp = getArguments().getBoolean("connectOnStartUp", false);
        final View view = inflater.inflate(R.layout.network_settings_layout, container, false);
        final EditText txtIPAddress = (EditText) view.findViewById(R.id.txtIPAddress);
        txtIPAddress.setText(ipAddress);

        final EditText txtPort = (EditText) view.findViewById(R.id.txtPort);
        txtPort.setText(port + "");

        final CheckBox cbConnectOnStartUp = (CheckBox) view.findViewById(R.id.cbConnectOnStartUp);
        cbConnectOnStartUp.setChecked(connectOnStartUp);

        Button saveButton = (Button) view.findViewById(R.id.btnFormer);
        saveButton.setText(R.string.action_save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String newIPAddress = txtIPAddress.getText().toString();
                int newPort = Integer.parseInt(txtPort.getText().toString());
                boolean newConnectOnStartUp = cbConnectOnStartUp.isChecked();

                NetworkSettingsDialogFragment.this.dismiss();
                NetworkSettingsDialogFragment.this.mNetworkSettingsCallback.onNetworkSettingsChanged(newIPAddress, newPort, newConnectOnStartUp);

            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.btnLatter);
        cancelButton.setText(R.string.action_close);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NetworkSettingsDialogFragment.this.dismiss();
            }
        });

        Dialog dlg = getDialog();
        dlg.setCancelable(false);
        dlg.setTitle(R.string.frg_network_settings);
        dlg.setCanceledOnTouchOutside(false);

        return view;
    }

    public interface OnNetworkSettingsChangedListener {
        public void onNetworkSettingsChanged(String ipAddress, int port, boolean connectOnStartUp);
    }
}
