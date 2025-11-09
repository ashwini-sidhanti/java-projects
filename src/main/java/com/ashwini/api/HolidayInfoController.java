package com.ashwini.api;

import com.ashwini.exception.CountryCodesEmptyException;
import com.ashwini.exception.CountryNotFoundException;
import com.ashwini.model.DedepulicateHolidayDataOutput;
import com.ashwini.model.DedepulicateHolidayInput;
import com.ashwini.model.HolidayDateNotWeekend;
import com.ashwini.model.HolidayInput;
import com.ashwini.model.LastThreeHolidayData;
import com.ashwini.services.HolidayInfoService;
import com.github.javaparser.quality.NotNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/holiday-info")
public class HolidayInfoController {
    HolidayInfoService holidayInfoService;

    @Inject
    public HolidayInfoController(HolidayInfoService holidayInfoService) {
        this.holidayInfoService = holidayInfoService;
    }

    @Get("/{officialName}")
    public List<LastThreeHolidayData> getLastThreeHolidayInfo(@NotNull String officialName) {
        try {
            return holidayInfoService.getLastThreeHolidayInfo(officialName);
        } catch (CountryNotFoundException exception) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @Post()
    public List<HolidayDateNotWeekend> getInfoAboutPublicHolidayNotWeekend(@Body HolidayInput holidayInput) {
        try {
            return holidayInfoService.getInfoAboutPublicHolidays(holidayInput);
        } catch (CountryCodesEmptyException exception) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @Post("/deduplicated-holidays")
    public List<DedepulicateHolidayDataOutput> getInfoAboutPublicHolidayDeduplicated(@Body DedepulicateHolidayInput dedepulicateHolidayInput) {
        try {
            return holidayInfoService.getInfoAboutDedepulicatedHolidays(dedepulicateHolidayInput);
        } catch (CountryCodesEmptyException exception) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

    }
}