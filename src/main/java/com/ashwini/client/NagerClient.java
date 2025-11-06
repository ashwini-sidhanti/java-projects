package com.ashwini.client;

import com.ashwini.model.HolidayData;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;

import java.util.List;

@Client(id = "nager")
public interface NagerClient {
    @CircuitBreaker(reset = "30s", attempts = "2", delay = "2s", multiplier = "1")
    @Get(value = "https://date.nager.at/api/v3/NextPublicHolidays/{countryCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Cacheable(value = "holiday-info-day-cache")
    List<HolidayData> getNagerInfoAboutHolidays(String countryCode);

    @CircuitBreaker(reset = "30s", attempts = "2", delay = "2s", multiplier = "1")
    @Get(value = "https://date.nager.at/api/v3/PublicHolidays/{year}/{countryCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Cacheable(value = "holiday-info-day-cache")
    List<HolidayData> getNagerInfoAboutPublicHolidays(String year, String countryCode);

}
