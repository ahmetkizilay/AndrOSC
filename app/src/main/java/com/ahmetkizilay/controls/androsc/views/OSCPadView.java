package com.ahmetkizilay.controls.androsc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.ahmetkizilay.controls.androsc.osc.OSCWrapper;
import com.ahmetkizilay.controls.androsc.utils.SimpleDoubleTapDetector;
import com.ahmetkizilay.controls.androsc.views.params.OSCPadParameters;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OSCPadView extends OSCControlView {

	OSCPadParameters mParams;

	private Paint mDefaultPaint;
	private Paint mThumbPaint;
	private Paint mBorderPaint;

	private RectF buttonRect;
    private RectF borderRect;
    private RectF mThumbRect;

    private int mThumbSize = 40;
    private int mHalfThumbSize = mThumbSize / 2;
    private int mThumbX = mHalfThumbSize;
    private int mThumbY = mHalfThumbSize;


	private SimpleDoubleTapDetector mDoubleTapDetector;
    private DecimalFormat mDecimalFormat;

    private static final int BORDER_SIZE = 3;

	public OSCPadView(Context context, OSCViewGroup parent, OSCPadParameters params) {
		super(context, parent);
		
		this.mParams = params;
		this.mDoubleTapDetector = new SimpleDoubleTapDetector();
		
		init();

        this.mDecimalFormat = new DecimalFormat("#.###");
	}

    public OSCPadParameters getParameters() {
        return this.mParams;
    }
	
	private void init() {

		this.buttonRect = new RectF(BORDER_SIZE, BORDER_SIZE, this.mParams.getWidth() - BORDER_SIZE, this.mParams.getHeight() - BORDER_SIZE);
        this.borderRect = new RectF(0, 0, this.mParams.getWidth(), this.mParams.getHeight());
        this.mThumbRect = new RectF(this.mThumbX - this.mHalfThumbSize, this.mThumbY - this.mHalfThumbSize, this.mThumbX + this.mHalfThumbSize, this.mThumbY + this.mHalfThumbSize);

		this.mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mDefaultPaint.setColor(this.mParams.getDefaultFillColor());
		
		this.mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mThumbPaint.setColor(this.mParams.getThumbColor());
		
		this.mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mBorderPaint.setStyle(Paint.Style.STROKE);
		this.mBorderPaint.setColor(this.mParams.getBorderColor());
		this.mBorderPaint.setStrokeWidth(BORDER_SIZE);
	}

    public void setDefaultFillColor(int color) {
        this.mParams.setDefaultFillColor(color);
        this.mDefaultPaint.setColor(color);
    }

    public void setThumbColor(int color) {
        this.mParams.setThumbColor(color);
        this.mThumbPaint.setColor(color);
    }

    public void setBorderColor(int color) {
        this.mParams.setBorderColor(color);
        this.mBorderPaint.setColor(color);
    }

	@Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);

        this.mThumbRect.left = Math.min(Math.max(this.mThumbX - this.mHalfThumbSize, 0), this.getWidth() - this.mThumbSize);
        this.mThumbRect.right = Math.max(Math.min(this.mThumbX + this.mHalfThumbSize, this.getWidth()), this.mThumbSize);

        this.mThumbRect.top = Math.min(Math.max(this.mThumbY - this.mHalfThumbSize, 0), this.getHeight() - this.mThumbSize);
        this.mThumbRect.bottom = Math.max(Math.min(this.mThumbY + this.mHalfThumbSize, this.getHeight()), this.mThumbSize);

		canvas.drawRoundRect(this.buttonRect, 8, 8, this.mDefaultPaint);
        canvas.drawRoundRect(this.borderRect, 8, 8, this.mBorderPaint);
        canvas.drawRoundRect(this.mThumbRect, 8, 8, this.mThumbPaint);
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
                this.mThumbX = (int) event.getX();
                this.mThumbY = (int) event.getY();
                fireOSCMessage();
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

    private void fireOSCMessage() {
        try {

            double relXPos;
            double relYPos;
            if(this.mThumbX < 0) {
                relXPos = 0.0;
            } else if(this.mThumbX > this.mParams.getWidth()) {
                relXPos = 1.0;
            } else {
                relXPos = (double) this.mThumbX / (double) (this.mParams.getWidth() - this.mThumbSize);
            }

            double calcXPos = (relXPos * (this.mParams.getMaxXValue() - this.mParams.getMinXValue())) + this.mParams.getMinXValue();

            if(this.mThumbY < 0) {
                relYPos = 0.0;
            } else if(this.mThumbY > this.mParams.getHeight()) {
                relYPos = 1.0;
            } else {
                relYPos = (double) this.mThumbY / (double) (this.mParams.getHeight() - this.mThumbSize);
            }

            double calcYPos = (relYPos * (this.mParams.getMaxYValue() - this.mParams.getMinYValue())) + this.mParams.getMinYValue();

            String oscMessage = this.mParams.getOSCValueChanged();
            oscMessage = oscMessage.replace("$1", this.mDecimalFormat.format(calcXPos)).replace("$2", this.mDecimalFormat.format(calcYPos));

            String[] oscParts = oscMessage.split(" ");
            ArrayList<Object> oscArgs = new ArrayList<Object>();
            for(int i = 1; i < oscParts.length; i += 1) {
                oscArgs.add(oscParts[i]);
            }

            OSCWrapper.getInstance().sendOSC(oscParts[0], oscArgs);
        }
        catch(Exception exp) {}
    }

	@Override
	public void buildJSONParamString(StringBuilder sb) {
		if(sb == null) throw new IllegalArgumentException("StringBuilder cannot be null");
		
		sb.append("{\n");
		sb.append("\ttype:\"pad\",\n");
		sb.append("\tdefaultFillColor: [" + Color.red(this.mParams.getDefaultFillColor()) + ", " + Color.green(this.mParams.getDefaultFillColor()) + ", " + Color.blue(this.mParams.getDefaultFillColor()) + "],\n");
		sb.append("\theight: " + this.mParams.getHeight() + ",\n");
		sb.append("\tthumbFillColor: [" + Color.red(this.mParams.getThumbColor()) + ", " + Color.green(this.mParams.getThumbColor()) + ", " + Color.blue(this.mParams.getThumbColor()) + "],\n");
        sb.append("\tborderColor: [" + Color.red(this.mParams.getBorderColor()) + ", " + Color.green(this.mParams.getBorderColor()) + ", " + Color.blue(this.mParams.getBorderColor()) + "],\n");
    	sb.append("\trect: [" + this.mParams.getLeft() + ", " + this.mParams.getTop() + ", " + this.mParams.getRight() + ", " + this.mParams.getBottom() + "],\n");
		sb.append("\twidth: " + this.mParams.getWidth() + ",\n");
        sb.append("\tmaxXValue: " + this.mParams.getMaxXValue() + ",\n");
        sb.append("\tminXValue: " + this.mParams.getMinXValue() + ",\n");
        sb.append("\tmaxYValue: " + this.mParams.getMaxYValue() + ",\n");
        sb.append("\tminYValue: " + this.mParams.getMinYValue() + ",\n");
        sb.append("\tOSCValueChanged: \"" + this.mParams.getOSCValueChanged() + "\"\n");
		sb.append("}");
	}
	
	public static OSCPadParameters getDefaultParameters() {
        OSCPadParameters params = new OSCPadParameters();
		
		params.setDefaultFillColor(Color.rgb(0, 0, 60));
		params.setHeight(200);
		params.setThumbColor(Color.rgb(255, 9, 0));
		params.setWidth(300);
		params.setLeft(100);
		params.setTop(100);
		params.setRight(400);
		params.setBottom(300);
        params.setMinXValue(0);
        params.setMaxXValue(100);
        params.setMinYValue(0);
        params.setMaxYValue(100);
        params.setOSCValueChanged("pad $1 $2");
        params.setBorderColor(Color.rgb(155, 0, 0));
		return params;
	}

    public OSCPadView cloneView() {
        OSCPadParameters clonedParams = this.mParams.cloneParams();
        clonedParams.setLeft(clonedParams.getLeft() + 20);
        clonedParams.setTop(clonedParams.getTop() + 20);
        clonedParams.setRight(clonedParams.getRight() + 20);
        clonedParams.setBottom(clonedParams.getBottom() + 20);

        return new OSCPadView(this.getContext(), super.mParent, clonedParams);
    }
}

