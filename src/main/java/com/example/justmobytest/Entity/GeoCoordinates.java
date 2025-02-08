package com.example.justmobytest.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoCoordinates {
    @JsonProperty
    public long lat;
    @JsonProperty
    public long lon;

    public GeoCoordinates(long lat, long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }


    public long getLon() {
        return lon;
    }

}
