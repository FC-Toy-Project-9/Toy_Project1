package domain.itinerary.dto;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        return "Itinerary [id : " + id + ", " +
            "departurePlace : " + departurePlace + ", " +
            "destination : " + destination + ", " +
            "departureTime : " + departureTime + ", " +
            "arrivalTime : " + arrivalTime + ", " +
            "checkIn : " + checkIn + ", " +
            "checkOut : " + checkOut + "]" + "\n" ;

    }

}
