package domain.itinerary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryNotFoundException;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import domain.trip.service.TripService;
import global.dto.TripCsvDTO;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    class Context_getItineraryListFromJson {

        @Test
        @DisplayName("특정 여행의 여정 기록을 조회할 수 있다.")
        void _willSuccess() throws FileNotFoundException {
            //given
            postFileJSON();
            List<ItineraryDTO> itineraryList = new ArrayList<>();
            itineraryList.add(ItineraryDTO.builder().id(1).build());
            lenient().when(mockItineraryService.getItineraryListFromJson(3)).thenReturn(itineraryList);

            //when
            List<ItineraryDTO> result = itineraryService.getItineraryListFromJson(3);

            //then
            tripService.deleteTripFromJson(3);
            Assertions.assertEquals(result.get(0).getId(), itineraryList.get(0).getId());
        }

        @Test
        @DisplayName("특정 여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                tripService.getTripFromJson(5);
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
            //given, when
            postFileJSON();
            boolean result = itineraryService.deleteItineraryFromJson(3, 1);
            tripService.deleteTripFromJson(3);

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
            postFileCSV();
            ItineraryDTO itinerary = TripCsvDTO.builder()
                .tripId(3).tripName("Family Vacation")
                .startDate(LocalDate.parse("2023-08-15", DateTimeFormatter.ISO_DATE))
                .endDate(LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE))
                .itineraryId(1).build().toItineraryDTO();
            List<ItineraryDTO> itineraryList = new ArrayList<>();
            itineraryList.add(itinerary);
            lenient().when(mockItineraryService.getItineraryListFromCSV(3))
                .thenReturn(itineraryList);

            //given,when
            List<ItineraryDTO> result = itineraryService.getItineraryListFromCSV(3);
            tripService.deleteTripFromCsv(3);

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
            postFileCSV();
            boolean result = itineraryService.deleteItineraryFromCSV(3, 1);
            tripService.deleteTripFromCsv(3);

            //then
            Assertions.assertTrue(result);
        }
    }

    public void postFileJSON(){
        FileWriter fw = null;
        try {
            fw = new FileWriter("src/main/resources/trip/json/trip_3.json");
            String text = "{\n"
                + "  \"trip_id\": 3,\n"
                + "  \"trip_name\": \"Family Vacation\",\n"
                + "  \"start_date\": \"2023-07-15\",\n"
                + "  \"end_date\": \"2023-07-20\",\n"
                + "  \"itineraries\": [\n"
                + "    {\n"
                + "      \"itinerary_id\": 1,\n"
                + "      \"departure_place\": \"City A\",\n"
                + "      \"destination\": \"City B\",\n"
                + "      \"departure_time\": \"2023-07-15T08:00:00\",\n"
                + "      \"arrival_time\": \"2023-07-15T10:00:00\",\n"
                + "      \"check_in\": \"2023-07-15T12:00:00\",\n"
                + "      \"check_out\": \"2023-07-30T10:00:00\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
            fw.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw !=null) {
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void postFileCSV(){
        FileWriter fw = null;
        try {
            fw = new FileWriter("src/main/resources/trip/csv/trip_3.csv");
            String text = "trip_id,trip_name,start_date,end_date,itinerary_id,departure,destination,departure_time,arrival_time,check_in,check_out\n"
                + "3,Family Vacation,2023-08-15,2023-08-25,1,New York,Los Angeles,2023-07-15T08:00:00,2023-07-15T10:00:00,,\n"
                + "3,Family Vacation,2023-08-15,2023-08-25,2,,,2023-07-15T08:00:00,2023-07-15T08:00:00,2023-07-15T08:00:00,2023-07-15T08:00:00\n"
                + "3,Family Vacation,2023-08-15,2023-08-25,3,Los Angeles,Las Vegas,2023-07-15T08:00:00,2023-07-15T08:00:00,,\n"
                + "3,Family Vacation,2023-08-15,2023-08-25,4,,,2023-07-15T08:00:00,2023-07-15T08:00:00,2023-07-15T08:00:00,2023-07-15T08:00:00\n"
                + "3,Family Vacation,2023-08-15,2023-08-25,5,Las Vegas,New York,2023-07-15T08:00:00,2023-07-15T08:00:00,,";
            fw.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw !=null) {
                    fw.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
