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
    private int mFontColor;
    private int mBorderColor;

    private int mCachedLeft;
    private int mCachedTop;
    private int mCachedRight;
    private int mCachedBottom;

    private int mCachedWidth;
    private int mCachedHeight;

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
        initPositionLayout();
        initDimensionsLayout();
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
                savePositionLayout();
                saveDimensionsLayout();
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

    private void initPositionLayout() {
        View layout = this.mRoot.findViewById(R.id.layPosition);

        this.mCachedLeft = this.mControl.getParameters().getLeft();
        EditText posLeft = (EditText) layout.findViewById(R.id.txtLeft);
        posLeft.setText("" + this.mCachedLeft);

        this.mCachedTop = this.mControl.getParameters().getTop();
        EditText posTop = (EditText) layout.findViewById(R.id.txtTop);
        posTop.setText("" + this.mCachedTop);

        this.mCachedRight = this.mControl.getParameters().getRight();
        EditText posRight = (EditText) layout.findViewById(R.id.txtRight);
        posRight.setText("" + this.mCachedRight);

        this.mCachedBottom = this.mControl.getParameters().getBottom();
        EditText posBottom = (EditText) layout.findViewById(R.id.txtBottom);
        posBottom.setText("" + this.mCachedBottom);
    }

    private void initDimensionsLayout() {
        View layout = this.mRoot.findViewById(R.id.layDimensions);

        this.mCachedWidth =  this.mControl.getParameters().getWidth();
        EditText txtWidth = (EditText) layout.findViewById(R.id.txtWidth);
        txtWidth.setText("" + this.mCachedWidth);

        this.mCachedHeight = this.mControl.getParameters().getHeight();
        EditText txtHeight = (EditText) layout.findViewById(R.id.txtHeight);
        txtHeight.setText("" + this.mCachedHeight);
    }

    private void initDefaultTextLayout() {
        View layout = this.mRoot.findViewById(R.id.layDefaultText);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("Default Text");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getDefaultText());
    }

    private void savePositionLayout() {
        View layout = this.mRoot.findViewById(R.id.layPosition);

        EditText txtLeft = (EditText) layout.findViewById(R.id.txtLeft);
        EditText txtTop = (EditText) layout.findViewById(R.id.txtTop);
        EditText txtRight = (EditText) layout.findViewById(R.id.txtRight);
        EditText txtBottom = (EditText) layout.findViewById(R.id.txtBottom);

        int left = Integer.parseInt(txtLeft.getText().toString());
        int top = Integer.parseInt(txtTop.getText().toString());
        int right = Integer.parseInt(txtRight.getText().toString());
        int bottom = Integer.parseInt(txtBottom.getText().toString());

        if(left != this.mCachedLeft || top != this.mCachedTop || right != this.mCachedRight || bottom != this.mCachedBottom) {
            this.mControl.updatePosition(left, top, right, bottom);
        }
    }

    private void saveDimensionsLayout() {
        View layout = this.mRoot.findViewById(R.id.layDimensions);

        EditText txtWidth = (EditText) layout.findViewById(R.id.txtWidth);
        EditText txtHeight = (EditText) layout.findViewById(R.id.txtHeight);

        int width = Integer.parseInt(txtWidth.getText().toString());
        int height = Integer.parseInt(txtHeight.getText().toString());

        if(width != this.mCachedWidth || height != this.mCachedHeight) {
            this.mControl.updateDimensions(width, height);
        }
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
        this.mControl.updateOSCToggleOn(txtText.getText().toString());
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
        this.mControl.updateOSCToggleOff(txtText.getText().toString());
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

    private void initBorderColorLayout() {
        View layout = this.mRoot.findViewById(R.id.layBorderColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Border Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mBorderColor = this.mControl.getParameters().getToggledFillColor();
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

    private void saveToggledFillColorLayout() {
        this.mControl.setToggledFillColor(this.mToggledColor);
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
