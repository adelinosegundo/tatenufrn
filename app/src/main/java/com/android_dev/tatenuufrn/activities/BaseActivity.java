
package com.android_dev.tatenuufrn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android_dev.tatenuufrn.R;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_events_list:
                eventsList();
                return true;
            case R.id.menu_item_events_map:
                eventsMap();
                return true;
            case R.id.menu_item_preferences:
                preferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void eventsList(){
        startActivity(new Intent(this, ListEvents.class));
    }

    protected void eventsMap(){
        startActivity(new Intent(this, Map.class));
    }

    protected void preferences(){
        startActivity(new Intent(this, Preferences.class));
    }

}
