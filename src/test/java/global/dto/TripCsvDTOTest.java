package global.dto;

import domain.itinerary.dto.ItineraryDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TripCsvDTOTest {

    @Nested
    @DisplayName("toItinerary()는 ")
    class Context_toItinerary {

        @Test
        @DisplayName("ItineraryDTO로 변환하여 반환할 수 있다.")
        void _willSuccess() {
            // given
            LocalDateTime dateTime = LocalDateTime.now();
            TripCsvDTO csv = new TripCsvDTO(0, "trip", LocalDate.now(), LocalDate.now(), 0,
                "departure", "destination", dateTime, dateTime, dateTime, dateTime);
            ItineraryDTO itinerary = ItineraryDTO.builder().id(0).departurePlace("departure")
                .destination("destination").departureTime(dateTime).arrivalTime(dateTime)
                .checkIn(dateTime).checkOut(dateTime).build();

            // when
            ItineraryDTO result = csv.toItineraryDTO();

            // then
            Assertions.assertEquals(result.toString(), itinerary.toString());
        }
    }
}
