package ca.stefanm.traylibrarysample.PreferenceModels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.SharedPreferencesImport;

import ca.stefanm.traylibrarysample.Models.ComposedFoo;

/**
 * Created by Stefan on 10/22/2016.
 */

public class AbstractPreferenceModel {

    private String TAG = "AbstractPreferenceModel";

    protected static final String PREFERENCE_FOO_KEY = "key_foo";
    protected static final String PREFERENCE_BAZ_KEY = "key_baz";
    protected static final String PREFERENCE_CHILD_KEY = "key_child";

    protected static final String SHARED_PREFERENCES_FILE = "spfile";

    protected Context mContext;

    protected AppPreferences appPreferences;


    public ComposedFoo getChildFoo(){

        ComposedFoo returned = null;

        returned = (ComposedFoo)getObject(PREFERENCE_CHILD_KEY, ComposedFoo.class);

        if (returned == null){
            return new ComposedFoo("", "", 0);
        } else {
            return returned;
        }
    }

    public void setChildFoo(ComposedFoo childFoo) {
        setObject(PREFERENCE_CHILD_KEY, childFoo);
    }

    public String getBaz() {
        return (String) getObject(PREFERENCE_BAZ_KEY, String.class);
    }

    public void setBaz(String val) {
        setObject(PREFERENCE_BAZ_KEY, val);
    }

    public String getFoo() {
        return (String) getObject(PREFERENCE_FOO_KEY, String.class);
    }

    public void setFoo(String val) {
        setObject(PREFERENCE_FOO_KEY, val);
    }

    protected Integer getOSVersion(){
        //override in the unit tests to populate various stores.
        //In the unit tests, override this method in a subclass of CommmonPreferenceModel
        return Build.VERSION.SDK_INT;
    }

    protected Object getObject(String key, Class type){
        if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            return getTrayObject(key, type);
        } else {
            return getSharedPreferencesObject(key, type);
        }
    }

    protected void setObject(String key, Object obj){
        if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            setTrayObject(key, obj);
        } else {
            setSharedPreferencesObject(key, obj);
        }
    }


    private Object getSharedPreferencesObject(String key, Class type){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        try {
            if (type == String.class){
                return sharedPreferences.getString(key, "");
            } else if (type == Long.class){
                return sharedPreferences.getLong(key, 0);
            } else { //Incomplete, but good enough for this test
                Moshi moshi = new Moshi.Builder().build();
                return moshi.adapter(type).fromJson(sharedPreferences.getString(key, ""));
            }
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }

        return null;
    }

    private void setSharedPreferencesObject(String key, Object obj){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            if (obj.getClass() == String.class){
                editor.putString(key, (String) obj);
            } else if (obj.getClass() == Long.class) {
                editor.putLong(key, (Long) obj);
            } else if (obj.getClass() == ComposedFoo.class){
                //Little bit of a hack to switch on a type like this.
                Moshi moshi = new Moshi.Builder().build();
                editor.putString(key,
                        moshi.adapter(ComposedFoo.class)
                                .toJson((ComposedFoo)obj));
            } else {
                throw new Exception("Unsupported object attempted to be saved.");
            }

            editor.apply();
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }

    }

    private Object getTrayObject(String key, Class type){

        try {
            if (type == Long.class){
                return appPreferences.getLong(key, 0L);
            } else if (type == String.class) {
                return appPreferences.getString(key, "");
            } else if (type == Boolean.class){
                return appPreferences.getBoolean(key, false);
            } else {
                Moshi moshi = new Moshi.Builder().build();
                return moshi.adapter(type).fromJson(
                        appPreferences.getString(key, ""));
            }
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private void setTrayObject(String key, Object obj){

        try {
            if (obj.getClass() == Long.class){
                appPreferences.put(key, (Long) obj);
            } else if (obj.getClass() == String.class) {
                appPreferences.put(key, (String) obj);
            } else if (obj.getClass() == ComposedFoo.class){
                Moshi moshi = new Moshi.Builder().build();
                appPreferences.put(key,
                        moshi.adapter(ComposedFoo.class).toJson((ComposedFoo)obj));
            } else {
                throw new Exception("Unsupported object attempted to be saved.");
            }
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    public void migrateSharedPreferencesToTray(){

        String[] migrationKeys = {
                PREFERENCE_FOO_KEY,
                PREFERENCE_BAZ_KEY,
                PREFERENCE_CHILD_KEY};

        for (String key : migrationKeys){
            SharedPreferencesImport sharedPreferencesImport =
                    new SharedPreferencesImport(mContext,
                            SHARED_PREFERENCES_FILE,
                            key,
                            key);

            appPreferences.migrate(sharedPreferencesImport);
        }
    }

    public enum Store {SHARED, TRAY};

    public static Integer fakeContentsInt = 1;

    public static void incrementFakeContents() { fakeContentsInt++; }

    public static void PopulatePrefsWithJunk(AbstractPreferenceModel prefs){
        prefs.setFoo("Foo " + fakeContentsInt.toString());
        prefs.setBaz("Baz " + fakeContentsInt.toString());
        prefs.setChildFoo(new ComposedFoo("ID" + fakeContentsInt, "ChildNote" + fakeContentsInt, 3));
    }

    public static void PopulatePrefsWithJunk(AbstractPreferenceModel prefs, Store store){

        if (store == Store.SHARED){
            prefs.setObject(PREFERENCE_FOO_KEY, "Foo " + fakeContentsInt.toString());
            prefs.setObject(PREFERENCE_BAZ_KEY, "Baz " + fakeContentsInt.toString());
            prefs.setObject(PREFERENCE_CHILD_KEY, new ComposedFoo("ID" + fakeContentsInt, "N" + fakeContentsInt, 1));
        } else {
            prefs.setTrayObject(PREFERENCE_FOO_KEY, "Foo " + fakeContentsInt.toString());
            prefs.setTrayObject(PREFERENCE_BAZ_KEY, "Baz " + fakeContentsInt.toString());
            prefs.setTrayObject(PREFERENCE_CHILD_KEY, new ComposedFoo("ID" + fakeContentsInt, "N" + fakeContentsInt, 1));
        }

    }

}
