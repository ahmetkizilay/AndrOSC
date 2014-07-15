package com.ahmetkizilay.controls.androsc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
            "Version 1.0\n\nPERISONiC Sound And Media")
            .setCancelable(false)
            .setTitle("AndrOSC")
            .setIcon(R.drawable.ic_default)
            .setNeutralButton("DONATE", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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
            });

        return builder.create();
    }
}
