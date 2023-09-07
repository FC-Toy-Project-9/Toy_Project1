package domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import domain.trip.service.TripService;
import java.io.FileNotFoundException;
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

    @Mock
    TripService mockTripService = new TripService();

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
        void _willSuccess() {
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


}
