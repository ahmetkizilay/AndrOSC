package com.ahmetkizilay.controls.androsc.views.settings.wrappers;

import android.view.ViewGroup;

import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCControlView;

/**
 * This abstract class defines the skeleton for inheriting classes to dynamically adds modular
 * ViewGroups to the containing ViewGroup.
 * In <code>setup</code> method, the child class should add a helper view group for each
 * configurable parameter of the corresponding OSC control.
 * In <code>save</code> method, the child class should check if the value for the parameter has
 * changed in the helper view group, and update the corresponding OSC control accordingly.
 * <code>setup</code> and <code>save</code> methods are called from OSCSettingsViewGroup.
 * It is important to call invalidate on the corresponding OSC control to see the configuration
 * change immediately.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public abstract class OSCControlSettingsWrapper {

    protected OSCControlView mControl;
    public OSCControlSettingsWrapper(OSCControlView control) {
        this.mControl = control;
    }

    public abstract void setup(ViewGroup container, HSLColorPicker colorPicker);
    public abstract void save();
}
