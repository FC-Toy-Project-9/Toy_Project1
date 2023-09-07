package domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryNotFoundException;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import domain.trip.service.TripService;
import global.dto.TripCsvDTO;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {

    TripService tripService = new TripService();
    ItineraryService itineraryService = new ItineraryService();
    @Mock
    ItineraryService mockItineraryService = new ItineraryService();


    @Nested
    @DisplayName("getItineraryListFromJson()은")
    class Context_getItineraryListFromTrip {

        @Test
        @DisplayName("특정 여행의 여정 기록을 조회할 수 있다.")
        void _willSuccess() throws FileNotFoundException {
            //given
            List<ItineraryDTO> itineraryList = new ArrayList<>();
            itineraryList.add(ItineraryDTO.builder().id(1).build());
            when(mockItineraryService.getItineraryListFromJson(1)).thenReturn(itineraryList);

            //when
            List<ItineraryDTO> result = itineraryService.getItineraryListFromJson(1);

            //then
            Assertions.assertEquals(result.get(0).getId(), itineraryList.get(0).getId());
        }

        @Test
        @DisplayName("특정 여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                tripService.getTripFromJson(3);
            });
            assertEquals("여행 파일을 찾을 수 없습니다.", exception.getMessage());
        }

    }


    @Nested
    @DisplayName("deleteItineraryFromJson()은 ")
    class Context_deleteItinerary {

        @Test
        @DisplayName("특정 여정을 삭제할 수 있다.")
        void _willSuccess() throws ItineraryNotFoundException {
            //given
            List<ItineraryDTO> itineraryList = new ArrayList<>();
            ItineraryDTO itinerary = ItineraryDTO.builder().id(4).build();
            itineraryList.add(itinerary);
            TripDTO trip = TripDTO.builder().id(1).itineraries(itineraryList).build();

            //when
            boolean result = itineraryService.deleteItineraryFromJson(1, 4);

            //then
            Assertions.assertTrue(result);
        }
    }

    @Nested
    @DisplayName("getItineraryFromCsv()는 ")
    class Context_getItineraryListFromCSV {

        @Test
        @DisplayName("특정 여행의 여정 기록을 조회할 수 있다.")
        void _willSuccess() throws FileNotFoundException {
            //given
            ItineraryDTO itinerary = TripCsvDTO.builder()
                .tripId(1).tripName("Family Vacation")
                .startDate(LocalDate.parse("2023-08-15", DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE))
                .itineraryId(5).build().toItineraryDTO();
            List<ItineraryDTO> itineraryList = new ArrayList<>();
            itineraryList.add(itinerary);
            lenient().when(mockItineraryService.getItineraryListFromCSV(1))
                .thenReturn(itineraryList);

            //when
            List<ItineraryDTO> result = itineraryService.getItineraryListFromCSV(1);

            //then
            Assertions.assertEquals(result.get(0).getId(), itineraryList.get(0).getId());
        }
    }

    @Nested
    @DisplayName("deleteItineraryFromCSV()는 ")
    class Context_deleteItineraryFromCSV {

        @Test
        @DisplayName("특정 여정을 삭제할 수 있다.")
        void _willSuccess() throws ItineraryNotFoundException {
            //given, when
            boolean result = itineraryService.deleteItineraryFromCSV(1, 5);

            //then
            Assertions.assertTrue(result);
        }
    }

}
