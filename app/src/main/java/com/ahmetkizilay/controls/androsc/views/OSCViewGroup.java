package com.ahmetkizilay.controls.androsc.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ahmetkizilay.controls.androsc.utils.OSCControlItem;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.controls.androsc.views.params.OSCButtonParameters;
import com.ahmetkizilay.controls.androsc.views.params.OSCSliderParameters;
import com.ahmetkizilay.controls.androsc.views.params.OSCToggleParameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class OSCViewGroup extends ViewGroup{

	private List<OSCControlView> controlList = new ArrayList<OSCControlView>();
	private boolean mEditEnabled = true;
	private AlignmentView mAlignmentView;
	private SelectionFrameView mSelectionFrameView;
	
	protected OSCControlCommandListener mOSCControlCommandCallback;
	
	public OSCViewGroup(Context context) {
		super(context);	
	}
	
	public OSCViewGroup(Context context, AttributeSet attr) {
		super(context, attr);
		
		this.mAlignmentView = new AlignmentView(getContext());
		this.addView(this.mAlignmentView);
		
		this.mSelectionFrameView = new SelectionFrameView(getContext(), this);
		this.addView(this.mSelectionFrameView);
	}
	
	
	private void init() {
		
		String templateFilePath = "empty.json";
		String templateContents = Utilities.readAssetFileContents(this.getContext(), templateFilePath);
		if(templateContents == null) {
			// TODO: error handling
			return;
		}
		
		try {
			JSONObject jsonContent = new JSONObject(templateContents);
			JSONArray jsonContents = jsonContent.getJSONArray("contents");
			
			for(int i = 0; i < jsonContents.length(); i++) {
				JSONObject jsonControl = jsonContents.getJSONObject(i);
				String controlType = jsonControl.getString("type");
				if(controlType.equals("button")) {
					// creating a OSCButtonParameters and OSCButtonView
					OSCButtonParameters oscButtonParams = OSCButtonParameters.parseJSON(jsonControl);
					OSCButtonView oscButtonControl = new OSCButtonView(this.getContext(), this, oscButtonParams);
					this.addOSCControlView(oscButtonControl);
				}
				else if(controlType.equals("toggle")) {
					// creating a OSCToggleParameters and OSCToggleView
					OSCToggleParameters oscToggleParams = OSCToggleParameters.parseJSON(jsonControl);
					OSCToggleView oscToggleControl = new OSCToggleView(this.getContext(), this, oscToggleParams);
					this.addOSCControlView(oscToggleControl);
				}
				else if(controlType.equals("slider")) {
					// creating a OSCSliderParameteres and OSCSliderView
					OSCSliderParameters oscSliderParams = OSCSliderParameters.parseJSON(jsonControl);
					OSCHorizontalSliderView oscSliderControl = new OSCHorizontalSliderView(this.getContext(), this, oscSliderParams);
					this.addOSCControlView(oscSliderControl);
				}
			}

		} catch (JSONException e) {
			// TODO: error handling
			return;
		}		
	}
		
	public void addNewControl(String controlType) {
		OSCControlView child = null;

		if(controlType.equals(OSCControlItem.OSC_BUTTON)) {
			child = new OSCButtonView(this.getContext(), this, OSCButtonView.getDefaultParameters());			
		}
		else if(controlType.equals(OSCControlItem.OSC_TOGGLE)) {
			child = new OSCToggleView(this.getContext(), this, OSCToggleView.getDefaultParameters());			
		}
		else if(controlType.equals(OSCControlItem.OSC_HSLIDER)) {
			child = new OSCHorizontalSliderView(this.getContext(), this, OSCHorizontalSliderView.getDefaultParameters());			
		}
		else if(controlType.equals(OSCControlItem.OSC_VSLIDER)) {
			child = new OSCVerticalSliderView(this.getContext(), this, OSCVerticalSliderView.getDefaultParameters());			
		}
		child.repositionView();
		addOSCControlView(child);
	}
	
	public void addOSCControlView(OSCControlView child) {
		this.controlList.add(child);
		this.addView(child);
	}
	
	public void removeSelectedOSCControl() {
		this.controlList.remove(this.mSelectedControl);
		this.removeView(this.mSelectedControl);
		this.hideSelectionFrame();
	}
	
	public boolean isEditEnabled() {
		return this.mEditEnabled;
	}
	
	public void setEditEnabled(boolean editEnabled) {
		this.mEditEnabled = editEnabled;
		this.hideSelectionFrame();
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// do nothing.....
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		this.mAlignmentView.layout(0, 0, this.getWidth(), this.getHeight());
		this.mSelectionFrameView.layout(0, 0, this.getWidth(), this.getHeight());		
		
		for(int i = 0; i < this.controlList.size(); i++) {
			this.controlList.get(i).repositionView();
		}
	}
	
	public void drawAlignLines(int top, int left, int width, int height) {
		this.mAlignmentView.setAlignDimensions(top, left, width, height);
		this.mAlignmentView.invalidate();
	}
	
	public void hideAlignLines() {
		this.mAlignmentView.stopPaint();
		this.mAlignmentView.invalidate();
	}
	
	public void drawSelectionFrame(int top, int left, int width, int height) {

		this.mSelectionFrameView.setFrameDimensions(top, left, width, height);
		this.mSelectionFrameView.invalidate();
	}
	
	public void hideSelectionFrame() {
		this.mSelectionFrameView.stopPaint();
	}
	
	public void resizeSelectedControl(int left, int top, int right, int bottom) {
		if(mSelectedControl != null) {
			this.mSelectedControl.updateDimensions(left, top, right, bottom);
		}
	}
	
	private OSCControlView mSelectedControl = null;
	public void setSelectedControlForEdit(OSCControlView selectedControl) {
		this.mSelectedControl = selectedControl;
        this.mOSCControlCommandCallback.onControlSelected(this.mSelectedControl);
	}
	
	public String buildJSONString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{\ncontents: [\n");
		for(OSCControlView thisView : this.controlList) {
			thisView.buildJSONParamString(sb);
			sb.append(",\n");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("\n]}");
		
		return sb.toString();
	}
	
	public void clearAllOSCViews() {
		OSCControlView currentView = null;
		for(Iterator<OSCControlView> it = this.controlList.iterator(); it.hasNext();) {
			currentView = it.next();
			this.removeView(currentView);
			currentView.setVisibility(View.INVISIBLE);
			it.remove();
			currentView = null;
		}
	}
	
	public boolean inflateJSONTemplate(String jsonString) {
		try {
			JSONObject jsonContent = new JSONObject(jsonString);
			JSONArray jsonContents = jsonContent.getJSONArray("contents");
			
			for(int i = 0; i < jsonContents.length(); i++) {
				JSONObject jsonControl = jsonContents.getJSONObject(i);
				String controlType = jsonControl.getString("type");
				if(controlType.equals("button")) {
					// creating a OSCButtonParameters and OSCButtonView
					OSCButtonParameters oscButtonParams = OSCButtonParameters.parseJSON(jsonControl);
					OSCButtonView oscButtonControl = new OSCButtonView(this.getContext(), this, oscButtonParams);
					oscButtonControl.repositionView();
					this.addOSCControlView(oscButtonControl);
				}
				else if(controlType.equals("toggle")) {
					// creating a OSCToggleParameters and OSCToggleView
					OSCToggleParameters oscToggleParams = OSCToggleParameters.parseJSON(jsonControl);
					OSCToggleView oscToggleControl = new OSCToggleView(this.getContext(), this, oscToggleParams);
					oscToggleControl.repositionView();
					this.addOSCControlView(oscToggleControl);
				}
				else if(controlType.equals("hslider")) {
					// creating a OSCSliderParameteres and OSCSliderView
					OSCSliderParameters oscSliderParams = OSCSliderParameters.parseJSON(jsonControl);
					OSCHorizontalSliderView oscSliderControl = new OSCHorizontalSliderView(this.getContext(), this, oscSliderParams);
					oscSliderControl.repositionView();
					this.addOSCControlView(oscSliderControl);
				}
				else if(controlType.equals("vslider")) {
					// creating a OSCSliderParameteres and OSCSliderView
					OSCSliderParameters oscSliderParams = OSCSliderParameters.parseJSON(jsonControl);
					OSCVerticalSliderView oscSliderControl = new OSCVerticalSliderView(this.getContext(), this, oscSliderParams);
					oscSliderControl.repositionView();
					this.addOSCControlView(oscSliderControl);
				}
				else {
					throw new Exception("Unknown OSC Control Type: " + controlType);
				}
			}

		} catch (JSONException e) {
			// TODO: error handling
			return false;
		}
		catch(Exception e) {
			// TODO: error handling
			return false;
		}
		
		return true;
	}
	
	public void showOSCControlSettingsFor(OSCControlView selectedControl) {
		this.mOSCControlCommandCallback.onControlSettingsRequested(selectedControl);
	}
	
	public void setOSCControlCommandListener(OSCControlCommandListener listener) {
		this.mOSCControlCommandCallback = listener;
	}
	
	public interface OSCControlCommandListener {
		public void onControlSelected(OSCControlView selectedControl);
		public void onControlSettingsRequested(OSCControlView selectedControl);
	}
}
