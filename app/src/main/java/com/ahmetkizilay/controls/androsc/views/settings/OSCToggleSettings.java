package com.ahmetkizilay.controls.androsc.views.settings;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCToggleView;

public class OSCToggleSettings {

    private HSLColorPicker mColorPicker;
    private View mRoot;
    private OSCToggleView mControl;

    private int mDefaultFillColor;
    private int mToggledColor;
    private int mBorderColor;
    private int mFontColor;

    private OnSettingsClosedListener mListener = null;

    public static OSCToggleSettings createInstance(View root, OSCToggleView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        return new OSCToggleSettings(root, control, colorPicker, listener);
    }

    private OSCToggleSettings(View root, OSCToggleView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        this.mColorPicker = colorPicker;
        this.mRoot = root;
        this.mControl = control;
        this.mListener = listener;

        initActionsLayout();
        initPosAndDimLayout();
        initDefaultTextLayout();
        initToggledTextLayout();
        initOSCToggleOn();
        initOSCToggleOff();
        initFireOSCOnToggleOff();
        initDefaultFillColorLayout();
        initToggledFillColorLayout();
        initBorderColorLayout();
        initFontColorLayout();
    }

    private void initActionsLayout() {
        View laySaveOrCancel = this.mRoot.findViewById(R.id.laySaveOrCancel);
        TextView lblTitle = (TextView) this.mRoot.findViewById(R.id.lblTitle);
        lblTitle.setText(R.string.control_toggle);

        Button btnSave = (Button) laySaveOrCancel.findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDefaultTextLayout();
                saveToggledTextLayout();
                saveOSCToggleOn();
                saveOSCToggleOff();
                saveFireOSCOnToggleOff();
                saveDefaultFillColorLayout();
                saveToggledFillColorLayout();
                saveBorderColorLayout();
                saveFontColorLayout();

                mRoot.setVisibility(View.GONE);
                mControl.invalidate();

                if(mListener != null) {
                    mListener.onSettingsViewSaved();
                }
            }
        });

        Button btnClose = (Button) laySaveOrCancel.findViewById(R.id.btnCloseSettings);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRoot.setVisibility(View.GONE);
                if(mListener != null) {
                    mListener.onSettingsViewClosed();
                }
            }
        });
    }

    private void initPosAndDimLayout() {
        View layout = this.mRoot.findViewById(R.id.layPosDim);

        TextView xPos = (TextView) layout.findViewById(R.id.lblXPos);
        xPos.setText("x: " + this.mControl.getParameters().getLeft());

        TextView yPos = (TextView) layout.findViewById(R.id.lblYPos);
        yPos.setText("y: " + this.mControl.getParameters().getTop());

        TextView wDim = (TextView) layout.findViewById(R.id.lblWidth);
        wDim.setText("w: " + this.mControl.getParameters().getWidth());

        TextView hDim = (TextView) layout.findViewById(R.id.lblHeight);
        hDim.setText("h: " + this.mControl.getParameters().getHeight());
    }

    private void initDefaultTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layDefaultText);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("Default Text");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getDefaultText());
    }

    private void saveDefaultTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layDefaultText);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setDefaultText(txtText.getText().toString());
    }

    private void initToggledTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layToggledText);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("Toggled Text");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getToggledText());
    }

    private void saveToggledTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layToggledText);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setToggledText(txtText.getText().toString());
    }

    private void initOSCToggleOn() {
        View layout = this.mRoot.findViewById(R.id.layOSCToggleOn);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC Toggle On");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getOSCToggleOn());
    }

    private void saveOSCToggleOn() {
        View layout = this.mRoot.findViewById(R.id.layOSCToggleOn);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setOSCToggleOn(txtText.getText().toString());
    }

    private void initOSCToggleOff() {
        View layout = this.mRoot.findViewById(R.id.layOSCToggleOff);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC Toggle Off");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getOSCToggleOff());
    }

    private void initFireOSCOnToggleOff() {
        View layout = this.mRoot.findViewById(R.id.layTiggerOnToggleOff);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("Fire OSC On Toggle Off");

        CheckBox cbValue = (CheckBox) layout.findViewById(R.id.cbValue);
        cbValue.setChecked(this.mControl.getParameters().getFireOSCOnToggleOff());

    }

    private void saveOSCToggleOff() {
        View layout = this.mRoot.findViewById(R.id.layOSCToggleOff);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setOSCToggleOff(txtText.getText().toString());
    }

    private void saveFireOSCOnToggleOff() {
        View layout = this.mRoot.findViewById(R.id.layTiggerOnToggleOff);

        CheckBox cbValue = (CheckBox) layout.findViewById(R.id.cbValue);
        this.mControl.getParameters().setFireOSCOnToggleOff(cbValue.isChecked());
    }

    private void initDefaultFillColorLayout() {
        View layout = this.mRoot.findViewById(R.id.layDefaultColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Default Fill Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mDefaultFillColor = this.mControl.getParameters().getDefaultFillColor();
        lblColor.setBackgroundColor(this.mDefaultFillColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mDefaultFillColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mDefaultFillColor = color;
                            lblColor.setBackgroundColor(color);
                        }

                        @Override
                        public void onCloseNotified() {
                            mColorPicker.setVisibility(View.GONE);
                        }
                    });
                }
                return true;
            }
        });
    }

    private void saveDefaultFillColorLayout() {
        this.mControl.setDefaultFillColor(this.mDefaultFillColor);
    }

    private void initToggledFillColorLayout() {
        View layout = this.mRoot.findViewById(R.id.layToggledColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Toggled Fill Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mToggledColor = this.mControl.getParameters().getToggledFillColor();
        lblColor.setBackgroundColor(this.mToggledColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mToggledColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mToggledColor = color;
                            lblColor.setBackgroundColor(color);
                        }

                        @Override
                        public void onCloseNotified() {
                            mColorPicker.setVisibility(View.GONE);
                        }
                    });
                }
                return true;
            }
        });
    }

    private void saveToggledFillColorLayout() {
        this.mControl.setToggledFillColor(this.mToggledColor);
    }

    private void initBorderColorLayout() {
        View layout = this.mRoot.findViewById(R.id.layBorderColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Border Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mBorderColor = this.mControl.getParameters().getBorderColor();
        lblColor.setBackgroundColor(this.mBorderColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mBorderColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mBorderColor = color;
                            lblColor.setBackgroundColor(color);
                        }

                        @Override
                        public void onCloseNotified() {
                            mColorPicker.setVisibility(View.GONE);
                        }
                    });
                }
                return true;
            }
        });
    }

    private void saveBorderColorLayout() {
        this.mControl.setBorderColor(this.mBorderColor);
    }

    private void initFontColorLayout() {
        View layout = this.mRoot.findViewById(R.id.layFontColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Font Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mFontColor = this.mControl.getParameters().getFontColor();
        lblColor.setBackgroundColor(this.mFontColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mFontColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mFontColor = color;
                            lblColor.setBackgroundColor(color);
                        }

                        @Override
                        public void onCloseNotified() {
                            mColorPicker.setVisibility(View.GONE);
                        }
                    });
                }
                return true;
            }
        });
    }

    private void saveFontColorLayout() {
        this.mControl.setFontColor(this.mFontColor);
    }

}
