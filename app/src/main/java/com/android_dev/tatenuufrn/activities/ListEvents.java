package com.android_dev.tatenuufrn.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.structure.container.JSONModel;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class ListEvents extends ActionBarActivity {
    private ListView listEvents;
    private EventAdapter adapter;
    private FlowQueryList<Event> events;
    private Button populateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        listEvents = (ListView) findViewById(R.id.event_list);
        events = new FlowQueryList<Event>(Event.class);
        adapter = new EventAdapter(this, R.layout.event_row);
        listEvents.setAdapter(adapter);


        populateButton = (Button) findViewById(R.id.populateButton);
        populateButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        class EventLoaderAssyncTask extends AsyncTask<String, Void, List<Event>> {

                            @Override
                            protected void onPostExecute(List<Event> result) {
                                super.onPostExecute(result);
                                System.out.println("Updating adapter");
                                adapter.update();
                            }

                            protected List<Event> doInBackground(String... params) {
                                System.out.println("Requesting events");
                                List<Event> result = new ArrayList<Event>();

                                try {

                                    InputStream is = new URL(params[0]).openStream();
                                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                                    StringBuilder sb = new StringBuilder();
                                    int cp;
                                    while ((cp = rd.read()) != -1) {
                                        sb.append((char) cp);
                                    }
                                    String jsonText = sb.toString();

                                    JSONArray arr = new JSONArray(jsonText);
                                    for (int i=0; i < arr.length(); i++) {
                                        JSONModel<Event> jsonModel = new JSONModel<>(arr.getJSONObject(i), Event.class);
                                        jsonModel.save();
                                        result.add(jsonModel.toModel());
                                    }
                                    System.out.println("Events saved");
                                    return result;
                                }
                                catch(Throwable t) {
                                    t.printStackTrace();
                                }
                                return null;
                            }
                        }

                        new EventLoaderAssyncTask().execute("http://192.168.25.20:3000/events.json");
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
