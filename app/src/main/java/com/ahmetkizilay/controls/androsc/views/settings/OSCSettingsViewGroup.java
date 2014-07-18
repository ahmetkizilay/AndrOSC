package com.ahmetkizilay.controls.androsc.views.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCButtonView;
import com.ahmetkizilay.controls.androsc.views.OSCControlView;
import com.ahmetkizilay.controls.androsc.views.OSCHorizontalSliderView;
import com.ahmetkizilay.controls.androsc.views.OSCPadView;
import com.ahmetkizilay.controls.androsc.views.OSCToggleView;
import com.ahmetkizilay.controls.androsc.views.OSCVerticalSliderView;
import com.ahmetkizilay.controls.androsc.views.settings.wrappers.OSCButtonSettingsWrapper;
import com.ahmetkizilay.controls.androsc.views.settings.wrappers.OSCControlSettingsWrapper;
import com.ahmetkizilay.controls.androsc.views.settings.wrappers.OSCHSliderSettingsWrapper;
import com.ahmetkizilay.controls.androsc.views.settings.wrappers.OSCPadSettingsWrapper;
import com.ahmetkizilay.controls.androsc.views.settings.wrappers.OSCToggleSettingsWrapper;
import com.ahmetkizilay.controls.androsc.views.settings.wrappers.OSCVSliderSettingsWrapper;

/**
 * This is a ViewGroup class that holds the setting parameters for control views.
 * in <code>populateSettingsFor</code> method, creates a OSCControlSettingsWrapper instance based
 * on the type of the control. It is important to remove all the children views of the containing
 * Linearlayout. Defined two callback methods to notify the parent fragment of settings saved and
 * settings closed. HSLColorPicker ViewGroup is only created once on the parent fragment and it is
 * shared among all the control wrappers. The reference to the color picker goes through this class.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class OSCSettingsViewGroup extends LinearLayout {
    private ViewGroup mContainer;
    private OSCControlSettingsWrapper mSettingsWrapper = null;
    private HSLColorPicker mColorPicker;
    private OSCSettingsActionListener mCallback;

    public OSCSettingsViewGroup(Context context) {
        super(context);
        init();
    }

    public OSCSettingsViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.control_settings, this, true);

        this.mContainer = (ViewGroup) findViewById(R.id.loSettingsContainer);
        Button btnSave = (Button) findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSettingsWrapper != null) {
                    mSettingsWrapper.save();
                }
                if(mCallback != null) {
                    mCallback.onSettingsSaved();
                }
            }
        });
        Button btnClose = (Button) findViewById(R.id.btnCloseSettings);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback != null) {
                    mCallback.onSettingsClosed();
                }
            }
        });
    }

    public void setColorPicker(HSLColorPicker colorPicker) {
        this.mColorPicker = colorPicker;
    }

    public void clearViews() {
        mContainer.removeAllViews();
    }

    public void populateSettingsFor(OSCControlView control) {
        mContainer.removeAllViews();
        TextView txtTitle = (TextView) findViewById(R.id.lblTitle);

        if(control instanceof OSCButtonView) {
            txtTitle.setText("OSC-Button");
            this.mSettingsWrapper = new OSCButtonSettingsWrapper((OSCButtonView) control);
        }
        else if(control instanceof OSCToggleView){
            txtTitle.setText("OSC-Toggle");
            this.mSettingsWrapper = new OSCToggleSettingsWrapper((OSCToggleView) control);
        }
        else if(control instanceof OSCHorizontalSliderView) {
            txtTitle.setText("OSC-HSlider");
            this.mSettingsWrapper = new OSCHSliderSettingsWrapper((OSCHorizontalSliderView) control);
        }
        else if(control instanceof OSCVerticalSliderView) {
            txtTitle.setText("OSC-VSlider");
            this.mSettingsWrapper = new OSCVSliderSettingsWrapper((OSCVerticalSliderView) control);
        }
        else if(control instanceof OSCPadView) {
            txtTitle.setText("OSC-Pad");
            this.mSettingsWrapper = new OSCPadSettingsWrapper((OSCPadView) control);
        }

        if(this.mSettingsWrapper != null) {
            this.mSettingsWrapper.setup(mContainer, mColorPicker);
        }
    }

    public void setOSCSettingsActionListener(OSCSettingsActionListener callback) {
        this.mCallback = callback;
    }

    public interface OSCSettingsActionListener {
        public void onSettingsClosed();
        public void onSettingsSaved();
    }
}
