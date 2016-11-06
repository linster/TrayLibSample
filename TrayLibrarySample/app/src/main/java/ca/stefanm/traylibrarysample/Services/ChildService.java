package ca.stefanm.traylibrarysample.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import ca.stefanm.traylibrarysample.PreferenceModels.CommonPreferenceModel;

public class ChildService extends Service {

    private String TAG = "ChildService";


    public ChildService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand for Childservice");
        return super.onStartCommand(intent, flags, startId);

    }

    private CommonPreferenceModel commonPreferenceModel;

    @Override
    public void onCreate(){
        super.onCreate();
        commonPreferenceModel = CommonPreferenceModel.getInstance(getApplicationContext());
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IChildService.Stub mBinder = new IChildService.Stub(){
        @Override
        public void setPreferences() throws RemoteException {
            incrementPrefContents();
        }

        @Override
        public void readPreferences() throws RemoteException {
            readPrefs();
        }
    };



    //Read preferences and post to toast
    private void readPrefs() {
        CommonPreferenceModel prefs = commonPreferenceModel;
        Log.d(TAG, "[ChildService] Reading Preferences: " + prefs.getFoo());
    }

    //Increment preference val, write out a toast.
    private void incrementPrefContents() {
        CommonPreferenceModel prefs = commonPreferenceModel;

        CommonPreferenceModel.incrementFakeContents();

        CommonPreferenceModel.PopulatePrefsWithJunk(prefs);
        Log.d(TAG, "[ChildService] Writing Preferences: Incremented prefs");
    }


}
