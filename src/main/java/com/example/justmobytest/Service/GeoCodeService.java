package com.example.justmobytest.Service;

import com.example.justmobytest.Entity.GeoCoordinates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoCodeService {

    @Value("${api.url.geo}")
    private String API_GEO;

    private final RestTemplate restTemplate;

    public GeoCodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAPI_GEO(String API_GEO) {
        this.API_GEO = API_GEO;
    }

    public GeoCoordinates getCoordinates(String cityName) {
        String url = API_GEO.replace("{cityName}", cityName);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                    JsonNode firstCity = root.get(0);
                    long lat = firstCity.get("lat").asLong();
                    long lon = firstCity.get("lon").asLong();

                    return new GeoCoordinates(lat, lon);

            } else {
                throw new RuntimeException(String.valueOf(response.getStatusCode()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
