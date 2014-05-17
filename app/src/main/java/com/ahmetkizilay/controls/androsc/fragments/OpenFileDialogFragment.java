package com.ahmetkizilay.controls.androsc.fragments;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OpenFileDialogFragment extends DialogFragment{
	public interface OnOpenFileNameSelectedListener {
		public void onOpenFileSelected(String fileName);
	}	
	private OnOpenFileNameSelectedListener mOpenFileSelectedCallback;
	
	public static OpenFileDialogFragment newInstance(String baseFolder) {
		OpenFileDialogFragment dlgFrag = new OpenFileDialogFragment();
		
		Bundle args = new Bundle();
		args.putString("baseFolder", baseFolder);
		dlgFrag.setArguments(args);
		
		return dlgFrag;
	}
	
	@Override
	public void onAttach(Activity activity) {	
		super.onAttach(activity);
		
		try {
			this.mOpenFileSelectedCallback = (OnOpenFileNameSelectedListener) activity;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnOpenFileNameSelectedListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String baseFolder = getArguments().getString("baseFolder");
		File fileBaseFolder = new File(baseFolder);
		final String[] fileArray = fileBaseFolder.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".json");
			}
		});
		
		return new AlertDialog.Builder(getActivity())
			.setTitle("Open Template")
			.setCancelable(true)
			.setItems(fileArray, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mOpenFileSelectedCallback.onOpenFileSelected(fileArray[which]);
					getDialog().dismiss();
					OpenFileDialogFragment.this.dismiss();					
				}
			}).create();
	}
}
