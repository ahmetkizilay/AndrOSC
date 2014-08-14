package com.ahmetkizilay.controls.androsc.views;

import com.ahmetkizilay.controls.androsc.osc.OSCWrapper;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.controls.androsc.views.params.OSCButtonParameters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class OSCButtonView extends OSCControlView {

	OSCButtonParameters mParams;
	
	private Paint mDefaultPaint;
    private Paint mBorderPaint;
	private Paint mPressedPaint;
	private Paint mTextPaint;
	
	private RectF buttonRect;
    private RectF borderRect;
	
	private boolean mFingerDown = false;

    private static final int BORDER_SIZE = 3;

	private SimpleDoubleTapDetector mDoubleTapDetector;
		
	public OSCButtonView(Context context, OSCViewGroup parent, OSCButtonParameters params) {
		super(context, parent);
		
		this.mParams = params;
		this.mDoubleTapDetector = new SimpleDoubleTapDetector();
		
		init();
	}

    public OSCButtonParameters getParameters() {
        return this.mParams;
    }
	
	private void init() {
		
		this.buttonRect = new RectF(BORDER_SIZE, BORDER_SIZE, this.mParams.getWidth() - BORDER_SIZE, this.mParams.getHeight() - BORDER_SIZE);
        this.borderRect = new RectF(0, 0, this.mParams.getWidth(), this.mParams.getHeight());

		this.mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mDefaultPaint.setColor(this.mParams.getDefaultFillColor());

        this.mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBorderPaint.setColor(this.mParams.getBorderColor());
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setStrokeWidth(BORDER_SIZE);

		this.mPressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mPressedPaint.setColor(this.mParams.getPressedFillColor());

		this.mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mTextPaint.setTextSize(20);
		this.mTextPaint.setColor(this.mParams.getFontColor());
		this.mTextPaint.setTextAlign(Paint.Align.CENTER);

        initialOSCParse();
	}

    public void setDefaultFillColor(int color) {
        this.mParams.setDefaultFillColor(color);
        this.mDefaultPaint.setColor(color);
    }

    public void setPressedFillColor(int color) {
        this.mParams.setPressedFillColor(color);
        this.mPressedPaint.setColor(color);
    }

    public void setFontColor(int color) {
        this.mParams.setFontColor(color);
        this.mTextPaint.setColor(color);
    }

    public void setBorderColor(int color) {
        this.mParams.setBorderColor(color);
        this.mBorderPaint.setColor(color);
    }

	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
				
		if(this.mFingerDown) {
			canvas.drawRoundRect(this.buttonRect, 8, 8, this.mPressedPaint);
		}
		else {
			canvas.drawRoundRect(this.buttonRect, 8, 8, this.mDefaultPaint);
		}

		canvas.drawText(this.mParams.getText(), this.mParams.getWidth() / 2, (this.mParams.getHeight() / 2) + 5, this.mTextPaint);

        canvas.drawRoundRect(this.borderRect, 8, 8, this.mBorderPaint);
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
				this.mFingerDown = true;
                fireOSCMessage();
			}
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				this.mFingerDown = false;
			}
	
			invalidate(0, 0, this.mParams.getWidth(), this.mParams.getHeight());
			return true;		
		}
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
		}
		return true;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(this.mParams.getWidth());
		int height = MeasureSpec.getSize(this.mParams.getHeight());
		
		setMeasuredDimension(width, height);	
	}
	
	public void repositionView() {
		this.layout(this.mParams.getLeft(), this.mParams.getTop(), 
				    this.mParams.getRight(), 
				    this.mParams.getBottom());
		
	}

	@Override
	public void updatePosition(int left, int top, int right, int bottom) {
		this.mParams.setLeft(left); this.mParams.setTop(top);
		this.mParams.setRight(right); this.mParams.setBottom(bottom);
		this.mParams.setWidth(right - left);
		this.mParams.setHeight(bottom - top);
		
		this.buttonRect.right = this.mParams.getWidth() - BORDER_SIZE;
		this.buttonRect.bottom = this.mParams.getHeight() - BORDER_SIZE;

        this.borderRect.right = this.mParams.getWidth();
        this.borderRect.bottom = this.mParams.getHeight();
		
		repositionView(); invalidate();
	}

    @Override
    public void updateDimensions(int width, int height) {
        this.mParams.setWidth(width);
        this.mParams.setHeight(height);
        this.mParams.setRight(this.mParams.getLeft() + width);
        this.mParams.setBottom(this.mParams.getTop() + height);

        this.buttonRect.right = this.mParams.getWidth() - BORDER_SIZE;
        this.buttonRect.bottom = this.mParams.getHeight() - BORDER_SIZE;

        this.borderRect.right = this.mParams.getWidth();
        this.borderRect.bottom = this.mParams.getHeight();

        repositionView(); invalidate();
    }

    private String oscMessage;
    private List<Object> oscArgs;

    public void updateOSCPressed(String value) {
        if(!value.equals(this.getParameters().getOSCButtonPressed())) {
            this.getParameters().setOSCButtonPressed(value);
            initialOSCParse();
        }
    }

    private void initialOSCParse() {
        String[] oscParts = this.getParameters().getOSCButtonPressed().split(" ");
        this.oscArgs = new ArrayList<Object>();
        for(int i = 1; i < oscParts.length; i += 1) {
            this.oscArgs.add(Utilities.simpleParse(oscParts[i]));
        }

        this.oscMessage = oscParts[0];
    }

    private void fireOSCMessage() {
        try {
            OSCWrapper.getInstance().sendOSC(this.oscMessage, this.oscArgs);
        }
        catch(Exception exp) {}
    }

	@Override
	public void buildJSONParamString(StringBuilder sb) {
		if(sb == null) throw new IllegalArgumentException("StringBuilder cannot be null");
		
		sb.append("{\n");
		sb.append("\ttype:\"button\",\n");
		sb.append("\tdefaultFillColor: [" + Color.red(this.mParams.getDefaultFillColor()) + ", " + Color.green(this.mParams.getDefaultFillColor()) + ", " + Color.blue(this.mParams.getDefaultFillColor()) + "],\n");
		sb.append("\theight: " + this.mParams.getHeight() + ",\n");
		sb.append("\tpressedFillColor: [" + Color.red(this.mParams.getPressedFillColor()) + ", " + Color.green(this.mParams.getPressedFillColor()) + ", " + Color.blue(this.mParams.getPressedFillColor()) + "],\n");
        sb.append("\tborderColor: [" + Color.red(this.mParams.getBorderColor()) + ", " + Color.green(this.mParams.getBorderColor()) + ", " + Color.blue(this.mParams.getBorderColor()) + "],\n");
        sb.append("\tfontColor: [" + Color.red(this.mParams.getFontColor()) + ", " + Color.green(this.mParams.getFontColor()) + ", " + Color.blue(this.mParams.getFontColor()) + "],\n");
		sb.append("\ttext: \"" + this.mParams.getText() + "\",\n");
		sb.append("\trect: [" + this.mParams.getLeft() + ", " + this.mParams.getTop() + ", " + this.mParams.getRight() + ", " + this.mParams.getBottom() + "],\n");
		sb.append("\twidth: " + this.mParams.getWidth() + ",\n");
        sb.append("\toscButtonPressed: \"" + this.mParams.getOSCButtonPressed() + "\"\n");
		sb.append("}");
	}
	
	public static OSCButtonParameters getDefaultParameters() {
		OSCButtonParameters params = new OSCButtonParameters();		
		
		params.setDefaultFillColor(Color.rgb(0, 0, 60));
		params.setHeight(100);
		params.setPressedFillColor(Color.rgb(255, 9, 0));
        params.setFontColor(Color.rgb(255, 255, 255));
        params.setBorderColor(Color.rgb(255, 0, 0));
		params.setText("button");
		params.setWidth(100);
		params.setLeft(100);
		params.setTop(100);
		params.setRight(200);
		params.setBottom(200);
        params.setOSCButtonPressed("button 1");

		return params;
	}

    public OSCButtonView cloneView() {
        OSCButtonParameters clonedParams = this.mParams.cloneParams();
        clonedParams.setLeft(clonedParams.getLeft() + 20);
        clonedParams.setTop(clonedParams.getTop() + 20);
        clonedParams.setRight(clonedParams.getRight() + 20);
        clonedParams.setBottom(clonedParams.getBottom() + 20);

        return new OSCButtonView(this.getContext(), super.mParent, clonedParams);
    }
}

