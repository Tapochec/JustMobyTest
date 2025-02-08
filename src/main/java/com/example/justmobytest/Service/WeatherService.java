package com.example.justmobytest.Service;

import com.example.justmobytest.Entity.GeoCoordinates;
import com.example.justmobytest.Entity.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${api.url}")
    private String API_URL;

    private final RestTemplate restTemplate;

    @Autowired
    private final GeoCodeService geoCodeService;

    public WeatherService(RestTemplate restTemplate, GeoCodeService geoCodeService) {
        this.restTemplate = restTemplate;
        this.geoCodeService = geoCodeService;
    }

    public Weather getWeather(String cityName) {
        GeoCoordinates geoCoordinates = geoCodeService.getCoordinates(cityName);
        if (geoCoordinates == null) {
            System.out.println("Координаты не получены");
            return null;
        }
        String url = API_URL.replace("{LAT}", String.valueOf(geoCoordinates.getLat()))
                .replace("{LON}", String.valueOf(geoCoordinates.getLon()));
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                JsonNode main = root.get("main");


                int temp = main.get("temp").asInt();
                int temp_min = main.get("temp_min").asInt();
                int temp_max = main.get("temp_max").asInt();

                return new Weather( Math.round(temp),  Math.round(temp_min), Math.round(temp_max));

            } else {
                throw new RuntimeException(String.valueOf(response.getStatusCode()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }


}
