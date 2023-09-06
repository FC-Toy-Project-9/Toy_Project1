package domain.itinerary.service;

import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import domain.trip.service.TripService;
import global.util.JsonUtil;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ItineraryService {

    private static final String JSONPATH = "src/main/resources/trip/json";

    /**
     * json에서 특정 여정을 삭제하는 메서드
     *
     * @param_id TripId, ItineraryId
     */
    public static boolean deleteItinerary(int TripId, int ItineraryId) {
        TripDTO trip = TripService.getTripFromJson(TripId);
        for (int i = 0; i < trip.getItineraries().size(); i++) {

            if (trip.getItineraries().get(i).getId() == ItineraryId) {
                trip.getItineraries().remove(i);
                try {
                    String newTrip = JsonUtil.toJson(trip);
                    FileWriter file = new FileWriter(JSONPATH + "/trip_" + TripId + ".json");
                    file.write(newTrip);
                    file.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return false;
    }


}
