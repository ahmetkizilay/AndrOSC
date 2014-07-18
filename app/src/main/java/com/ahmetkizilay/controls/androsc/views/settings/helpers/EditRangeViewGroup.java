package com.ahmetkizilay.controls.androsc.views.settings.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;

/**
 * This ViewGroup contains a title, and two EditText views to display and edit a range of values.
 * The EditText views hold double values.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class EditRangeViewGroup extends LinearLayout {
    public EditRangeViewGroup(Context context) {
        super(context);
        init();
    }

    public EditRangeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.settings_rangeedit_layout, this, true);

        this.etMinValue = (EditText) findViewById(R.id.txtMinValue);
        this.etMaxValue = (EditText) findViewById(R.id.txtMaxValue);
    }

    private double mCachedMinValue;
    private double mCachedMaxValue;

    private EditText etMinValue;
    private EditText etMaxValue;

    public void setRange(double minValue, double maxValue) {
        this.mCachedMaxValue = maxValue;
        this.mCachedMinValue = minValue;

        this.etMaxValue.setText(this.mCachedMaxValue + "");
        this.etMinValue.setText(this.mCachedMinValue + "");
    }

    public void setTitle(String title) {
        TextView txtTitle = (TextView) findViewById(R.id.lblIdentifier);
        txtTitle.setText(title);
    }

    public boolean valueChanged() {
        return this.mCachedMaxValue != Double.parseDouble(this.etMaxValue.getText().toString()) ||
               this.mCachedMinValue != Double.parseDouble(this.etMinValue.getText().toString());
    }

    public double getMaxValue() {
        return Double.parseDouble(this.etMaxValue.getText().toString());
    }

    public double getMinValue() {
        return Double.parseDouble(this.etMinValue.getText().toString());
    }

}
