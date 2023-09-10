package domain.itinerary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryException;
import domain.itinerary.exception.ItineraryNotFoundException;
import domain.trip.dto.TripDTO;
import domain.trip.service.TripService;
import global.dto.TripCsvDTO;
import global.exception.TripFileNotFoundException;
import global.util.CsvUtil;
import global.util.FileUtil;
import global.util.InputUtil;
import global.util.JsonUtil;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     */
    public List<ItineraryDTO> getItineraryListFromJson(int tripId)
        throws ItineraryException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        TripDTO trip = tripService.getTripFromJson(tripId);
        List<ItineraryDTO> itineraryList = new ArrayList<>();
        String jsonFilePath = JSONPATH + "/trip_" + tripId + ".json";
        if (!trip.getItineraries().isEmpty()) {
            File file = new File(jsonFilePath);
            // 파일이 없는 경우 TripFileNotFoundException을 던진다.
            if (!file.isFile() || !file.canRead()) {
                throw new TripFileNotFoundException();
            }
            try (FileReader reader = new FileReader(file)) {
                // Json 파일을 읽어와 Itinerary 정보들을 파싱한다.
                JsonElement element = JsonParser.parseReader(reader);
                JsonObject tripObj = element.getAsJsonObject();
                JsonArray itineraryArr = tripObj.getAsJsonArray("itineraries");
                // 파싱된 Itinerary 정보들을 ItineraryDTO Array로 역질렬화한다.
                ItineraryDTO[] array = jsonUtil.fromJson(itineraryArr.toString(),
                    ItineraryDTO[].class);
                // ItineraryDTO Array를 List로 변환한다.
                itineraryList = Arrays.asList(array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Itinerary 정보가 없을경우 Itinerary 기록하기 API로 연동된다.
            postItinerary(tripId);
        }
        return itineraryList;
    }

    /**
     * (json) 특정 여정을 삭제하는 메서드
     *
     * @param tripId      조회할 특정 여행 tripId
     * @param itineraryId 특정 여행의 itineraryId
     * @return 삭제 성공 여부 (삭제 성공: true, 삭제 실패: false)
     */
    public boolean deleteItineraryFromJson(int tripId, int itineraryId)
        throws ItineraryNotFoundException {
        TripDTO trip = tripService.getTripFromJson(tripId);
        String jsonFilePath = JSONPATH + "/trip_" + tripId + ".json";
        boolean found = false;
        // 해당 Trip의 ItineraryDTO List에서 삭제할 itineraryId와 같은 객체를 찾아 삭제한다.
        for (int i = 0; i < trip.getItineraries().size(); i++) {
            if (trip.getItineraries().get(i).getId() == itineraryId) {
                trip.getItineraries().remove(i);
                found = true;
            }
        }
        // 삭제한 경험이 없다면, ItineraryNotFoundException 예외처리를 해준다.
        if (!found) {
            throw new ItineraryNotFoundException();
        }
        // 삭제한 뒤 새로운 객체를 Json으로 직렬화하여 파일에 덮어씌운다.
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
     */
    public List<ItineraryDTO> getItineraryListFromCSV(int tripId)
        throws IOException, ItineraryException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<ItineraryDTO> itineraryList = new ArrayList<>();
        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";
        // CSV 파일 경로를 따라 tripCsvDTO를 정보를 List로 받아온다.
        List<TripCsvDTO> tripCsvList = fileUtil.readCsvFile(csvFilePath);
        for (TripCsvDTO tripCsv : tripCsvList) {
            Integer id = tripCsv.getItineraryId();
            // ItineraryId가 null이 아닐 경우 itineraryList에 정보를 담는다.
            if (id != 0) {
                ItineraryDTO itinerary = tripCsv.toItineraryDTO();
                itineraryList.add(itinerary);
            } else {
                // Itinerary 정보가 없을경우 Itinerary 기록하기 API로 연동된다.
                System.out.println("여정 기록이 없습니다. 여정을 추가해주세요.");
                postItinerary(tripId);
            }
        }
        return itineraryList;
    }

    /**
     * (csv) 특정 메소드를 삭제하는 메서드
     *
     * @param tripId      조회할 특정 여행 tripId
     * @param itineraryId 특정 여행의 itineraryId
     * @return 삭제 성공 여부 (삭제 성공: true, 삭제 실패: false)
     */
    public boolean deleteItineraryFromCSV(int tripId, int itineraryId)
        throws ItineraryNotFoundException {
        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";
        List<TripCsvDTO> tripCsvList = fileUtil.readCsvFile(csvFilePath);
        // 삭제시, List의 size변동으로 인해 tripCsvList를 복사해 임시 List를 만든다.
        List<TripCsvDTO> tmpTripCsvList = fileUtil.readCsvFile(csvFilePath);
        List<ItineraryDTO> itineraryList = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < tripCsvList.size(); i++) {
            ItineraryDTO itinerary = tripCsvList.get(i).toItineraryDTO();
            // 삭제할 itineraryId와 값이 동일하면, 임시 List에서 해당 id의 itinerary 정보를 삭제한다.
            if (itinerary.getId() == itineraryId) {
                found = true;
                tmpTripCsvList.removeIf(m -> (m.getItineraryId() == itineraryId));
            } else {
                // 삭제할 itineraryId와 값이 다르므로, 반환할 itineraryList에 itinerary 정보를 담는다.
                itineraryList.add(itinerary);
            }
        }
        // 삭제한 경험이 없다면, ItineraryNotFoundException 예외처리를 해준다.
        if (!found) {
            throw new ItineraryNotFoundException();
        }
        try {
            List<TripCsvDTO> updatedCsvList = new ArrayList<>();
            for (int i = 0; i < itineraryList.size(); i++) {
                // 삭제가 반영된 itineraryList와 tmpTripCsvList를 토대로 새로운 tripCsvDTO객체를 만든다.
                TripCsvDTO tripCsvDTO = itineraryList.get(i)
                    .toTripCsvDTO(tmpTripCsvList.get(i).getTripId(),
                        tmpTripCsvList.get(i).getTripName(), tmpTripCsvList.get(i).getStartDate(),
                        tmpTripCsvList.get(i).getEndDate());
                updatedCsvList.add(tripCsvDTO);
            }
            // 삭제가 반영된 tripCsvDTO를 CSV로 직렬화하여 파일에 저장한다.
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

    /**
     * 특정 여행의 여정 정보를 입력받아 해당 여행의 json 파일 및 csv 파일에 여정 정보를 업데이트 하는 메서드
     *
     * @param tripId 여행의 ID
     * @throws ItineraryException             여정 정보가 올바르지 않을 때 발생하는 예외
     * @throws CsvRequiredFieldEmptyException 필수 필드 누락 예외 (CSV 라이브러리 사용 시)
     * @throws CsvDataTypeMismatchException   데이터 타입 불일치 예외 (CSV 라이브러리 사용 시)
     * @throws IOException                    파일 입출력 예외
     */
    public void postItinerary(int tripId)
        throws ItineraryException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        // JSON 및 CSV 파일 경로 설정한다.
        String jsonFilePath = JSONPATH + "/trip_" + tripId + ".json";
        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";

        // JSON 파일로부터 여행 정보 읽어온다.
        TripDTO trip = fileUtil.readJsonFile(jsonFilePath, TripDTO.class);

        while (true) {
            // 사용자로부터 여정 정보 입력 및 생성한다.
            ItineraryDTO itinerary = createItinerary(trip);

            // 도착 날짜, 체크인 날짜, 체크아웃 날짜, 출발 날짜 간의 관계를 검증
            validateItineraryDates(trip.getStartDate(), trip.getEndDate(),
                itinerary.getDepartureTime(), itinerary.getArrivalTime(),
                itinerary.getCheckIn(), itinerary.getCheckOut());

            // TripDTO 객체의 itineraries 리스트에 새로운 여정 정보 추가한다.
            addItineraryToTrip(trip, itinerary);

            // TripDTO 객체를 JSON 문자열로 변환하여 JSON 파일에 업데이트된 정보 저장한다.
            String updatedJson = JsonUtil.toJson(trip);
            saveJsonToFile(jsonFilePath, updatedJson);

            // CSV 파일 여정 정보 저장한다.
            saveItineraryToCSV(csvFilePath, itinerary);

            // 여정 정보에 관해 추가 저장 여부를 묻는다.
            System.out.print("추가로 여정 정보를 저장하시겠습니까? (y/n) ");
            String answer = InputUtil.getInputString("");
            if (!(answer.equals("y") | answer.equals("Y"))) {
                break;
            }
        }
    }

    /**
     * 날짜 관계를 검증하는 메서드
     *
     * @param startDate         여행의 시작 날짜
     * @param endDate           여행의 종료 날짜
     * @param departureDateTime 출발 일시
     * @param arrivalDateTime   도착 일시
     * @param checkInDateTime   체크인 일시
     * @param checkOutDateTime  체크아웃 일시
     * @throws ItineraryException 여행 일정 관련 예외 발생 시
     */
    private static void validateItineraryDates(LocalDate startDate, LocalDate endDate,
        LocalDateTime departureDateTime, LocalDateTime arrivalDateTime,
        LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime)
        throws ItineraryException {
        // 출발 시간이 도착 시간 이전인지 검증한다.
        if (arrivalDateTime.isBefore(departureDateTime)) {
            throw new ItineraryException("출발 시간은 도착 시간 전으로 입력해주세요.");
        }
        // 체크아웃 시간이 체크인 시간 이전인지 검증한다.
        if (checkOutDateTime.isBefore(checkInDateTime)) {
            throw new ItineraryException("체크인 시간은 체크아웃 시간 전으로 입력해주세요.");
        }
        // 체크인 시간이 도착 시간 이후인지 검증한다.
        if (checkInDateTime.isBefore(arrivalDateTime)) {
            throw new ItineraryException("체크인 시간은 도착 시간 후로 입력해주세요.");
        }
        // 출발 시간이 여행 종료일 자정 전인지 검증한다.
        if (endDate.atTime(23, 59).isBefore(departureDateTime)) {
            throw new ItineraryException("출발 시간은 여행 종료일 자정 전으로 입력해주세요.");
        }
        // 출발 시간이 여행 시작 이후인지 검증한다.
        if (departureDateTime.isBefore(startDate.atTime(0, 0))) {
            throw new ItineraryException("출발 시간은 여행 시작 이후로 입력해주세요.");
        }
    }

    /**
     * 새로 입력받은 여정정보에 관한 객체를 생성하는 메서드
     *
     * @param trip 여행 정보를 나타내는 TripDTO 객체
     * @return 생성된 ItineraryDTO 객체
     */
    private static ItineraryDTO createItinerary(TripDTO trip) {
        int previousItineraryId = 0;

        // 기존 여정 목록에서 가장 큰 여정 ID(가장 최신 여정)를 찾아 새로운 여정 ID를 설정한다.
        if (trip.getItineraries() != null && !trip.getItineraries().isEmpty()) {
            for (ItineraryDTO itinerary : trip.getItineraries()) {
                if (itinerary.getId() > previousItineraryId) {
                    previousItineraryId = itinerary.getId();
                }
            }
        }

        // 사용자로부터 여정 정보 입력받는다.
        String departurePlace = InputUtil.getInputString("출발지");
        String destination = InputUtil.getInputString("도착지");
        LocalDateTime departureTime = InputUtil.getInputLocalDateTime("출발 날짜");
        LocalDateTime arrivalTime = InputUtil.getInputLocalDateTime("도착 날짜");
        LocalDateTime checkIn = InputUtil.getInputLocalDateTime("체크인 날짜");
        LocalDateTime checkOut = InputUtil.getInputLocalDateTime("체크아웃 날짜");

        // 새로운 ItineraryDTO 객체 생성하여 반환한다.
        return new ItineraryDTO(previousItineraryId + 1, departurePlace, destination,
            departureTime, arrivalTime, checkIn, checkOut);
    }

    /**
     * 새로 입력받은 여정 정보를 기존의 특정 여행 객체에 추가하는 메서드
     *
     * @param trip      여행 정보를 나타내는 TripDTO 객체
     * @param itinerary 추가할 ItineraryDTO 객체
     */
    private static void addItineraryToTrip(TripDTO trip, ItineraryDTO itinerary) {
        trip.getItineraries().add(itinerary); // 여행 객체에 새로운 여정 정보를 추가한다.
    }

    /**
     * JSON 파일에 업데이트된 여정 정보를 저장하는 메서드
     *
     * @param filePath    저장할 JSON 파일 경로
     * @param jsonContent 저장할 JSON 형식의 문자열
     */
    private static void saveJsonToFile(String filePath, String jsonContent) {
        try (FileWriter fileWriter = new FileWriter(filePath);) {
            fileWriter.write(jsonContent); // JSON 파일에 업데이트된 여정 정보를 저장한다.
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * CSV 파일에 업데이트된 여정 정보를 저장하는 메서드
     *
     * @param csvFilePath CSV 파일 경로
     * @param itinerary   추가할 ItineraryDTO 객체
     * @throws IOException                    파일 입출력 예외 발생 시
     * @throws CsvRequiredFieldEmptyException 필수 필드 누락 예외 발생 시 (CSV 라이브러리 사용 시)
     * @throws CsvDataTypeMismatchException   데이터 타입 불일치 예외 발생 시 (CSV 라이브러리 사용 시)
     */
    private void saveItineraryToCSV(String csvFilePath, ItineraryDTO itinerary)
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        // 기존 CSV 파일 내용을 읽는다.
        List<TripCsvDTO> trip = fileUtil.readCsvFile(csvFilePath);

        // 여행 정보를 가져옵니다.
        int tripId = trip.get(0).getTripId();
        String tripName = trip.get(0).getTripName();
        LocalDate startDate = trip.get(0).getStartDate();
        LocalDate endDate = trip.get(0).getEndDate();

        // ItineraryDTO를 TripCsvDTO로 변환하여 새로운 여정 정보를 생성한다.
        TripCsvDTO tripCsvDTO = itinerary.toTripCsvDTO(tripId, tripName, startDate, endDate);

        // 여정 정보를 기존 CSV 파일에 추가한다.
        if (trip.get(0).getItineraryId() == 0) {
            trip.remove(0);
        }
        trip.add(tripCsvDTO);

        // CSV 파일로 저장한다.
        CsvUtil.toCsv(trip, csvFilePath);
    }
}