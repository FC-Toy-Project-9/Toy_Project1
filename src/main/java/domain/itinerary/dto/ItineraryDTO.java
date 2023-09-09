package domain.itinerary.dto;

import com.google.gson.annotations.SerializedName;
import global.dto.TripCsvDTO;
import java.time.LocalDate;
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

    /**
     * Builder 기능이 추가된 ItineraryDTO 클래스 생성자 메서드
     *
     * @param id             여정 ID
     * @param departurePlace 여정 출발지
     * @param destination    여정 도착지
     * @param departureTime  여정 출발시간
     * @param arrivalTime    여정 도착시간
     * @param checkIn        숙박 체크인
     * @param checkOut       숙박 체크아웃
     */
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

    /**
     * ItineraryDTO toString 메서드 Overriding
     *
     * @return 여정 기록을 담은 문자열
     */
    @Override
    public String toString() {
        return "여정 ID: " + id
            + "\n출발지: " + departurePlace
            + "\n도착지: " + destination
            + "\n출발 시간: " + departureTime
            + "\n도착 시간: " + arrivalTime
            + "\n체크인: " + checkIn
            + "\n체크아웃: " + checkOut
            + "\n------------------------------";
    }

    /**
     * TripDTO와 ItineraryDTO의 정보를 담아 TripCsvDTO를 반환하는 메서드
     *
     * @param tripId
     * @param tripName
     * @param startDate
     * @param endDate
     * @return TripCsvDTO
     */
    public TripCsvDTO toTripCsvDTO(int tripId, String tripName, LocalDate startDate,
        LocalDate endDate) {
        return TripCsvDTO.builder().tripId(tripId).tripName(tripName).startDate(startDate)
            .endDate(endDate).itineraryId(this.id).departure(this.departurePlace)
            .destination(this.destination)
            .departureTime(this.departureTime).arrivalTime(this.arrivalTime)
            .checkIn(this.checkIn).checkOut(this.checkOut).build();
    }

}
