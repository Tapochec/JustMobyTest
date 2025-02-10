package com.example.justmobytest.Service;

import com.example.justmobytest.Entity.GeoCoordinates;
import com.example.justmobytest.Entity.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;


@Service
public class WeatherService {

    public static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

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
            throw new RuntimeException("Координаты города не найдены");
        }

        String url = buildWeatherUrl(geoCoordinates.getLat(), geoCoordinates.getLon());

        LOG.info("Coordinates for the city " + cityName + ": Lat: " + geoCoordinates.getLat() + ", Lon: " + geoCoordinates.getLon());

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                JsonNode main = root.get("main");


                int temp = main.get("temp").asInt();
                int tempMin = main.get("temp_min").asInt();
                int tempMax = main.get("temp_max").asInt();

                LOG.info("Weather request for " + cityName + " city successful \n" +
                        "temp = " + temp + "\n" +
                        "tempMin = " + tempMin + "\n" +
                        "tempMax = " + tempMax);

                return new Weather(Math.round(temp),  Math.round(tempMin), Math.round(tempMax));

            } else {
                throw new RuntimeException(String.valueOf(response.getStatusCode()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private String buildWeatherUrl(double lat, double lon) {
        return MessageFormat.format(API_URL, lat, lon);
    }


}
