package com.android_dev.tatenuufrn.applications;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.android_dev.tatenuufrn.services.EventLocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.raizlabs.android.dbflow.config.FlowManager;

public class TatenuUFRNApplication extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static String SHARED_PREFERENCES_NAME = "TatenUFRN";
    public static String EVENT_LOCATION_PREFERENCE_NAME = "eneableEventLocationServices";

//    public static final String API_HOST = "http://tatenufrn-webservice.herokuapp.com";
    public static final String API_HOST = "http://192.168.0.6:3000";

    public static GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        buildGoogleApiClient();
        startServices();
        resetApp();
    }

    public static boolean isServiceRunning(String serviceClass, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(serviceClass, false);
    }

    public void startServices(){
        SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(EVENT_LOCATION_PREFERENCE_NAME, true)
                && !isServiceRunning(EventLocationService.class.toString(), this))
            startService(new Intent(this, EventLocationService.class));
    }

    public void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public void resetApp(){
        //ResetDB
        FlowManager.getDatabase(TatenUFRNDatabase.NAME).reset(this);

        //ResetPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences.edit().clear().commit();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("GOOGLE API", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("GOOGLE API", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
