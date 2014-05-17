package com.ahmetkizilay.controls.androsc;

import java.io.File;

import com.ahmetkizilay.controls.androsc.fragments.AddNewOSCControlListDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.MenuFragment;
import com.ahmetkizilay.controls.androsc.fragments.OpenFileDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.SaveFileDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.MenuFragment.OSCMenuActionEvent;
import com.ahmetkizilay.controls.androsc.fragments.OSCViewFragment;
import com.ahmetkizilay.controls.androsc.utils.Utilities;

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
		com.ahmetkizilay.controls.androsc.fragments.OpenFileDialogFragment.OnOpenFileNameSelectedListener
{

	private final static String TAG_DIALOG_ADD_NEW_ITEM = "dlgAddNewItem";
	private final static String TAG_DIALOG_SAVE_FILE_NAME = "dlgSaveFileName";
	private final static String TAG_DIALOG_OPEN_FILE_NAME = "dlgOpenFileName";

	private OSCViewFragment mOSCViewFragment;
	private String mBaseFolder;
	
	private String mCurrentFileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		handleExternalStorage();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		this.mOSCViewFragment = (OSCViewFragment) getSupportFragmentManager().findFragmentById(R.id.frgOSCView);

		ft.commit();

		
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
	public void onNewOSCControlSelected(String selectedItem) {
		this.mOSCViewFragment.addNewOSCControl(selectedItem);
	}

	@Override
	public void oscMenuItemSelected(OSCMenuActionEvent event) {
		if(event.getAction() == OSCMenuActionEvent.ACTION_NEW) {
			this.mOSCViewFragment.clearForNewTemplate();
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
		else if(event.getAction() == OSCMenuActionEvent.ACTION_EDIT) {
			this.mOSCViewFragment.enableTemplateEditing();
		}
		else if (event.getAction() == OSCMenuActionEvent.ACTION_SAVE) {
			// Show Save Dialog, pass the return to the OSCViewFragment
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_SAVE_FILE_NAME);
			if (prev != null) {
				ft.remove(prev);
			}

			ft.addToBackStack(null);

			SaveFileDialogFragment saveDlgFrag = SaveFileDialogFragment.getInstance(this.mBaseFolder, this.mCurrentFileName);
			saveDlgFrag.show(ft, AndrOSCMainActivity.TAG_DIALOG_SAVE_FILE_NAME);
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
	}
}
