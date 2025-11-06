package com.ashwini.api;

import com.ashwini.client.NagerClient;
import com.ashwini.model.HolidayData;
import com.ashwini.services.NagerService;
import com.github.javaparser.quality.NotNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
//import io.micronaut.security.annotation.Secured;
//import io.micronaut.security.authentication.Authentication;
//import io.micronaut.security.rules.SecurityRule;
//import io.micronaut.validation.Validated;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

//@Validated
@ExecuteOn(TaskExecutors.IO)
@Controller("/api/nager")
//@Tags(@Tag(name = "Concessions"))
//@Secured(SecurityRule.IS_AUTHENTICATED)
public class NagerController {
    NagerService nagerService;
    @Inject
    public NagerController(NagerService nagerService) {
        this.nagerService = nagerService;
    }

    @Get("/{countryCode}")
    public List<HolidayData> getNagerInfo(@NotNull String countryCode) throws IOException {
        return  nagerService.nager(countryCode) ;
    }

    @Get("/{year}/{countryCode}")
    public HashMap<String, Long> getNagerInfoAboutPublicHoliday(@NotNull String year, @NotNull List<String> countryCode) throws IOException {
        return  nagerService.getNagerInfoAboutPublicHolidays(year, countryCode) ;
    }
}