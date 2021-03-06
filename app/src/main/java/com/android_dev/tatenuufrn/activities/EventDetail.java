package com.android_dev.tatenuufrn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.domain.Event$Table;
import com.android_dev.tatenuufrn.domain.EventUser;
import com.android_dev.tatenuufrn.domain.EventUser$Table;
import com.android_dev.tatenuufrn.managers.APIManager;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.container.JSONModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventDetail extends BaseActivity implements OnMapReadyCallback {
    private LayoutInflater inflater;
    private FlowCursorList<Event> events;
    private Event event;

    private List<EventUser> eventUsers;
    private EventUser eventUser;

    private MapFragment eventLocationMap;

    private Dialog ratingDialog;

    private ImageButton likeButton;
    private Button goingButton;

    private FrameLayout titleLayout;

    private TextView titleTextView;
    private TextView dateTimeTextView;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private TextView attendeesTitleTextView;
    private TextView attendeesTextView;

    private View ratingDialogView;
    private RatingBar ratingDialogViewRatingBar;
    private Button ratingDialogViewDismissButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentView(R.layout.activity_event_detail);

        //Recover Event
        String event_id = getIntent().getExtras().getString("event_id");
        events = new FlowCursorList<>(true, Event.class, Condition.column(Event$Table.ID).like((event_id)));
        event = events.getItem(0);

        //Recover EventUser
        eventUsers = new Select().from(EventUser.class).where(
                Condition.column(EventUser$Table.USERID).eq(APIManager.getInstance().getAuthenticatedUserId()),
                Condition.column(EventUser$Table.EVENTID).eq(event_id)
        ).queryList();
        if (!eventUsers.isEmpty()) {
            eventUser = eventUsers.get(0);
            Log.d("EventDetalEventUser", eventUser.toString());
        }

        initializeUI();
        initializeDefaultActions();

        if (eventUser != null){
            if (eventUser.getLiked()) setLikedButton();
            if (eventUser.getLiked()) setGoingButton();
            if (eventUser.getRate() == null) engineRatingDialog();
        } else {
            engineRatingDialog();
        }

        attendeesTitleTextView.setText(String.valueOf(event.getAttendeesCount()) + " ATTENDEES");
        attendeesTextView.setText(event.getAttendees());
    }

    public void initializeUI() {
        eventLocationMap = MapFragment.newInstance();
        eventLocationMap.getMapAsync(this);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.eventLocationContainer, eventLocationMap);
        fragmentTransaction.commit();

        // TITLE BACKGROUND
        titleLayout = (FrameLayout) findViewById(R.id.titleLayout);
        Drawable d = new BitmapDrawable(getResources(), event.getImageBitmap());
        titleLayout.setBackground(d);

        // LIKE BUTTON
        likeButton = (ImageButton) findViewById(R.id.eventDetailLikeButton);

        // LIKE BUTTON
        goingButton = (Button) findViewById(R.id.eventDetailJoinButton);

        // TITLE
        titleTextView = (TextView) findViewById(R.id.eventDetailTitleTextView);
        titleTextView.setText(event.getTitle());

        // TIME
        dateTimeTextView = (TextView) findViewById(R.id.eventDetailDateTimeTextView);
        dateTimeTextView.setText(event.getStringDateAndTime());

        // ADDRESS
        addressTextView = (TextView) findViewById(R.id.eventDetailAddressTextView);
        addressTextView.setText(event.getAddress());

        // DESCRIPTION
        descriptionTextView = (TextView) findViewById(R.id.eventDetailDescriptionTextView);
        descriptionTextView.setText(event.getDescription());

        // ATTENDEES
        attendeesTitleTextView= (TextView) findViewById(R.id.eventDetailAttendeesTitleTextView);
        attendeesTextView = (TextView) findViewById(R.id.eventDetailAttendeesTextView);

        // RATING DIALOG VIEW
        ratingDialogView = inflater.inflate(R.layout.rating_dialog, null);

        ratingDialogViewRatingBar = (RatingBar) ratingDialogView.findViewById(R.id.ratingDialogRatingBar);
        LayerDrawable stars = (LayerDrawable) ratingDialogViewRatingBar.getProgressDrawable();
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        ratingDialogViewDismissButton = (Button) ratingDialogView.findViewById(R.id.ratingDialogDismissButton);

        // RATING DIALOG
        ratingDialog = new Dialog(this);
        ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        ratingDialog.setContentView(ratingDialogView);

        // RATING DIALOG
        engineRatingDialog();
    }

    public void initializeDefaultActions(){
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeEvent();
            }
        });


        goingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToEvent();
            }
        });

        ratingDialogViewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rateEvent(rating);
            }
        });

        ratingDialogViewDismissButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ratingDialog.dismiss();
            }
        });
    }

    public void engineRatingDialog() {
        if (event.hasLocation()) {
            Location eventLocation = new Location("EventLocation");
            eventLocation.setLatitude(event.getLocX());
            eventLocation.setLongitude(event.getLocY());
            Location userLocation = LocationServices.FusedLocationApi
                    .getLastLocation(TatenuUFRNApplication.mGoogleApiClient);
            if ((eventUser == null || eventUser.getRate() == null) && userLocation != null) {
                float distance = userLocation.distanceTo(eventLocation);
                Log.i("EventDistance", String.valueOf(distance));
                if (event.getRadiusTrigger().floatValue() > distance) {
                    Log.i("NearEvent", "TRUE");

                    ratingDialog.show();
                }
            }
        }
    }

    public void updateEventUser(String eventUserJsonString){
        JSONModel<EventUser> jsonModel = null;
        try {
            jsonModel = new JSONModel<>(new JSONObject(eventUserJsonString), EventUser.class);
            eventUser = jsonModel.toModel();
            Log.d("eventUserJson", eventUserJsonString);
            Log.d("eventUserModel", eventUser.toString());
            eventUser.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void likeEvent(){
        APIManager.getInstance().like(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LIKE", response);
                updateEventUser(response);
                setLikedButton();
            }
        }, event.getId());
    }

    public void goingToEvent(){
        APIManager.getInstance().going(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JOIN", response);
                updateEventUser(response);
                setGoingButton();
            }
        }, event.getId());
    }

    public void rateEvent(float rating){
        APIManager.getInstance().rate(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RATED", response);
                updateEventUser(response);
                ratingDialog.dismiss();
            }
        }, event.getId(), rating);
    }

    public void setLikedButton(){
        likeButton.setImageResource(R.drawable.i_heart_g40);
    }

    public void setGoingButton(){
        goingButton.setText("GOING");
        goingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.i_check_w40, 0, 0, 0);
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
}