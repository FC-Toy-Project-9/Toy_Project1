import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryException;
import domain.itinerary.service.ItineraryService;
import domain.trip.dto.TripDTO;
import global.util.CsvUtil;
import global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {

    private ItineraryService itineraryService;
    private static final String TEST_CSV_PATH = "src/test/resources/test_trip.csv";

    @Mock
    private JsonUtil jsonUtil;

    @Mock
    private CsvUtil csvUtil;

    @BeforeEach
    void setUp() {
        itineraryService = new ItineraryService();
    }

    @Test
    @DisplayName("여정 정보를 정상적으로 입력하고 JSON 파일에 저장")
    void recordItinerary_willSuccess() throws IOException, ItineraryException {
        // Arrange
        TripDTO trip = createSampleTrip();
        ItineraryDTO itinerary = createSampleItinerary();

        // Mock JSON 파일 읽기
        Mockito.when(jsonUtil.fromJson(Mockito.anyString(), Mockito.eq(TripDTO.class))).thenReturn(trip);

        // Mock 사용자 입력
        Mockito.when(jsonUtil.toJson(Mockito.any(TripDTO.class))).thenReturn("updatedJson");

        // Act
        itineraryService.recordItinerary(1);

        // Assert
        assertTrue(Files.exists(Path.of(TEST_CSV_PATH)), "CSV 파일이 생성되어야 합니다.");
    }

    @Test
    @DisplayName("날짜 관계가 잘못된 여정 정보 입력 시 예외 발생")
    void recordItinerary_invalidDates_willThrowException() {
        // Arrange
        TripDTO trip = createSampleTrip();
        ItineraryDTO invalidItinerary = createInvalidDatesItinerary();

        // Mock JSON 파일 읽기
        Mockito.when(jsonUtil.fromJson(Mockito.anyString(), Mockito.eq(TripDTO.class))).thenReturn(trip);

        // Act & Assert
        assertThrows(ItineraryException.class, () -> {
            itineraryService.recordItinerary(1);
        });
    }

    @Test
    @DisplayName("샘플 TripDTO 객체를 생성할 수 있다.")
    void createSampleTrip_willSuccess() {
        // given

        // when
        TripDTO trip = createSampleTrip();

        // then
        assertEquals(1, trip.getTripId());
        assertEquals("Sample Trip", trip.getName());
        assertEquals(LocalDate.of(2023, 9, 1), trip.getStartDate());
        assertEquals(LocalDate.of(2023, 9, 7), trip.getEndDate());
        assertEquals(new ArrayList<>(), trip.getItineraries());
    }

    @Test
    @DisplayName("CSV 파일에서 여정 정보를 읽어올 때 정상적으로 파싱")
    void getItineraryListFromCSV_willSuccess() throws IOException {
        // Arrange
        List<ItineraryDTO> expectedItineraryList = List.of(createSampleItinerary());

        // Mock CSV 파일 읽기
        Mockito.when(csvUtil.fromCsv(Mockito.any(), Mockito.eq(ItineraryDTO.class))).thenReturn(expectedItineraryList);

        // Act
        List<ItineraryDTO> itineraryList = itineraryService.getItineraryListFromCSV(String.valueOf(1));

        // Assert
        assertEquals(expectedItineraryList.size(), itineraryList.size());
        assertEquals(expectedItineraryList.get(0).getDeparturePlace(), itineraryList.get(0).getDeparturePlace());
        assertEquals(expectedItineraryList.get(0).getDestination(), itineraryList.get(0).getDestination());
        // ... 다른 필드들도 비교
    }

    private TripDTO createSampleTrip() {
        return TripDTO.builder()
                .id(1) // tripId 대신 id로 수정
                .name("Sample Trip") // tripName 대신 name으로 수정
                .startDate(LocalDate.of(2023, 9, 1)) // startDate 수정
                .endDate(LocalDate.of(2023, 9, 7)) // endDate 수정
                .itineraries(new ArrayList<>())
                .build();
    }


    private ItineraryDTO createSampleItinerary() {
        return ItineraryDTO.builder()
                .id(1)
                .departurePlace("Departure City")
                .destination("Destination City")
                .departureTime(LocalDateTime.of(2023, 9, 2, 10, 0))
                .arrivalTime(LocalDateTime.of(2023, 9, 3, 14, 0))
                .checkIn(LocalDateTime.of(2023, 9, 2, 8, 0))
                .checkOut(LocalDateTime.of(2023, 9, 4, 12, 0))
                .build();
    }

    private ItineraryDTO createInvalidDatesItinerary() {
        // 출발 날짜가 도착 날짜보다 나중인 경우
        return ItineraryDTO.builder()
                .id(1)
                .departurePlace("Departure City")
                .destination("Destination City")
                .departureTime(LocalDateTime.of(2023, 9, 4, 10, 0))
                .arrivalTime(LocalDateTime.of(2023, 9, 3, 14, 0)) // 잘못된 날짜 관계
                .checkIn(LocalDateTime.of(2023, 9, 2, 8, 0))
                .checkOut(LocalDateTime.of(2023, 9, 4, 12, 0))
                .build();
    }
}
