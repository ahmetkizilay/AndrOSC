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
	
	public void defaultFillColor(int defaultFillColor) {
		this.mDefaultFillColor = defaultFillColor;
	}
	
	public void setPressedFillColor(int pressedFillColor) {
		this.mPressedFillColor = pressedFillColor;
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
		
		return oscButtonParams;
	}
}
