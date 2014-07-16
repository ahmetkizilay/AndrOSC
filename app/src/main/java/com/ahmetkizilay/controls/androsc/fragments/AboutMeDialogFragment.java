package com.ahmetkizilay.controls.androsc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ahmetkizilay.controls.androsc.R;

public class AboutMeDialogFragment extends DialogFragment{

    public static AboutMeDialogFragment newInstance() {
        AboutMeDialogFragment frag = new AboutMeDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        PackageManager pm = getActivity().getPackageManager();
        String versionName = "1.0.0";
        try {
            versionName = pm.getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (Exception e) {}

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
            .setTitle("AndrOSC - v" + versionName)
            .setIcon(R.drawable.ic_default)
            .setNeutralButton("DONATE", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if(mCallback != null) {
                        mCallback.onDonationsRequested();
                    }
                }
            })
            .setPositiveButton("RATE ME", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName()));
                    startActivity(rateIntent);
                }
                catch(Exception exp) {

                }

               dialog.dismiss();;
                }
            }).setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_aboutme, null));

        return builder.create();
    }

    private RequestListener mCallback;

    public void setRequestListener(RequestListener callback) {
        this.mCallback = callback;
    }

    public interface RequestListener {
        public void onDonationsRequested();
    }
}
