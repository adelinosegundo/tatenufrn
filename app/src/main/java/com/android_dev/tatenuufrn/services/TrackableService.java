package com.android_dev.tatenuufrn.services;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by adelinosegundo on 11/25/15.
 */
public abstract class TrackableService extends Service {
    protected void setRunning(boolean running) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(this.getClass().toString(), running);
        editor.apply();
    }
}
