package com.example.justmobytest.Service.Controller;

import com.example.justmobytest.Controller.WeatherContoller;
import com.example.justmobytest.Entity.Weather;
import com.example.justmobytest.Service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "api.url=https://api.openweathermap.org/data/2.5/weather?lat={LAT}&lon={LON}&units=metric",
        "api.url.geo=https://example.com/geocode?cityName={cityName}"
})
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherContoller weatherController;

    @Test
    void getWeatherSuccess() {
        Weather weather = new Weather(10, 8, 12);
        String cityName = "Moscow";

        //when
        when(weatherService.getWeather(cityName)).thenReturn(weather);

        //then
        ResponseEntity<Weather> response = weatherController.getWeather(cityName);

        //validation
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weather, response.getBody());
    }

    @Test
    void getWeatherCityNotFound() {

        String cityName = "TestCity";

        //when
        when(weatherService.getWeather(cityName)).thenReturn(null);

        //then
        ResponseEntity<Weather> response = weatherController.getWeather(cityName);

        //validation
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

}
