package domain.itinerary.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime; // 날짜와 시간을 다루기 위한 패키지 추가

@Data
public class ItineraryDTO {
    private int itineraryId;             // 여정 ID
    private String departurePlace;       // 출발지
    private String destination;          // 도착지
    private LocalDateTime departureTime; // 출발 시간
    private LocalDateTime arrivalTime;   // 도착 시간
    private LocalDateTime checkIn;       // 체크인 시간
    private LocalDateTime checkOut;      // 체크아웃 시간

    @Builder
    public ItineraryDTO(int itineraryId, String departurePlace, String destination, LocalDateTime departureTime,
                        LocalDateTime arrivalTime, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.itineraryId = itineraryId;
        this.departurePlace = departurePlace;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

}
