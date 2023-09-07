package global.dto;

import com.google.gson.annotations.SerializedName;
import domain.itinerary.dto.ItineraryDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TripCsvDTO {

    @SerializedName("trip_id")
    private int tripId;
    @SerializedName("trip_name")
    private String tripName;
    @SerializedName("start_date")
    private LocalDate startDate;
    @SerializedName("end_date")
    private LocalDate endDate;
    @SerializedName("itinerary_id")
    private int itineraryId;
    @SerializedName("departure")
    private String departure;
    @SerializedName("destination")
    private String destination;
    @SerializedName("departure_time")
    private LocalDateTime departureTime;
    @SerializedName("arrival_time")
    private LocalDateTime arrivalTime;
    @SerializedName("check_in")
    private LocalDateTime checkIn;
    @SerializedName("check_out")
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
