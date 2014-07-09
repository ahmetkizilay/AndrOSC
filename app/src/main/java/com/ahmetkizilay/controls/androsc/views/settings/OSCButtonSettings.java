package com.ahmetkizilay.controls.androsc.views.settings;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;
import com.ahmetkizilay.controls.androsc.views.OSCButtonView;

public class OSCButtonSettings {
    private HSLColorPicker mColorPicker;
    private View mRoot;
    private OSCButtonView mControl;

    private int mDefaultFillColor;
    private int mPressedFillColor;
    private int mBorderColor;
    private int mFontColor;

    private OnSettingsClosedListener mSettingsClosedCallback = null;

    public static OSCButtonSettings createInstance(View root, OSCButtonView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        return new OSCButtonSettings(root, control, colorPicker, listener);
    }

    private OSCButtonSettings(View root, OSCButtonView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        this.mColorPicker = colorPicker;
        this.mRoot = root;
        this.mControl = control;
        this.mSettingsClosedCallback = listener;

        initPosAndDimLayout();
        initTextLayout();
        initOSCPressedLayout();
        initDefaultFillColorLayout();
        initPressedFillColorLayout();
        initBorderColorLayout();
        initFontColorLayout();
        initActionsLayout();
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

    private void initTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layText);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("Display Text");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getText());
    }

    private void initDefaultFillColorLayout() {
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

    private void initPressedFillColorLayout() {
        this.mPressedFillColor = this.mControl.getParameters().getPressedFillColor();

        View layout = this.mRoot.findViewById(R.id.layPressedFillColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Button Pressed Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        lblColor.setBackgroundColor(this.mPressedFillColor);
        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mPressedFillColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mPressedFillColor = color;
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

    private void initBorderColorLayout() {
        this.mBorderColor = this.mControl.getParameters().getBorderColor();

        View layout = this.mRoot.findViewById(R.id.layBorderColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Border Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
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

    private void initOSCPressedLayout() {
        View layout = this.mRoot.findViewById(R.id.layOSCPressed);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC: Button Pressed");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getOSCButtonPressed());
    }

    private void initFontColorLayout() {
        this.mFontColor = this.mControl.getParameters().getFontColor();

        View layout = this.mRoot.findViewById(R.id.layFontColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Button Font Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
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

    private void initActionsLayout() {
        View laySaveOrCancel = this.mRoot.findViewById(R.id.laySaveOrCancel);
        Button btnSave = (Button) laySaveOrCancel.findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextLayout();
                saveOSCPressedLayout();
                saveDefaultFillColorLayout();
                savePressedFillColorLayout();
                saveBorderColorLayout();
                saveFontColor();

                mRoot.setVisibility(View.GONE);
                mControl.invalidate();

                if(mSettingsClosedCallback != null) {
                    mSettingsClosedCallback.onSettingsViewSaved();
                }
            }
        });

        Button btnClose = (Button) laySaveOrCancel.findViewById(R.id.btnCloseSettings);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRoot.setVisibility(View.GONE);
                if(mSettingsClosedCallback != null) {
                    mSettingsClosedCallback.onSettingsViewClosed();
                }
            }
        });
    }

    private void saveTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layText);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setText(txtText.getText().toString());
    }

    private void saveOSCPressedLayout() {
        View layout = this.mRoot.findViewById(R.id.layOSCPressed);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setOSCButtonPressed(txtText.getText().toString());
    }

    private void saveDefaultFillColorLayout() {
        this.mControl.setDefaultFillColor(this.mDefaultFillColor);
    }

    private void savePressedFillColorLayout() {
        this.mControl.setPressedFillColor(this.mPressedFillColor);
    }

    private void saveBorderColorLayout() {
        this.mControl.setBorderColor(this.mBorderColor);
    }

    private void saveFontColor() {
        this.mControl.setFontColor(this.mFontColor);
    }
}
