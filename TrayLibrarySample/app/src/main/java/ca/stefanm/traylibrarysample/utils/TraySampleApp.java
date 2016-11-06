package ca.stefanm.traylibrarysample.utils;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import net.grandcentrix.tray.core.TrayLog;

import ca.stefanm.traylibrarysample.PreferenceModels.CommonPreferenceModel;
import ca.stefanm.traylibrarysample.Services.ChildService;

/**
 * Created by Stefan on 10/22/2016.
 */

public class TraySampleApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Log.d("TraySampleApp", "Updating Tray logging level");
        TrayLog.DEBUG = true;

        Log.d("TraySampleApp", "onCreate()");
        //Make a common preference model.
        CommonPreferenceModel commonPreferenceModel =
                CommonPreferenceModel.getInstance(getApplicationContext());

        //Perform a migration here
        commonPreferenceModel.migrateSharedPreferencesToTray();
    }



}
