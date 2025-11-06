package com.ashwini.services;

import com.ashwini.client.NagerClient;
import com.ashwini.model.HolidayData;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

@Singleton
public class NagerService {
    private final NagerClient nagerClient;

    @Inject
    public NagerService(NagerClient nagerClient) {
        this.nagerClient = nagerClient;
    }

    public List<HolidayData> nager(String countryCode) {
        return nagerClient.getNagerInfoAboutHolidays(countryCode);
    }

    public HashMap<String, Long> getNagerInfoAboutPublicHolidays(String year, List<String> countryCodes) {
        if (countryCodes.isEmpty()) {
            return null;
        }
        HashMap<String, Long> result = new HashMap<>();
        countryCodes.forEach(countryCode -> {
            // check if the countryCode is in available country code.
            List<HolidayData> holidayDataList = nagerClient.getNagerInfoAboutPublicHolidays(year, countryCode);
            if (holidayDataList != null) {
               result.put(countryCode, holidayDataList.stream()
                        .filter(holidayData -> holidayData.type().contains("Public"))
                        .filter(holidayData -> holidayData.date().getDayOfWeek()!= DayOfWeek.SATURDAY && holidayData.date().getDayOfWeek() != DayOfWeek.SUNDAY)
                       .count());
            }
        });
        return result;
    }
}
