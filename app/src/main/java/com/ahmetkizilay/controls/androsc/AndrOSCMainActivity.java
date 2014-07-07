package com.ahmetkizilay.controls.androsc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.ahmetkizilay.controls.androsc.fragments.AddNewOSCControlListDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.MenuFragment;
import com.ahmetkizilay.controls.androsc.fragments.NetworkSettingsDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.OpenFileDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.SaveFileDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.MenuFragment.OSCMenuActionEvent;
import com.ahmetkizilay.controls.androsc.fragments.OSCViewFragment;
import com.ahmetkizilay.controls.androsc.osc.OSCWrapper;
import com.ahmetkizilay.controls.androsc.utils.Utilities;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class AndrOSCMainActivity extends FragmentActivity implements 
		com.ahmetkizilay.controls.androsc.fragments.OSCViewFragment.OnMenuToggledListener,
		com.ahmetkizilay.controls.androsc.fragments.AddNewOSCControlListDialogFragment.OnNewOSCControlSelected, 
		com.ahmetkizilay.controls.androsc.fragments.MenuFragment.OnOSCMenuActionListener,
		com.ahmetkizilay.controls.androsc.fragments.SaveFileDialogFragment.OnSaveFileNameSelectedListener,
		com.ahmetkizilay.controls.androsc.fragments.OpenFileDialogFragment.OnOpenFileNameSelectedListener,
        com.ahmetkizilay.controls.androsc.fragments.NetworkSettingsDialogFragment.OnNetworkSettingsChangedListener
{

	private final static String TAG_DIALOG_ADD_NEW_ITEM = "dlgAddNewItem";
	private final static String TAG_DIALOG_SAVE_FILE_NAME = "dlgSaveFileName";
	private final static String TAG_DIALOG_OPEN_FILE_NAME = "dlgOpenFileName";
    private final static String TAG_DIALOG_NETWORK_SETTINGS = "dlgNetworkSettings";

    private final static String NETWORK_SETTINGS_FILE = "androsc_network.cfg";

	private OSCViewFragment mOSCViewFragment;
	private String mBaseFolder;
	
	private String mCurrentFileName;

    private String mIPAddress = "192.168.2.1";
    private int mPort = 8000;
    private boolean mConnectOnStartUp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		handleExternalStorage();
        restoreNetworkSettingsFromFile();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		this.mOSCViewFragment = (OSCViewFragment) getSupportFragmentManager().findFragmentById(R.id.frgOSCView);

		ft.commit();



        if(this.mConnectOnStartUp) {
            connectOSC();
        }
	}

	private void handleExternalStorage() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Toast.makeText(this, "External Storage Read Only!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "External Storage Not Available!", Toast.LENGTH_LONG).show();
			return;
		}

		StringBuilder folderPathBuilder = new StringBuilder();
		folderPathBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append("perisonic");
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append("androsc");
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append("templates");
		File baseFolderFile = new File(folderPathBuilder.toString());
		if (!baseFolderFile.exists()) {
			if (!baseFolderFile.mkdirs()) {
				Toast.makeText(this, "Unable To Build Application Folder!", Toast.LENGTH_LONG).show();
				return;
			}
		}

		this.mBaseFolder = baseFolderFile.getAbsolutePath();
	}

	@Override
	public void toggleMenu() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.frgMenu);

		if (menuFragment.isVisible()) {
			ft.hide(menuFragment);
		} else {
			ft.show(menuFragment);
		}
		ft.commit();
	}

    @Override
    public void closeMenu() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.frgMenu);

        if (menuFragment.isVisible()) {
            ft.hide(menuFragment);
        }

        ft.commit();
    }
	
	@Override
	public void openNewOSCItemDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_ADD_NEW_ITEM);
		if (prev != null) {
			ft.remove(prev);
		}

		ft.addToBackStack(null);

		AddNewOSCControlListDialogFragment newDialog = AddNewOSCControlListDialogFragment.newInstance();
		newDialog.show(ft, AndrOSCMainActivity.TAG_DIALOG_ADD_NEW_ITEM);
	}

    @Override
    public void openSaveTemplateDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_SAVE_FILE_NAME);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        SaveFileDialogFragment saveDlgFrag = SaveFileDialogFragment.getInstance(this.mBaseFolder, this.mCurrentFileName);
        saveDlgFrag.show(ft, AndrOSCMainActivity.TAG_DIALOG_SAVE_FILE_NAME);
    }

	@Override
	public void onNewOSCControlSelected(String selectedItem) {
		this.mOSCViewFragment.addNewOSCControl(selectedItem);
	}

	@Override
	public void oscMenuItemSelected(OSCMenuActionEvent event) {
		if(event.getAction() == OSCMenuActionEvent.ACTION_NEW) {
			this.mOSCViewFragment.clearForNewTemplate();
            toggleMenu(); // I don't know if this is a good idea...
		}
		else if (event.getAction() == OSCMenuActionEvent.ACTION_OPEN) {
			// Show Save Dialog, pass the return to the OSCViewFragment
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_OPEN_FILE_NAME);
			if (prev != null) {
				ft.remove(prev);
			}

			ft.addToBackStack(null);

			OpenFileDialogFragment openDlgFrag = OpenFileDialogFragment.newInstance(this.mBaseFolder);
			openDlgFrag.show(ft, AndrOSCMainActivity.TAG_DIALOG_OPEN_FILE_NAME);

		}
//		else if(event.getAction() == OSCMenuActionEvent.ACTION_EDIT) {
//			this.mOSCViewFragment.enableTemplateEditing();
//		}
//		else if (event.getAction() == OSCMenuActionEvent.ACTION_SAVE) {
//			// Show Save Dialog, pass the return to the OSCViewFragment
//			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//			Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_SAVE_FILE_NAME);
//			if (prev != null) {
//				ft.remove(prev);
//			}
//
//			ft.addToBackStack(null);
//
//			SaveFileDialogFragment saveDlgFrag = SaveFileDialogFragment.getInstance(this.mBaseFolder, this.mCurrentFileName);
//			saveDlgFrag.show(ft, AndrOSCMainActivity.TAG_DIALOG_SAVE_FILE_NAME);
//		}
        else if(event.getAction() == OSCMenuActionEvent.ACTION_NETWORK) {
            // Show Network settings gragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_NETWORK_SETTINGS);
            if(prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            NetworkSettingsDialogFragment frgNetworkSettingsDialog = NetworkSettingsDialogFragment.getInstance(this.mIPAddress, this.mPort, this.mConnectOnStartUp);
            frgNetworkSettingsDialog.show(ft, AndrOSCMainActivity.TAG_DIALOG_NETWORK_SETTINGS);
        }
	}

    private void connectOSC() {
        try {
            OSCWrapper.getInstance(this, this.mIPAddress, this.mPort);
        }
        catch(Exception exp) {
            Toast.makeText(this, "I couldn't initialize OSC", Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onSaveFileSelected(String fileName) {

		String jsonTemplate = this.mOSCViewFragment.buildJSONString();
		if (!Utilities.readStringIntoFile(jsonTemplate, fileName)) {
			Toast.makeText(this, "Error Saving Template", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Template Saved", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onOpenFileSelected(String fileName) {
		this.mOSCViewFragment.inflateTemplate(this.mBaseFolder + File.separator + fileName);
		this.mOSCViewFragment.disableTemplateEditing();
		this.mCurrentFileName = fileName.substring(0, fileName.length() - 5);

        toggleMenu(); // I don't know if this is a good idea
    }

    @Override
    public void onNetworkSettingsChanged(String ipAddress, int port, boolean connectOnStartUp) {

        this.mIPAddress = ipAddress;
        this.mPort = port;
        this.mConnectOnStartUp = connectOnStartUp;

        connectOSC();

        saveOSCNetworkSettings();
    }

    private void saveOSCNetworkSettings() {
        try {
            FileOutputStream fos = openFileOutput(NETWORK_SETTINGS_FILE, Context.MODE_PRIVATE);

            String data = this.mIPAddress + "#" + this.mPort + "#" + this.mConnectOnStartUp;
            fos.write(data.getBytes());
            fos.close();
        }
        catch(Exception exp) {
            Toast.makeText(this, "Could Not Update OSC Network Settings File", Toast.LENGTH_SHORT).show();
            exp.printStackTrace();
        }
    }

    private void restoreNetworkSettingsFromFile() {
        try {
            FileInputStream fis = openFileInput(NETWORK_SETTINGS_FILE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int bytes_read;
            while((bytes_read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytes_read);
            }

            String data = new String(baos.toByteArray());
            String[] pieces = data.split("#");

            if(pieces.length != 3) {
                throw new Exception("Network Settings File Seems To Be Corrupt");
            }

            this.mIPAddress = pieces[0];
            this.mPort = Integer.parseInt(pieces[1]);
            this.mConnectOnStartUp = Boolean.parseBoolean(pieces[2]);

        }
        catch(FileNotFoundException fnfe) {}
        catch(Exception exp) {
            Toast.makeText(this, "Could Not Read OSC Network Settings File", Toast.LENGTH_SHORT).show();
        }
    }
}
