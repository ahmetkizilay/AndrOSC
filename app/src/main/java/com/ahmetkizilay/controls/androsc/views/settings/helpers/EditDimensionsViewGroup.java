package com.ahmetkizilay.controls.androsc.views.settings.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ahmetkizilay.controls.androsc.R;

/**
 * This ViewGroup contains a title, and two EditText views to display and edit width and height
 * values. Techically any two values can be displayed here, but for the practical purposes of this
 * project Wİdth and Height are used by default.
 * Note: Maybe I should rename this class to something like EditTwoValuesViewGroup...
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class EditDimensionsViewGroup extends LinearLayout {
    public EditDimensionsViewGroup(Context context) {
        super(context);
        init();
    }

    public EditDimensionsViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.settings_edit_dim_layout, this, true);

        this.etWidth = (EditText) findViewById(R.id.txtWidth);
        this.etHeight = (EditText) findViewById(R.id.txtHeight);
    }

    private int mCachedWidth;
    private int mCachedHeight;

    private EditText etWidth;
    private EditText etHeight;

    public void setDimensions(int width, int height) {
        this.mCachedWidth = width;
        this.mCachedHeight = height;

        this.etWidth.setText(this.mCachedWidth + "");

        this.etHeight.setText(this.mCachedHeight + "");
    }

    public boolean valueChanged() {
        return this.mCachedWidth != Integer.parseInt(this.etWidth.getText().toString()) ||
               this.mCachedHeight != Integer.parseInt(this.etHeight.getText().toString());
    }

    public int getWidthValue() {
        return Integer.parseInt(this.etWidth.getText().toString());
    }

    public int getHeightValue() {
        return Integer.parseInt(this.etHeight.getText().toString());
    }

}
