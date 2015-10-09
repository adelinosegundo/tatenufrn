package com.android_dev.tatenuufrn.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.adapters.EventAdapter;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.helpers.DbBitmapUtility;
import com.android_dev.tatenuufrn.helpers.EventPopulator;

import java.util.List;


public class ListEvents extends ActionBarActivity {
    private ListView listEvents;
    private EventAdapter adapter;
    private List<Event> events;
    private Button populateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        listEvents = (ListView) findViewById(R.id.event_list);

        adapter = new EventAdapter(this, R.layout.event_row, Event.listAll(Event.class));
        listEvents.setAdapter(adapter);


        populateButton = (Button) findViewById(R.id.populateButton);
        populateButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Bitmap bitmap = DbBitmapUtility.LoadImageFromWebOperations("http://icons.iconarchive.com/icons/martz90/circle/512/android-icon.png");
                        EventPopulator.populate(5, bitmap);
                        System.out.println("populated");
                        adapter = new EventAdapter(v.getContext(), R.layout.event_row, Event.listAll(Event.class));
                        listEvents.setAdapter(adapter);
                    }
                }
        );

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0) {
            Bitmap foto = (Bitmap) data.getExtras().get("data");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_events, menu);
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
