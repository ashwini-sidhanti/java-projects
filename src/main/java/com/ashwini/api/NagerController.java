package com.ashwini.api;

import com.ashwini.model.DedepulicateHolidayInput;
import com.ashwini.model.HolidayInput;
import com.ashwini.model.HolidayData;
import com.ashwini.model.LastThreeHolidayData;
import com.ashwini.services.NagerService;
import com.github.javaparser.quality.NotNull;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import java.time.LocalDate;
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

    @Get("/{officialName}")
    public List<LastThreeHolidayData> getLastThreeHolidayInfo(@NotNull String officialName) {
       // return List.of(new LastThreeHolidayData(LocalDate.now(), "Watever"));
        return nagerService.getLastThreeHolidayInfo(officialName);
    }

    @Post()
    public HashMap<String, Long> getNagerInfoAboutPublicHoliday(@Body HolidayInput holidayInput) {
        return nagerService.getNagerInfoAboutPublicHolidays(holidayInput);
    }

    @Post("/deduplicated-holidays")
    public List<HolidayData> getNagerInfoAboutPublicHolidayDeduplicated(@Body DedepulicateHolidayInput dedepulicateHolidayInput) {
        //return null;
         return nagerService.getNagerInfoAboutDedepulicatedHolidays(dedepulicateHolidayInput);
    }
}