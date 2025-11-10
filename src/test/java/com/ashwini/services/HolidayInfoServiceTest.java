package com.ashwini.services;

import com.ashwini.client.NagerClient;
import com.ashwini.exception.CountryCodesEmptyException;
import com.ashwini.exception.CountryNotFoundException;
import com.ashwini.model.AvailableCountry;
import com.ashwini.model.DedepulicateHolidayDataOutput;
import com.ashwini.model.DedepulicateHolidayInput;
import com.ashwini.model.HolidayData;
import com.ashwini.model.HolidayDateNotWeekend;
import com.ashwini.model.HolidayInput;
import com.ashwini.model.LastThreeHolidayData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayInfoServiceTest {
    @Mock
    NagerClient nagerClient;
    @InjectMocks
    HolidayInfoService holidayInfoService;

    @BeforeEach
    void setup() {
        holidayInfoService = new HolidayInfoService(nagerClient);
    }

    @Test
    void testGetLastThreeHolidayInfo() throws CountryNotFoundException {
        //given
        when(nagerClient.getAvailableCountries()).thenReturn(List.of(new AvailableCountry("NL", "Netherlands")));
        List<HolidayData> holidayDataList = createHolidayData("Netherlands", "NL");
        when(nagerClient.getNagerInfoAboutPublicHolidays(String.valueOf(LocalDate.now().getYear()), "NL")).thenReturn(holidayDataList);
        //when
        List<LastThreeHolidayData> result = holidayInfoService.getLastThreeHolidayInfo("Netherlands");
        //then
        assertEquals(1, result.size());
    }

    @Test
    void testGetLastThreeHolidaysInforException() {
        //given
        when(nagerClient.getAvailableCountries()).thenReturn(List.of());
        //when
        assertThrows(CountryNotFoundException.class, () -> holidayInfoService.getLastThreeHolidayInfo("Netherlands"));
        //then
    }

    @Test
    void testGetInfoAboutPublicHolidays() throws CountryCodesEmptyException {
        //given
        when(nagerClient.getAvailableCountries()).thenReturn(List.of(new AvailableCountry("NL", "Netherlands"),
                new AvailableCountry("AD", "Andorra")));
        List<HolidayData> holidayDataListNL = createHolidayData("Netherlands", "NL");
        List<HolidayData> holidayDataListAD = createHolidayData("Andorra", "AD");
        when(nagerClient.getNagerInfoAboutPublicHolidays(any(), any())).thenReturn(holidayDataListNL);
        when(nagerClient.getNagerInfoAboutPublicHolidays(any(), any())).thenReturn(holidayDataListAD);
        //when
        List<HolidayDateNotWeekend> result = holidayInfoService.getInfoAboutPublicHolidays(new HolidayInput(String.valueOf(LocalDate.now().getYear()), List.of("NL", "AD")));
        //then
        assertEquals(2, result.size());
        assertEquals("Netherlands", result.getFirst().CountryName());
        assertEquals("Andorra", result.getLast().CountryName());
    }

    @Test
    void testGetInfoAboutPublicHolidaysEmptyList() {
        //given
        //when
        assertThrows(CountryCodesEmptyException.class, () -> holidayInfoService.getInfoAboutPublicHolidays(new HolidayInput(String.valueOf(LocalDate.now().getYear()), List.of())));
        //then
    }

    @Test
    void testGetInfoAboutDedepulicatedHolidays() throws CountryCodesEmptyException, CountryNotFoundException {
        //given
        when(nagerClient.getAvailableCountries()).thenReturn(List.of(new AvailableCountry("NL", "Netherlands"),
                new AvailableCountry("AD", "Andorra")));
        List<HolidayData> holidayDataListNL = createHolidayData("Netherlands", "NL");
        List<HolidayData> holidayDataListAD = createHolidayData("Andorra", "AD");
        when(nagerClient.getNagerInfoAboutPublicHolidays(any(), any())).thenReturn(holidayDataListNL);
        when(nagerClient.getNagerInfoAboutPublicHolidays(any(), any())).thenReturn(holidayDataListAD);
        //when
        List<DedepulicateHolidayDataOutput> result = holidayInfoService.getInfoAboutDedepulicatedHolidays(new DedepulicateHolidayInput(String.valueOf(LocalDate.now().getYear()), "NL", "AD"));
        //then
        assertEquals(1, result.size());
    }

    private List<HolidayData> createHolidayData(String name, String countryCode) {
        HolidayData holidayData1 = new HolidayData(LocalDate.now().minusDays(1), name, name, countryCode, false, false, null, null, List.of("Public Holiday"));
        HolidayData holidayData2 = new HolidayData(LocalDate.now().plusDays(2), name, name, countryCode, false, false, null, null, List.of("Public Holiday"));
        return List.of(holidayData1, holidayData2);
    }

}