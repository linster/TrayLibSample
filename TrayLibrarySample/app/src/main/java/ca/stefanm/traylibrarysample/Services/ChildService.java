package ca.stefanm.traylibrarysample.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import ca.stefanm.traylibrarysample.PreferenceModels.CommonPreferenceModel;

public class ChildService extends Service {

    private String TAG = "ChildService";


    public ChildService() {
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


    //Send an intent to this service to update the shared preferences

    //TODO Receive the intent here.....

    //Read preferences and post to toast
    private void readPrefs() {
        CommonPreferenceModel prefs = commonPreferenceModel;

        Toast.makeText(this, "[ChildService] Reading Preferences: " + prefs.getFoo(), Toast.LENGTH_SHORT).show();
    }

    //Increment preference val, write out a toast.
    private void incrementPrefContents() {
        CommonPreferenceModel prefs = commonPreferenceModel;

        CommonPreferenceModel.incrementFakeContents();

        CommonPreferenceModel.PopulatePrefsWithJunk(prefs);

        Toast.makeText(this, "Incremented prefs", Toast.LENGTH_SHORT).show();
    }


}
