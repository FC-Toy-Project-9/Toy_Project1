package domain.itinerary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryNotFoundException;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import domain.trip.service.TripService;
import global.dto.TripCsvDTO;
import global.util.CsvUtil;
import global.util.FileUtil;
import global.util.JsonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItineraryService {

    private static final String JSONPATH = "src/main/resources/trip/json";
    private static final String CSVPATH = "src/main/resources/trip/csv";
    private static final TripService tripService = new TripService();
    private static final FileUtil fileUtil = new FileUtil();

    /**
     * (json) 특정 여행의 여정을 조회하는 메서드
     *
     * @return 여행 기록 id에 해당하는 json에서 읽어온 TripDTO 객체의 itineraries 필드의 리스트
     * @throws FileNotFoundException
     * @param_id 조회할 특정 여행 trip_id 값
     */
    public List<ItineraryDTO> getItineraryListFromJson(int TripId)
        throws FileNotFoundException {
        TripDTO trip = tripService.getTripFromJson(TripId);
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

    /**
     * (json) 특정 여정을 삭제하는 메서드
     *
     * @param_id TripId, ItineraryId
     */
    public boolean deleteItineraryFromJson(int TripId, int ItineraryId) {
        TripDTO trip = tripService.getTripFromJson(TripId);
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

    /**
     * (csv) 특정 여행의 여정을 조회하는 메서드
     *
     * @param
     * @throws FileNotFoundException
     * @throws
     */

    public List<ItineraryDTO> getItineraryListFromCSV(int TripId) throws FileNotFoundException {
        List<ItineraryDTO> ItineraryList = new ArrayList<>();
        String path = CSVPATH + "/trip_" + TripId + ".csv";
        List<TripCsvDTO> tripCsvList = fileUtil.readCsvFile(path);
        for (TripCsvDTO tripCsv : tripCsvList) {
            ItineraryDTO itinerary = tripCsv.toItineraryDTO();
            ItineraryList.add(itinerary);
        }
        return ItineraryList;


    }

    /**
     * (csv) 특정 메소드를 삭제하는 메서드
     */
    public boolean deleteItineraryFromCSV(int TripId, int ItineraryId)
        throws ItineraryNotFoundException {
        List<ItineraryDTO> ItineraryList = new ArrayList<>();
        String path = CSVPATH + "/trip_" + TripId + ".csv";
        List<TripCsvDTO> tripCsvList = fileUtil.readCsvFile(path);
        List<TripCsvDTO> tripCsvDTOList = new ArrayList<>();
        int forCheck = 1;

        for (TripCsvDTO tripCsv : tripCsvList) {
            ItineraryDTO itinerary = tripCsv.toItineraryDTO();
            ItineraryList.add(itinerary);
        }

        for (int i = 0; i < ItineraryList.size(); i++) {
            if (ItineraryList.get(i).getId() == ItineraryId) {
                ItineraryList.remove(i);
                tripCsvList.remove(i);
                forCheck = 0;
            }
        }
        if (forCheck == 0) {
            try {
                for (int i = 0; i < ItineraryList.size(); i++) {
                    TripCsvDTO tripCsvDTO = ItineraryList.get(i)
                        .toTripCsvDTO(tripCsvList.get(i).getTripId(),
                            tripCsvList.get(i).getTripName(), tripCsvList.get(i).getStartDate(),
                            tripCsvList.get(i).getEndDate());
                    tripCsvDTOList.add(tripCsvDTO);
                }
                CsvUtil.toCsv(tripCsvDTOList, path);
                return true;
            } catch (CsvRequiredFieldEmptyException e) {
                e.printStackTrace();
            } catch (CsvDataTypeMismatchException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;

    }


}




