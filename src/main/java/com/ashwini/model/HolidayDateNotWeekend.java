package com.ashwini.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record HolidayDateNotWeekend(String CountryName, long count) {
}
