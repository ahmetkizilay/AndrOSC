package com.ahmetkizilay.controls.androsc.views.params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

public class OSCSliderParameters {
	private int mLeft;
	private int mTop;
	private int mRight;
	private int mBottom;
	
	private int mWidth;
	private int mHeight;
	
	private int mDefaultFillColor;
	private int mSlidedFillColor;
	private int mCursorFillColor;

    private double mMinValue;
    private double mMaxValue;
    private String mOSCValueChanged;
	
	public OSCSliderParameters() {
		
	}
		
	
	
	public int getLeft() {
		return mLeft;
	}

	public void setLeft(int left) {
		this.mLeft = left;
	}

	public int getTop() {
		return mTop;
	}

	public void setTop(int mTop) {
		this.mTop = mTop;
	}
	
	public int getRight() {
		return mRight;
	}

	public void setRight(int right) {
		this.mRight = right;
	}

	public int getBottom() {
		return mBottom;
	}

	public void setBottom(int bottom) {
		this.mBottom = bottom;
	}

	public int getWidth() {
		return mWidth;
	}



	public void setWidth(int mWidth) {
		this.mWidth = mWidth;
	}



	public int getHeight() {
		return mHeight;
	}

    public double getMinValue() {
        return this.mMinValue;
    }

    public double getMaxValue() {
       return this.mMaxValue;
    }

    public String getOSCValueChanged() {
        return this.mOSCValueChanged;
    }

	public void setHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public int getDefaultFillColor() {
		return mDefaultFillColor;
	}



	public void setDefaultFillColor(int mDefaultFillColor) {
		this.mDefaultFillColor = mDefaultFillColor;
	}



	public int getSlidedFillColor() {
		return mSlidedFillColor;
	}



	public void setSlidedFillColor(int mSlidedFillColor) {
		this.mSlidedFillColor = mSlidedFillColor;
	}



	public int getCursorFillColor() {
		return mCursorFillColor;
	}



	public void setCursorFillColor(int mCursorFillColor) {
		this.mCursorFillColor = mCursorFillColor;
	}

    public void setMinValue(double minValue) {
        this.mMinValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.mMaxValue = maxValue;
    }

    public void setOSCValueChanged(String oscValueChanged) {
        this.mOSCValueChanged = oscValueChanged;
    }

	public static OSCSliderParameters parseJSON(JSONObject jsonObj) throws JSONException {
		OSCSliderParameters oscSliderParams = new OSCSliderParameters();
		
		JSONArray jsonRectArray = jsonObj.getJSONArray("rect");
		oscSliderParams.setLeft(jsonRectArray.getInt(0));		
		oscSliderParams.setTop(jsonRectArray.getInt(1));		
		oscSliderParams.setRight(jsonRectArray.getInt(2));		
		oscSliderParams.setBottom(jsonRectArray.getInt(3));
		
		oscSliderParams.setWidth(jsonObj.getInt("width"));
		
		oscSliderParams.setHeight(jsonObj.getInt("height"));

		JSONArray jsonDefaultFillColorArray = jsonObj.getJSONArray("defaultFillColor");
		oscSliderParams.setDefaultFillColor(Color.rgb(jsonDefaultFillColorArray.getInt(0), jsonDefaultFillColorArray.getInt(1), jsonDefaultFillColorArray.getInt(2)));
		
		JSONArray jsonSlidedFillColorArray = jsonObj.getJSONArray("slidedFillColor");
		oscSliderParams.setSlidedFillColor(Color.rgb(jsonSlidedFillColorArray.getInt(0), jsonSlidedFillColorArray.getInt(1), jsonSlidedFillColorArray.getInt(2)));
		
		JSONArray jsoncursorFillColorArray = jsonObj.getJSONArray("cursorFillColor");
		oscSliderParams.setCursorFillColor(Color.rgb(jsoncursorFillColorArray.getInt(0), jsoncursorFillColorArray.getInt(1), jsoncursorFillColorArray.getInt(2)));

        try {
            oscSliderParams.setOSCValueChanged(jsonObj.getString("OSCValueChanged"));
        }
        catch(Exception e) {
            oscSliderParams.setOSCValueChanged("/vslider $1");
        }

        oscSliderParams.setOSCValueChanged(jsonObj.getString("OSCValueChanged"));
        oscSliderParams.setMaxValue(jsonObj.getDouble("maxValue"));
        oscSliderParams.setMinValue(jsonObj.getDouble("minValue"));

		return oscSliderParams;
	}
}
