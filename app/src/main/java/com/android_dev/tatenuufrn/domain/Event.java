package com.android_dev.tatenuufrn.domain;

import android.graphics.Bitmap;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.android_dev.tatenuufrn.helpers.DbBitmapUtility;
import com.orm.SugarRecord;

import java.sql.Date;

/**
 * Created by adelinosegundo on 10/7/15.
 */
public class Event extends SugarRecord<Event> implements Parcelable {

    private String title;
    private String description;
    private String imageName;
    private String image;
    private String startTime;
    private String endTime;
    private String address;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImage() {
        return DbBitmapUtility.decodeBitmapFromBase64(image);
    }

    public void setImage(Bitmap image) {
        this.imageName = image.toString();
        this.image = DbBitmapUtility.encodeBitmapToBase64(image);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(address);
        dest.writeInt(fbEventId);
        dest.writeFloat(radiusTrigger);
        dest.writeFloat(rating);
        this.getImage().writeToParcel(dest,0);
    }
}
