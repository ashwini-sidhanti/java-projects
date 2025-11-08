package com.ashwini.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Introspected
@Serdeable
public record LastThreeHolidayData(LocalDate date, String name) {
}
