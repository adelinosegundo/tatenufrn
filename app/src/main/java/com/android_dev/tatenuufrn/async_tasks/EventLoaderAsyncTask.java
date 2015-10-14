package com.android_dev.tatenuufrn.async_tasks;

import android.os.AsyncTask;

import com.android_dev.tatenuufrn.adapters.EventAdapter;
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

/**
 * Created by adelinosegundo on 10/13/15.
 */
public class EventLoaderAsyncTask extends AsyncTask<EventAdapter, Void, EventAdapter> {

    @Override
    protected void onPostExecute(EventAdapter result) {
        super.onPostExecute(result);
        result.update();
    }

    protected EventAdapter doInBackground(EventAdapter... adapter) {
        System.out.println("Requesting events");
        List<Event> result = new ArrayList<Event>();

        try {

            InputStream is = new URL("http://tatenufrn-webservice.herokuapp.com/events.json").openStream();
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
            return adapter[0];
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

}
