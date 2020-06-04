package com.example.notiquake.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Earthquake implements Parcelable {
    private double mag;
    private String title;
    private String place;
    private long timeInMiliseconds;
    private String url;
    private int felt;
    private double cdi;
    private String tsunameAlert;
    private double longitude;
    private double latitude;
    private double depth;

    public Earthquake(double mag, String place, long timeInMiliseconds, String url) {
        this.mag = mag;
        this.place = place;
        this.timeInMiliseconds = timeInMiliseconds;
        this.url = url;
    }

    public Earthquake(String title, long timeInMiliseconds, String url, int felt, double cdi, String tsunameAlert, double longitude, double latitude, double depth) {
        this.title = title;
        this.timeInMiliseconds = timeInMiliseconds;
        this.url = url;
        this.felt = felt;
        this.cdi = cdi;
        this.tsunameAlert = tsunameAlert;
        this.longitude = longitude;
        this.latitude = latitude;
        this.depth = depth;
    }

    public Earthquake(double mag, String title, String place, long timeInMiliseconds, String url, int felt, double cdi, String tsunameAlert, double longitude, double latitude, double depth) {
        this.mag = mag;
        this.title = title;
        this.place = place;
        this.timeInMiliseconds = timeInMiliseconds;
        this.url = url;
        this.felt = felt;
        this.cdi = cdi;
        this.tsunameAlert = tsunameAlert;
        this.longitude = longitude;
        this.latitude = latitude;
        this.depth = depth;
    }

    protected Earthquake(Parcel in) {
        mag = in.readDouble();
        title = in.readString();
        place = in.readString();
        timeInMiliseconds = in.readLong();
        url = in.readString();
        felt = in.readInt();
        cdi = in.readDouble();
        tsunameAlert = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        depth = in.readDouble();
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    public double getMag() {
        return mag;
    }

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public long getTimeInMiliseconds() {
        return timeInMiliseconds;
    }

    public String getUrl() {
        return url;
    }

    public int getFelt() {
        return felt;
    }

    public double getCdi() {
        return cdi;
    }

    public String getTsunameAlert() {
        return tsunameAlert;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getDepth() {
        return depth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mag);
        dest.writeString(title);
        dest.writeString(place);
        dest.writeLong(timeInMiliseconds);
        dest.writeString(url);
        dest.writeInt(felt);
        dest.writeDouble(cdi);
        dest.writeString(tsunameAlert);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeDouble(depth);
    }
}
