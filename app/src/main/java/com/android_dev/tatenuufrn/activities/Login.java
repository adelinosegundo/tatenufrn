package com.android_dev.tatenuufrn.activities;

import android.content.Intent;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.domain.Event;
import com.raizlabs.android.dbflow.structure.container.JSONModel;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class Login extends Activity {
    private EditText usernameEditText;
    private EditText passowrdEditText;
    private Button loginButton;
    private Button populateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passowrdEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listEventsIntent = new Intent(getBaseContext(), ListEvents.class);
                startActivity(listEventsIntent);
            }
        });

        populateButton = (Button) findViewById(R.id.populateButton);
        populateButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        class EventLoaderAssyncTask extends AsyncTask<String, Void, List<Event>> {

                            @Override
                            protected void onPostExecute(List<Event> result) {
                                super.onPostExecute(result);
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
                                        Event event = jsonModel.toModel();
                                        event.updateImageString();
                                        event.save();
                                        result.add(event);
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

                        new EventLoaderAssyncTask().execute("http://tatenufrn-webservice.herokuapp.com/events.json");
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
