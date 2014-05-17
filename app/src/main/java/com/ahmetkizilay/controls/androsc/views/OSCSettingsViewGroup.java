package com.ahmetkizilay.controls.androsc.views;

import com.ahmetkizilay.controls.androsc.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;


public class OSCSettingsViewGroup extends LinearLayout {

	public OSCSettingsViewGroup(Context context) {
		super(context);		
	}

	
	public OSCSettingsViewGroup(Context context, AttributeSet attr) {
		super(context, attr);
		
		initialize();
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// super(changed, l, t, r, b);		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int height = 0;
		this.getChildAt(0).layout(0, 0, w, 120);
		this.getChildAt(1).layout(0, 120, w, 240);
		
	}
	
	public void initialize() {
		
		this.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.temp_layout, null));
		this.addView(LayoutInflater.from(this.getContext()).inflate(R.layout.temp_layout2, null));
	}
}
