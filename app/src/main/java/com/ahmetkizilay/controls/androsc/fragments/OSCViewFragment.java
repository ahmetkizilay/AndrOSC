package com.ahmetkizilay.controls.androsc.fragments;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.controls.androsc.views.OSCSettingsViewGroup;
import com.ahmetkizilay.controls.androsc.views.OSCControlView;
import com.ahmetkizilay.controls.androsc.views.OSCViewGroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OSCViewFragment extends Fragment{
	
	private OSCViewGroup mOSCViewGroup;
	private OSCSettingsViewGroup mOSCSettings;
	private ImageButton btnAddNewControl;
	private ImageButton btnToggleEdit;
	private ImageButton btnDeleteControl;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.oscview_fragment_layout, container, false);
	}
	
	@Override
	public void onAttach(Activity activity) {	
		super.onAttach(activity);
		
		try {
			this.mToggleCallback = (OnMenuToggledListener) activity;			
		}
		catch(ClassCastException exp) {
			throw new ClassCastException(activity.toString() + " must implement OnMenuToggledListener");
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
				
		this.mOSCViewGroup = (OSCViewGroup) getActivity().findViewById(R.id.wgOSCPanel);
		this.mOSCViewGroup.setOSCControlCommandListener(new OSCViewGroup.OSCControlCommandListener() {

			@Override
			public void onControlSelected(OSCControlView selectedControl) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onControlSettingsRequested(OSCControlView selectedControl) {
				// TODO Auto-generated method stub
				
			}
			
		});
				
		ImageButton toggleButton = (ImageButton) getActivity().findViewById(R.id.btnToggleMenu);
		toggleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mToggleCallback.toggleMenu();
			}
		});
		
		btnAddNewControl = (ImageButton) getActivity().findViewById(R.id.btnAddNew);
		btnAddNewControl.setVisibility(View.INVISIBLE);
		btnAddNewControl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mToggleCallback.openNewOSCItemDialog();
			}
		});
		
		btnToggleEdit = (ImageButton) getActivity().findViewById(R.id.btnToggleEdit);
		btnToggleEdit.setVisibility(View.INVISIBLE);
		btnToggleEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(OSCViewFragment.this.mOSCViewGroup.isEditEnabled()) {
					((ImageButton) v).setImageResource(R.drawable.actionlock);
					OSCViewFragment.this.mOSCViewGroup.setEditEnabled(false);
					btnAddNewControl.setVisibility(View.INVISIBLE);
					btnDeleteControl.setVisibility(View.INVISIBLE);
//					getActivity().findViewById(R.id.loOSCFragment).setBackgroundColor(Color.rgb(24, 24, 200));
				}
				else {
					((ImageButton) v).setImageResource(R.drawable.actionunlock);
					OSCViewFragment.this.mOSCViewGroup.setEditEnabled(true);
					btnAddNewControl.setVisibility(View.VISIBLE);
					btnDeleteControl.setVisibility(View.VISIBLE);
//					getActivity().findViewById(R.id.loOSCFragment).setBackgroundResource(R.drawable.osc_edit_bg);
				}

			}
		});
		
		btnDeleteControl = (ImageButton) getActivity().findViewById(R.id.btnDeleteControl);
		btnDeleteControl.setVisibility(View.INVISIBLE);
		btnDeleteControl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OSCViewFragment.this.mOSCViewGroup.removeSelectedOSCControl();
			}
		});
		
		this.mOSCSettings = (OSCSettingsViewGroup) getActivity().findViewById(R.id.wgOSCSettings);
		// this.mOSCSettings.setVisibility(View.INVISIBLE);
		
	}
	
	public void addNewOSCControl(String selectedItem) {
		this.mOSCViewGroup.addNewControl(selectedItem);
	}
	
	public void removeSelectedOSCControl() {
		this.mOSCViewGroup.removeSelectedOSCControl();
	}
	
	public String buildJSONString() {
		return this.mOSCViewGroup.buildJSONString();
	}
	
	public void clearForNewTemplate() {
		this.mOSCViewGroup.clearAllOSCViews();		
		this.enableTemplateEditing();
		
	}
	
	public void disableTemplateEditing() {
		this.btnToggleEdit.setVisibility(View.INVISIBLE);
		this.btnToggleEdit.setImageResource(R.drawable.actionlock);
		this.mOSCViewGroup.setEditEnabled(false);
		btnAddNewControl.setVisibility(View.INVISIBLE);
		btnDeleteControl.setVisibility(View.INVISIBLE);
	}
	
	public void enableTemplateEditing() {
		this.btnToggleEdit.setVisibility(View.VISIBLE);
		this.btnToggleEdit.setImageResource(R.drawable.actionunlock);
		this.mOSCViewGroup.setEditEnabled(true);
		btnAddNewControl.setVisibility(View.VISIBLE);
		btnDeleteControl.setVisibility(View.VISIBLE);
//		getActivity().findViewById(R.id.loOSCFragment).setBackgroundResource(R.drawable.osc_edit_bg);
	}
	
	public void inflateTemplate(String filePath) {
		
		String jsonTemplateString = Utilities.readFileContents(filePath);
		if(jsonTemplateString == null) {
			Toast.makeText(getActivity(), "Cannot Read Template File", Toast.LENGTH_SHORT).show();
			return;
		}
		
		this.mOSCViewGroup.clearAllOSCViews();		
		if(!this.mOSCViewGroup.inflateJSONTemplate(jsonTemplateString)) {
			Toast.makeText(getActivity(), "Unable To Decode Template File", Toast.LENGTH_SHORT).show();
			this.mOSCViewGroup.clearAllOSCViews();
			return;
		}
	}
	
	private OnMenuToggledListener mToggleCallback;
	public interface OnMenuToggledListener {
		public void toggleMenu();
		public void openNewOSCItemDialog();
	}
}
