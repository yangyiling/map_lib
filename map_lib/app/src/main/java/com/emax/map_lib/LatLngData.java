package com.emax.map_lib;

/**
 * Created by Administrator on 2017/8/27 0027.
 */

public class LatLngData {
    private double latitude;
    private  double longitude;

    public LatLngData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
