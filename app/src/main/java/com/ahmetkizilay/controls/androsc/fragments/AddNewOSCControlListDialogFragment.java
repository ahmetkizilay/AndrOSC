package com.ahmetkizilay.controls.androsc.fragments;

import com.ahmetkizilay.controls.androsc.utils.OSCControlItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AddNewOSCControlListDialogFragment extends DialogFragment{
	public interface OnNewOSCControlSelected {
		public void onNewOSCControlSelected(String selectedItem);
	}
		
	private OnNewOSCControlSelected mOSCSelectedCallback;
	
	public static AddNewOSCControlListDialogFragment newInstance() {
		AddNewOSCControlListDialogFragment dlgFrag = new AddNewOSCControlListDialogFragment();
		return dlgFrag;
	}
	
	@Override
	public void onAttach(Activity activity) {	
		super.onAttach(activity);
		
		try {
			mOSCSelectedCallback = (OnNewOSCControlSelected) activity;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnNewOSCControlSelected");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		return new AlertDialog.Builder(getActivity())
			.setTitle("New Control")
			.setCancelable(true)
			.setItems(OSCControlItem.getItems(), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOSCSelectedCallback.onNewOSCControlSelected(OSCControlItem.getItems()[which]);
					getDialog().dismiss();
					AddNewOSCControlListDialogFragment.this.dismiss();					
				}
			}).create();
	}
}
