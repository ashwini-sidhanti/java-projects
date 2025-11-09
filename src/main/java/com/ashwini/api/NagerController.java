package com.ashwini.api;

import com.ashwini.exception.CountryNotFoundException;
import com.ashwini.model.DedepulicateHolidayDataOutput;
import com.ashwini.model.DedepulicateHolidayInput;
import com.ashwini.model.HolidayDateNotWeekend;
import com.ashwini.model.HolidayInput;
import com.ashwini.model.LastThreeHolidayData;
import com.ashwini.services.NagerService;
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
@Controller("/api/nager")
public class NagerController {
    NagerService nagerService;

    @Inject
    public NagerController(NagerService nagerService) {
        this.nagerService = nagerService;
    }

    @Get("/{officialName}")
    public List<LastThreeHolidayData> getLastThreeHolidayInfo(@NotNull String officialName) {
        try {
            return nagerService.getLastThreeHolidayInfo(officialName);
        } catch (CountryNotFoundException exception) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @Post()
    public List<HolidayDateNotWeekend> getNagerInfoAboutPublicHolidayNotWeekend(@Body HolidayInput holidayInput) {
        return nagerService.getNagerInfoAboutPublicHolidays(holidayInput);
    }

    @Post("/deduplicated-holidays")
    public List<DedepulicateHolidayDataOutput> getNagerInfoAboutPublicHolidayDeduplicated(@Body DedepulicateHolidayInput dedepulicateHolidayInput) {
        return nagerService.getNagerInfoAboutDedepulicatedHolidays(dedepulicateHolidayInput);
    }
}