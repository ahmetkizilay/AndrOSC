package com.ahmetkizilay.controls.androsc.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahmetkizilay.controls.androsc.R;

/**
 * Created by ahmetkizilay on 08.07.2014.
 */
public class NavigationDrawerView extends RelativeLayout {

    private OnOSCMenuItemClickedListener mOnMenuItemClickedCallback;
    private TextView mCurrentTemplate;

    public NavigationDrawerView(Context context) {
        super(context);
        init();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationDrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        LayoutInflater mInflater = LayoutInflater.from(this.getContext());
        mInflater.inflate(R.layout.navigation_drawer_layout, this, true);

        TextView twNewAction = (TextView) this.findViewById(R.id.txtMenuNew);
        twNewAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnMenuItemClickedCallback.oscMenuItemActionSelected(new OSCMenuActionEvent(OSCMenuActionEvent.ACTION_NEW));
            }
        });

        TextView twOpenAction = (TextView) this.findViewById(R.id.txtMenuOpen);
        twOpenAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnMenuItemClickedCallback.oscMenuItemActionSelected(new OSCMenuActionEvent(OSCMenuActionEvent.ACTION_OPEN));
            }
        });

        TextView twNetworkSettingsAction = (TextView) this.findViewById(R.id.txtMenuNetwork);
        twNetworkSettingsAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnMenuItemClickedCallback.oscMenuItemActionSelected(new OSCMenuActionEvent(OSCMenuActionEvent.ACTION_NETWORK));
            }
        });

        this.mCurrentTemplate = (TextView) this.findViewById(R.id.txtCurrentTemplate);
    }

    public void setOnOSCMenuActionClicked(OnOSCMenuItemClickedListener callback) {
        this.mOnMenuItemClickedCallback = callback;
    }

    public void setCurrentTemplate(String templateName) {
        this.mCurrentTemplate.setText(templateName);
    }

    public interface OnOSCMenuItemClickedListener {
        public void oscMenuItemActionSelected(OSCMenuActionEvent event);
        public void oscMenuItemTemplateSelected(String templateName);
    }

    public final class OSCMenuActionEvent {
        public static final int ACTION_NEW = 1;
        public static final int ACTION_OPEN = 2;
        public static final int ACTION_NETWORK = 3;

        private int mAction;

        private OSCMenuActionEvent(int action) {
            this.mAction = action;
        }

        public int getAction() {
            return this.mAction;
        }
    }
}
