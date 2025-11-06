package com.ashwini.api;

import com.ashwini.model.HolidayInput;
import com.ashwini.model.HolidayData;
import com.ashwini.services.NagerService;
import com.github.javaparser.quality.NotNull;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/nager")
public class NagerController {
    NagerService nagerService;

    @Inject
    public NagerController(NagerService nagerService) {
        this.nagerService = nagerService;
    }

    @Get("/{countryCode}")
    public List<HolidayData> getNagerInfo(@NotNull String countryCode) {
        return nagerService.nager(countryCode);
    }

    @Post()
    public HashMap<String, Long> getNagerInfoAboutPublicHoliday(@Body HolidayInput holidayInput) {
        return nagerService.getNagerInfoAboutPublicHolidays(holidayInput);
    }
}