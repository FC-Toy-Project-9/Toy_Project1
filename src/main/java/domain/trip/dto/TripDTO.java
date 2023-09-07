package domain.trip.dto;

import com.google.gson.annotations.SerializedName;
import domain.itinerary.dto.ItineraryDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public TripDTO(int id, String name, LocalDate startDate, LocalDate endDate,
        List<ItineraryDTO> itineraries) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itineraries = itineraries;
    }

    @Override
    public String toString() {
        return "TripDTO{" + "id=" + id + ", name='" + name + '\'' + ", startDate=" + startDate
            + ", endDate=" + endDate + ", itineraries=" + itineraries + '}';
    }
}
