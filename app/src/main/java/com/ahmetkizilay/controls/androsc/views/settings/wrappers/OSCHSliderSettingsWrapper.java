package com.ahmetkizilay.controls.androsc.views.settings.wrappers;

import android.view.ViewGroup;

import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCHorizontalSliderView;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditColorViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditDimensionsViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditPositionViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditRangeViewGroup;
import com.ahmetkizilay.controls.androsc.views.settings.helpers.EditTextViewGroup;

/**
 * This class dynamically adds modular ViewGroups to the containing ViewGroup.
 * All the fields in this class refer to one configurable parameter of the corresponding OSC
 * control. <code>setup</code> and <code>save</code> methods are called from OSCSettingsViewGroup.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class OSCHSliderSettingsWrapper extends OSCControlSettingsWrapper {

    private EditPositionViewGroup vgPosition;
    private EditDimensionsViewGroup vgDimensions;
    private EditTextViewGroup vgOSCValueChanged;
    private EditRangeViewGroup vgOSCValueRange;
    private EditColorViewGroup vgDefaultFillColor;
    private EditColorViewGroup vgSlidedFillColor;
    private EditColorViewGroup vgSliderBarFillColor;
    private EditColorViewGroup vgBorderStrokeColor;


    public OSCHSliderSettingsWrapper(OSCHorizontalSliderView control) {
        super(control);
    }

    @Override
    public void setup(ViewGroup container, HSLColorPicker colorPicker) {
        OSCHorizontalSliderView thisControl = (OSCHorizontalSliderView) super.mControl;

        this.vgPosition = new EditPositionViewGroup(container.getContext());
        this.vgPosition.setPosition(thisControl.getParameters().getLeft(), thisControl.getParameters().getTop(),
                thisControl.getParameters().getRight(), thisControl.getParameters().getBottom());
        this.vgPosition.setDimensions(thisControl.getParameters().getWidth(), thisControl.getParameters().getHeight());
        container.addView(this.vgPosition);

        this.vgDimensions = new EditDimensionsViewGroup(container.getContext());
        this.vgDimensions.setDimensions(thisControl.getParameters().getWidth(), thisControl.getParameters().getHeight());
        container.addView(this.vgDimensions);

        this.vgOSCValueChanged = new EditTextViewGroup(container.getContext());
        this.vgOSCValueChanged.setTitle("OSC: Value Changed");
        this.vgOSCValueChanged.setText(thisControl.getParameters().getOSCValueChanged());
        container.addView(this.vgOSCValueChanged);

        this.vgOSCValueRange = new EditRangeViewGroup(container.getContext());
        this.vgOSCValueRange.setTitle("OSC Value Range");
        this.vgOSCValueRange.setRange(thisControl.getParameters().getMinValue(), thisControl.getParameters().getMaxValue());
        container.addView(this.vgOSCValueRange);

        this.vgDefaultFillColor = new EditColorViewGroup(container.getContext());
        this.vgDefaultFillColor.setTitle("Default Fill Color");
        this.vgDefaultFillColor.setColor(thisControl.getParameters().getDefaultFillColor());
        this.vgDefaultFillColor.setColorPicker(colorPicker);
        container.addView(this.vgDefaultFillColor);

        this.vgSlidedFillColor = new EditColorViewGroup(container.getContext());
        this.vgSlidedFillColor.setTitle("Slided Fill Color");
        this.vgSlidedFillColor.setColor(thisControl.getParameters().getSlidedFillColor());
        this.vgSlidedFillColor.setColorPicker(colorPicker);
        container.addView(this.vgSlidedFillColor);

        this.vgSliderBarFillColor = new EditColorViewGroup(container.getContext());
        this.vgSliderBarFillColor.setTitle("Slider Bar Fill Color");
        this.vgSliderBarFillColor.setColor(thisControl.getParameters().getCursorFillColor());
        this.vgSliderBarFillColor.setColorPicker(colorPicker);
        container.addView(this.vgSliderBarFillColor);

        this.vgBorderStrokeColor = new EditColorViewGroup(container.getContext());
        this.vgBorderStrokeColor.setTitle("Border Color");
        this.vgBorderStrokeColor.setColor(thisControl.getParameters().getBorderColor());
        this.vgBorderStrokeColor.setColorPicker(colorPicker);
        container.addView(this.vgBorderStrokeColor);
    }

    @Override
    public void save() {
        OSCHorizontalSliderView thisControl = (OSCHorizontalSliderView) super.mControl;

        if(vgPosition.valueChanged()) {
            thisControl.updatePosition(vgPosition.getLeftValue(), vgPosition.getTopValue(), vgPosition.getRightValue(), vgPosition.getBottomValue());
        }

        if(vgDimensions.valueChanged()) {
            thisControl.updateDimensions(vgDimensions.getWidthValue(), vgDimensions.getHeightValue());
        }

        if(vgOSCValueChanged.valueChanged()) {
            thisControl.getParameters().setOSCValueChanged(vgOSCValueChanged.getValue());
        }

        if(vgOSCValueRange.valueChanged()) {
            thisControl.getParameters().setMinValue(vgOSCValueRange.getMinValue());
            thisControl.getParameters().setMaxValue(vgOSCValueRange.getMaxValue());
        }

        if(vgDefaultFillColor.valueChanged()) {
            thisControl.setDefaultFillColor(vgDefaultFillColor.getColor());
        }

        if(vgSlidedFillColor.valueChanged()) {
            thisControl.setSlidedFillColor(vgSlidedFillColor.getColor());
        }

        if(vgSliderBarFillColor.valueChanged()) {
            thisControl.setSliderBarFillColor(vgSliderBarFillColor.getColor());
        }

        if(vgBorderStrokeColor.valueChanged()) {
            thisControl.setBorderColor(vgBorderStrokeColor.getColor());
        }

        thisControl.invalidate();
    }
}
