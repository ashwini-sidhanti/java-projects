package com.ashwini.exception;

public class CountryNotFoundException extends Exception{
    public CountryNotFoundException(String officialName) {
        super("Country not found with id %s".formatted(officialName));
    }
}
