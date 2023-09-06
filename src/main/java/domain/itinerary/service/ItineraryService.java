package domain.itinerary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import domain.trip.service.TripService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import global.util.JsonUtil;

public class ItineraryService {

    private static final String JSONPATH = "src/main/resources/trip/json";

    /**
     * 특정 여행의 여정을 조회하는 메서드
     *
     * @return 여행 기록 id에 해당하는 json에서 읽어온 TripDTO 객체의 itineraries 필드의 리스트
     * @throws FileNotFoundException
     * @param_id 조회할 특정 여행 trip_id 값
     */
    public static List<ItineraryDTO> getItineraryListFromTrip(int TripId)
        throws FileNotFoundException {
        TripDTO trip = TripService.getTripFromJson(TripId);
        List<ItineraryDTO> ItineraryList = new ArrayList<>();
        if (trip.getItineraries() != null) {
            File file = new File(JSONPATH + "/trip_" + TripId + ".json");
            if (!file.isFile() || !file.canRead()) {
                throw new TripFileNotFoundException();
            }
            try {
                FileReader fr = new FileReader(file);
                JsonElement element = JsonParser.parseReader(fr);
                JsonObject tripObj = element.getAsJsonObject();
                JsonArray itineraryArr = (JsonArray) tripObj.get("itineraries");
                ItineraryDTO[] array = JsonUtil.fromJson(itineraryArr.toString(),
                    ItineraryDTO[].class);
                ItineraryList = Arrays.asList(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Itinerary 기록하기 API로 연동
        }
        return ItineraryList;
    }

}
