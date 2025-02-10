package com.example.justmobytest.Exception;

public class InvalidCityNameException extends RuntimeException {
    public InvalidCityNameException(String message) {
        super(message);
    }
}
