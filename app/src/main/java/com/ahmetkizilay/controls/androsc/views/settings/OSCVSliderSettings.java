package com.ahmetkizilay.controls.androsc.views.settings;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCHorizontalSliderView;
import com.ahmetkizilay.controls.androsc.views.OSCVerticalSliderView;

public class OSCVSliderSettings {

    private HSLColorPicker mColorPicker;
    private View mRoot;
    private OSCVerticalSliderView mControl;

    private OnSettingsClosedListener mListener = null;

    private int mDefaultFillColor;
    private int mSlidedFillColor;
    private int mSliderBarFillColor;

    public static OSCVSliderSettings createInstance(View root, OSCVerticalSliderView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        return new OSCVSliderSettings(root, control, colorPicker, listener);
    }

    private OSCVSliderSettings(View root, OSCVerticalSliderView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        this.mColorPicker = colorPicker;
        this.mRoot = root;
        this.mControl = control;
        this.mListener = listener;

        initActionsLayout();
        initPosAndDimLayout();
        initOSCValueChanged();
        initOSCValueRange();
        initDefaultFillColor();
        initSlidedFillColor();
        initSliderBarFillColor();
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

    private void initOSCValueChanged() {
        View layout = this.mRoot.findViewById(R.id.layOSCValueChanged);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC Value Changed");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getOSCValueChanged());
    }

    private void initOSCValueRange() {
        View layout = this.mRoot.findViewById(R.id.layOSCValueRange);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC Value Range");

        EditText txtMinText = (EditText) layout.findViewById(R.id.txtMinValue);
        txtMinText.setText("" + this.mControl.getParameters().getMinValue());

        EditText txtMaxText = (EditText) layout.findViewById(R.id.txtMaxValue);
        txtMaxText.setText("" + this.mControl.getParameters().getMaxValue());
    }

    private void initDefaultFillColor() {
        View layout = this.mRoot.findViewById(R.id.layDefaultFillColor);

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

    private void initSlidedFillColor() {
        View layout = this.mRoot.findViewById(R.id.laySlidedFillColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Slided Fill Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mSlidedFillColor = this.mControl.getParameters().getSlidedFillColor();
        lblColor.setBackgroundColor(this.mSlidedFillColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mSlidedFillColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mSlidedFillColor = color;
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

    private void initSliderBarFillColor() {
        View layout = this.mRoot.findViewById(R.id.laySliderBarFillColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Slider Bar Fill Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mSliderBarFillColor = this.mControl.getParameters().getCursorFillColor();
        lblColor.setBackgroundColor(this.mSliderBarFillColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mSliderBarFillColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mSliderBarFillColor = color;
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

    private void initActionsLayout() {
        View laySaveOrCancel = this.mRoot.findViewById(R.id.laySaveOrCancel);
        TextView lblTitle = (TextView) this.mRoot.findViewById(R.id.lblTitle);
        lblTitle.setText(R.string.control_vslider);

        Button btnSave = (Button) laySaveOrCancel.findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveOSCValueChanged();
                saveOSCValueRange();
                saveDefaultFillColor();
                saveSlidedFillColor();
                saveSliderBarFillColor();

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

    private void saveOSCValueChanged() {
        View layout = this.mRoot.findViewById(R.id.layOSCValueChanged);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setOSCValueChanged(txtText.getText().toString());
    }

    private void saveOSCValueRange() {
        View layout = this.mRoot.findViewById(R.id.layOSCValueRange);

        EditText txtMinText = (EditText) layout.findViewById(R.id.txtMinValue);
        this.mControl.getParameters().setMinValue(Double.parseDouble(txtMinText.getText().toString()));

        EditText txtMaxText = (EditText) layout.findViewById(R.id.txtMaxValue);
        this.mControl.getParameters().setMaxValue(Double.parseDouble(txtMaxText.getText().toString()));
    }

    private void saveDefaultFillColor() {
        this.mControl.setDefaultFillColor(this.mDefaultFillColor);
    }

    private void saveSlidedFillColor() {
        this.mControl.setSlidedFillColor(this.mSlidedFillColor);
    }

    private void saveSliderBarFillColor() {
        this.mControl.setSliderBarFillColor(this.mSliderBarFillColor);
    }
}
