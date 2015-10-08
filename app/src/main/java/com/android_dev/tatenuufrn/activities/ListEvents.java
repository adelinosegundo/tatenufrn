package com.android_dev.tatenuufrn.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.adapters.EventAdapter;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.helpers.EventPopulator;

import java.util.List;


public class ListEvents extends ActionBarActivity {
    private ListView listEvents;
    private EventAdapter adapter;
    private List<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);


        EventPopulator.populate(1);
        listEvents = (ListView) findViewById(R.id.event_list);
        events = Event.listAll(Event.class);
        events = Event.listAll(Event.class);
        adapter = new EventAdapter(this, R.layout.event_row, Event.listAll(Event.class));
        listEvents.setAdapter(adapter);
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
