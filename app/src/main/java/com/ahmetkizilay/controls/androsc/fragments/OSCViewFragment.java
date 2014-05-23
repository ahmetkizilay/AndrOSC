package com.ahmetkizilay.controls.androsc.fragments;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCButtonView;
import com.ahmetkizilay.controls.androsc.views.OSCControlView;
import com.ahmetkizilay.controls.androsc.views.OSCToggleView;
import com.ahmetkizilay.controls.androsc.views.OSCViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.OSCButtonSettings;
import com.ahmetkizilay.controls.androsc.views.settings.OSCToggleSettings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.Toast;

public class OSCViewFragment extends Fragment{
	
	private OSCViewGroup mOSCViewGroup;
	private ImageButton btnAddNewControl;
	private ImageButton btnToggleEdit;
	private ImageButton btnDeleteControl;

    private HSLColorPicker mColorPicker;
	
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

        this.mColorPicker = (HSLColorPicker) getActivity().findViewById(R.id.layColorPicker);

		this.mOSCViewGroup = (OSCViewGroup) getActivity().findViewById(R.id.wgOSCPanel);
        this.mOSCViewGroup.setOSCControlCommandListener(new OSCViewGroup.OSCControlCommandListener() {
            @Override
            public void onControlSelected(OSCControlView selectedControl) {
                OSCViewFragment.this.btnDeleteControl.setVisibility(View.VISIBLE);
            }

            @Override
            public void onControlSettingsRequested(OSCControlView selectedControl) {
                if(selectedControl instanceof OSCButtonView) {
                    View inflatedView = getActivity().findViewById(R.id.infBtnSettings);
                    if(inflatedView == null) {
                        ViewStub stubButtonSettings = (ViewStub) getActivity().findViewById(R.id.stubBtnSettings);
                        inflatedView = stubButtonSettings.inflate();
                    }
                    else {
                        inflatedView.setVisibility(View.VISIBLE);
                    }

                    OSCButtonSettings.createInstance(inflatedView, (OSCButtonView) selectedControl, mColorPicker);
                }
                if(selectedControl instanceof OSCToggleView) {
                    View inflatedView = getActivity().findViewById(R.id.infToggleSettings);
                    if(inflatedView == null) {
                        ViewStub stubButtonSettings = (ViewStub) getActivity().findViewById(R.id.stubToggleSettings);
                        inflatedView = stubButtonSettings.inflate();
                    }
                    else {
                        inflatedView.setVisibility(View.VISIBLE);
                    }

                    OSCToggleSettings.createInstance(inflatedView, (OSCToggleView) selectedControl, mColorPicker);
                }
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
		btnAddNewControl.setVisibility(View.VISIBLE);
		btnAddNewControl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mToggleCallback.openNewOSCItemDialog();
			}
		});
		
		btnToggleEdit = (ImageButton) getActivity().findViewById(R.id.btnToggleEdit);

		btnToggleEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(OSCViewFragment.this.mOSCViewGroup.isEditEnabled()) {
					disableTemplateEditing();
				}
				else {
					enableTemplateEditing();
				}

			}
		});
		
		btnDeleteControl = (ImageButton) getActivity().findViewById(R.id.btnDeleteControl);
		btnDeleteControl.setVisibility(View.INVISIBLE);
		btnDeleteControl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OSCViewFragment.this.mOSCViewGroup.removeSelectedOSCControl();
                btnDeleteControl.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	public void addNewOSCControl(String selectedItem) {
		this.mOSCViewGroup.addNewControl(selectedItem);
	}
	
	public String buildJSONString() {
		return this.mOSCViewGroup.buildJSONString();
	}
	
	public void clearForNewTemplate() {
		this.mOSCViewGroup.clearAllOSCViews();		
		this.enableTemplateEditing();
		
	}
	
	public void disableTemplateEditing() {
		this.btnToggleEdit.setImageResource(R.drawable.actionlock);
		this.mOSCViewGroup.setEditEnabled(false);
		btnAddNewControl.setVisibility(View.INVISIBLE);
        btnDeleteControl.setVisibility(View.INVISIBLE);
	}
	
	public void enableTemplateEditing() {
		this.btnToggleEdit.setImageResource(R.drawable.actionunlock);
		this.mOSCViewGroup.setEditEnabled(true);
		btnAddNewControl.setVisibility(View.VISIBLE);
        btnDeleteControl.setVisibility(View.INVISIBLE);
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
		}
	}

    public HSLColorPicker getColorPicker() {
        return this.mColorPicker;
    }
	
	private OnMenuToggledListener mToggleCallback;
	public interface OnMenuToggledListener {
		public void toggleMenu();
		public void openNewOSCItemDialog();
	}
}
