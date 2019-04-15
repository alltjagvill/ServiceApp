package Utilites;

import android.location.Location;

public class CalcGeoPoints {

    public float withinDistance(Double lat1, Double lon1, Double lat2, Double lon2) {

        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);
        //float distanceInMeters = loc1.distanceTo(loc2);
        return loc1.distanceTo(loc2);
    }
}
