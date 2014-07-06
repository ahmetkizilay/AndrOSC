package com.ahmetkizilay.controls.androsc.views.params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

public class OSCToggleParameters {
	private int mLeft;
	private int mTop;
	private int mRight;
	private int mBottom;
	
	private int mWidth;
	private int mHeight;
	
	private String mDefaultText;
	private String mToggledText;
	
	private int mDefaultFillColor;
	private int mToggledFillColor;

    private String mOSCToggleOn;
    private String mOSCToggleOff;

    private int mFontColor;

    private boolean mFireOSCOnToggleOff;

	public OSCToggleParameters() {
		
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
	
	public String getDefaultText() {
		return this.mDefaultText;
	}
	
	public String getToggledText() {
		return this.mToggledText;
	}

	public int getDefaultFillColor() {
		return this.mDefaultFillColor;
	}
	
	public int getToggledFillColor() {
		return this.mToggledFillColor;
	}

    public String getOSCToggleOn() {
        return this.mOSCToggleOn;
    }

    public String getOSCToggleOff() {
        return this.mOSCToggleOff;
    }

    public int getFontColor() {
        return this.mFontColor;
    }

    public boolean getFireOSCOnToggleOff() {
        return this.mFireOSCOnToggleOff;
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
	
	public void setDefaultText(String defaultText) {
		this.mDefaultText = defaultText;
	}
	
	public void setToggledText(String toggledText) {
		this.mToggledText = toggledText;
	}
	
	public void setDefaultFillColor(int defaultFillColor) {
		this.mDefaultFillColor = defaultFillColor;
	}
	
	public void setToggledFillColor(int toggledFillColor) {
		this.mToggledFillColor = toggledFillColor;
	}

    public void setOSCToggleOn(String oscToggleOn) {
        this.mOSCToggleOn = oscToggleOn;
    }

    public void setOSCToggleOff(String oscToggleOff) {
        this.mOSCToggleOff = oscToggleOff;
    }

    public void setFontColor(int fontColor) {
        this.mFontColor = fontColor;
    }

    public void setFireOSCOnToggleOff(boolean fireOSCOnToggleOff) {
        this.mFireOSCOnToggleOff = fireOSCOnToggleOff;
    }
	
	public static OSCToggleParameters parseJSON(JSONObject jsonObj) throws JSONException {
		OSCToggleParameters oscToggleParams = new OSCToggleParameters();
		
		JSONArray jsonRectArray = jsonObj.getJSONArray("rect");
		oscToggleParams.setLeft(jsonRectArray.getInt(0));		
		oscToggleParams.setTop(jsonRectArray.getInt(1));		
		oscToggleParams.setRight(jsonRectArray.getInt(2));		
		oscToggleParams.setBottom(jsonRectArray.getInt(3));
		
		oscToggleParams.setWidth(jsonObj.getInt("width"));
		
		oscToggleParams.setHeight(jsonObj.getInt("height"));
		
		oscToggleParams.setDefaultText(jsonObj.getString("defaultText"));
		
		oscToggleParams.setToggledText(jsonObj.getString("toggledText"));
		
		JSONArray jsonDefaultFillColorArray = jsonObj.getJSONArray("defaultFillColor");
		oscToggleParams.setDefaultFillColor(Color.rgb(jsonDefaultFillColorArray.getInt(0), jsonDefaultFillColorArray.getInt(1), jsonDefaultFillColorArray.getInt(2)));
		
		JSONArray jsonToggledFillColorArray = jsonObj.getJSONArray("toggledFillColor");
		oscToggleParams.setToggledFillColor(Color.rgb(jsonToggledFillColorArray.getInt(0), jsonToggledFillColorArray.getInt(1), jsonToggledFillColorArray.getInt(2)));

        oscToggleParams.setOSCToggleOn(jsonObj.getString("oscToggleOn"));
        oscToggleParams.setOSCToggleOff(jsonObj.getString("oscToggleOff"));

        JSONArray jsonFontColorArray = jsonObj.getJSONArray("fontColor");
        oscToggleParams.setFontColor(Color.rgb(jsonFontColorArray.getInt(0), jsonFontColorArray.getInt(1), jsonFontColorArray.getInt(2)));

        try {
            oscToggleParams.setFireOSCOnToggleOff(jsonObj.getBoolean("fireOSCOnToggleOff"));
        }
        catch(JSONException exp) {
            oscToggleParams.setFireOSCOnToggleOff(false);
        }

		return oscToggleParams;
	}
}
