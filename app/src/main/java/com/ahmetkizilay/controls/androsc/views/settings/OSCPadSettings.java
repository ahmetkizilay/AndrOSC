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
import com.ahmetkizilay.controls.androsc.views.OSCPadView;

public class OSCPadSettings {

    private HSLColorPicker mColorPicker;
    private View mRoot;
    private OSCPadView mControl;

    private OnSettingsClosedListener mListener = null;

    private int mDefaultFillColor;
    private int mBorderColor;
    private int mThumbFillColor;

    private int mCachedLeft;
    private int mCachedTop;
    private int mCachedRight;
    private int mCachedBottom;

    private int mCachedWidth;
    private int mCachedHeight;

    public static OSCPadSettings createInstance(View root, OSCPadView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        return new OSCPadSettings(root, control, colorPicker, listener);
    }

    private OSCPadSettings(View root, OSCPadView control, HSLColorPicker colorPicker, OnSettingsClosedListener listener) {
        this.mColorPicker = colorPicker;
        this.mRoot = root;
        this.mControl = control;
        this.mListener = listener;

        initActionsLayout();
        initPositionLayout();
        initDimensionsLayout();
        initOSCValueChanged();
        initOSCXValueRange();
        initOSCYValueRange();
        initDefaultFillColor();
        initBorderColor();
        initThumbFillColor();
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

    private void initOSCValueChanged() {
        View layout = this.mRoot.findViewById(R.id.layOSCValueChanged);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC Value Changed");

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        txtText.setText(this.mControl.getParameters().getOSCValueChanged());
    }

    private void initOSCXValueRange() {
        View layout = this.mRoot.findViewById(R.id.layOSCXValueRange);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC X Value Range");

        EditText txtMinText = (EditText) layout.findViewById(R.id.txtMinValue);
        txtMinText.setText("" + this.mControl.getParameters().getMinXValue());

        EditText txtMaxText = (EditText) layout.findViewById(R.id.txtMaxValue);
        txtMaxText.setText("" + this.mControl.getParameters().getMaxXValue());
    }

    private void initOSCYValueRange() {
        View layout = this.mRoot.findViewById(R.id.layOSCYValueRange);

        TextView lblText = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblText.setText("OSC Y Value Range");

        EditText txtMinText = (EditText) layout.findViewById(R.id.txtMinValue);
        txtMinText.setText("" + this.mControl.getParameters().getMinYValue());

        EditText txtMaxText = (EditText) layout.findViewById(R.id.txtMaxValue);
        txtMaxText.setText("" + this.mControl.getParameters().getMaxYValue());
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

    private void initBorderColor() {
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

    private void initThumbFillColor() {
        View layout = this.mRoot.findViewById(R.id.layThumbFillColor);

        TextView lblIdentifier = (TextView) layout.findViewById(R.id.lblIdentifier);
        lblIdentifier.setText("Thumb Fill Color");

        final TextView lblColor = (TextView) layout.findViewById(R.id.lblColorDisplay);
        this.mThumbFillColor = this.mControl.getParameters().getThumbColor();
        lblColor.setBackgroundColor(this.mThumbFillColor);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mThumbFillColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mThumbFillColor = color;
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
        lblTitle.setText(R.string.control_pad);

        Button btnSave = (Button) laySaveOrCancel.findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePositionLayout();
                saveDimensionsLayout();
                saveOSCValueChanged();
                saveOSCXValueRange();
                saveOSCYValueRange();
                saveDefaultFillColor();
                saveBorderColor();
                saveThumbFillColor();

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

    private void saveOSCValueChanged() {
        View layout = this.mRoot.findViewById(R.id.layOSCValueChanged);

        EditText txtText = (EditText) layout.findViewById(R.id.txtValue);
        this.mControl.getParameters().setOSCValueChanged(txtText.getText().toString());
    }

    private void saveOSCXValueRange() {
        View layout = this.mRoot.findViewById(R.id.layOSCXValueRange);

        EditText txtMinText = (EditText) layout.findViewById(R.id.txtMinValue);
        this.mControl.getParameters().setMinXValue(Double.parseDouble(txtMinText.getText().toString()));

        EditText txtMaxText = (EditText) layout.findViewById(R.id.txtMaxValue);
        this.mControl.getParameters().setMaxXValue(Double.parseDouble(txtMaxText.getText().toString()));
    }

    private void saveOSCYValueRange() {
        View layout = this.mRoot.findViewById(R.id.layOSCYValueRange);

        EditText txtMinText = (EditText) layout.findViewById(R.id.txtMinValue);
        this.mControl.getParameters().setMinYValue(Double.parseDouble(txtMinText.getText().toString()));

        EditText txtMaxText = (EditText) layout.findViewById(R.id.txtMaxValue);
        this.mControl.getParameters().setMaxYValue(Double.parseDouble(txtMaxText.getText().toString()));
    }

    private void saveDefaultFillColor() {
        this.mControl.setDefaultFillColor(this.mDefaultFillColor);
    }

    private void saveBorderColor() {
        this.mControl.setBorderColor(this.mBorderColor);
    }

    private void saveThumbFillColor() {
        this.mControl.setThumbColor(this.mThumbFillColor);
    }
}
