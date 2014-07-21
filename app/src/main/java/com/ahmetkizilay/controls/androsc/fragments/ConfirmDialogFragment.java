package com.ahmetkizilay.controls.androsc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmDialogFragment extends DialogFragment {

    private ConfirmDialogResultListener mCallback;

    private static final String MESSAGE_LABEL = "message";
    private static final String POSITIVE_LABEL = "confirm";
    private static final String NEGATIVE_LABEL = "cancel";

	public static ConfirmDialogFragment newInstance(String message) {
		ConfirmDialogFragment frag = new ConfirmDialogFragment();
		Bundle args = new Bundle();
        args.putString(MESSAGE_LABEL, message);
		frag.setArguments(args);
		return frag;
	}

    public static ConfirmDialogFragment newInstance(String message, String positiveLabel, String negativeLabel) {
        ConfirmDialogFragment frag = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putInt("num", 6);
        args.putString(MESSAGE_LABEL, message);
        args.putString(POSITIVE_LABEL, positiveLabel);
        args.putString(NEGATIVE_LABEL, negativeLabel);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        String message = (String) bundle.get(MESSAGE_LABEL);
        String positiveLabel = bundle.get(POSITIVE_LABEL) != null ? (String) bundle.get(POSITIVE_LABEL) : "Confirm";
        String negativeLabel = bundle.get(NEGATIVE_LABEL) != null ? (String) bundle.get(NEGATIVE_LABEL) : "Cancel";

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(positiveLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								mCallback.onPositiveSelected();
							}
						})
				.setNegativeButton(negativeLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
                                mCallback.onNegativeSelected();
							}
						});
		return builder.create();
	}

    public void setConfirmDialogResultListener(ConfirmDialogResultListener callback) {
        this.mCallback = callback;
    }

    public interface ConfirmDialogResultListener {
        public void onPositiveSelected();
        public void onNegativeSelected();
    }
}
