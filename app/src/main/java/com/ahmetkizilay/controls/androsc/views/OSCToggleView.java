package com.ahmetkizilay.controls.androsc.views;

import com.ahmetkizilay.controls.androsc.osc.OSCWrapper;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.params.OSCToggleParameters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

public class OSCToggleView extends OSCControlView {

	OSCToggleParameters mParams;
	
	private Paint mDefaultPaint;
	private Paint mToggledPaint;
	private Paint mTextPaint;
    private Paint mBorderPaint;
	
	private RectF buttonRect;
	
	private boolean mFingerDown = false;
	private SimpleDoubleTapDetector mDoubleTapDetector;

    private static final int BORDER_SIZE = 3;

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

		this.mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mTextPaint.setTextSize(20);
		this.mTextPaint.setColor(this.mParams.getFontColor());
		this.mTextPaint.setTextAlign(Paint.Align.CENTER);

        this.mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setColor(this.mParams.getBorderColor());
        this.mBorderPaint.setStrokeWidth(BORDER_SIZE);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);

        this.buttonRect.left = BORDER_SIZE;
        this.buttonRect.top = BORDER_SIZE;
        this.buttonRect.right = this.mParams.getWidth() - BORDER_SIZE;
        this.buttonRect.bottom = this.mParams.getHeight() - BORDER_SIZE;

		if(this.mFingerDown) {
			canvas.drawRoundRect(this.buttonRect, 8, 8, this.mToggledPaint);
			canvas.drawText(this.mParams.getToggledText(), this.mParams.getWidth() / 2, this.mParams.getHeight() / 2 + 5, this.mTextPaint);
		}
		else {
			canvas.drawRoundRect(this.buttonRect, 8, 8, this.mDefaultPaint);
			canvas.drawText(this.mParams.getDefaultText(), this.mParams.getWidth() / 2, this.mParams.getHeight() / 2 + 5, this.mTextPaint);
		}

        this.buttonRect.left = 0;
        this.buttonRect.top = 0;
        this.buttonRect.right = this.mParams.getWidth();
        this.buttonRect.bottom = this.mParams.getHeight();
        canvas.drawRoundRect(this.buttonRect, 8, 8, this.mBorderPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if(this.mParent.isSettingsEnabled()) {
            return true;
        }

		if(this.mParent.isEditEnabled()) {
			return handleEditTouchEvent(event);
		}
		else {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				this.mFingerDown = !this.mFingerDown;

                if(this.mFingerDown) {
                    fireOSCToggleOn();
                }
                else {
                    fireOSCToggleOff();
                }
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
		sb.append("\tdefaultFillColor: [" + Color.red(this.mParams.getDefaultFillColor()) + ", " + Color.green(this.mParams.getDefaultFillColor()) + ", " + Color.blue(this.mParams.getDefaultFillColor()) + "],\n");
		sb.append("\tdefaultText: \"" + this.mParams.getDefaultText() + "\",\n");
        sb.append("\tfontColor: [" + Color.red(this.mParams.getFontColor()) + ", " + Color.green(this.mParams.getFontColor()) + ", " + Color.blue(this.mParams.getFontColor()) + "],\n");
        sb.append("\tborderColor: [" + Color.red(this.mParams.getBorderColor()) + ", " + Color.green(this.mParams.getBorderColor()) + ", " + Color.blue(this.mParams.getBorderColor()) + "],\n");
        sb.append("\theight: " + this.mParams.getHeight() + ",\n");
		sb.append("\ttoggledFillColor: [" + Color.red(this.mParams.getToggledFillColor()) + ", " + Color.green(this.mParams.getToggledFillColor()) + ", " + Color.blue(this.mParams.getToggledFillColor()) + "],\n");
		sb.append("\ttoggledText: \"" + this.mParams.getToggledText() + "\",\n");
		sb.append("\twidth: " + this.mParams.getWidth() + ",\n");
		sb.append("\trect: [" + this.mParams.getLeft() + ", " + this.mParams.getTop() + ", " + this.mParams.getRight() + ", " + this.mParams.getBottom() + "],\n");
        sb.append("\toscToggleOn: \"" + this.mParams.getOSCToggleOn() + "\",\n");
        sb.append("\toscToggleOff: \"" + this.mParams.getOSCToggleOff() + "\",\n");
        sb.append("\tfireOSCOnToggleOff: \"" + this.mParams.getFireOSCOnToggleOff() + "\"\n");
		sb.append("}");
	}

    private void fireOSCToggleOn() {
        try {
            String[] oscParts = this.mParams.getOSCToggleOn().split(" ");
            ArrayList<Object> oscArgs = new ArrayList<Object>();
            for(int i = 1; i < oscParts.length; i += 1) {
                oscArgs.add(oscParts[i]);
            }

            OSCWrapper.getInstance().sendOSC(oscParts[0], oscArgs);
        }
        catch(Exception exp) {}
    }

    private void fireOSCToggleOff() {

        if(!this.getParameters().getFireOSCOnToggleOff()) {
            return;
        }

        try {
            String[] oscParts = this.mParams.getOSCToggleOff().split(" ");
            ArrayList<Object> oscArgs = new ArrayList<Object>();
            for(int i = 1; i < oscParts.length; i += 1) {
                oscArgs.add(oscParts[i]);
            }

            OSCWrapper.getInstance().sendOSC(oscParts[0], oscArgs);
        }
        catch(Exception exp) {}
    }
	
	@Override
	public void updatePosition(int left, int top, int right, int bottom) {
		this.mParams.setLeft(left); this.mParams.setTop(top);
		this.mParams.setRight(right); this.mParams.setBottom(bottom);
		this.mParams.setWidth(right - left);
		this.mParams.setHeight(bottom - top);
		
		this.buttonRect.right = this.mParams.getWidth();
		this.buttonRect.bottom = this.mParams.getHeight();
		
		repositionView(); invalidate();
	}

    @Override
    public void updateDimensions(int width, int height) {
        this.mParams.setWidth(width);
        this.mParams.setHeight(height);
        this.mParams.setRight(this.mParams.getLeft() + width);
        this.mParams.setBottom(this.mParams.getTop() + height);

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
		
		params.setDefaultFillColor(Color.rgb(20, 0, 0));
		params.setDefaultText("Toggle Off");
		params.setHeight(100);
		params.setToggledFillColor(Color.rgb(250, 0, 0));
        params.setBorderColor(Color.rgb(250, 0, 0));
		params.setToggledText("Toggle On");
		params.setWidth(100);
		params.setLeft(100);
		params.setTop(100);
		params.setRight(200);
		params.setBottom(200);
        params.setOSCToggleOn("/toggle 1");
        params.setOSCToggleOff("/toggle 0");
        params.setFontColor(Color.rgb(255, 255, 255));
        params.setFireOSCOnToggleOff(true);
		
		return params;
	}

    public OSCToggleParameters getParameters() {
        return this.mParams;
    }

    public void setDefaultFillColor(int color) {
        this.mParams.setDefaultFillColor(color);
        this.mDefaultPaint.setColor(color);
    }

    public void setToggledFillColor(int color) {
        this.mParams.setToggledFillColor(color);
        this.mToggledPaint.setColor(color);
    }

    public void setFontColor(int color) {
        this.mParams.setFontColor(color);
        this.mTextPaint.setColor(color);
    }

    public void setBorderColor(int color) {
        this.mParams.setBorderColor(color);
        this.mBorderPaint.setColor(color);
    }

    public OSCToggleView cloneView() {
        OSCToggleParameters clonedParams = this.mParams.cloneParams();
        clonedParams.setLeft(clonedParams.getLeft() + 20);
        clonedParams.setTop(clonedParams.getTop() + 20);
        clonedParams.setRight(clonedParams.getRight() + 20);
        clonedParams.setBottom(clonedParams.getBottom() + 20);

        return new OSCToggleView(this.getContext(), super.mParent, clonedParams);
    }

}
