package com.ahmetkizilay.controls.androsc.views.settings.wrappers;

import android.view.ViewGroup;

import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCButtonView;
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
public class OSCButtonSettingsWrapper extends OSCControlSettingsWrapper {

    private EditPositionViewGroup vgPosition;
    private EditDimensionsViewGroup vgDimensions;
    private EditTextViewGroup vgText;
    private EditTextViewGroup vgOSCPressed;
    private EditColorViewGroup vgDefaultFillColor;
    private EditColorViewGroup vgPressedFillColor;
    private EditColorViewGroup vgBorderStrokeColor;
    private EditColorViewGroup vgFontColor;

    public OSCButtonSettingsWrapper(OSCButtonView control) {
        super(control);
    }

    @Override
    public void setup(ViewGroup container, HSLColorPicker colorPicker) {
        OSCButtonView thisControl = (OSCButtonView) super.mControl;

        this.vgPosition = new EditPositionViewGroup(container.getContext());
        this.vgPosition.setPosition(thisControl.getParameters().getLeft(), thisControl.getParameters().getTop(),
                thisControl.getParameters().getRight(), thisControl.getParameters().getBottom());
        this.vgPosition.setDimensions(thisControl.getParameters().getWidth(), thisControl.getParameters().getHeight());
        container.addView(this.vgPosition);

        this.vgDimensions = new EditDimensionsViewGroup(container.getContext());
        this.vgDimensions.setDimensions(thisControl.getParameters().getWidth(), thisControl.getParameters().getHeight());
        container.addView(this.vgDimensions);

        this.vgText = new EditTextViewGroup(container.getContext());
        this.vgText.setTitle("Display Text");
        this.vgText.setText(thisControl.getParameters().getText());
        container.addView(this.vgText);

        this.vgOSCPressed = new EditTextViewGroup(container.getContext());
        this.vgOSCPressed.setTitle("OSC: Button Pressed");
        this.vgOSCPressed.setText(thisControl.getParameters().getOSCButtonPressed());
        container.addView(this.vgOSCPressed);

        this.vgDefaultFillColor = new EditColorViewGroup(container.getContext());
        this.vgDefaultFillColor.setTitle("Default Fill Color");
        this.vgDefaultFillColor.setColor(thisControl.getParameters().getDefaultFillColor());
        this.vgDefaultFillColor.setColorPicker(colorPicker);
        container.addView(this.vgDefaultFillColor);

        this.vgPressedFillColor = new EditColorViewGroup(container.getContext());
        this.vgPressedFillColor.setTitle("Pressed Fill Color");
        this.vgPressedFillColor.setColor(thisControl.getParameters().getPressedFillColor());
        this.vgPressedFillColor.setColorPicker(colorPicker);
        container.addView(this.vgPressedFillColor);

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
        OSCButtonView thisControl = (OSCButtonView) super.mControl;
        if(vgPosition.valueChanged()) {
            thisControl.updatePosition(vgPosition.getLeftValue(), vgPosition.getTopValue(), vgPosition.getRightValue(), vgPosition.getBottomValue());
        }

        if(vgDimensions.valueChanged()) {
            thisControl.updateDimensions(vgDimensions.getWidthValue(), vgDimensions.getHeightValue());
        }

        if(vgText.valueChanged()) {
            thisControl.getParameters().setText(vgText.getValue());
        }

        if(vgOSCPressed.valueChanged()) {
            thisControl.getParameters().setOSCButtonPressed(vgOSCPressed.getValue());
        }

        if(vgDefaultFillColor.valueChanged()) {
            thisControl.setDefaultFillColor(vgDefaultFillColor.getColor());
        }

        if(vgPressedFillColor.valueChanged()) {
            thisControl.setPressedFillColor(vgPressedFillColor.getColor());
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
