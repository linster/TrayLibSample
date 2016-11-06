package ca.stefanm.traylibrarysample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.stefanm.traylibrarysample.PreferenceModels.CommonPreferenceModel;
import ca.stefanm.traylibrarysample.Services.ChildService;
import ca.stefanm.traylibrarysample.Services.IChildService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bindService(new Intent(this, ChildService.class), childServiceConnection, BIND_AUTO_CREATE);

    }

    @BindView(R.id.txtPrefContents) TextView sharedPreferenceContents;

    @OnClick(R.id.updateview) void updateSharedPreferenceTextView(){
        Log.d("MainActivity", "Clicked Update View Button");
        String contents = new String();

        CommonPreferenceModel prefs = CommonPreferenceModel.getInstance(getApplicationContext());

        contents = contents + "Configuration contents: \n" +
                "FOO: " + prefs.getFoo() + "\n" +
                "BAZ: " + prefs.getBaz() + "\n" +
                "Child foo:" + prefs.getChildFoo().toString() + "\n";

        sharedPreferenceContents.setText(contents);
    }

    @OnClick(R.id.migratebutton)
    protected void migrateSharedPrefsToTray(){
        CommonPreferenceModel prefs = CommonPreferenceModel.getInstance(getApplicationContext());

        prefs.migrateSharedPreferencesToTray();

        Toast.makeText(this, "Migrated preferences to Tray", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.incrementbutton)
    protected void incrementPrefContents() {
        CommonPreferenceModel prefs = CommonPreferenceModel.getInstance(getApplicationContext());

        CommonPreferenceModel.incrementFakeContents();

        CommonPreferenceModel.PopulatePrefsWithJunk(prefs);

        Toast.makeText(this, "Incremented prefs", Toast.LENGTH_SHORT).show();

        updateSharedPreferenceTextView();
    }

    public IChildService mChildService;

    private ServiceConnection childServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mChildService = IChildService.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, "Bound to ChildService", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "Bound to child service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("MainActivity", "Service has unexpectedly disconnected");
            mChildService = null;
        }
    };

    //Srv read prefs
    @OnClick(R.id.srvread)
    protected void srvReadPrefs(){
        try {
            if (mChildService != null) {
                mChildService.readPreferences();
            } else {
                Toast.makeText(this, "Haven't bound to child service yet.", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }
    //Srv write prefs
    @OnClick(R.id.srvwrite)
    protected void srvWritePrefs(){
        try {
            if (mChildService != null) {
                mChildService.setPreferences();
            } else {
                Toast.makeText(this, "Haven't bound to child service yet.", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
