package com.ahmetkizilay.controls.androsc.views;

import android.content.Context;
import android.view.View;

public abstract class OSCControlView extends View {
	protected OSCViewGroup mParent;
	public OSCControlView(Context context, OSCViewGroup parent) {
		super(context);
		this.mParent = parent;
	}

	public abstract void repositionView();
	public abstract void buildJSONParamString(StringBuilder sb);
	public abstract void updatePosition(int left, int top, int right, int bottom);
    public abstract void updateDimensions(int width, int height);
    public abstract OSCControlView cloneView();
	
	protected void showOSCControllerSettings() {
		mParent.showOSCControlSettingsFor(this);
	}

}
