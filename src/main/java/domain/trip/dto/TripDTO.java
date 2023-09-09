package domain.trip.dto;

import com.google.gson.annotations.SerializedName;
import domain.itinerary.dto.ItineraryDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 기록 DTO 클래스
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TripDTO {

    @SerializedName("trip_id")
    private int id;
    @SerializedName("trip_name")
    private String name;
    @SerializedName("start_date")
    private LocalDate startDate;
    @SerializedName("end_date")
    private LocalDate endDate;
    @SerializedName("itineraries")
    private List<ItineraryDTO> itineraries;

    /**
     * Builder 기능이 추가된 TripDTO 클래스 생성자 메서드
     *
     * @param id          여행 ID
     * @param name        여행 이름
     * @param startDate   여행 시작일
     * @param endDate     여행 종료일
     * @param itineraries 해당 여행의 여정 리스트
     */
    @Builder
    public TripDTO(int id, String name, LocalDate startDate, LocalDate endDate,
        List<ItineraryDTO> itineraries) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itineraries = itineraries;
    }

    /**
     * TripDTO toString 메서드 Overriding
     *
     * @return 여행 기록(여정 기록 제외)를 담은 문자열
     */
    @Override
    public String toString() {
        return "여행 ID: " + id + "\n여행 이름: " + name + "\n시작일: " + startDate + "\n종료일: " + endDate
            + "\n------------------------------";
    }
}
