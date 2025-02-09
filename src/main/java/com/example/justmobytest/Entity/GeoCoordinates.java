package com.example.justmobytest.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoCoordinates {
    @JsonProperty
    public double lat;
    @JsonProperty
    public double lon;

    public GeoCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }


    public double getLon() {
        return lon;
    }

}
