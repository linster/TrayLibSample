package ca.stefanm.traylibrarysample.PreferenceModels;

import android.content.Context;
import android.util.Log;

import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.Tray;

/**
 * Created by Stefan on 10/22/2016.
 */
public class CommonPreferenceModel extends AbstractPreferenceModel {

    private static String TAG = "CommonPreferenceModel";


    private static CommonPreferenceModel ourInstance;

    public static CommonPreferenceModel getInstance(Context c) {

        if (ourInstance == null){
            Log.d(TAG, "Instantiated CommonPreferenceModel");
            ourInstance = new CommonPreferenceModel(c);
        }
        return ourInstance;
    }

    private CommonPreferenceModel(Context c) {
        mContext = c;
        appPreferences = new AppPreferences(c);
    }
}
