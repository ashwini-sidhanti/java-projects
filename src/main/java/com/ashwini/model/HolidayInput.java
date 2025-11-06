package com.ashwini.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

import java.util.List;
@Introspected
@Serdeable
public record HolidayInput(@NotNull String year, @NotNull List<String> countryCode) {
}
