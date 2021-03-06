package com.android_dev.tatenuufrn.activities;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.adapters.EventAdapter;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.domain.EventUser;
import com.android_dev.tatenuufrn.helpers.DateHelper;
import com.android_dev.tatenuufrn.managers.APIManager;
import com.android_dev.tatenuufrn.managers.EventManager;
import com.raizlabs.android.dbflow.structure.container.JSONModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ListEvents extends BaseActivity {
    ListView listEvents;
    EventAdapter adapter;
    private EventLoaderAsyncTask eventLoaderAsyncTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        listEvents = (ListView) findViewById(R.id.event_list);
        adapter = new EventAdapter(this, R.layout.activity_list_events);
        listEvents.setAdapter(adapter);

        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.listEventsSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvents();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
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

    public void updateEventUserData(){
        Log.d("updateEventUserData", "CALL");
        APIManager.getInstance().getEventUserData(this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONModel<EventUser> jsonModel = new JSONModel<>(arr.getJSONObject(i), EventUser.class);
                        EventUser eventUser = jsonModel.toModel();
                        Log.d("eventUserJson", arr.getJSONObject(i).toString());
                        Log.d("eventUserModel", eventUser.toString());
                        eventUser.save();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application_menu, menu);
        return true;
    }

    private class EventLoaderAsyncTask extends AsyncTask<Void, Integer, Void> {

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

        @Override
        protected void onProgressUpdate(Integer... progress){
            adapter.update();
        }

        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            String lastUpdated = sharedPreferences.getString("lastUpdated", "");
//            EventManager.refreshEvents(lastUpdated);
            JSONArray arr = EventManager.getUpdatedData(lastUpdated);
            try {
                List<Event> result = new ArrayList<Event>();
                for (int i=0; i < arr.length(); i++) {
                    JSONModel<Event> jsonModel = new JSONModel<>(arr.getJSONObject(i), Event.class);
                    Event event = jsonModel.toModel();
                    event.updateImageString();
                    event.save();
                    result.add(event);
                    publishProgress(i, arr.length()+1);
                }
                updateEventUserData();
                publishProgress(1, 1);
            }
            catch(Throwable t) {
                t.printStackTrace();
            }

            return null;
        }

    }
}
