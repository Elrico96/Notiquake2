package com.example.notiquake.app.model;

public class Earthquake {
    private double mag;
    private String title;
    private String place;
    private long timeInMiliseconds;
    private String url;
    private String felt;
    private double cdi;
    private int tsunameAlert;
    private double longitude;
    private double latitude;
    private double depth;

    public Earthquake(double mag, String place, long timeInMiliseconds, String url) {
        this.mag = mag;
        this.place = place;
        this.timeInMiliseconds = timeInMiliseconds;
        this.url = url;
    }

    public Earthquake(String title, long timeInMiliseconds, String url, String felt, double cdi, int tsunameAlert, double longitude, double latitude, double depth) {
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

    public Earthquake(double mag, String title, String place, long timeInMiliseconds, String url, String felt, double cdi, int tsunameAlert, double longitude, double latitude, double depth) {
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

    public String getFelt() {
        return felt;
    }

    public double getCdi() {
        return cdi;
    }

    public int getTsunameAlert() {
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
}
