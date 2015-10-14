package com.android_dev.tatenuufrn.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.adapters.EventAdapter;
import com.android_dev.tatenuufrn.async_tasks.EventLoaderAsyncTask;
import com.android_dev.tatenuufrn.domain.Event;
import com.raizlabs.android.dbflow.list.FlowQueryList;



public class ListEvents extends Activity {
    private ListView listEvents;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        listEvents = (ListView) findViewById(R.id.event_list);
        adapter = new EventAdapter(this, R.layout.event_row);
        listEvents.setAdapter(adapter);
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String event_id = ((Event) adapterView.getItemAtPosition(i)).getId();
                Intent intent = new Intent(getBaseContext(), EventDetail.class);
                intent.putExtra("event_id", event_id);
                startActivity(intent);
            }
        });
        new EventLoaderAsyncTask().execute(adapter);

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
