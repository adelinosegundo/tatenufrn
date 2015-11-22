package com.android_dev.tatenuufrn.managers;

import com.android_dev.tatenuufrn.activities.ListEvents;
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
import java.util.concurrent.Callable;

/**
 * Created by adelinosegundo on 10/14/15.
 */
public class EventManager {
//    private static final String HOST = "http://tatenufrn-webservice.herokuapp.com/api/v1";
    private static final String HOST = "http://192.168.25.2:3000/api/v1";

    public static void refreshEvents(String lastUpdated){
        JSONArray arr = EventManager.getUpdatedData(lastUpdated);
        EventManager.saveUpdatedData(arr);
    }

    public static void saveUpdatedData(JSONArray arr){
        try {
            List<Event> result = new ArrayList<Event>();
            for (int i=0; i < arr.length(); i++) {
                JSONModel<Event> jsonModel = new JSONModel<>(arr.getJSONObject(i), Event.class);
                Event event = jsonModel.toModel();
                event.updateImageString();
                event.save();
                result.add(event);
            }
            System.out.println("Events saved");
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public static JSONArray getUpdatedData(String lastUpdated){
        try {
            String urlString = HOST + "/events";

            if (!lastUpdated.equals(""))
                urlString += "?last_updated=" + lastUpdated;
            System.out.println("Requesting events from " + urlString);
            InputStream is = new URL(urlString).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();

            JSONArray arr = new JSONArray(jsonText);
            return arr;
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }
}
