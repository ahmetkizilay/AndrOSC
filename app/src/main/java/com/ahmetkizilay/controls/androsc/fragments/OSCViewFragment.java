package com.ahmetkizilay.controls.androsc.fragments;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCControlView;
import com.ahmetkizilay.controls.androsc.views.settings.OSCSettingsViewGroup;
import com.ahmetkizilay.controls.androsc.views.OSCViewGroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class OSCViewFragment extends Fragment{
	
	private OSCViewGroup mOSCViewGroup;
	private ImageButton btnAddNewControl;
	private ImageButton btnToggleEdit;
	private ImageButton btnDeleteControl;
    private ImageButton btnSaveTemplate;
    private ImageButton btnDuplicateControl;
    private ImageButton btnShowSettings;

    private HSLColorPicker mColorPicker;
    private OSCSettingsViewGroup vgSettings;

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
        this.vgSettings = (OSCSettingsViewGroup) getActivity().findViewById(R.id.vgSettings);
        this.vgSettings.setColorPicker(this.mColorPicker);

		this.mOSCViewGroup = (OSCViewGroup) getActivity().findViewById(R.id.wgOSCPanel);
        this.mOSCViewGroup.setOSCControlCommandListener(new OSCViewGroup.OSCControlCommandListener() {
            @Override
            public void onControlSelected(OSCControlView selectedControl) {
                OSCViewFragment.this.btnDeleteControl.setVisibility(View.VISIBLE);
                OSCViewFragment.this.btnDuplicateControl.setVisibility(View.VISIBLE);
                OSCViewFragment.this.btnShowSettings.setVisibility(View.VISIBLE);
            }

            @Override
            public void onControlReleased() {
                OSCViewFragment.this.btnDeleteControl.setVisibility(View.INVISIBLE);
                OSCViewFragment.this.btnDuplicateControl.setVisibility(View.INVISIBLE);
                OSCViewFragment.this.btnShowSettings.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onControlSettingsRequested(OSCControlView selectedControl) {
                showSettingsForControl(selectedControl);
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

        btnSaveTemplate = (ImageButton) getActivity().findViewById(R.id.btnSaveTemplate);
        btnSaveTemplate.setVisibility(View.VISIBLE);
        btnSaveTemplate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mToggleCallback.openSaveTemplateDialog();
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
                btnDuplicateControl.setVisibility(View.INVISIBLE);
                btnShowSettings.setVisibility(View.INVISIBLE);
			}
		});

        btnDuplicateControl = (ImageButton) getActivity().findViewById(R.id.btnDuplicateControl);
        btnDuplicateControl.setVisibility(View.INVISIBLE);
        btnDuplicateControl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OSCViewFragment.this.mOSCViewGroup.duplicateSelectedOSCControl();
            }
        });

        btnShowSettings = (ImageButton) getActivity().findViewById(R.id.btnShowSettings);
        btnShowSettings.setVisibility(View.INVISIBLE);
        btnShowSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSettingsForControl(mOSCViewGroup.getSelectedControl());
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
        btnDuplicateControl.setVisibility(View.INVISIBLE);
        btnShowSettings.setVisibility(View.INVISIBLE);
        btnSaveTemplate.setVisibility(View.INVISIBLE);
        this.vgSettings.setVisibility(View.INVISIBLE);
        closeSettingsView();
	}
	
	public void enableTemplateEditing() {
		this.btnToggleEdit.setImageResource(R.drawable.actionunlock);
		this.mOSCViewGroup.setEditEnabled(true);
		btnAddNewControl.setVisibility(View.VISIBLE);
        btnSaveTemplate.setVisibility(View.VISIBLE);
        btnDeleteControl.setVisibility(View.INVISIBLE);
        btnDuplicateControl.setVisibility(View.INVISIBLE);
        btnShowSettings.setVisibility(View.INVISIBLE);
        this.vgSettings.setVisibility(View.INVISIBLE);
        closeSettingsView();
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

    /***
     * called when a request made to close the settings view.
     * can be called from the settings view itself when close button is pressed
     * or from the main activity when back is pressed
     */
    public void closeSettingsView() {
        vgSettings.clearViews();
        vgSettings.setVisibility(View.INVISIBLE);
        this.mSettingsVisible = false;
        mOSCViewGroup.setSettingsEnabled(false);
        mToggleCallback.notifySettingsClosed();
    }

    private void showSettingsForControl(OSCControlView selectedControl) {
        if(mSettingsVisible) {
            return; // if any of them are visible just don't display any other
        }

        mSettingsVisible = true;
        btnDeleteControl.setVisibility(View.INVISIBLE);
        btnDuplicateControl.setVisibility(View.INVISIBLE);
        btnShowSettings.setVisibility(View.INVISIBLE);
        mOSCViewGroup.setSettingsEnabled(true);

        // displays the settings panel on the right side
        // populates the parameter fields based on the type of control selected.
        vgSettings.populateSettingsFor(selectedControl);
        vgSettings.setOSCSettingsActionListener(new OSCSettingsViewGroup.OSCSettingsActionListener() {
            @Override
            public void onSettingsClosed() {
                closeSettingsView();
            }

            @Override
            public void onSettingsSaved() {
                // do nothing really...
                // maybe figure out how to lose the keypad view
            }
        });
        vgSettings.setVisibility(View.VISIBLE);
    }

    /***
     *
     * @return true if settings window is open
     */
    public boolean isSettingsActive() {
        return this.mSettingsVisible;
    }

    /***
     * just a wrapper for the parent activity to query if edit is enabled.
     * it is meant to be called from the parent activity when back is pressed.
     * for example, if in edit mode (and maybe some changes occurred) show confirmation fragment.
     * @return true if edit is enabled
     */
    public boolean isEditEnabled() {
        return OSCViewFragment.this.mOSCViewGroup.isEditEnabled();
    }

    public interface OnMenuToggledListener {
		public void openNewOSCItemDialog();
        public void openSaveTemplateDialog();

        // this method is added with the purpose of regaining immersive mode
        // after settings panel is closed. it seems like the lower navigation panel
        // does not exit after the settings panel gains text input focus.
        // the parent activity will call the method to regain immersive mode with this callback.
        public void notifySettingsClosed();
	}
}
