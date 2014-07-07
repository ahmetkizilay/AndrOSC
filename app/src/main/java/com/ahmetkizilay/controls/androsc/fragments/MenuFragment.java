package com.ahmetkizilay.controls.androsc.fragments;

import com.ahmetkizilay.controls.androsc.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment extends Fragment{

	private OnOSCMenuActionListener mOnMenuActionCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_fragment_layout, container, false);
	}
	
	@Override
	public void onAttach(Activity activity) { 
		super.onAttach(activity);
		
		try {
			this.mOnMenuActionCallback = (OnOSCMenuActionListener) activity;
		}
		catch(ClassCastException ex) {
			throw new ClassCastException(activity.toString() + " must implement OnMenuActionListener");
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		TextView twNewAction = (TextView) getActivity().findViewById(R.id.txtMenuNew);
		twNewAction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				mOnMenuActionCallback.oscMenuItemSelected(new OSCMenuActionEvent(OSCMenuActionEvent.ACTION_NEW));
			}
		});	
				
		TextView twOpenAction = (TextView) getActivity().findViewById(R.id.txtMenuOpen);
		twOpenAction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				mOnMenuActionCallback.oscMenuItemSelected(new OSCMenuActionEvent(OSCMenuActionEvent.ACTION_OPEN));
			}
		});	

		TextView twNetworkSettingsAction = (TextView) getActivity().findViewById(R.id.txtMenuNetwork);
		twNetworkSettingsAction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				mOnMenuActionCallback.oscMenuItemSelected(new OSCMenuActionEvent(OSCMenuActionEvent.ACTION_NETWORK));
			}
		});
	}
	
	public interface OnOSCMenuActionListener {
		public void oscMenuItemSelected(OSCMenuActionEvent event);
	}
	
	public final class OSCMenuActionEvent {
		public static final int ACTION_NEW = 0;
		public static final int ACTION_OPEN = 1;
		//public static final int ACTION_EDIT = 2;
		//public static final int ACTION_SAVE = 3;
		public static final int ACTION_NETWORK = 4;

		private int mAction;
		
		private OSCMenuActionEvent(int action) {
			this.mAction = action;
		}
		
		public int getAction() {
			return this.mAction;
		}
	}
}
