package com.ahmetkizilay.controls.androsc.fragments;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCButtonView;
import com.ahmetkizilay.controls.androsc.views.OSCControlView;
import com.ahmetkizilay.controls.androsc.views.OSCHorizontalSliderView;
import com.ahmetkizilay.controls.androsc.views.OSCToggleView;
import com.ahmetkizilay.controls.androsc.views.OSCVerticalSliderView;
import com.ahmetkizilay.controls.androsc.views.OSCViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.OSCButtonSettings;
import com.ahmetkizilay.controls.androsc.views.settings.OSCHSliderSettings;
import com.ahmetkizilay.controls.androsc.views.settings.OSCToggleSettings;
import com.ahmetkizilay.controls.androsc.views.settings.OSCVSliderSettings;
import com.ahmetkizilay.controls.androsc.views.settings.OnSettingsClosedListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.Toast;

public class OSCViewFragment extends Fragment implements OnSettingsClosedListener{
	
	private OSCViewGroup mOSCViewGroup;
	private ImageButton btnAddNewControl;
	private ImageButton btnToggleEdit;
	private ImageButton btnDeleteControl;
    private ImageButton btnToggleMenu;

    private HSLColorPicker mColorPicker;

    private boolean mSettingsVisible = false;
	
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
                if(mSettingsVisible) {
                    return; // if any of them are visible just don't display any other
                }

                mSettingsVisible = true;
                btnDeleteControl.setVisibility(View.INVISIBLE);
                btnToggleMenu.setVisibility(View.INVISIBLE);
                if(mToggleCallback != null) {
                    mToggleCallback.closeMenu();
                }
                mOSCViewGroup.setSettingsEnabled(true);

                if(selectedControl instanceof OSCButtonView) {
                    View inflatedView = getActivity().findViewById(R.id.infBtnSettings);
                    if(inflatedView == null) {
                        ViewStub stubButtonSettings = (ViewStub) getActivity().findViewById(R.id.stubBtnSettings);
                        inflatedView = stubButtonSettings.inflate();
                    }
                    else {
                        inflatedView.setVisibility(View.VISIBLE);
                    }

                    OSCButtonSettings.createInstance(inflatedView, (OSCButtonView) selectedControl, mColorPicker, OSCViewFragment.this);
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

                    OSCToggleSettings.createInstance(inflatedView, (OSCToggleView) selectedControl, mColorPicker, OSCViewFragment.this);
                }
                if(selectedControl instanceof OSCHorizontalSliderView) {
                    View inflatedView = getActivity().findViewById(R.id.infHSliderSettings);
                    if(inflatedView == null) {
                        ViewStub stubButtonSettings = (ViewStub) getActivity().findViewById(R.id.stubHSliderSettings);
                        inflatedView = stubButtonSettings.inflate();
                    }
                    else {
                        inflatedView.setVisibility(View.VISIBLE);
                    }

                    OSCHSliderSettings.createInstance(inflatedView, (OSCHorizontalSliderView) selectedControl, mColorPicker, OSCViewFragment.this);
                }
                if(selectedControl instanceof OSCVerticalSliderView) {
                    View inflatedView = getActivity().findViewById(R.id.infVSliderSettings);
                    if(inflatedView == null) {
                        ViewStub stubButtonSettings = (ViewStub) getActivity().findViewById(R.id.stubVSliderSettings);
                        inflatedView = stubButtonSettings.inflate();
                    }
                    else {
                        inflatedView.setVisibility(View.VISIBLE);
                    }

                    OSCVSliderSettings.createInstance(inflatedView, (OSCVerticalSliderView) selectedControl, mColorPicker, OSCViewFragment.this);
                }
            }
        });

		btnToggleMenu = (ImageButton) getActivity().findViewById(R.id.btnToggleMenu);
        btnToggleMenu.setOnClickListener(new View.OnClickListener() {
			
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

	private OnMenuToggledListener mToggleCallback;

    private void handleSettingsViewClosed() {
        this.mSettingsVisible = false;
        btnToggleMenu.setVisibility(View.VISIBLE);
        mOSCViewGroup.setSettingsEnabled(false);
    }

    @Override
    public void onSettingsViewClosed() {
        handleSettingsViewClosed();
    }

    @Override
    public void onSettingsViewSaved() {
        handleSettingsViewClosed();
    }

    public interface OnMenuToggledListener {
		public void toggleMenu();
        public void closeMenu();
		public void openNewOSCItemDialog();
	}
}
