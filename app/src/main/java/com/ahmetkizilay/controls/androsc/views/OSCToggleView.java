package com.ahmetkizilay.controls.androsc.views;

import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.params.OSCToggleParameters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

public class OSCToggleView extends OSCControlView {

	OSCToggleParameters mParams;
	
	private Paint mDefaultPaint;
	private Paint mToggledPaint;
	private Paint mBorderPaint;
	private Paint mTextPaint;
	
	private RectF buttonRect;
	
	private boolean mFingerDown = false;
	private SimpleDoubleTapDetector mDoubleTapDetector;
		
	public OSCToggleView(Context context, OSCViewGroup parent, OSCToggleParameters params) {
		super(context, parent);
		
		this.mParams = params;
		this.mDoubleTapDetector = new SimpleDoubleTapDetector();
		init();
	}
	
	private void init() {
		
		this.buttonRect = new RectF(0, 0, this.mParams.getWidth(), this.mParams.getHeight());

		this.mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mDefaultPaint.setColor(this.mParams.getDefaultFillColor());
		
		this.mToggledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mToggledPaint.setColor(this.mParams.getToggledFillColor());
		
		this.mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mBorderPaint.setStyle(Paint.Style.STROKE);
		this.mBorderPaint.setColor(this.mParams.getBorderColor());
		this.mBorderPaint.setStrokeWidth(2);
		
		this.mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mTextPaint.setTextSize(20);
		this.mTextPaint.setColor(Color.WHITE);
		this.mTextPaint.setTextAlign(Paint.Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		
		if(this.mFingerDown) {
			canvas.drawRoundRect(this.buttonRect, 8, 8, this.mToggledPaint);
			canvas.drawText(this.mParams.getToggledText(), this.mParams.getWidth() / 2, this.mParams.getHeight() / 2 + 5, this.mTextPaint);
		}
		else {
			canvas.drawRoundRect(this.buttonRect, 8, 8, this.mDefaultPaint);
			canvas.drawText(this.mParams.getDefaultText(), this.mParams.getWidth() / 2, this.mParams.getHeight() / 2 + 5, this.mTextPaint);
		}
		
		canvas.drawRoundRect(this.buttonRect, 8, 8, this.mBorderPaint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(this.mParent.isEditEnabled()) {
			return handleEditTouchEvent(event);
		}
		else {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				this.mFingerDown = !this.mFingerDown;
			}
			invalidate(0, 0, this.mParams.getWidth(), this.mParams.getHeight());
			return true;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(this.mParams.getWidth());
		int height = MeasureSpec.getSize(this.mParams.getHeight());
		
		setMeasuredDimension(width, height);	
	}
	
	@Override
	public void repositionView() {
		this.layout(this.mParams.getLeft(), this.mParams.getTop(), 
				    this.mParams.getRight(),
				    this.mParams.getBottom());
		
	}
	
	@Override
	public void buildJSONParamString(StringBuilder sb) {
		if(sb == null) throw new IllegalArgumentException("StringBuilder cannot be null");
		
		sb.append("{\n");
		sb.append("\ttype:\"toggle\",\n");
		sb.append("\tborderColor: [" + Color.red(this.mParams.getBorderColor()) + ", " + Color.green(this.mParams.getBorderColor()) + ", " + Color.blue(this.mParams.getBorderColor()) + "],\n");	
		sb.append("\tdefaultFillColor: [" + Color.red(this.mParams.getDefaultFillColor()) + ", " + Color.green(this.mParams.getDefaultFillColor()) + ", " + Color.blue(this.mParams.getDefaultFillColor()) + "],\n");
		sb.append("\tdefaultText: \"" + this.mParams.getDefaultText() + "\",\n");
		sb.append("\theight: " + this.mParams.getHeight() + ",\n");
		sb.append("\ttoggledFillColor: [" + Color.red(this.mParams.getToggledFillColor()) + ", " + Color.green(this.mParams.getToggledFillColor()) + ", " + Color.blue(this.mParams.getToggledFillColor()) + "],\n");
		sb.append("\ttoggledText: \"" + this.mParams.getToggledText() + "\",\n");
		sb.append("\twidth: " + this.mParams.getWidth() + ",\n");
		sb.append("\trect: [" + this.mParams.getLeft() + ", " + this.mParams.getTop() + ", " + this.mParams.getRight() + ", " + this.mParams.getBottom() + "]\n");
		sb.append("}");
	}
	
	@Override
	public void updateDimensions(int left, int top, int right, int bottom) {
		this.mParams.setLeft(left); this.mParams.setTop(top);
		this.mParams.setRight(right); this.mParams.setBottom(bottom);
		this.mParams.setWidth(right - left);
		this.mParams.setHeight(bottom - top);
		
		this.buttonRect.right = this.mParams.getWidth();
		this.buttonRect.bottom = this.mParams.getHeight();
		
		repositionView(); invalidate();
	}
	
	private int xDelta; private int yDelta;
	protected boolean handleEditTouchEvent(MotionEvent event) {
		if(this.mDoubleTapDetector.isThisDoubleTap(event)) {
			this.showOSCControllerSettings();
		}
		else {
			final int x = (int) event.getRawX();
		    final int y = (int) event.getRawY();
		    
		    switch(event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				xDelta = x - this.mParams.getLeft();
				yDelta = y - this.mParams.getTop();
				this.mParent.setSelectedControlForEdit(this);
				this.mParent.drawSelectionFrame(this.mParams.getLeft(), this.mParams.getTop(), this.mParams.getWidth(), this.mParams.getHeight());
				break;
			case MotionEvent.ACTION_UP:
				this.mParent.hideAlignLines();
	            break;
	        case MotionEvent.ACTION_POINTER_DOWN:
	            break;
	        case MotionEvent.ACTION_POINTER_UP:
	            break;
	        case MotionEvent.ACTION_MOVE:
	            this.mParams.setLeft(x - xDelta);
	            this.mParams.setTop(y - yDelta);
	            
	            this.mParams.setRight(this.mParams.getLeft() + this.mParams.getWidth());
	            this.mParams.setBottom(this.mParams.getTop() + this.mParams.getHeight());
	            
				this.mParent.drawSelectionFrame(this.mParams.getLeft(), this.mParams.getTop(), this.mParams.getWidth(), this.mParams.getHeight());
	            this.mParent.drawAlignLines(this.mParams.getLeft(), this.mParams.getTop(), this.mParams.getWidth(), this.mParams.getHeight());
	    		repositionView();		
	    		invalidate(0, 0, this.mParams.getWidth(), this.mParams.getHeight());
	            break;
			}
			
			repositionView();
			invalidate(0, 0, this.mParams.getWidth(), this.mParams.getHeight());		
		}
		return true;
	}
	
	public static OSCToggleParameters getDefaultParameters() {
		OSCToggleParameters params = new OSCToggleParameters();
		
		params.setBorderColor(Color.rgb(255,  0,  0));
		params.setDefaultFillColor(Color.rgb(20, 0, 0));
		params.setDefaultText("Toggle Off");
		params.setHeight(100);
		params.setToggledFillColor(Color.rgb(250, 0, 0));
		params.setToggledText("Toggle On");
		params.setWidth(100);
		params.setLeft(100);
		params.setTop(100);
		params.setRight(200);
		params.setBottom(200);
		
		return params;
	}
}
