package com.ahmetkizilay.controls.androsc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.ahmetkizilay.controls.androsc.fragments.AboutMeDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.AddNewOSCControlListDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.NetworkSettingsDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.OpenFileDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.SaveFileDialogFragment;
import com.ahmetkizilay.controls.androsc.fragments.OSCViewFragment;
import com.ahmetkizilay.controls.androsc.osc.OSCWrapper;
import com.ahmetkizilay.controls.androsc.utils.NavigationDrawerView;
import com.ahmetkizilay.controls.androsc.utils.Utilities;
import com.ahmetkizilay.modules.donations.PaymentDialogFragment;
import com.ahmetkizilay.modules.donations.ThankYouDialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.Toast;

public class AndrOSCMainActivity extends FragmentActivity implements 
		com.ahmetkizilay.controls.androsc.fragments.OSCViewFragment.OnMenuToggledListener,
		com.ahmetkizilay.controls.androsc.fragments.AddNewOSCControlListDialogFragment.OnNewOSCControlSelected,
		com.ahmetkizilay.controls.androsc.fragments.SaveFileDialogFragment.OnSaveFileNameSelectedListener,
		com.ahmetkizilay.controls.androsc.fragments.OpenFileDialogFragment.OnOpenFileNameSelectedListener,
        com.ahmetkizilay.controls.androsc.fragments.NetworkSettingsDialogFragment.OnNetworkSettingsChangedListener,
        com.ahmetkizilay.controls.androsc.utils.NavigationDrawerView.OnOSCMenuItemClickedListener
{

	private final static String TAG_DIALOG_ADD_NEW_ITEM = "dlgAddNewItem";
	private final static String TAG_DIALOG_SAVE_FILE_NAME = "dlgSaveFileName";
	private final static String TAG_DIALOG_OPEN_FILE_NAME = "dlgOpenFileName";
    private final static String TAG_DIALOG_NETWORK_SETTINGS = "dlgNetworkSettings";
    private final static String TAG_DIALOG_ABOUT_ME = "dlgAboutMe";
    private final static String TAG_DIALOG_DONATIONS = "dlgDonations";
    private final static String TAG_DIALOG_THANKS = "dkgThanks";

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

        initializeNavigationDrawer();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		this.mOSCViewFragment = (OSCViewFragment) getSupportFragmentManager().findFragmentById(R.id.frgOSCView);

		ft.commit();



        if(this.mConnectOnStartUp) {
            connectOSC();
        }
	}

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        }, 1000);
    }

    private DrawerLayout mDrawerLayout;
    private com.ahmetkizilay.controls.androsc.utils.NavigationDrawerView mDrawer;

    private void initializeNavigationDrawer() {
       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       mDrawer = (com.ahmetkizilay.controls.androsc.utils.NavigationDrawerView) findViewById(R.id.left_drawer);
       mDrawer.setOnOSCMenuActionClicked(this);
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
	public void oscMenuItemActionSelected(NavigationDrawerView.OSCMenuActionEvent event) {
		if(event.getAction() == NavigationDrawerView.OSCMenuActionEvent.ACTION_NEW) {
			this.mOSCViewFragment.clearForNewTemplate();
            this.mDrawer.setCurrentTemplate("untitled");
            this.mCurrentFileName = "";
		}
		else if (event.getAction() == NavigationDrawerView.OSCMenuActionEvent.ACTION_OPEN) {
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
        else if(event.getAction() == NavigationDrawerView.OSCMenuActionEvent.ACTION_NETWORK) {
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
        else if(event.getAction() == NavigationDrawerView.OSCMenuActionEvent.ACTION_ABOUT) {
            // Show Network settings gragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_ABOUT_ME);
            if(prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            final AboutMeDialogFragment frgAboutMeDialog = AboutMeDialogFragment.newInstance();
            frgAboutMeDialog.setRequestListener(new AboutMeDialogFragment.RequestListener() {
                public void onDonationsRequested() {
                    frgAboutMeDialog.dismiss();
                    showDonationDialog();
                }
            });
            frgAboutMeDialog.show(ft, AndrOSCMainActivity.TAG_DIALOG_ABOUT_ME);
        }

        this.mDrawerLayout.closeDrawer(Gravity.START);
	}

    private void showDonationDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_ABOUT_ME);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final PaymentDialogFragment frgDonationsDialog = PaymentDialogFragment.getInstance(R.array.product_ids);
        frgDonationsDialog.setPaymentCompletedListener(new PaymentDialogFragment.PaymentCompletedListener() {
            public void onPaymentCompleted() {
                frgDonationsDialog.dismiss();
                showThankYouDialog();
            }
        });
        frgDonationsDialog.show(ft, AndrOSCMainActivity.TAG_DIALOG_DONATIONS);
    }

    private void showThankYouDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_ABOUT_ME);
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final ThankYouDialogFragment frgThankYouDialog = ThankYouDialogFragment.newInstance();
        frgThankYouDialog.show(ft, AndrOSCMainActivity.TAG_DIALOG_THANKS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // pass the request back to the fragment
        if(requestCode == PaymentDialogFragment.PAYMENT_RESULT_CODE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(AndrOSCMainActivity.TAG_DIALOG_DONATIONS);
            if (fragment != null)
            {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void oscMenuItemTemplateSelected(String templateName) {

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

        File file = new File(fileName);
        String fn = file.getName();
        this.mCurrentFileName = fn.substring(0, fn.length() - 5);
        this.mDrawer.setCurrentTemplate(this.mCurrentFileName);
	}

	@Override
	public void onOpenFileSelected(String fileName) {
		this.mOSCViewFragment.inflateTemplate(this.mBaseFolder + File.separator + fileName);
		this.mOSCViewFragment.disableTemplateEditing();
		this.mCurrentFileName = fileName.substring(0, fileName.length() - 5);
        this.mDrawer.setCurrentTemplate(this.mCurrentFileName);
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
