package Model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class User {
    Double lat;
    Double lon;


    /*public  User() {

    }*/
    public User(Double lat, Double lon) {

        this.lat = lat;
        this.lon = lon;

    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
