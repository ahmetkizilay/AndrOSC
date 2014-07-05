package com.ahmetkizilay.controls.androsc.views.params;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OSCPadParameters {
	private int mLeft;
	private int mTop;
	private int mRight;
	private int mBottom;

	private int mWidth;
	private int mHeight;

	private int mBorderColor;
	private int mDefaultFillColor;
	private int mThumbColor;

    private double mMinXValue;
    private double mMaxXValue;
    private double mMinYValue;
    private double mMaxYValue;

    private String mOSCValueChanged;

	public OSCPadParameters() {
		
	}
		
	public int getLeft() {
		return this.mLeft;
	}
	
	public int getTop() {
		return this.mTop;
	}
	
	public int getRight() {
		return this.mRight;
	}
	
	public int getBottom() {
		return this.mBottom;
	}
	
	public int getWidth() {
		return this.mWidth;		
	}
	
	public int getHeight() {
		return this.mHeight;
	}

	public int getBorderColor() {
		return this.mBorderColor;
	}
	
	public int getDefaultFillColor() {
		return this.mDefaultFillColor;
	}

    public int getThumbColor() {
        return this.mThumbColor;
    }

    public String getOSCValueChanged() {
        return this.mOSCValueChanged;
    }

    public double getMinXValue() {
        return this.mMinXValue;
    }

    public double getMaxXValue() {
        return this.mMaxXValue;
    }

    public double getMinYValue() {
        return this.mMinYValue;
    }

    public double getMaxYValue() {
        return this.mMaxYValue;
    }

	public void setLeft(int left) {
		this.mLeft = left;
	}
	
	public void setTop(int top) {
		this.mTop = top;
	}
	
	public void setRight(int right) {
		this.mRight = right;
	}
	
	public void setBottom(int bottom) {
		this.mBottom = bottom;
	}
	
	public void setWidth(int width) {
		this.mWidth = width;
	}
	
	public void setHeight(int height) {
		this.mHeight = height;
	}

	public void setDefaultFillColor(int defaultFillColor) {
		this.mDefaultFillColor = defaultFillColor;
	}
	
	public void setBorderColor(int borderColor) {
		this.mBorderColor = borderColor;
	}

    public void setThumbColor(int thumbColor) {
        this.mThumbColor = thumbColor;
    }

    public void setOSCValueChanged(String oscValueChanged) {
        this.mOSCValueChanged = oscValueChanged;
    }

    public void setMinXValue(double minXValue) {
        this.mMinXValue = minXValue;
    }

    public void setMaxXValue(double maxXValue) {
        this.mMaxXValue = maxXValue;
    }

    public void setMinYValue(double minYValue) {
        this.mMinYValue = minYValue;
    }

    public void setMaxYValue(double maxYValue) {
        this.mMaxYValue = maxYValue;
    }

	public static OSCPadParameters parseJSON(JSONObject jsonObj) throws JSONException {
		OSCPadParameters oscPadParams = new OSCPadParameters();
		
		JSONArray jsonRectArray = jsonObj.getJSONArray("rect");
        oscPadParams.setLeft(jsonRectArray.getInt(0));
        oscPadParams.setTop(jsonRectArray.getInt(1));
        oscPadParams.setRight(jsonRectArray.getInt(2));
        oscPadParams.setBottom(jsonRectArray.getInt(3));

        oscPadParams.setWidth(jsonObj.getInt("width"));

        oscPadParams.setHeight(jsonObj.getInt("height"));

		JSONArray jsonDefaultFillColorArray = jsonObj.getJSONArray("defaultFillColor");
        oscPadParams.setDefaultFillColor(Color.rgb(jsonDefaultFillColorArray.getInt(0), jsonDefaultFillColorArray.getInt(1), jsonDefaultFillColorArray.getInt(2)));

        JSONArray jsonThumbColorArray = jsonObj.getJSONArray("thumbFillColor");
        oscPadParams.setThumbColor(Color.rgb(jsonThumbColorArray.getInt(0), jsonThumbColorArray.getInt(1), jsonThumbColorArray.getInt(2)));

        oscPadParams.setOSCValueChanged(jsonObj.getString("OSCValueChanged"));

        oscPadParams.setMaxXValue(jsonObj.getDouble("maxXValue"));
        oscPadParams.setMinXValue(jsonObj.getDouble("minXValue"));

        oscPadParams.setMaxYValue(jsonObj.getDouble("maxYValue"));
        oscPadParams.setMinYValue(jsonObj.getDouble("minYValue"));

        return oscPadParams;
	}
}
