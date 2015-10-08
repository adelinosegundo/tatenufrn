package com.android_dev.tatenuufrn.domain;

import android.graphics.Bitmap;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.sql.Date;

/**
 * Created by adelinosegundo on 10/7/15.
 */
public class Event extends SugarRecord<Event> implements Parcelable {
//    public static final String[] COLUMNS = new String[]{
//            "id",
//            "title",
//            "description",
//            "image",
//            "startTime",
//            "endTime",
//            "address",
//            "radiusTrigger",
//            "fbEventId",
//            "rating"
//    };

    private String title;
    private String description;
    private byte[] image;
    private byte[] startTime;
    private byte[] endTime;
    private byte[] address;
    private Float radiusTrigger;
    private Integer fbEventId;
    private Float rating;


    public Event(){
    }

    public Event(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getStartTime() {
        return startTime;
    }

    public void setStartTime(byte[] startTime) {
        this.startTime = startTime;
    }

    public byte[] getEndTime() {
        return endTime;
    }

    public void setEndTime(byte[] endTime) {
        this.endTime = endTime;
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public Float getRadiusTrigger() {
        return radiusTrigger;
    }

    public void setRadiusTrigger(Float radiusTrigger) {
        this.radiusTrigger = radiusTrigger;
    }

    public Integer getFbEventId() {
        return fbEventId;
    }

    public void setFbEventId(Integer fbEventId) {
        this.fbEventId = fbEventId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(startTime.toString());
        dest.writeString(endTime.toString());
        dest.writeString(address.toString());
        dest.writeInt(fbEventId);
        dest.writeFloat(radiusTrigger);
        dest.writeFloat(rating);

    }
}
