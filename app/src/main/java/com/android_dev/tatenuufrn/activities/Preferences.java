package com.android_dev.tatenuufrn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.applications.TatenuUFRNApplication;
import com.android_dev.tatenuufrn.services.EventLocationService;

public class Preferences extends BaseActivity {
    private Switch rateEventNotificationSwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        final Context context = this;

        final SharedPreferences sharedPreferences = getSharedPreferences(TatenuUFRNApplication.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        boolean eventRatePreference = sharedPreferences.getBoolean(TatenuUFRNApplication.EVENT_LOCATION_PREFERENCE_NAME, true);
        rateEventNotificationSwich = (Switch) findViewById(R.id.preferencesRateEventNotificationSwitch);
        rateEventNotificationSwich.setChecked(eventRatePreference);
        rateEventNotificationSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean(TatenuUFRNApplication.EVENT_LOCATION_PREFERENCE_NAME, b);
                if(b)
                    startService(new Intent(context, EventLocationService.class));
                else
                    stopService(new Intent(context, EventLocationService.class));
            }
        });
    }

}
