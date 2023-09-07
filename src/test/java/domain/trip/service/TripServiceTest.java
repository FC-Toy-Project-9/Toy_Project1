package domain.trip.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
public class TripServiceTest {

    TripService tripService = new TripService();

    @Mock
    TripService mockTripService = new TripService();

    @Nested
    @DisplayName("getTripListFromJson()는 ")
    class Context_getTripList {

        @Test
        @DisplayName("전체 여행 기록을 조회할 수 있다.")
        void _willSuccess() {
            // given
            List<TripDTO> tripList = new ArrayList<>();
            tripList.add(TripDTO.builder().name("Family Vacation").build());
            when(mockTripService.getTripListFromJson()).thenReturn(tripList);

            // when
            List<TripDTO> result = mockTripService.getTripListFromJson();

            //then
            Assertions.assertEquals(result, tripList);
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
            TripDTO trip = TripDTO.builder().id(1).build();
            when(mockTripService.getTripFromJson(1)).thenReturn(trip);

            // when
            TripDTO result = mockTripService.getTripFromJson(1);

            // then
            Assertions.assertEquals(result.getId(), trip.getId());
        }

        @Test
        @DisplayName("특정 여행 기록이 없으면 조회할 수 없다.")
        void tripFileNotFound_willFail() {
            // given, when, then
            Throwable exception = assertThrows(TripFileNotFoundException.class, () -> {
                tripService.getTripFromJson(2);
            });
            assertEquals("여행 파일을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteTrip()은")
    class Context_deleteTrip {

        @Test
        @DisplayName("여행 기록을 삭제할 수 있다.")
        void _willSuccess() {
            // given
            // Test 를 위해 삭제할 파일 생성
            FileWriter fw = null;
            try {
                fw = new FileWriter("./src/main/resources/trip/json/trip_1.json");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // when
            boolean result = tripService.deleteTripFromJson(1);

            // then
            Assertions.assertTrue(result);
        }

        @Test
        @DisplayName("해달 여핼 기록이 없으면 여행 기록을 삭제할 수 없다.")
        void tripFileNotFound_willFail() {
            // given, when
            boolean result = tripService.deleteTripFromJson(1);

            // then
            Assertions.assertFalse(result);
        }
    }
}
