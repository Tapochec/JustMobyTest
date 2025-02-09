package com.example.justmobytest.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {


    public Weather(int temperature, int temperatureMin, int temperatureMax) {
        this.temperature = temperature;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
    }

    @JsonProperty("temp")
    private int temperature;
    @JsonProperty("temp_min")
    private int temperatureMin;
    @JsonProperty("temp_max")
    private int temperatureMax;

}
