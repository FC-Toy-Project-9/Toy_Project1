package domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    ItineraryService itineraryService = new ItineraryService();

    @Mock
    ItineraryService mockItinerarySerivce = new ItineraryService();

    @Nested
    @DisplayName("deleteItinerary()은 ")
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
            boolean result = ItineraryService.deleteItinerary(1,4);

            //then
            Assertions.assertTrue(result);
        }
    }


}
