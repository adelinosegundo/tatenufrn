package com.android_dev.tatenuufrn.applications;

import android.app.Application;
import android.content.SharedPreferences;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.android_dev.tatenuufrn.helpers.DateHelper;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by adelinosegundo on 10/13/15.
 */
public class TatenuUFRNApplication extends Application {
    public static String SHARED_PREFERENCES_NAME = "TatenUFRN";
    public static final String API_HOST = "http://192.168.0.5:3000";

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        resetApp();
    }

    public void resetApp(){
        //ResetDB
        FlowManager.getDatabase(TatenUFRNDatabase.NAME).reset(this);

        //ResetPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastUpdated", "");
        editor.commit();
    }
}
