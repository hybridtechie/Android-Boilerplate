package me.nithin.james.androidboilerplate.application;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;

import java.util.HashMap;
import java.util.UUID;

import me.nithin.james.androidboilerplate.core.DefaultActivity;

/**
 * Created by njames on 1/03/2018..
 */

public class DefaultApplication extends Application {

    private DefaultActivity mCurrentActivity;
    private HashMap<String, Object> mTempStorage;
    private boolean mHasFingerPrintCapability;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTempStorage = new HashMap<>();
    }

    public void setCurrentActivity(DefaultActivity a) {
        mCurrentActivity = a;
    }

    public void saveData(String key, Object data) {

        mTempStorage.put(key, data);
    }

    public void removeData(String key) {

        if (mTempStorage.containsKey(key))
            mTempStorage.remove(key);
    }

    public Object restoreData(String key) {

        return mTempStorage.get(key);
    }

    public String getAppVersion(Context context) {

        String appVersion = "n/a";

        try {

            // App Version
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return appVersion;
    }

    public String getDeviceName() {

        String manufacturer = Build.MANUFACTURER.substring(0, 1).toUpperCase() + Build.MANUFACTURER.substring(1);
        String model = Build.MODEL.substring(0, 1).toUpperCase() + Build.MODEL.substring(1);
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    /**
     * Returns the name assigned to the phone through the device's settings.
     *
     * @return
     */
    public String getPhoneName() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            return adapter.getName();
        } else {
            return "";
        }
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public String getUniqueID() {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
}
