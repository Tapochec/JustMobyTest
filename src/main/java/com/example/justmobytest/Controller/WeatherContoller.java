package com.example.justmobytest.Controller;

import com.example.justmobytest.Entity.Weather;
import com.example.justmobytest.Exception.InvalidCityNameException;
import com.example.justmobytest.Exception.InvalidNetworkException;
import com.example.justmobytest.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class WeatherContoller {

    @Autowired
    private final WeatherService weatherService;


    public WeatherContoller(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/getWeather/{cityName}")
    @ResponseBody
    public ResponseEntity<Weather> getWeather(@PathVariable("cityName") String cityName) {
        Weather weather = weatherService.getWeather(cityName);

        if (weather != null) {
            return new ResponseEntity<>(weather, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidCityNameException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidCityNameException ex) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidNetworkException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidNetworkException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
}
