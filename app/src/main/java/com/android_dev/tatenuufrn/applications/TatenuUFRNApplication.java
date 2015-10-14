package com.android_dev.tatenuufrn.applications;

import android.app.Application;
import android.content.SharedPreferences;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by adelinosegundo on 10/13/15.
 */
public class TatenuUFRNApplication extends Application {
    public static String SHARED_PREFERENCES_NAME = "TatenUFRN";


    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        FlowManager.getDatabase(TatenUFRNDatabase.NAME).reset(this);
    }
}
