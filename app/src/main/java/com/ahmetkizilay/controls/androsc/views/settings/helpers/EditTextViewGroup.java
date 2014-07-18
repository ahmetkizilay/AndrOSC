package com.ahmetkizilay.controls.androsc.views.settings.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;

/**
 * This ViewGroup contains a title, and a EditText view to view and display a text value.
 *
 * Created by ahmetkizilay on 18.07.2014.
 */
public class EditTextViewGroup extends LinearLayout {
    public EditTextViewGroup(Context context) {
        super(context);
        init();
    }

    public EditTextViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.settings_textedit_layout, this, true);

        this.etValue = (EditText) findViewById(R.id.txtValue);
    }

    private String mCachedValue;

    private EditText etValue;

    public void setText(String value) {
        mCachedValue = value;
        this.etValue.setText(this.mCachedValue);
    }

    public void setTitle(String title) {
        TextView txtTitle = (TextView) findViewById(R.id.lblIdentifier);
        txtTitle.setText(title);
    }

    public boolean valueChanged() {
        return !this.mCachedValue.equals(this.etValue.getText().toString());
    }

    public String getValue() {
        return this.etValue.getText().toString();
    }
}
