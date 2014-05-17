package com.ahmetkizilay.controls.androsc.utils;

import android.os.SystemClock;
import android.view.MotionEvent;

public class SimpleDoubleTapDetector {
	private final int MAX_TAP_DELAY = 500;
	private long mPrevTapTime;
	
	public boolean isThisDoubleTap(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) {
			return false;
		}
		
		long thisTapTime = SystemClock.uptimeMillis();
		if((thisTapTime - mPrevTapTime) > MAX_TAP_DELAY) {
			this.mPrevTapTime = thisTapTime;
			return false;
		}
		else {
			this.mPrevTapTime = thisTapTime;
			return true;
		}		
	}
	
}
