package com.example.linez;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LinezLocation implements Serializable  {

    public double Latitude;
    public double Longitude;
    public String name;
    public double curWait;

    public LinezLocation(double latitude, double longitude, String name, double curWait) {
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.name = name;
        this.curWait = curWait;
    }


    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurWait() {
        return curWait;
    }

    public void setCurWait(double curWait) {
        this.curWait = curWait;
    }

}
