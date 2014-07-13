package com.ahmetkizilay.controls.androsc.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;


public class HSLColorPicker extends LinearLayout implements SeekBar.OnSeekBarChangeListener {

    private int[] mHueGradientColors = new int[11];
    private int[] mSaturationGradientColors = new int[11];
    private int[] mLuminosityGradientColors = new int[11];

    private GradientDrawable mHueGradientDrawable;
    private GradientDrawable mSaturationGradientDrawable;
    private GradientDrawable mLuminosityGradientDrawable;

    private SeekBar mHueSelector;
    private SeekBar mSaturationSelector;
    private SeekBar mLuminositySelector;

    private EditText mHueEditText;
    private EditText mSaturationEditText;
    private EditText mLuminosityEditText;

    private Button mSelectButton;
    private Button mDoneButton;

    private View mColorDisplayLayout;

    private HSLColorPickerActionListener mColorPickerActionCallback;

    private boolean mCloseOnSelect = true;
    private int mColor;
    private float[] mColorHSL;

    public HSLColorPicker(Context context, int color) {
        super(context);
        this.mColor = color;

        init();
    }

    public HSLColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HSLColorPicker, 0, 0);
        try {
            this.mColor = ta.getColor(R.styleable.HSLColorPicker_color, Color.RED);
            this.mCloseOnSelect = ta.getBoolean(R.styleable.HSLColorPicker_closeOnSelect, true);
        }
        finally {
            ta.recycle();
        }

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.colorpicker_layout, this);

        this.setBackgroundResource(R.drawable.colorpicker_background);

        this.mColorHSL = rgbToHsl(this.mColor);

        this.mHueGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                this.mHueGradientColors);
        this.mHueGradientDrawable.setCornerRadius(8);

        this.mSaturationGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                this.mSaturationGradientColors);
        this.mSaturationGradientDrawable.setCornerRadius(8);

        this.mLuminosityGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                this.mLuminosityGradientColors);
        this.mLuminosityGradientDrawable.setCornerRadius(8);

        // this is used for showing the selected color
        this.mColorDisplayLayout = findViewById(R.id.cpDisplayColor);
        this.mColorDisplayLayout.setBackgroundColor(this.mColor);

        // LUMINOSITY SELECTOR
        LinearLayout luminosityLayout = (LinearLayout) findViewById(R.id.cpLuminosity);
        TextView luminosityTag = (TextView) luminosityLayout.getChildAt(0);
        luminosityTag.setText("L");

        this.mLuminositySelector = (SeekBar) luminosityLayout.getChildAt(1);
        this.mLuminositySelector.setProgress((int) (100.f * this.mColorHSL[2]));
        this.mLuminositySelector.setContentDescription("Luminosity Selector");
        this.mLuminositySelector.setOnSeekBarChangeListener(this);

        this.mLuminosityEditText = (EditText) luminosityLayout.getChildAt(2);
        this.mLuminosityEditText.setText(Integer.toString(this.mLuminositySelector.getProgress()));
        this.mLuminosityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int val;
                try {
                    val = Integer.parseInt(editable.toString());
                }
                catch(NumberFormatException nfe) {
                    return;
                }

                if(val < 0 || val > 100) {
                    return;
                }

                if(HSLColorPicker.this.mLuminosityEditText.isFocused()) {
                    HSLColorPicker.this.mLuminosityEditText.setSelection(HSLColorPicker.this.mLuminosityEditText.getText().length());
                }

                if(HSLColorPicker.this.mLuminositySelector.getProgress() == val) {
                    return;
                }

                HSLColorPicker.this.mLuminositySelector.setProgress(val);
                handleProgressChange(HSLColorPicker.this.mLuminositySelector);
            }
        });

        // SATURATION SELECTOR
        LinearLayout saturationLayout = (LinearLayout) findViewById(R.id.cpSaturation);
        TextView saturationTag = (TextView) saturationLayout.getChildAt(0);
        saturationTag.setText("S");

        this.mSaturationSelector = (SeekBar) saturationLayout.getChildAt(1);
        this.mSaturationSelector.setProgress((int) (100.f * this.mColorHSL[1]));
        this.mSaturationSelector.setContentDescription("Saturation Selector");
        this.mSaturationSelector.setOnSeekBarChangeListener(this);

        this.mSaturationEditText = (EditText) saturationLayout.getChildAt(2);
        this.mSaturationEditText.setText(Integer.toString(this.mSaturationSelector.getProgress()));
        this.mSaturationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int val;
                try {
                    val = Integer.parseInt(editable.toString());
                }
                catch(NumberFormatException nfe) {
                    return;
                }

                if(val < 1 || val > 100) {
                    return;
                }

                if(HSLColorPicker.this.mSaturationEditText.isFocused()) {
                    HSLColorPicker.this.mSaturationEditText.setSelection(HSLColorPicker.this.mSaturationEditText.getText().length());
                }

                if(HSLColorPicker.this.mSaturationSelector.getProgress() == val) {
                    return;
                }

                HSLColorPicker.this.mSaturationSelector.setProgress(val);
                handleProgressChange(HSLColorPicker.this.mSaturationSelector);
            }
        });

        // HUE SELECTOR
        LinearLayout hueLayout = (LinearLayout) findViewById(R.id.cpHue);
        TextView hueTag = (TextView) hueLayout.getChildAt(0);
        hueTag.setText("H");

        this.mHueSelector = (SeekBar) hueLayout.getChildAt(1);
        this.mHueSelector.setProgress((int) (100.f * this.mColorHSL[0]));
        this.mHueSelector.setContentDescription("Hue Selector");
        this.mHueSelector.setOnSeekBarChangeListener(this);

        this.mHueEditText = (EditText) hueLayout.getChildAt(2);
        this.mHueEditText.setText(Integer.toString(this.mHueSelector.getProgress()));
        this.mHueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int val;
                try {
                    val = Integer.parseInt(editable.toString());
                }
                catch(NumberFormatException nfe) {
                    return;
                }

                if(val < 0 || val > 100) {
                    return;
                }


                if(HSLColorPicker.this.mHueEditText.isFocused()) {
                    HSLColorPicker.this.mHueEditText.setSelection(HSLColorPicker.this.mHueEditText.getText().length());
                }

                if(HSLColorPicker.this.mHueSelector.getProgress() == val) {
                    return;
                }

                HSLColorPicker.this.mHueSelector.setProgress(val);
                handleProgressChange(HSLColorPicker.this.mHueSelector);
            }
        });

        // ACTION LAYOUT
        LinearLayout actionButtonsLayout = (LinearLayout) findViewById(R.id.cpAction);

        this.mSelectButton = (Button) actionButtonsLayout.getChildAt(0);
        this.mSelectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(HSLColorPicker.this.mColorPickerActionCallback != null) {
                    HSLColorPicker.this.mColorPickerActionCallback.onColorSelected(HSLColorPicker.this.mColor);
                    if(HSLColorPicker.this.mCloseOnSelect) {
                        HSLColorPicker.this.mColorPickerActionCallback.onCloseNotified();
                    }
                }
            }
        });

        this.mDoneButton = (Button) actionButtonsLayout.getChildAt(1);
        this.mDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(HSLColorPicker.this.mColorPickerActionCallback != null) {
                    HSLColorPicker.this.mColorPickerActionCallback.onCloseNotified();
                }
            }
        });

        updateSeekBarGradients(this.mColorHSL[0], Math.max(0.01f, this.mColorHSL[1]));
    }

    private void updateSeekBarGradients(float h, float s) {
        updateHueGradientBackground(1.0f, 0.5f);
        updateSaturationGradientBackground(h, 0.5f);
        updateLuminosityGradientBackground(h, s);
    }

    private void updateHueGradientBackground(float s, float l) {
        updateHueGradientColors(s, l);

        GradientDrawable hueGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                this.mHueGradientColors);
        hueGradientDrawable.setCornerRadius(8);

        if(Build.VERSION.SDK_INT >= 16) {
            this.mHueSelector.setBackground(hueGradientDrawable);
        }
        else {
            this.mHueSelector.setBackgroundDrawable(hueGradientDrawable);
        }
    }

    private void updateSaturationGradientBackground(float h, float l) {
        updateSaturationGradientColors(h, l);

        GradientDrawable saturationGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                this.mSaturationGradientColors);
        saturationGradientDrawable.setCornerRadius(8);

        if(Build.VERSION.SDK_INT >= 16) {
            this.mSaturationSelector.setBackground(saturationGradientDrawable);
        }
        else {
            this.mSaturationSelector.setBackgroundDrawable(saturationGradientDrawable);
        }
    }

    private void updateLuminosityGradientBackground(float h, float s) {
        updateLuminosityGradientColors(h, s);

        GradientDrawable luminosityGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                this.mLuminosityGradientColors);
        luminosityGradientDrawable.setCornerRadius(8);

        if(Build.VERSION.SDK_INT >= 16) {
            this.mLuminositySelector.setBackground(luminosityGradientDrawable);
        }
        else {
            this.mLuminositySelector.setBackgroundDrawable(luminosityGradientDrawable);
        }
    }

    private void  handleProgressChange(SeekBar seekBar) {
        this.mColorHSL[0] = this.mHueSelector.getProgress() * 0.01f;
        this.mColorHSL[1] = Math.max(0.01f, this.mSaturationSelector.getProgress() * 0.01f);
        this.mColorHSL[2] = this.mLuminositySelector.getProgress() * 0.01f;

        this.mColor = hslToRgb(this.mColorHSL[0], this.mColorHSL[1], this.mColorHSL[2]);
        this.mColorDisplayLayout.setBackgroundColor(this.mColor);

        if(seekBar == this.mHueSelector) {
            updateSaturationGradientBackground(this.mColorHSL[0], 0.5f);
            updateLuminosityGradientBackground(this.mColorHSL[0], this.mColorHSL[1]);
        } else if(seekBar.equals(this.mSaturationSelector)) {
            updateLuminosityGradientBackground(this.mColorHSL[0], this.mColorHSL[1]);
        }
    }

    /**
     * source: http://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion
     * @param h hue value
     * @param s saturation value
     * @param l luminosity value
     * @return rgb equivalent of hsl color value
     */
    private static int hslToRgb(float h, float s, float l) {
        float r, g, b;

        if(s == 0.0f) {
            r = g = b = 1;
        }
        else {
            float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
            float p = 2f * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }

        return Color.rgb((int) Math.floor(r * 255f), (int) Math.floor(g * 255f), (int) Math.floor(b * 255f));
    }

    private static float hueToRgb(float p, float q, float t) {
        if(t < 0) t += 1;
        if(t > 1) t -= 1;
        if(t < 1f/6f) return p + (q - p) * 6f * t;
        if(t < 1f/2f) return q;
        if(t < 2f/3f) return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }

    private static float[] rgbToHsl(float r, float g, float b) {
        r = r / 255.0f;
        g = g / 255.0f;
        b = b / 255.0f;

        float max = Math.max(Math.max(r, g), b), min = Math.min(Math.min(r, g), b);
        float h = 0, s, l = (max + min) / 2f;

        if(max == min){
            h = s = 0f; // achromatic
        }else{
            float d = max - min;
            s = l > 0.5f ? d / (2f - max - min) : d / (max + min);

            if(max == r) {
                h = (g - b) / d + (g < b ? 6f : 0f);
            } else if(max == g) {
                h = (b - r) / d + 2f;
            }
            else if(max == b) {
                h = (r - g) / d + 4f;
            }

            h = h / 6f;
        }

        return new float[] {h, s, l};
    }

    private static float[] rgbToHsl(int color){
        return rgbToHsl(Color.red(color), Color.green(color), Color.blue(color));
    }

    private void updateHueGradientColors(float s, float l) {
        for(int i = 0; i < this.mHueGradientColors.length; i++) {
            this.mHueGradientColors[i] = hslToRgb(0.1f * i, s, l);
        }
    }

    private void updateSaturationGradientColors(float h, float l) {
        this.mSaturationGradientColors[0] = hslToRgb(h, 0.01f, l);
        for(int i = 1; i < this.mSaturationGradientColors.length; i++) {
            this.mSaturationGradientColors[i] = hslToRgb(h, 0.1f * i, l);
        }
    }

    private void updateLuminosityGradientColors(float h, float s) {
        for(int i = 0; i < this.mLuminosityGradientColors.length; i++) {
            this.mLuminosityGradientColors[i] = hslToRgb(h, s, 0.1f * i);
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser) {
            if(seekBar == this.mHueSelector) {
                this.mHueEditText.setText(Integer.toString(this.mHueSelector.getProgress()));
            } else if(seekBar == this.mSaturationSelector) {
                this.mSaturationEditText.setText(Integer.toString(this.mSaturationSelector.getProgress()));
            } else if(seekBar == this.mLuminositySelector) {
                this.mLuminosityEditText.setText(Integer.toString(this.mLuminositySelector.getProgress()));
            }

            handleProgressChange(seekBar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setColor(int color) {
        this.mColor = color;
        this.mColorHSL = rgbToHsl(this.mColor);

        this.mColorDisplayLayout.setBackgroundColor(this.mColor);

        this.mLuminositySelector.setProgress((int) (100.f * this.mColorHSL[2]));
        this.mSaturationSelector.setProgress((int) (100.f * this.mColorHSL[1]));
        this.mHueSelector.setProgress((int) (100.f * this.mColorHSL[0]));

        this.mLuminosityEditText.setText(Integer.toString(this.mLuminositySelector.getProgress()));
        this.mSaturationEditText.setText(Integer.toString(this.mSaturationSelector.getProgress()));
        this.mHueEditText.setText(Integer.toString(this.mHueSelector.getProgress()));

        updateSeekBarGradients(this.mColorHSL[0], Math.max(0.01f, this.mColorHSL[1]));
    }

    public void setHSLColorPickerActionListener(HSLColorPickerActionListener listener) {
        this.mColorPickerActionCallback = listener;
    }

    public interface HSLColorPickerActionListener {
        public void onColorSelected(int color);
        public void onCloseNotified();
    }
}
