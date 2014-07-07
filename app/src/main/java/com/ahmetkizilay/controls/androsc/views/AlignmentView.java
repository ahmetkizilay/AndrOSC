package com.ahmetkizilay.controls.androsc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class AlignmentView extends View {
	
	private Paint mDefaultPaint;
	
	private int nwX, nwY, seX, seY;
	private boolean mPaint;

    private static final int PADDING = 1;

	public AlignmentView(Context context) {
		super(context);
		
		init();
	}
	
	private void init() {
		

		this.mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mDefaultPaint.setColor(Color.rgb(255, 255, 255));
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		
		if(this.mPaint) {
			canvas.drawLine(this.nwX, this.nwY, 0, this.nwY, this.mDefaultPaint);
			canvas.drawLine(this.nwX, this.nwY, this.nwX, 0, this.mDefaultPaint);
			
			canvas.drawLine(this.seX, this.nwY, this.seX, 0, this.mDefaultPaint);
			canvas.drawLine(this.seX, this.nwY, this.getWidth(), this.nwY, this.mDefaultPaint);	
			
			canvas.drawLine(this.seX, this.seY, this.seX, this.getHeight(), this.mDefaultPaint);
			canvas.drawLine(this.seX, this.seY, this.getWidth(), this.seY, this.mDefaultPaint);	
			
			canvas.drawLine(this.nwX, this.seY, 0, this.seY, this.mDefaultPaint);
			canvas.drawLine(this.nwX, this.seY, this.nwX, this.getHeight(), this.mDefaultPaint);	
		}	
	}
	
	public void setAlignDimensions(int top, int left, int width, int height) {
		this.nwX = top - PADDING; this.nwY = left - PADDING;
		this.seX = top + width + PADDING; this.seY = left + height + PADDING;
		this.mPaint = true;
	}
	
	public void stopPaint() {
		this.mPaint = false;
	}
}
