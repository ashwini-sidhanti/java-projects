package com.ashwini.exception;

public class CountryCodesEmptyException extends Exception{
    public CountryCodesEmptyException() {
        super("Country codes is empty");
    }
}
