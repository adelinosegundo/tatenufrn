package com.android_dev.tatenuufrn.applications;

import android.app.Application;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by adelinosegundo on 10/13/15.
 */
public class TatenuUFRNApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        FlowManager.getDatabase(TatenUFRNDatabase.NAME).reset(this);
    }
}
