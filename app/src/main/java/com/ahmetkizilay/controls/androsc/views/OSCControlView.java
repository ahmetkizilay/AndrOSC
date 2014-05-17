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
	public abstract void updateDimensions(int left, int top, int right, int bottom);
	
	protected void showOSCControllerSettings() {
		mParent.showOSCControlSettingsFor(this);
	}

}
