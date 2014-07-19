package com.ahmetkizilay.controls.androsc.views.settings.wrappers;

import android.view.ViewGroup;

import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCToggleView;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditCheckBoxViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditColorViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditDimensionsViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditPositionViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditTextViewGroup;

/**
 * This class dynamically adds modular ViewGroups to the containing ViewGroup.
 * All the fields in this class refer to one configurable parameter of the corresponding OSC
 * control. <code>setup</code> and <code>save</code> methods are called from OSCSettingsViewGroup.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class OSCToggleSettingsWrapper extends OSCControlSettingsWrapper {

    private EditPositionViewGroup vgPosition;
    private EditDimensionsViewGroup vgDimensions;
    private EditTextViewGroup vgToggledOffText;
    private EditTextViewGroup vgToggledOnText;
    private EditTextViewGroup vgOSCToggleOn;
    private EditTextViewGroup vgOSCToggleOff;
    private EditCheckBoxViewGroup vgTriggerOnToggleOff;
    private EditColorViewGroup vgDefaultFillColor;
    private EditColorViewGroup vgToggledFillColor;
    private EditColorViewGroup vgBorderStrokeColor;
    private EditColorViewGroup vgFontColor;

    public OSCToggleSettingsWrapper(OSCToggleView control) {
        super(control);
    }

    @Override
    public void setup(ViewGroup container, HSLColorPicker colorPicker) {
        OSCToggleView thisControl = (OSCToggleView) super.mControl;

        this.vgPosition = new EditPositionViewGroup(container.getContext());
        this.vgPosition.setPosition(thisControl.getParameters().getLeft(), thisControl.getParameters().getTop(),
                thisControl.getParameters().getRight(), thisControl.getParameters().getBottom());
        this.vgPosition.setDimensions(thisControl.getParameters().getWidth(), thisControl.getParameters().getHeight());
        container.addView(this.vgPosition);

        this.vgDimensions = new EditDimensionsViewGroup(container.getContext());
        this.vgDimensions.setDimensions(thisControl.getParameters().getWidth(), thisControl.getParameters().getHeight());
        container.addView(this.vgDimensions);

        this.vgToggledOffText = new EditTextViewGroup(container.getContext());
        this.vgToggledOffText.setTitle("Toggled Off Text");
        this.vgToggledOffText.setText(thisControl.getParameters().getDefaultText());
        container.addView(this.vgToggledOffText);

        this.vgToggledOnText = new EditTextViewGroup(container.getContext());
        this.vgToggledOnText.setTitle("Toggled On Text");
        this.vgToggledOnText.setText(thisControl.getParameters().getToggledText());
        container.addView(this.vgToggledOnText);

        this.vgOSCToggleOn = new EditTextViewGroup(container.getContext());
        this.vgOSCToggleOn.setTitle("OSC: Toggled On");
        this.vgOSCToggleOn.setText(thisControl.getParameters().getOSCToggleOn());
        container.addView(this.vgOSCToggleOn);

        this.vgOSCToggleOff = new EditTextViewGroup(container.getContext());
        this.vgOSCToggleOff.setTitle("OSC: Toggled Off");
        this.vgOSCToggleOff.setText(thisControl.getParameters().getOSCToggleOff());
        container.addView(this.vgOSCToggleOff);

        this.vgTriggerOnToggleOff = new EditCheckBoxViewGroup(container.getContext());
        this.vgTriggerOnToggleOff.setTitle("OSC: Trigger On Toggle Off");
        this.vgTriggerOnToggleOff.setValue(thisControl.getParameters().getFireOSCOnToggleOff());
        container.addView(this.vgTriggerOnToggleOff);

        this.vgDefaultFillColor = new EditColorViewGroup(container.getContext());
        this.vgDefaultFillColor.setTitle("Toggled Off Color");
        this.vgDefaultFillColor.setColor(thisControl.getParameters().getDefaultFillColor());
        this.vgDefaultFillColor.setColorPicker(colorPicker);
        container.addView(this.vgDefaultFillColor);

        this.vgToggledFillColor = new EditColorViewGroup(container.getContext());
        this.vgToggledFillColor.setTitle("Toggled On Fill Color");
        this.vgToggledFillColor.setColor(thisControl.getParameters().getToggledFillColor());
        this.vgToggledFillColor.setColorPicker(colorPicker);
        container.addView(this.vgToggledFillColor);

        this.vgBorderStrokeColor = new EditColorViewGroup(container.getContext());
        this.vgBorderStrokeColor.setTitle("Border Color");
        this.vgBorderStrokeColor.setColor(thisControl.getParameters().getBorderColor());
        this.vgBorderStrokeColor.setColorPicker(colorPicker);
        container.addView(this.vgBorderStrokeColor);

        this.vgFontColor = new EditColorViewGroup(container.getContext());
        this.vgFontColor.setTitle("Font Color");
        this.vgFontColor.setColor(thisControl.getParameters().getFontColor());
        this.vgFontColor.setColorPicker(colorPicker);
        container.addView(this.vgFontColor);
    }

    @Override
    public void save() {
        OSCToggleView thisControl = (OSCToggleView) super.mControl;
        if(vgPosition.valueChanged()) {
            thisControl.updatePosition(vgPosition.getLeftValue(), vgPosition.getTopValue(), vgPosition.getRightValue(), vgPosition.getBottomValue());
        }

        if(vgDimensions.valueChanged()) {
            thisControl.updateDimensions(vgDimensions.getWidthValue(), vgDimensions.getHeightValue());
        }

        if(vgToggledOffText.valueChanged()) {
            thisControl.getParameters().setDefaultText(vgToggledOffText.getValue());
        }

        if(vgToggledOnText.valueChanged()) {
            thisControl.getParameters().setToggledText(vgToggledOnText.getValue());
        }

        if(vgOSCToggleOn.valueChanged()) {
            thisControl.getParameters().setOSCToggleOn(vgOSCToggleOn.getValue());
        }

        if(vgOSCToggleOff.valueChanged()) {
            thisControl.getParameters().setOSCToggleOff(vgOSCToggleOff.getValue());
        }

        if(vgTriggerOnToggleOff.valueChanged()) {
            thisControl.getParameters().setFireOSCOnToggleOff(vgTriggerOnToggleOff.getValue());
        }

        if(vgDefaultFillColor.valueChanged()) {
            thisControl.setDefaultFillColor(vgDefaultFillColor.getColor());
        }

        if(vgToggledFillColor.valueChanged()) {
            thisControl.setToggledFillColor(vgToggledFillColor.getColor());
        }

        if(vgBorderStrokeColor.valueChanged()) {
            thisControl.setBorderColor(vgBorderStrokeColor.getColor());
        }

        if(vgFontColor.valueChanged()) {
            thisControl.setFontColor(vgFontColor.getColor());
        }

        thisControl.invalidate();
    }
}
