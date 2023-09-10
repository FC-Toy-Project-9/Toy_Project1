package domain.trip.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import global.exception.TripFileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * TripService 테스트 클래스
 */
@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    TripService tripService = new TripService();

    @Mock
    TripService mockTripService = new TripService();

    @Nested
    @DisplayName("getTripListFromJson()는 ")
    class Context_getTripListFromJson {

        @Test
        @DisplayName("전체 여행 기록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            createTestTripJson();

            // when
            List<TripDTO> result = tripService.getTripListFromJson();

            //then
            Assertions.assertFalse(result.isEmpty());
            deleteTestTripJson();
        }

        @Test
        @DisplayName("여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            // given, when
            when(mockTripService.getTripListFromJson()).thenThrow(new TripFileNotFoundException());

            // then
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                mockTripService.getTripListFromJson();
            });
            assertEquals("여행 파일을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getTripFromJson()는")
    class Context_getTripFromJson {

        @Test
        @DisplayName("특정 여행 기록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            createTestTripJson();

            // when
            TripDTO result = tripService.getTripFromJson(99999);

            // then
            Assertions.assertEquals(result.toString(), getTestTripDTO().toString());
            deleteTestTripJson();
        }

        @Test
        @DisplayName("특정 여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            // given, when, then
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                tripService.getTripFromJson(99999);
            });
            assertEquals("여행 파일을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteTripFromJson()은")
    class Context_deleteTripFromJson {

        @Test
        @DisplayName("여행 기록을 삭제할 수 있다.")
        void _willSuccess() throws IOException {
            // given
            createTestTripJson();

            // when
            boolean result = tripService.deleteTripFromJson(99999);

            // then
            Assertions.assertTrue(result);
            deleteTestTripJson();
        }

        @Test
        @DisplayName("해당 여행 기록이 없으면 여행 기록을 삭제할 수 없다.")
        void tripFileNotFound_willFail() throws IOException {
            // given, when
            boolean result = tripService.deleteTripFromJson(99999);

            // then
            Assertions.assertFalse(result);
        }
    }

    @Nested
    @DisplayName("getTripListFromCsv()는 ")
    class Context_getTripListFromCsv {

        @Test
        @DisplayName("전체 여행 기록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            createTestTripCsv();

            // when
            List<TripDTO> result = tripService.getTripListFromCsv();

            //then
            Assertions.assertFalse(result.isEmpty());
            deleteTestTripCsv();
        }

        @Test
        @DisplayName("여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            // given, when, then
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                tripService.getTripFromCsv(99999);
            });
            assertEquals("여행 파일을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getTripFromCsv()는")
    class Context_getTripFromCsv {

        @Test
        @DisplayName("특정 여행 기록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            createTestTripCsv();

            // when
            TripDTO result = tripService.getTripFromCsv(99999);

            // then
            Assertions.assertEquals(result.toString(), getTestTripDTO().toString());
            deleteTestTripCsv();
        }

        @Test
        @DisplayName("특정 여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            // given, when, then
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                tripService.getTripFromCsv(99999);
            });
            assertEquals("여행 파일을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteTripFromCsv()은")
    class Context_deleteTripFromCsv {

        @Test
        @DisplayName("여행 기록을 삭제할 수 있다.")
        void _willSuccess() throws IOException {
            // given
            createTestTripCsv();

            // when
            boolean result = tripService.deleteTripFromCsv(99999);

            // then
            Assertions.assertTrue(result);
            deleteTestTripCsv();
        }

        @Test
        @DisplayName("해당 여행 기록이 없으면 여행 기록을 삭제할 수 없다.")
        void tripFileNotFound_willFail() throws IOException {
            // given, when
            boolean result = tripService.deleteTripFromCsv(99999);

            // then
            Assertions.assertFalse(result);
        }
    }

    /**
     * 테스트를 위해 여행 기록 JSON을 생성해주는 메서드
     */
    void createTestTripJson() {
        try (BufferedWriter bw = new BufferedWriter(
            new FileWriter("src/main/resources/trip/json/trip_99999.json"))) {
            bw.write("{\n" + "  \"trip_id\": 99999,\n" + "  \"trip_name\": \"테스트 여행\",\n"
                + "  \"start_date\": \"2023-07-15\",\n" + "  \"end_date\": \"2023-07-20\",\n"
                + "  \"itineraries\": [\n" + "    {\n" + "      \"itinerary_id\": 1,\n"
                + "      \"departure_place\": \"서울\",\n"
                + "      \"destination\": \"춘천\",\n"
                + "      \"departure_time\": \"2023-07-15T08:00:00\",\n"
                + "      \"arrival_time\": \"2023-07-15T10:00:00\",\n"
                + "      \"check_in\": \"2023-07-15T12:00:00\",\n"
                + "      \"check_out\": \"2023-07-30T10:00:00\"\n" + "    }\n" + "  ]\n" + "}");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 테스트를 위해 여행 기록 CSV을 생성해주는 메서드
     */
    void createTestTripCsv() {
        try (BufferedWriter bw = new BufferedWriter(
            new FileWriter("src/main/resources/trip/csv/trip_99999.csv"))) {
            bw.write(
                "trip_id,trip_name,start_date,end_date,itinerary_id,departure,destination,departure_time,arrival_time,check_in,check_out\n"
                    + "99999,테스트 여행,2023-07-15,2023-07-20,1,서울,춘천,2023-07-15T08:00:00,2023-07-15T10:00:00,2023-07-15T12:00:00,2023-07-30T10:00:00");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 테스트를 위해 여행 기록 DTO를 반환하는 메서드
     */
    TripDTO getTestTripDTO() {
        List<ItineraryDTO> itineraries = new ArrayList<>();
        itineraries.add(
            ItineraryDTO.builder().id(1).departurePlace("서울").destination("춘천").departureTime(
                    LocalDateTime.of(2023, 7, 15, 8, 0, 0))
                .arrivalTime(LocalDateTime.of(2023, 7, 15, 10, 0, 0))
                .checkIn(LocalDateTime.of(2023, 7, 15, 12, 0, 0))
                .checkOut(LocalDateTime.of(2023, 7, 30, 10, 0, 0)).build());
        return TripDTO.builder().id(99999).name("테스트 여행").startDate(LocalDate.of(2023, 7, 15))
            .endDate(LocalDate.of(2023, 7, 20)).itineraries(itineraries).build();
    }

    /**
     * 테스트를 위해 여행 기록 JSON을 삭제해주는 메서드
     */
    void deleteTestTripJson() {
        try {
            Files.deleteIfExists(Path.of("src/main/resources/trip/json/trip_99999.json"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 테스트를 위해 여행 기록 CSV을 삭제해주는 메서드
     */
    void deleteTestTripCsv() {
        try {
            Files.deleteIfExists(Path.of("src/main/resources/trip/csv/trip_99999.csv"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
