package com.ashwini.services;

import com.ashwini.client.NagerClient;
import com.ashwini.model.AvailableCountry;
import com.ashwini.model.DedepulicateHolidayInput;
import com.ashwini.model.HolidayData;
import com.ashwini.model.HolidayInput;
import com.ashwini.model.LastThreeHolidayData;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class NagerService {
    private final NagerClient nagerClient;

    @Inject
    public NagerService(NagerClient nagerClient) {
        this.nagerClient = nagerClient;
    }

    public List<LastThreeHolidayData> getLastThreeHolidayInfo(String officialName) {
        // convert it to code
        AvailableCountry availableCountryResult = nagerClient.getAvailableCountries().stream()
                .filter(availableCountry -> availableCountry.name().equalsIgnoreCase(officialName))
                .findFirst().orElse(null);
        if (availableCountryResult == null) {
            return null;
        }
        return nagerClient.getNagerInfoAboutPublicHolidays(String.valueOf(LocalDate.now().getYear()), availableCountryResult.countryCode()).stream()
                .filter(holidayData -> holidayData.date().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(HolidayData::date).reversed())
                .limit(3)
                .map(holidayData -> new LastThreeHolidayData(holidayData.date(), holidayData.name()))
                .toList();
    }

    public HashMap<String, Long> getNagerInfoAboutPublicHolidays(HolidayInput hoildayInput) {
        if (hoildayInput.countryCode().isEmpty()) {
            return null;
        }
        HashMap<String, Long> result = new HashMap<>();
        hoildayInput.countryCode().forEach(countryCode -> {
            // check if the countryCode is in available country code.
            List<HolidayData> holidayDataList = nagerClient.getNagerInfoAboutPublicHolidays(hoildayInput.year(), countryCode);
            if (holidayDataList != null) {
                result.put(countryCode, holidayDataList.stream()
                        .filter(holidayData -> holidayData.type().contains("Public"))
                        .filter(holidayData -> holidayData.date().getDayOfWeek() != DayOfWeek.SATURDAY && holidayData.date().getDayOfWeek() != DayOfWeek.SUNDAY)
                        .count());
            }
        });
        return result;
    }

    public List<HolidayData> getNagerInfoAboutDedepulicatedHolidays(DedepulicateHolidayInput dedepulicateHolidayInput) {
        List<HolidayData> firstHolidayData = nagerClient.getNagerInfoAboutPublicHolidays(dedepulicateHolidayInput.year(),
                dedepulicateHolidayInput.firstCountryCode());
        List<HolidayData> secondHolidayData = nagerClient.getNagerInfoAboutPublicHolidays(dedepulicateHolidayInput.year(),
                dedepulicateHolidayInput.secondCountryCode());
        return Stream.concat(firstHolidayData.stream(), secondHolidayData.stream())
                .filter(distinctByKey(HolidayData::name))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
