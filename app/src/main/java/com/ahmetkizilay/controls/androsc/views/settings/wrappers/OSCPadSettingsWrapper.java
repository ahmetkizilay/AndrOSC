package com.ahmetkizilay.controls.androsc.views.settings.wrappers;

import android.view.ViewGroup;

import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCPadView;
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
public class OSCPadSettingsWrapper extends OSCControlSettingsWrapper {

    private EditPositionViewGroup vgPosition;
    private EditDimensionsViewGroup vgDimensions;
    private EditTextViewGroup vgOSCValueChanged;
    private EditRangeViewGroup vgOSCXValueRange;
    private EditRangeViewGroup vgOSCYValueRange;
    private EditColorViewGroup vgDefaultFillColor;
    private EditColorViewGroup vgThumbFillColor;
    private EditColorViewGroup vgBorderStrokeColor;


    public OSCPadSettingsWrapper(OSCPadView control) {
        super(control);
    }

    @Override
    public void setup(ViewGroup container, HSLColorPicker colorPicker) {
        OSCPadView thisControl = (OSCPadView) super.mControl;

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

        this.vgOSCXValueRange = new EditRangeViewGroup(container.getContext());
        this.vgOSCXValueRange.setTitle("OSC X Value Range");
        this.vgOSCXValueRange.setRange(thisControl.getParameters().getMinXValue(), thisControl.getParameters().getMaxXValue());
        container.addView(this.vgOSCXValueRange);

        this.vgOSCYValueRange = new EditRangeViewGroup(container.getContext());
        this.vgOSCYValueRange.setTitle("OSC X Value Range");
        this.vgOSCYValueRange.setRange(thisControl.getParameters().getMinYValue(), thisControl.getParameters().getMaxYValue());
        container.addView(this.vgOSCYValueRange);

        this.vgDefaultFillColor = new EditColorViewGroup(container.getContext());
        this.vgDefaultFillColor.setTitle("Default Fill Color");
        this.vgDefaultFillColor.setColor(thisControl.getParameters().getDefaultFillColor());
        this.vgDefaultFillColor.setColorPicker(colorPicker);
        container.addView(this.vgDefaultFillColor);

        this.vgThumbFillColor = new EditColorViewGroup(container.getContext());
        this.vgThumbFillColor.setTitle("Thumb Fill Color");
        this.vgThumbFillColor.setColor(thisControl.getParameters().getThumbColor());
        this.vgThumbFillColor.setColorPicker(colorPicker);
        container.addView(this.vgThumbFillColor);

        this.vgBorderStrokeColor = new EditColorViewGroup(container.getContext());
        this.vgBorderStrokeColor.setTitle("Border Color");
        this.vgBorderStrokeColor.setColor(thisControl.getParameters().getBorderColor());
        this.vgBorderStrokeColor.setColorPicker(colorPicker);
        container.addView(this.vgBorderStrokeColor);
    }

    @Override
    public void save() {
        OSCPadView thisControl = (OSCPadView) super.mControl;

        if(vgPosition.valueChanged()) {
            thisControl.updatePosition(vgPosition.getLeftValue(), vgPosition.getTopValue(), vgPosition.getRightValue(), vgPosition.getBottomValue());
        }

        if(vgDimensions.valueChanged()) {
            thisControl.updateDimensions(vgDimensions.getWidthValue(), vgDimensions.getHeightValue());
        }

        if(vgOSCValueChanged.valueChanged()) {
            thisControl.getParameters().setOSCValueChanged(vgOSCValueChanged.getValue());
        }

        if(vgOSCXValueRange.valueChanged()) {
            thisControl.getParameters().setMinXValue(vgOSCXValueRange.getMinValue());
            thisControl.getParameters().setMaxXValue(vgOSCXValueRange.getMaxValue());
        }

        if(vgOSCYValueRange.valueChanged()) {
            thisControl.getParameters().setMinYValue(vgOSCYValueRange.getMinValue());
            thisControl.getParameters().setMaxYValue(vgOSCYValueRange.getMaxValue());
        }

        if(vgDefaultFillColor.valueChanged()) {
            thisControl.setDefaultFillColor(vgDefaultFillColor.getColor());
        }

        if(vgThumbFillColor.valueChanged()) {
            thisControl.setThumbColor(vgThumbFillColor.getColor());
        }

        if(vgBorderStrokeColor.valueChanged()) {
            thisControl.setBorderColor(vgBorderStrokeColor.getColor());
        }

        thisControl.invalidate();
    }
}
