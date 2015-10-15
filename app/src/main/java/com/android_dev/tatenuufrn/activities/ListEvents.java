package com.android_dev.tatenuufrn.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.adapters.EventAdapter;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.helpers.DateHelper;
import com.android_dev.tatenuufrn.managers.EventManager;


public class ListEvents extends Activity {
    ListView listEvents;
    EventAdapter adapter;
    private EventLoaderAsyncTask eventLoaderAsyncTask;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.listEventsSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvents();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                refreshEvents();
                swipeRefreshLayout.setRefreshing(true);
            }
        });

    }

    public void refreshEvents(){
        if (eventLoaderAsyncTask == null) {
            eventLoaderAsyncTask = new EventLoaderAsyncTask();
            eventLoaderAsyncTask.execute();
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

    private class EventLoaderAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter.update();
            SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastUpdated", DateHelper.getCurrentDateInFormat("dd-M-yyyy'T'HH:mm:ss.SSSZ"));
            editor.commit();
            eventLoaderAsyncTask = null;
            swipeRefreshLayout.setRefreshing(false);
        }

        protected Void doInBackground(Void... adapter) {
            SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            EventManager.refreshEvents(sharedPreferences.getString("lastUpdated", ""));
            return null;
        }

    }
}
