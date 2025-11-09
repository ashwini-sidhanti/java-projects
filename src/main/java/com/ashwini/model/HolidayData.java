package com.ashwini.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.convert.format.Format;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HolidayData(@JsonProperty("date") @Format("yyyy-MM-dd") LocalDate date,
                          @JsonProperty("name") String name,
                          @JsonProperty("localName") String localName,
                          @JsonProperty("countryCode") String countryCode,
                          @JsonProperty("fixed") boolean fixedHoliday,
                          @JsonProperty("global") boolean global,
                          @JsonProperty("counties") List<String> counties,
                          @JsonProperty("launchYear") String launchYear,
                          @JsonProperty("types") List<String> type
                          ) {
}
