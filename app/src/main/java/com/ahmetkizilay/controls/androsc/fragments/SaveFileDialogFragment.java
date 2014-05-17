package com.ahmetkizilay.controls.androsc.fragments;

import java.io.File;

import com.ahmetkizilay.controls.androsc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SaveFileDialogFragment extends DialogFragment implements View.OnClickListener{
	
	public interface OnSaveFileNameSelectedListener {
		public void onSaveFileSelected(String fileName);
	}
	
	public static SaveFileDialogFragment getInstance(String baseFolder, String currentFileName) {
		SaveFileDialogFragment dlgFrag = new SaveFileDialogFragment();
		
		Bundle args = new Bundle();
		args.putString("baseFolder", baseFolder);
		args.putString("currentFile", currentFileName);
		dlgFrag.setArguments(args);
		
		return dlgFrag;
	}
	
	private OnSaveFileNameSelectedListener mSaveFileNameCallback;
	private EditText etSaveFileName;
	private TextView twNotifyText;
	private Button btnSave;
	private Button btnCancel;
	
	private String mBaseFolder;
	private String mFileNameToOverWrite;
	private String mCurrentFileName;
	
	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);
		
		try {
			this.mSaveFileNameCallback = (OnSaveFileNameSelectedListener) activity;
		}
		catch(ClassCastException ex) {
			throw new ClassCastException(activity.toString() + " must implement OnSaveFileNameSelectedListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				
		this.mBaseFolder = getArguments().getString("baseFolder");	
		this.mCurrentFileName = getArguments().getString("currentFile");
		
		final View view = inflater.inflate(R.layout.save_dialog, container, false);
		this.etSaveFileName = (EditText) view.findViewById(R.id.dlgSaveTextInput);
		if(this.mCurrentFileName != null) {
			this.etSaveFileName.setText(this.mCurrentFileName);
		}
		
		this.twNotifyText = (TextView) view.findViewById(R.id.dlgSaveNotifyText);
		this.btnSave = (Button) view.findViewById(R.id.dlgSaveBtnPositive);
		this.btnCancel = (Button) view.findViewById(R.id.dlgSaveBtnCancel);
		
		this.btnSave.setOnClickListener(this);
		this.btnCancel.setOnClickListener(this);
		
		Dialog dlg = getDialog();
		dlg.setCancelable(false);
		dlg.setTitle(R.string.dlgSave_save_file_name);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(this.btnSave)) {
			if(this.btnSave.getText().equals(getResources().getString(R.string.action_save))) {
				String strFileName = this.etSaveFileName.getText().toString();
				if(strFileName.equals("")) {
					this.twNotifyText.setText("please enter file name");
					return;
				}
				
				if(!strFileName.endsWith(".json")) strFileName = strFileName + ".json";
				File saveFile = new File(this.mBaseFolder + File.separator + strFileName);
				if(saveFile.exists()) {
					this.btnSave.setText(R.string.action_overwrite);
					this.twNotifyText.setText("file exists. overwrite?");
					this.mFileNameToOverWrite = saveFile.getAbsolutePath();
					return;
				}
				else {
					this.dismiss();
					this.mSaveFileNameCallback.onSaveFileSelected(saveFile.getAbsolutePath());
				}
			}
			else {
				this.dismiss();
				this.mSaveFileNameCallback.onSaveFileSelected(this.mFileNameToOverWrite);				
			}			
		}
		
		if(v.equals(this.btnCancel)) {
			this.dismiss();
		}
	}	
}
