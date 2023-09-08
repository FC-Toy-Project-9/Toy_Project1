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
    private static final JsonUtil jsonUtil = new JsonUtil();

    /**
     * (json) 특정 여행의 여정을 조회하는 메서드
     *
     * @param tripId 조회할 특정 여행 tripId
     * @return 여행 기록 id에 해당하는 json에서 읽어온 itinerary 정보 리스트
     * @throws FileNotFoundException JSON 파일을 찾을 수 없을 때 발생
     */
    public List<ItineraryDTO> getItineraryListFromJson(int tripId) throws FileNotFoundException {
        TripDTO trip = tripService.getTripFromJson(tripId);
        List<ItineraryDTO> itineraryList = new ArrayList<>();

        String jsonFilePath = JSONPATH + "/trip_" + tripId + ".json";

        if (trip.getItineraries() != null) {
            File file = new File(jsonFilePath);
            if (!file.isFile() || !file.canRead()) {
                throw new TripFileNotFoundException();
            }
            try (FileReader reader = new FileReader(file)) {
                JsonElement element = JsonParser.parseReader(reader);
                JsonObject tripObj = element.getAsJsonObject();
                JsonArray itineraryArr = tripObj.getAsJsonArray("itineraries");
                ItineraryDTO[] array = jsonUtil.fromJson(itineraryArr.toString(),
                    ItineraryDTO[].class);
                itineraryList = Arrays.asList(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Itinerary 기록하기 API로 연동
        }
        return itineraryList;
    }

    /**
     * (json) 특정 여정을 삭제하는 메서드
     *
     * @param tripId      조회할 특정 여행 tripId
     * @param itineraryId 특정 여행의 itineraryId
     * @return 삭제 성공 여부 (삭제 성공: true, 삭제 실패: false)
     * @throws ItineraryNotFoundException Itinerary를 찾을 수 없을 때 발생
     */
    public boolean deleteItineraryFromJson(int tripId, int itineraryId)
        throws ItineraryNotFoundException {
        TripDTO trip = tripService.getTripFromJson(tripId);

        String jsonFilePath = JSONPATH + "/trip_" + tripId + ".json";

        boolean found = false;

        for (int i = 0; i < trip.getItineraries().size(); i++) {
            if (trip.getItineraries().get(i).getId() == itineraryId) {
                trip.getItineraries().remove(i);
                found = true;
            }
        }
        if (!found) {
            throw new ItineraryNotFoundException();
        }

        try (FileWriter file = new FileWriter(jsonFilePath)) {
            String newTrip = jsonUtil.toJson(trip);
            file.write(newTrip);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * (csv) 특정 여행의 여정을 조회하는 메서드
     *
     * @param tripId 조회할 특정 여행 tripId
     * @return 여행 기록 id에 해당하는 csv에서 읽어온 itinerary 정보 리스트
     * @throws FileNotFoundException CSV 파일을 찾을 수 없을 때 발생
     */
    public List<ItineraryDTO> getItineraryListFromCSV(int tripId) throws FileNotFoundException {
        List<ItineraryDTO> itineraryList = new ArrayList<>();

        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";

        List<TripCsvDTO> tripCsvList = fileUtil.readCsvFile(csvFilePath);
        for (TripCsvDTO tripCsv : tripCsvList) {
            ItineraryDTO itinerary = tripCsv.toItineraryDTO();
            itineraryList.add(itinerary);
        }
        return itineraryList;

    }

    /**
     * (csv) 특정 메소드를 삭제하는 메서드
     *
     * @param tripId      조회할 특정 여행 tripId
     * @param itineraryId 특정 여행의 itineraryId
     * @return 삭제 성공 여부 (삭제 성공: true, 삭제 실패: false)
     * @throws ItineraryNotFoundException Itinerary를 찾을 수 없을 때 발생
     */
    public boolean deleteItineraryFromCSV(int tripId, int itineraryId)
        throws ItineraryNotFoundException {
        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";

        List<TripCsvDTO> tripCsvList = fileUtil.readCsvFile(csvFilePath);
        List<TripCsvDTO> tmpTripCsvList = fileUtil.readCsvFile(csvFilePath);
        List<ItineraryDTO> itineraryList = new ArrayList<>();

        boolean found = false;

        for (int i = 0; i < tripCsvList.size(); i++) {
            ItineraryDTO itinerary = tripCsvList.get(i).toItineraryDTO();
            if (itinerary.getId() == itineraryId) {
                found = true;
                tmpTripCsvList.removeIf(m -> (m.getItineraryId() == itineraryId));
            } else {
                itineraryList.add(itinerary);
            }
        }

        if (!found) {
            throw new ItineraryNotFoundException();
        }

        try {
            List<TripCsvDTO> updatedCsvList = new ArrayList<>();

            for (int i = 0; i < itineraryList.size(); i++) {
                TripCsvDTO tripCsvDTO = itineraryList.get(i)
                    .toTripCsvDTO(tmpTripCsvList.get(i).getTripId(),
                        tmpTripCsvList.get(i).getTripName(), tmpTripCsvList.get(i).getStartDate(),
                        tmpTripCsvList.get(i).getEndDate());
                updatedCsvList.add(tripCsvDTO);
            }
            CsvUtil.toCsv(updatedCsvList, csvFilePath);
            return true;
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}




