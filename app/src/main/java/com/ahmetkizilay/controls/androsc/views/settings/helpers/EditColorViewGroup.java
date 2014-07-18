package com.ahmetkizilay.controls.androsc.views.settings.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.HSLColorPicker;

/**
 * This is a View Group with a title and a colored label.
 * Using the SimpleDoubleTapDetector class, double-clicking on the label brings up the
 * HSLColorPicker ViewGroup to select a color. Upon positive selectioni the colored label is
 * updated.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class EditColorViewGroup extends LinearLayout {
    public EditColorViewGroup(Context context) {
        super(context);
        init();
    }

    public EditColorViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.settings_coloredit_layout, this, true);

        final SimpleDoubleTapDetector doubleTapDetector = new SimpleDoubleTapDetector();

        this.lblColor = (TextView) findViewById(R.id.lblColorDisplay);
        lblColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (doubleTapDetector.isThisDoubleTap(event)) {
                    if(mColorPicker == null) {
                        return true;
                    }

                    if (mColorPicker.getVisibility() == View.VISIBLE) {
                        return true;
                    }

                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColor(mLastSelectedColor);
                    mColorPicker.setHSLColorPickerActionListener(new HSLColorPicker.HSLColorPickerActionListener() {
                        @Override
                        public void onColorSelected(int color) {
                            mLastSelectedColor = color;
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

    private int mCachedColor;
    private int mLastSelectedColor;
    private TextView lblColor;
    private HSLColorPicker mColorPicker;

    public void setColorPicker(HSLColorPicker colorPicker) {
        this.mColorPicker = colorPicker;
    }

    public void setColor(int color) {
        this.mCachedColor = color;
        this.mLastSelectedColor = color;
        this.lblColor.setBackgroundColor(color);
    }

    public void setTitle(String title) {
        TextView txtTitle = (TextView) findViewById(R.id.lblIdentifier);
        txtTitle.setText(title);
    }

    public boolean valueChanged() {
        return this.mCachedColor != this.mLastSelectedColor;
    }

    public int getColor() {
        return this.mLastSelectedColor;
    }
}
