package com.ahmetkizilay.controls.androsc.views.settings.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;

/**
 * This is a ViewGroup with a title and a checkbox.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class EditCheckBoxViewGroup extends LinearLayout {
    public EditCheckBoxViewGroup(Context context) {
        super(context);
        init();
    }

    public EditCheckBoxViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.settings_checkbox_layout, this, true);

        this.cbValue = (CheckBox) findViewById(R.id.cbValue);
    }

    private boolean mCachedValue;

    private CheckBox cbValue;

    public void setValue(boolean value) {
        this.mCachedValue = value;
        this.cbValue.setChecked(this.mCachedValue);
    }

    public void setTitle(String title) {
        TextView txtTitle = (TextView) findViewById(R.id.lblIdentifier);
        txtTitle.setText(title);
    }

    public boolean valueChanged() {
        return this.mCachedValue != this.cbValue.isChecked();
    }

    public boolean getValue() {
        return this.cbValue.isChecked();
    }
}
