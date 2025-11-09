package com.ashwini.client;

import com.ashwini.model.AvailableCountry;
import com.ashwini.model.HolidayData;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;

import java.util.List;

@Client(id = "holiday-data")
public interface NagerClient {
    @CircuitBreaker(reset = "30s", attempts = "2", delay = "2s", multiplier = "1")
    @Get("api/v3/AvailableCountries")
    @Produces(MediaType.APPLICATION_JSON)
    @Cacheable(value = "available-country")
    List<AvailableCountry> getAvailableCountries();

    @CircuitBreaker(reset = "30s", attempts = "2", delay = "2s", multiplier = "1")
    @Get("api/v3/PublicHolidays/{year}/{countryCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Cacheable(value = "public-holiday-info-day")
    List<HolidayData> getNagerInfoAboutPublicHolidays(String year, String countryCode);

}
