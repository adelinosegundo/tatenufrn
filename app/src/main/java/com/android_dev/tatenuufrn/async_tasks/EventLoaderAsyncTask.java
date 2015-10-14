package com.android_dev.tatenuufrn.async_tasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.activities.EventDetail;
import com.android_dev.tatenuufrn.activities.ListEvents;
import com.android_dev.tatenuufrn.adapters.EventAdapter;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.helpers.DateHelper;
import com.android_dev.tatenuufrn.managers.EventManager;

/**
 * Created by adelinosegundo on 10/13/15.
 */
public class EventLoaderAsyncTask extends AsyncTask<Void, Void, Void> {

    private ListEvents listEventsActivity;
    private ProgressDialog progressDialog;
    public EventLoaderAsyncTask(ListEvents listEventsActivity){
        this.listEventsActivity = listEventsActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();;
        progressDialog = ProgressDialog.show(listEventsActivity, "Looking for posts", "Loading...", true, false);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        ListView eventsList = (ListView) listEventsActivity.findViewById(R.id.event_list);
        EventAdapter adapter = new EventAdapter(listEventsActivity, R.id.event_row);
        eventsList.setAdapter(adapter);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String event_id = ((Event) adapterView.getItemAtPosition(i)).getId();
                Intent intent = new Intent(listEventsActivity.getBaseContext(), EventDetail.class);
                intent.putExtra("event_id", event_id);
                listEventsActivity.startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = listEventsActivity.getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, listEventsActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastUpdated", DateHelper.getCurrentDateInFormat("dd-M-yyyy'T'HH:mm:ss.SSSZ"));
        editor.commit();
        progressDialog.dismiss();
    }

    protected Void doInBackground(Void... adapter) {
        SharedPreferences sharedPreferences = listEventsActivity.getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, listEventsActivity.MODE_PRIVATE);
        EventManager.refreshEvents(sharedPreferences.getString("lastUpdated", ""));
        return null;
    }

}
