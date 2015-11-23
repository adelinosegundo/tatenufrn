package com.android_dev.tatenuufrn.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.domain.Event$Table;
import com.android_dev.tatenuufrn.managers.APIManager;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.builder.Condition;

public class EventDetail extends Activity implements OnMapReadyCallback {
    private FlowCursorList<Event> events;
    private Event event;
    private MapFragment eventLocationMap;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private FrameLayout titleLayout;
    private RatingBar ratingEventRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);
        String event_id = getIntent().getExtras().getString("event_id");
        events = new FlowCursorList<>(true, Event.class, Condition.column(Event$Table.ID).like((event_id)));
        event = events.getItem(0);
        eventLocationMap = MapFragment.newInstance();
        eventLocationMap.getMapAsync(this);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.eventLocationContainer, eventLocationMap);
        fragmentTransaction.commit();

        titleTextView = (TextView) findViewById(R.id.titleEventDetailTextView);

        descriptionTextView = (TextView) findViewById(R.id.descriptionEventDetailTextView);

        titleLayout = (FrameLayout) findViewById(R.id.titleLayout);

        ratingEventRatingBar = (RatingBar) findViewById(R.id.ratingEventRatingBar);

        LayerDrawable stars = (LayerDrawable) ratingEventRatingBar.getProgressDrawable();
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        titleTextView.setText(event.getTitle());

        descriptionTextView.setText(event.getDescription());

        Drawable d = new BitmapDrawable(getResources(), event.getImageBitmap());

        titleLayout.setBackground(d);

        setRatingBar();
    }

    public void setRatingBar() {
        ratingEventRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rateEvent(rating);
            }
        });
        ratingEventRatingBar.setVisibility(View.GONE);
        Location eventLocation = new Location("EventLocation");
        eventLocation.setLatitude(event.getLocX());
        eventLocation.setLongitude(event.getLocY());
        Location userLocation = LocationServices.FusedLocationApi
                .getLastLocation(TatenuUFRNApplication.mGoogleApiClient);
        if (userLocation != null){
            float distance = userLocation.distanceTo(eventLocation);
            Log.i("EventDistance", String.valueOf(distance));
            if (event.getRadiusTrigger().floatValue() > distance) {
                Log.i("NearEvent", "TRUE");
                ratingEventRatingBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void rateEvent(float rating){
        APIManager.getInstance().rate(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RATED", response);
            }
        }, event.getId(), rating);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (event.hasLocation()) {
            LatLng eventLocation = new LatLng(event.getLocX(), event.getLocY());
            map.addMarker(new MarkerOptions()
                    .position(eventLocation)
                    .title(event.getTitle()));
            map.getUiSettings().setScrollGesturesEnabled(false);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 17));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}