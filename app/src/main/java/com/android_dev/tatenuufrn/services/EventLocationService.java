package com.android_dev.tatenuufrn.services;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.managers.TatenUFRNNotificationManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

/**
 * Created by adelinosegundo on 11/25/15.
 */
public class EventLocationService extends TrackableService {

    LocationManager locationManager;
    LocationListener locationListener;

    public EventLocationService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("CheckNearEvents", "Service created");
        final Context context = this;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                checkIfNearAnyEvent(location);
            }

            private void checkIfNearAnyEvent(Location location) {
                List<Event> events = new Select().from(Event.class).queryList();
                Log.d("CheckNearEvents", String.valueOf(events.size()));
                for(Event event : events){
                    Location eventLocation = new Location("EventLocation");
                    eventLocation.setLatitude(event.getLocX());
                    eventLocation.setLongitude(event.getLocY());
                    float distance = location.distanceTo(eventLocation);
                    Log.i("EventDistance", String.valueOf(distance));
                    if (event.getRadiusTrigger().floatValue() > distance) {
                        String event_id = event.getId();
                        TatenUFRNNotificationManager.buildEventNotification(context, event_id);
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
        this.setRunning(true);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(locationListener);
        this.setRunning(false);
    }
}
