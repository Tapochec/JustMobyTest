package com.example.justmobytest.Service;

import com.example.justmobytest.Entity.GeoCoordinates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;


@Service
public class GeoCodeService {

    public static final Logger LOG = LoggerFactory.getLogger(GeoCodeService.class);

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
        String url = buildGeoUrl(cityName);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                JsonNode firstCity = root.get(0);

                if (firstCity == null) {
                    String errorMessage = String.format("Не удалось найти координаты для города " + cityName + ". Проверьте правильность написания названия города.");
                    LOG.warn(errorMessage + " Response: {}", response.getBody());
                    throw new IllegalArgumentException(errorMessage);
                }

                double lat = firstCity.get("lat").asDouble();
                double lon = firstCity.get("lon").asDouble();

                LOG.info("Coordinates for the city " + cityName + ": Lat: " + firstCity.get("lat").asDouble() + ", Lon: " + firstCity.get("lon").asDouble());


                return new GeoCoordinates(lat, lon);

            } else {
                LOG.info("No coordinates found for the city " + cityName);
                throw new RuntimeException(String.valueOf(response.getStatusCode()));
            }
        } catch (RestClientException | JsonProcessingException e) {
            LOG.error("Error network while getting coordinates for city {}: {}", cityName, e.getMessage(), e);
            throw new RuntimeException("Ошибка сети при получении координат города " + cityName, e);
        }
    }

    public String buildGeoUrl(String cityName) {
        return MessageFormat.format(API_GEO, cityName);
    }


}
