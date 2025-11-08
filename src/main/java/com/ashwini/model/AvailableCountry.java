package com.ashwini.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record AvailableCountry(String countryCode, String name) {
}
