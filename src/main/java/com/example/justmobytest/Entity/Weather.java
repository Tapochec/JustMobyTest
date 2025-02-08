package com.example.justmobytest.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {

    public Weather() {
    }

    public Weather(int temperature, int temperature_Min, int temperature_Max) {
        this.temperature = temperature;
        this.temperature_Min = temperature_Min;
        this.temperature_Max = temperature_Max;
    }

    @JsonProperty("temp")
    private int temperature;
    @JsonProperty("temp_min")
    private int temperature_Min;
    @JsonProperty("temp_max")
    private int temperature_Max;

    public int getTemperature() {
        return temperature;
    }

    public int getTemperature_Min() {
        return temperature_Min;
    }

    public int getTemperature_Max() {
        return temperature_Max;
    }
}
