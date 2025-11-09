package com.ashwini.services;

import com.ashwini.client.NagerClient;
import com.ashwini.model.AvailableCountry;
import com.ashwini.model.DedepulicateHolidayDataOutput;
import com.ashwini.model.DedepulicateHolidayInput;
import com.ashwini.model.HolidayData;
import com.ashwini.model.HolidayDateNotWeekend;
import com.ashwini.model.HolidayInput;
import com.ashwini.model.LastThreeHolidayData;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
        AvailableCountry availableCountryResult = getAvailableCountry(officialName);
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

    public List<HolidayDateNotWeekend> getNagerInfoAboutPublicHolidays(HolidayInput hoildayInput) {
        if (hoildayInput.countryCode().isEmpty()) {
            return null;
        }
        List<HolidayDateNotWeekend> result = new ArrayList<>();
        hoildayInput.countryCode().forEach(countryCode -> {
            AvailableCountry availableCountryResult = nagerClient.getAvailableCountries().stream()
                    .filter(availableCountry -> availableCountry.countryCode().equalsIgnoreCase(countryCode)).findFirst().orElse(null);

            if (availableCountryResult != null) {
                List<HolidayData> holidayDataList = nagerClient.getNagerInfoAboutPublicHolidays(hoildayInput.year(), countryCode);
                if (holidayDataList != null) {
                    result.add(new HolidayDateNotWeekend(availableCountryResult.name(), holidayDataList.stream()
                            .filter(holidayData -> holidayData.type().contains("Public"))
                            .filter(holidayData -> holidayData.date().getDayOfWeek() != DayOfWeek.SATURDAY && holidayData.date().getDayOfWeek() != DayOfWeek.SUNDAY)
                            .count()));
                }
            }
        });
        return result.stream().sorted(Comparator.comparing(HolidayDateNotWeekend::count).reversed()).collect(Collectors.toList());

    }

    public List<DedepulicateHolidayDataOutput> getNagerInfoAboutDedepulicatedHolidays(DedepulicateHolidayInput dedepulicateHolidayInput) {
        List<HolidayData> firstHolidayData = nagerClient.getNagerInfoAboutPublicHolidays(dedepulicateHolidayInput.year(),
                dedepulicateHolidayInput.firstCountryCode());
        List<HolidayData> secondHolidayData = nagerClient.getNagerInfoAboutPublicHolidays(dedepulicateHolidayInput.year(),
                dedepulicateHolidayInput.secondCountryCode());
        return Stream.concat(firstHolidayData.stream(), secondHolidayData.stream())
                .filter(distinctByKey(HolidayData::name))
                .toList().stream()
                .map(holidayData -> new DedepulicateHolidayDataOutput(holidayData.localName(), holidayData.date())).toList();
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private AvailableCountry getAvailableCountry(String officialName) {
        return nagerClient.getAvailableCountries().stream()
                .filter(availableCountry -> availableCountry.name().equalsIgnoreCase(officialName))
                .findFirst().orElse(null);
    }

}
