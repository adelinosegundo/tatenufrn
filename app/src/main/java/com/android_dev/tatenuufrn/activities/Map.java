package com.android_dev.tatenuufrn.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.domain.Event$Table;
import com.android_dev.tatenuufrn.managers.TatenUFRNNotificationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

public class Map extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private MapFragment eventLocationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        eventLocationMap = MapFragment.newInstance();
        eventLocationMap.getMapAsync(this);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.eventLocationContainer, eventLocationMap);
        fragmentTransaction.commit();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);
        List<Event> events = new Select().from(Event.class).queryList();
        Log.d("CheckNearEvents", String.valueOf(events.size()));
        for(Event event : events){
            if (event.hasLocation()) {
                LatLng eventLocation = new LatLng(event.getLocX(), event.getLocY());
                googleMap.addMarker(new MarkerOptions()
                        .position(eventLocation)
                        .title(event.getTitle())
                        .draggable(false)
                        .snippet(event.getId()));
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 11));
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String event_id = marker.getSnippet();
        Intent intent = new Intent(this, EventDetail.class);
        intent.putExtra("event_id", event_id);
        startActivity(intent);
        return false;
    }
}
