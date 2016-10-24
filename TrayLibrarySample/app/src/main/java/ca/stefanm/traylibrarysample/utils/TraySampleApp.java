package ca.stefanm.traylibrarysample.utils;

import android.app.Application;
import android.util.Log;

import ca.stefanm.traylibrarysample.PreferenceModels.CommonPreferenceModel;

/**
 * Created by Stefan on 10/22/2016.
 */

public class TraySampleApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Log.d("TraySampleApp", "onCreate()");
        //Make a common preference model.
        CommonPreferenceModel commonPreferenceModel =
                CommonPreferenceModel.getInstance(getApplicationContext());

        //Perform a migration here?
        commonPreferenceModel.migrateSharedPreferencesToTray();
    }



}
