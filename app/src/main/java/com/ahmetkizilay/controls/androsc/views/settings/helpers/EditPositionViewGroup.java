package com.ahmetkizilay.controls.androsc.views.settings.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ahmetkizilay.controls.androsc.R;

/**
 * This ViewGroup contains a title, and four EditText views to display and edit top, left, bottom
 * and right values. Techically any four values can be displayed here, but for the practical
 * purposes of this project the aforementioned labels are used by default.
 * if cbRetainWidth or cbRetainHeight is checked, the positions automatically update preserving the
 * width and height values.
 * Note: Maybe I should rename this class to something like EditFourValuesViewGroup...
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class EditPositionViewGroup extends LinearLayout {
    public EditPositionViewGroup(Context context) {
        super(context);
        init();
    }

    public EditPositionViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.settings_edit_pos_layout, this, true);

        this.etLeft = (EditText) findViewById(R.id.txtLeft);
        this.etLeft.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!cbRetainWidth.isChecked()) return false;

                String text = etLeft.getText().toString();
                if(text.length() > 0) {
                    etRight.setText((Integer.parseInt(text) + mWidth) + "");
                }

                return false;
            }
        });

        this.etTop = (EditText) findViewById(R.id.txtTop);
        this.etTop.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!cbRetainHeight.isChecked()) return false;

                String text = etTop.getText().toString();
                if(text.length() > 0) {
                    etBottom.setText((Integer.parseInt(text) + mHeight) + "");
                }

                return false;
            }
        });
        this.etRight = (EditText) findViewById(R.id.txtRight);
        this.etRight.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!cbRetainWidth.isChecked()) return false;

                String text = etRight.getText().toString();
                if(text.length() > 0) {
                    etLeft.setText((Integer.parseInt(text) - mWidth) + "");
                }

                return false;
            }
        });
        this.etBottom = (EditText) findViewById(R.id.txtBottom);
        this.etBottom.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!cbRetainHeight.isChecked()) return false;

                String text = etBottom.getText().toString();
                if(text.length() > 0) {
                    etTop.setText((Integer.parseInt(text) - mHeight) + "");
                }

                return false;
            }
        });

        this.cbRetainHeight = (CheckBox) findViewById(R.id.cbRetainHeight);
        this.cbRetainWidth = (CheckBox) findViewById(R.id.cbRetainWidth);

    }

    private int mCachedLeft;
    private int mCachedTop;
    private int mCachedRight;
    private int mCachedBottom;

    private EditText etLeft;
    private EditText etTop;
    private EditText etRight;
    private EditText etBottom;

    private int mWidth;
    private int mHeight;

    private CheckBox cbRetainWidth;
    private CheckBox cbRetainHeight;

    public void setPosition(int left, int top, int right, int bottom) {
        this.mCachedLeft = left;
        this.mCachedRight = right;
        this.mCachedTop = top;
        this.mCachedBottom = bottom;

        this.etLeft.setText(this.mCachedLeft + "");
        this.etTop.setText(this.mCachedTop + "");
        this.etRight.setText(this.mCachedRight + "");
        this.etBottom.setText(this.mCachedBottom + "");
    }

    /***
     * Required if retain width or retain height checkboxes are ticked...
     * @param width
     * @param height
     */
    public void setDimensions(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public boolean valueChanged() {
        return this.mCachedLeft != Integer.parseInt(this.etLeft.getText().toString()) ||
               this.mCachedTop != Integer.parseInt(this.etTop.getText().toString()) ||
               this.mCachedRight != Integer.parseInt(this.etRight.getText().toString()) ||
               this.mCachedBottom != Integer.parseInt(this.etBottom.getText().toString());
    }

    public int getLeftValue() {
        return Integer.parseInt(this.etLeft.getText().toString());
    }

    public int getTopValue() {
        return Integer.parseInt(this.etTop.getText().toString());
    }

    public int getRightValue() {
        return Integer.parseInt(this.etRight.getText().toString());
    }

    public int getBottomValue() {
        return Integer.parseInt(this.etBottom.getText().toString());
    }
}
