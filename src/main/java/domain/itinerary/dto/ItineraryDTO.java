//package domain.itinerary.dto;
//
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime; // 날짜와 시간을 다루기 위한 패키지 추가
//
//@Data
//@NoArgsConstructor
//public class ItineraryDTO {
//    private int itineraryId;             // 여정 ID
//    private String departurePlace;       // 출발지
//    private String destination;          // 도착지
//    private LocalDateTime departureTime; // 출발 시간
//    private LocalDateTime arrivalTime;   // 도착 시간
//    private LocalDateTime checkIn;       // 체크인 시간
//    private LocalDateTime checkOut;      // 체크아웃 시간
//
//    @Builder
//    public ItineraryDTO(int itineraryId, String departurePlace, String destination, LocalDateTime departureTime,
//                        LocalDateTime arrivalTime, LocalDateTime checkIn, LocalDateTime checkOut) {
//        this.itineraryId = itineraryId;
//        this.departurePlace = departurePlace;
//        this.destination = destination;
//        this.departureTime = departureTime;
//        this.arrivalTime = arrivalTime;
//        this.checkIn = checkIn;
//        this.checkOut = checkOut;
//    }
//
//}
package domain.itinerary.dto;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

import global.dto.TripCsvDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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