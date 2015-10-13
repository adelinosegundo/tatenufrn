package com.android_dev.tatenuufrn.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.android_dev.tatenuufrn.helpers.DbBitmapUtility;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by adelinosegundo on 10/7/15.
 */

@Table(databaseName = TatenUFRNDatabase.NAME)
@ModelContainer
public class Event extends BaseModel implements Parcelable {


    @Column
    @PrimaryKey(autoincrement = false)
    private String id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @Column
    private String imageString;

    @Column
    private String startTime;

    @Column
    private String endTime;

    @Column
    private String address;

    @Column
    private Double radiusTrigger;

    @Column
    private Integer fbEventId;

    @Column
    private Double rating;


    public Event(){
    }

    public Event(String title, String description){
        this.title = title;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public void updateImageString(){
        System.out.println(this.imageUrl);
        this.setImageBitmap(DbBitmapUtility.LoadImageFromWebOperations(imageUrl));
    }

    public Bitmap getImageBitmap() {
        return DbBitmapUtility.decodeBitmapFromBase64(imageString);
    }

    public void setImageBitmap(Bitmap image) {
        this.imageString = DbBitmapUtility.encodeBitmapToBase64(image);
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

    public Double getRadiusTrigger() {
        return radiusTrigger;
    }

    public void setRadiusTrigger(Double radiusTrigger) {
        this.radiusTrigger = radiusTrigger;
    }

    public Integer getFbEventId() {
        return fbEventId;
    }

    public void setFbEventId(Integer fbEventId) {
        this.fbEventId = fbEventId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
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
        dest.writeDouble(radiusTrigger);
        dest.writeDouble(rating);
        this.getImageBitmap().writeToParcel(dest,0);
    }

    @Override
    public String toString(){
        return this.title;
    }
}
