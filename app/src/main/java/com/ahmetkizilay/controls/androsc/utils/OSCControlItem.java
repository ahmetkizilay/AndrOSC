package com.ahmetkizilay.controls.androsc.utils;


public final class OSCControlItem {
	public static final String OSC_BUTTON = "OSC_BUTTON";
	public static final String OSC_TOGGLE = "OSC_TOGGLE";
	public static final String OSC_HSLIDER = "OSC_HSLIDER";
	public static final String OSC_VSLIDER = "OSC_VSLIDER";
    public static final String OSC_PAD = "OSC_PAD";
	
	public static String[] getItems() {
		return new String[] { OSCControlItem.OSC_BUTTON, OSCControlItem.OSC_TOGGLE, OSCControlItem.OSC_HSLIDER, OSCControlItem.OSC_VSLIDER, OSCControlItem.OSC_PAD };
	}
}