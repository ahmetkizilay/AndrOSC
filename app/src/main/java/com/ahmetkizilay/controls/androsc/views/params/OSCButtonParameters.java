package com.ahmetkizilay.controls.androsc.views.params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

public class OSCButtonParameters {
	private int mLeft;
	private int mTop;
	private int mRight;
	private int mBottom;
	
	private int mWidth;
	private int mHeight;
	
	private String mText;
	
	private int mBorderColor;
	private int mDefaultFillColor;
	private int mPressedFillColor;
    private int mFontColor;

    private String mOSCButtonPressed;
	
	public OSCButtonParameters() {
		
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
	
	public String getText() {
		return this.mText;
	}
	
	public int getBorderColor() {
		return this.mBorderColor;
	}
	
	public int getDefaultFillColor() {
		return this.mDefaultFillColor;
	}
	
	public int getPressedFillColor() {
		return this.mPressedFillColor;
	}

    public int getFontColor() {
        return this.mFontColor;
    }

    public String getOSCButtonPressed() {
        return this.mOSCButtonPressed;
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
	
	public void setText(String text) {
		this.mText = text;
	}
	
	public void setDefaultFillColor(int defaultFillColor) {
		this.mDefaultFillColor = defaultFillColor;
	}
	
	public void setBorderColor(int borderColor) {
		this.mBorderColor = borderColor;
	}

	public void setPressedFillColor(int pressedFillColor) {
		this.mPressedFillColor = pressedFillColor;
	}

    public void setFontColor(int fontColor) {
        this.mFontColor = fontColor;
    }

    public void setOSCButtonPressed(String oscButtonPressed) {
        this.mOSCButtonPressed = oscButtonPressed;
    }

	public static OSCButtonParameters parseJSON(JSONObject jsonObj) throws JSONException {
		OSCButtonParameters oscButtonParams = new OSCButtonParameters();
		
		JSONArray jsonRectArray = jsonObj.getJSONArray("rect");
		oscButtonParams.setLeft(jsonRectArray.getInt(0));		
		oscButtonParams.setTop(jsonRectArray.getInt(1));		
		oscButtonParams.setRight(jsonRectArray.getInt(2));		
		oscButtonParams.setBottom(jsonRectArray.getInt(3));
		
		oscButtonParams.setWidth(jsonObj.getInt("width"));
		
		oscButtonParams.setHeight(jsonObj.getInt("height"));
		
		oscButtonParams.setText(jsonObj.getString("text"));
		
		JSONArray jsonBorderColorArray = jsonObj.getJSONArray("borderColor");
		oscButtonParams.setBorderColor(Color.rgb(jsonBorderColorArray.getInt(0), jsonBorderColorArray.getInt(1), jsonBorderColorArray.getInt(2)));
		
		JSONArray jsonDefaultFillColorArray = jsonObj.getJSONArray("defaultFillColor");
		oscButtonParams.setDefaultFillColor(Color.rgb(jsonDefaultFillColorArray.getInt(0), jsonDefaultFillColorArray.getInt(1), jsonDefaultFillColorArray.getInt(2)));
		
		JSONArray jsonPressedFillColorArray = jsonObj.getJSONArray("pressedFillColor");
		oscButtonParams.setPressedFillColor(Color.rgb(jsonPressedFillColorArray.getInt(0), jsonPressedFillColorArray.getInt(1), jsonPressedFillColorArray.getInt(2)));

        JSONArray jsonFontColorArray = jsonObj.getJSONArray("fontColor");
        oscButtonParams.setFontColor(Color.rgb(jsonFontColorArray.getInt(0), jsonFontColorArray.getInt(1), jsonFontColorArray.getInt(2)));

        oscButtonParams.setOSCButtonPressed(jsonObj.getString("oscButtonPressed"));

        return oscButtonParams;
	}
}
