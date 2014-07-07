package com.ahmetkizilay.controls.androsc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class SelectionFrameView extends View {
	
	private static final int FINGER_MARGIN = 24;
	
	private Paint mDefaultPaint;
	
	private int mLeft, mTop, mRight, mBottom;
	private boolean mPaint;

    private static final int MARGIN = 10;
    private static final int PADDING = 1;

    private static final int MARGIN_PLUS_PADDING = MARGIN + PADDING;
    private static final int MARGIN_MINUS_PADDING = MARGIN - PADDING;

	
	private OSCViewGroup mParent;
	
	private RectF mOvalRect;
	public SelectionFrameView(Context context, OSCViewGroup parent) {
		super(context);
		this.mParent = parent;
		init();
	}
	
	private void init() {
		
		this.mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mDefaultPaint.setStyle(Paint.Style.STROKE);
		this.mDefaultPaint.setStrokeWidth(2);
		this.mDefaultPaint.setColor(Color.rgb(190, 190, 190));

		this.mOvalRect = new RectF(this.mLeft, this.mTop, this.mLeft + 8, this.mTop + 8);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);

		if(this.mPaint) {
			
			canvas.drawRect(mLeft, mTop, mRight, mBottom, mDefaultPaint);
			
			this.mOvalRect.top = this.mTop - 6;
			this.mOvalRect.bottom = this.mTop + 6;
			this.mOvalRect.left = this.mLeft - 6;
			this.mOvalRect.right = this.mLeft + 6;
			canvas.drawArc(this.mOvalRect, 0, 360, false, this.mDefaultPaint);
			
			this.mOvalRect.top = this.mTop - 6;
			this.mOvalRect.bottom = this.mTop + 6;
			this.mOvalRect.left = this.mRight - 6;
			this.mOvalRect.right = this.mRight + 6;
			canvas.drawArc(this.mOvalRect, 0, 360, false, this.mDefaultPaint);
			
			this.mOvalRect.top = this.mBottom - 6;
			this.mOvalRect.bottom = this.mBottom + 6;
			this.mOvalRect.left = this.mLeft - 6;
			this.mOvalRect.right = this.mLeft + 6;
			canvas.drawArc(this.mOvalRect, 0, 360, false, this.mDefaultPaint);
			
			this.mOvalRect.top = this.mBottom - 6;
			this.mOvalRect.bottom = this.mBottom + 6;
			this.mOvalRect.left = this.mRight - 6;
			this.mOvalRect.right = this.mRight + 6;
			canvas.drawArc(this.mOvalRect, 0, 360, false, this.mDefaultPaint);			
		}	
	}

	private int mResizeAction = 0;
	private int deltaX, deltaY;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.mParent.isEditEnabled()) {			
			return false;
		}
		
		final int x = (int) event.getX();
	    final int y = (int) event.getY();
	    	    
		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		    
			if(this.mTop >= y && (this.mTop - y) < FINGER_MARGIN) {
				if(this.mLeft < x && this.mRight > x) {					
					this.mResizeAction = 1;
				}
			}
			else if(this.mBottom <= y && (y - this.mBottom) <  FINGER_MARGIN) {
				if(this.mLeft < x && this.mRight > x) {					
					this.mResizeAction = 2;
				}
			}
			else if(this.mLeft >= x && (this.mLeft - x) < FINGER_MARGIN) {
				if(this.mTop < y && this.mBottom > y) {					
					this.mResizeAction = 3;
				}
			}
			else if(this.mRight <= x && (x - this.mRight) < FINGER_MARGIN) {
				if(this.mTop < y && this.mBottom > y) {					
					this.mResizeAction = 4;
				}
			}
			else {
				this.mResizeAction = 0;
			}
			
			switch(this.mResizeAction) {
        	case 1:
        		this.deltaY = (int)event.getRawY() - this.mTop; 
        		break;
        	case 2:
        		this.deltaY = (int)event.getRawY() - this.mBottom; 
        		break;
        	case 3:
        		this.deltaX = (int)event.getRawX() - this.mLeft; 
        		break;
        	case 4:
        		this.deltaX = (int)event.getRawX() - this.mRight; 
        		break;
        	default:
        			break;
        	}
			
			break;
		case MotionEvent.ACTION_UP:
			this.mResizeAction = 0;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            break;
        case MotionEvent.ACTION_POINTER_UP:
            break;
        case MotionEvent.ACTION_MOVE:
        	switch(this.mResizeAction) {
        	case 1:
        		this.mTop = (int)event.getRawY() - this.deltaY; 
        		break;
        	case 2:
        		this.mBottom = (int)event.getRawY() - this.deltaY; 
        		break;
        	case 3:
        		this.mLeft = (int)event.getRawX() - this.deltaX; 
        		break;
        	case 4:
        		this.mRight = (int)event.getRawX() - this.deltaX; 
        		break;
        	default:
        			break;
        	}
        	
        	invalidate();
        	
        	this.mParent.resizeSelectedControl(this.mLeft + 4, this.mTop + 4, this.mRight - 4, this.mBottom - 4);
            break;
            
		}
		
		return true;
	}
	
	public void setFrameDimensions(int left, int top, int width, int height) {
		// this.mLeft = left - 4; this.mTop = top - 4; this.mRight = left + width + 4; this.mBottom = top + height + 4;

//        this.mRelLeft = left;
//        this.mRelTop = top;
//        this.mRelWidth = width;
//        this.mRelHeight = height;

        this.layout(left - MARGIN, top - MARGIN, left + width + MARGIN, top + height + MARGIN);
        this.mLeft = MARGIN_MINUS_PADDING;
        this.mTop = MARGIN_MINUS_PADDING;
        this.mRight = width + MARGIN_PLUS_PADDING;
        this.mBottom = height + MARGIN_PLUS_PADDING;

        this.mPaint = true;
	}
	
	public void stopPaint() {
		if(this.mPaint) {
			this.mPaint = false;
			this.invalidate();
		}
	}
}
