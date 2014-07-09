package com.ahmetkizilay.controls.androsc.views;

import com.ahmetkizilay.controls.androsc.osc.OSCWrapper;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.params.OSCSliderParameters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OSCHorizontalSliderView extends OSCControlView {

	OSCSliderParameters mParams;
	
	private Paint mDefaultPaint;
	private Paint mSlidedPaint;
	private Paint mCursorPaint;
    private Paint mBorderPaint;
	
	private RectF mSliderRect;
	private RectF mCursorRect;
			
	private int mCursorPosition;
	private SimpleDoubleTapDetector mDoubleTapDetector;
    private DecimalFormat mDecimalFormat;

    private static final int BORDER_SIZE = 3;
	
	public OSCHorizontalSliderView(Context context, OSCViewGroup parent, OSCSliderParameters params) {
		super(context, parent);
		
		this.mParams = params;
		this.mCursorPosition = 0;
		this.mDoubleTapDetector = new SimpleDoubleTapDetector();
		initHorizontal();
        this.mDecimalFormat = new DecimalFormat("#.###");
		
	}
	
	private void initHorizontal() {
			
		this.mSliderRect = new RectF(0, 0, this.mParams.getWidth(), this.mParams.getHeight());
		this.mCursorRect = new RectF(15, 2, 25, this.mParams.getHeight() - 2);

		this.mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mDefaultPaint.setColor(this.mParams.getDefaultFillColor());
		
		this.mSlidedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mSlidedPaint.setColor(this.mParams.getSlidedFillColor());

		this.mCursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mCursorPaint.setColor(this.mParams.getCursorFillColor());

        this.mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBorderPaint.setColor(this.mParams.getBorderColor());
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setStrokeWidth(BORDER_SIZE);
	}
		
	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);

        this.mCursorPosition = Math.max(7, this.mCursorPosition);
        this.mCursorPosition = Math.min(this.mCursorPosition, this.mParams.getWidth() - 7);

        this.mSliderRect.top = BORDER_SIZE;
        this.mSliderRect.bottom = this.mParams.getHeight() - BORDER_SIZE;
		this.mSliderRect.left = BORDER_SIZE;
		this.mSliderRect.right = this.mCursorPosition;
		canvas.drawRoundRect(this.mSliderRect, 8, 8, this.mSlidedPaint);
		
		this.mSliderRect.left = this.mCursorPosition;
		this.mSliderRect.right = this.mParams.getWidth() - BORDER_SIZE;
		canvas.drawRoundRect(this.mSliderRect, 8, 8, this.mDefaultPaint);

        this.mCursorRect.left = this.mCursorPosition - 5;
        this.mCursorRect.right = this.mCursorPosition + 5;
		canvas.drawRoundRect(this.mCursorRect, 8, 8, this.mCursorPaint);

        this.mSliderRect.left = 0;
        this.mSliderRect.right = this.mParams.getWidth();
        this.mSliderRect.top = 0;
        this.mSliderRect.bottom = this.mParams.getHeight();

        canvas.drawRoundRect(this.mSliderRect, 8, 8, this.mBorderPaint);
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
			if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
				this.mCursorPosition = (int) event.getX();
                fireOSCMessage();
			}
			else {
				//System.out.println("WAT " + event.getAction());
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
			
			repositionView();
			invalidate(0, 0, this.mParams.getWidth(), this.mParams.getHeight());
		}
		return true;
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
				    this.mParams.getRight(), this.mParams.getBottom());
		
	}
	
	@Override
	public void updatePosition(int left, int top, int right, int bottom) {
		this.mParams.setLeft(left); this.mParams.setTop(top);
		this.mParams.setRight(right); this.mParams.setBottom(bottom);
		this.mParams.setWidth(right - left);
		this.mParams.setHeight(bottom - top);
		
		this.mSliderRect.right = this.mParams.getWidth();
		this.mSliderRect.bottom = this.mParams.getHeight();
		
		this.mCursorRect.bottom = this.mParams.getHeight() - 2;
		
		repositionView(); invalidate();
	}

    @Override
    public void updateDimensions(int width, int height) {
        this.mParams.setWidth(width);
        this.mParams.setHeight(height);
        this.mParams.setRight(this.mParams.getLeft() + width);
        this.mParams.setBottom(this.mParams.getTop() + height);

        this.mSliderRect.right = this.mParams.getWidth();
        this.mSliderRect.bottom = this.mParams.getHeight();

        this.mCursorRect.bottom = this.mParams.getHeight() - 2;

        repositionView(); invalidate();
    }

    public void setDefaultFillColor(int color) {
        this.mParams.setDefaultFillColor(color);
        this.mDefaultPaint.setColor(color);
    }

    public void setSlidedFillColor(int color) {
        this.mParams.setSlidedFillColor(color);
        this.mSlidedPaint.setColor(color);
    }

    public void setSliderBarFillColor(int color) {
        this.mParams.setCursorFillColor(color);
        this.mCursorPaint.setColor(color);
    }

    public void setBorderColor(int color) {
        this.mParams.setBorderColor(color);
        this.mBorderPaint.setColor(color);
    }

	@Override
	public void buildJSONParamString(StringBuilder sb) {
		if(sb == null) throw new IllegalArgumentException("StringBuilder cannot be null");
		
		sb.append("{\n");
		sb.append("\ttype:\"hslider\",\n");
		sb.append("\tcursorFillColor: [" + Color.red(this.mParams.getCursorFillColor()) + ", " + Color.green(this.mParams.getCursorFillColor()) + ", " + Color.blue(this.mParams.getCursorFillColor()) + "],\n");
		sb.append("\tdefaultFillColor: [" + Color.red(this.mParams.getDefaultFillColor()) + ", " + Color.green(this.mParams.getDefaultFillColor()) + ", " + Color.blue(this.mParams.getDefaultFillColor()) + "],\n");
        sb.append("\tborderColor: [" + Color.red(this.mParams.getBorderColor()) + ", " + Color.green(this.mParams.getBorderColor()) + ", " + Color.blue(this.mParams.getBorderColor()) + "],\n");
		sb.append("\theight: " + this.mParams.getHeight() + ",\n");
		sb.append("\tslidedFillColor: [" + Color.red(this.mParams.getSlidedFillColor()) + ", " + Color.green(this.mParams.getSlidedFillColor()) + ", " + Color.blue(this.mParams.getSlidedFillColor()) + "],\n");		
		sb.append("\twidth: " + this.mParams.getWidth() + ",\n");
		sb.append("\trect: [" + this.mParams.getLeft() + ", " + this.mParams.getTop() + ", " + this.mParams.getRight() + ", " + this.mParams.getBottom() + "],\n");
        sb.append("\tmaxValue: " + this.mParams.getMaxValue() + ",\n");
        sb.append("\tminValue: " + this.mParams.getMinValue() + ",\n");
        sb.append("\tOSCValueChanged: \"" + this.mParams.getOSCValueChanged() + "\"\n");

		sb.append("}");
	}

    public OSCSliderParameters getParameters() {
        return this.mParams;
    }
	
	public static OSCSliderParameters getDefaultParameters() {
		OSCSliderParameters params = new OSCSliderParameters();

		params.setCursorFillColor(Color.rgb(255, 0, 0));
		params.setDefaultFillColor(Color.rgb(20, 0, 0));
		params.setHeight(120);
		params.setSlidedFillColor(Color.rgb(100,  0, 0));
		params.setWidth(480);
		params.setLeft(100);
		params.setTop(100);
		params.setRight(580);
		params.setBottom(220);
        params.setOSCValueChanged("/hslider $1");
        params.setMinValue(0.);
        params.setMaxValue(1.);
		
		return params;
	}

    private void fireOSCMessage() {
        try {

            double relPosition;
            if(this.mCursorPosition < 0) {
                relPosition = 0.0;
            } else if(this.mCursorPosition > this.mParams.getWidth()) {
                relPosition = 1.0;
            } else {
                relPosition = (double) this.mCursorPosition / (double) this.mParams.getWidth();
            }

            double calcPosition = (relPosition * (this.mParams.getMaxValue() - this.mParams.getMinValue())) + this.mParams.getMinValue();

            String oscMessage = this.mParams.getOSCValueChanged();
            oscMessage = oscMessage.replace("$1", this.mDecimalFormat.format(calcPosition));

            String[] oscParts = oscMessage.split(" ");
            ArrayList<Object> oscArgs = new ArrayList<Object>();
            for(int i = 1; i < oscParts.length; i += 1) {
                oscArgs.add(oscParts[i]);
            }

            OSCWrapper.getInstance().sendOSC(oscParts[0], oscArgs);
        }
        catch(Exception exp) {}
    }

    public OSCHorizontalSliderView cloneView() {
        OSCSliderParameters clonedParams = this.mParams.cloneParams();
        clonedParams.setLeft(clonedParams.getLeft() + 20);
        clonedParams.setTop(clonedParams.getTop() + 20);
        clonedParams.setRight(clonedParams.getRight() + 20);
        clonedParams.setBottom(clonedParams.getBottom() + 20);

        return new OSCHorizontalSliderView(this.getContext(), super.mParent, clonedParams);
    }
}