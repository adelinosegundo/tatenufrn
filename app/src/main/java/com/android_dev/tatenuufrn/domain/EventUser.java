package com.android_dev.tatenuufrn.domain;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by adelinosegundo on 11/24/15.
 */


@Table(databaseName = TatenUFRNDatabase.NAME)
@ModelContainer
public class EventUser extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = false)
    private String id;

    @Column
    private String eventId;

    @Column
    private String userId;

    @Column
    private boolean joined;

    @Column
    private boolean liked;

    @Column
    private Double rate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String toString(){
        return "liked: "+ String.valueOf(liked) + " joined: " + String.valueOf(joined) + " rate: " + String.valueOf(rate);
    }
}
