package global.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import domain.itinerary.dto.ItineraryDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TripCsvDTO {

    @CsvBindByName(column = "trip_id", required = true)
    private int tripId;

    @CsvBindByName(column = "trip_name", required = true)
    private String tripName;

    @CsvBindByName(column = "start_date", required = true)
    @CsvDate("yyyy-MM-dd")
    private LocalDate startDate;

    @CsvBindByName(column = "end_date", required = true)
    @CsvDate("yyyy-MM-dd")
    private LocalDate endDate;

    @CsvBindByName(column = "itinerary_id")
    private int itineraryId;

    @CsvBindByName(column = "departure")
    private String departure;

    @CsvBindByName(column = "destination")
    private String destination;

    @CsvBindByName(column = "departure_time")
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;

    @CsvBindByName(column = "arrival_time")
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalTime;

    @CsvBindByName(column = "check_in")
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkIn;

    @CsvBindByName(column = "check_out")
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOut;

    public TripCsvDTO(String tripId, String tripName, String startDate, String endDate, String itineraryId, String departure, String destination, String departureTime, String arrivalTime, String checkIn, String checkOut) {
    }

    /**
     * TripCsvDTO에서 여정 정보를 ItineraryDTO에 담아 반환하는 메서드
     *
     * @return ItineraryDTO
     */
    public ItineraryDTO toItineraryDTO() {
        String departure = null;
        String destination = null;
        if (this.departure != null) {
            departure = this.departure;
        }
        if (this.destination != null) {
            destination = this.destination;
        }
        return ItineraryDTO.builder().id(this.itineraryId).departurePlace(departure)
                .destination(destination).departureTime(this.departureTime)
                .arrivalTime(this.arrivalTime).checkIn(this.checkIn).checkOut(this.checkOut).build();
    }
}