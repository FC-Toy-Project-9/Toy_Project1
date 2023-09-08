package global.dto;

import com.google.gson.annotations.SerializedName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
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
    @CsvBindByPosition(position = 0)
    private int tripId;

    @CsvBindByName(column = "trip_name", required = true)
    @CsvBindByPosition(position = 1)
    private String tripName;

    @CsvBindByName(column = "start_date", required = true)
    @CsvBindByPosition(position = 2)
    @CsvDate("yyyy-MM-dd")
    private LocalDate startDate;

    @CsvBindByName(column = "end_date", required = true)
    @CsvBindByPosition(position = 3)
    @CsvDate("yyyy-MM-dd")
    private LocalDate endDate;

    @CsvBindByName(column = "itinerary_id")
    @CsvBindByPosition(position = 4)
    private int itineraryId;

    @CsvBindByName(column = "departure")
    @CsvBindByPosition(position = 5)
    private String departure;

    @CsvBindByName(column = "destination")
    @CsvBindByPosition(position = 6)
    private String destination;

    @CsvBindByName(column = "departure_time")
    @CsvBindByPosition(position = 7)
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;

    @CsvBindByName(column = "arrival_time")
    @CsvBindByPosition(position = 8)
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalTime;

    @CsvBindByName(column = "check_in")
    @CsvBindByPosition(position = 9)
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkIn;

    @CsvBindByName(column = "check_out")
    @CsvBindByPosition(position = 10)
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOut;

    @Builder
    public TripCsvDTO(int tripId, String tripName, LocalDate startDate, LocalDate endDate,
        int itineraryId, String departure, String destination, LocalDateTime departureTime,
        LocalDateTime arrivalTime, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itineraryId = itineraryId;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public TripCsvDTO(int tripId, String tripName, LocalDate startDate, LocalDate endDate){
        this.tripId = tripId;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itineraryId = -1;
        this.departure = null;
        this.destination = null;
        this.departureTime = null;
        this.arrivalTime = null;
        this.checkIn = null;
        this.checkOut = null;
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
