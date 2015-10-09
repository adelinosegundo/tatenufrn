package com.android_dev.tatenuufrn.helpers;

import android.graphics.Bitmap;

import com.android_dev.tatenuufrn.domain.Event;


/**
 * Created by adelinosegundo on 10/8/15.
 */
public class EventPopulator {
    public static void populate(int amount, Bitmap image){
        for(int i = 0; i < amount; i++){
            Event event = new Event();
            event.setTitle("Title " + i);
            event.setDescription("Description " + i);
            event.setImage(image);
            event.save();
        }
    }
}
