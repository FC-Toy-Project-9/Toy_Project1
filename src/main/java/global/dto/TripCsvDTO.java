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
    private int trip_id;
    @SerializedName("trip_name")
    private String trip_name;
    @SerializedName("start_date")
    private LocalDate start_date;
    @SerializedName("end_date")
    private LocalDate end_date;
    @SerializedName("itinerary_id")
    private int itinerary_id;
    @SerializedName("departure")
    private String departure;
    @SerializedName("destination")
    private String destination;
    @SerializedName("departure_time")
    private LocalDateTime departure_time;
    @SerializedName("arrival_time")
    private LocalDateTime arrival_time;
    @SerializedName("check_in")
    private LocalDateTime check_in;
    @SerializedName("check_out")
    private LocalDateTime check_out;

    @Builder
    public TripCsvDTO(int trip_id, String trip_name, LocalDate start_date, LocalDate end_date,
        int itinerary_id, String departure, String destination, LocalDateTime departure_time,
        LocalDateTime arrival_time, LocalDateTime check_in, LocalDateTime check_out) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.itinerary_id = itinerary_id;
        this.departure = departure;
        this.destination = destination;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.check_in = check_in;
        this.check_out = check_out;
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
        return ItineraryDTO.builder().id(this.itinerary_id).departurePlace(departure)
            .destination(destination).departureTime(this.departure_time)
            .arrivalTime(this.arrival_time).checkIn(this.check_in).checkOut(this.check_out).build();
    }
}
