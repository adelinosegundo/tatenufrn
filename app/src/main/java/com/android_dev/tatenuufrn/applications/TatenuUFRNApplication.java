package com.android_dev.tatenuufrn.applications;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.android_dev.tatenuufrn.helpers.DateHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.raizlabs.android.dbflow.config.FlowManager;

public class TatenuUFRNApplication extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static String SHARED_PREFERENCES_NAME = "TatenUFRN";
    public static final String API_HOST = "http://tatenufrn-webservice.herokuapp.com";
//    public static final String API_HOST = "http://192.168.43.147:3000";
    public static GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        buildGoogleApiClient();
        resetApp();
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
        editor.putString("lastUpdated", "");
        editor.commit();
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
