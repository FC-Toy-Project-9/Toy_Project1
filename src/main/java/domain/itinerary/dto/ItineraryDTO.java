package domain.itinerary.dto;

import com.google.gson.annotations.SerializedName;
import global.dto.TripCsvDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItineraryDTO {

    @SerializedName("itinerary_id")
    private int id;
    @SerializedName("departure_place")
    private String departurePlace;
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
    public ItineraryDTO(int id, String departurePlace, String destination,
        LocalDateTime departureTime, LocalDateTime arrivalTime,
        LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = id;
        this.departurePlace = departurePlace;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    @Override
    public String toString(){
        return "여정 ID: "+ id
            +"\n출발지: " + departurePlace
            +"\n도착지: " + destination
            +"\n출발 시간: " + departureTime
            +"\n도착 시간: " + arrivalTime
            +"\n체크인: " + checkIn
            +"\n체크아웃: " + checkOut
            +"\n------------------------------";
    }

    public TripCsvDTO toTripCsvDTO(int tripId, String tripName, LocalDate startDate, LocalDate endDate){
        return TripCsvDTO.builder().tripId(tripId).tripName(tripName).startDate(startDate)
            .endDate(endDate).itineraryId(this.id).departure(this.departurePlace).destination(this.destination)
            .departureTime(this.departureTime).arrivalTime(this.arrivalTime)
            .checkIn(this.checkIn).checkOut(this.checkOut).build();
    }

}
