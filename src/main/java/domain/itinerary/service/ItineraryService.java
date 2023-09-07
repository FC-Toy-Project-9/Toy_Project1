//
//package domain.itinerary.service;
//
//import com.opencsv.exceptions.CsvDataTypeMismatchException;
//import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
//import domain.itinerary.dto.ItineraryDTO;
//import domain.itinerary.exception.ItineraryException;
//import domain.trip.dto.TripDTO;
//import global.util.CsvUtil;
//import global.util.FileUtil;
//import global.util.InputUtil;
//import global.util.JsonUtil;
//
//import java.io.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ItineraryService {
//
//    private static final String JSONPATH = "src/main/resources/trip/json";
//    private static final String CSVPATH = "src/main/resources/trip/csv";
//    private static final FileUtil fileUtil = new FileUtil();
//
//
//    public static void main(String[] args) {
//        int tripId = 1; // 예시로 trip_id를 1로 하드코딩
//        recordItinerary(tripId);
//    }
//
//    // 여정 정보를 입력 및 저장하는 메서드
//    public static void recordItinerary(int tripId) {
//        String filePath = JSONPATH + "/trip_" + tripId + ".json";
//        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";
//
//        String jsonContent = readJsonFile(filePath);
//
//        if (jsonContent != null) {
//            // JSON 파일을 읽어와서 TripDTO 객체로 변환
//            TripDTO trip = JsonUtil.fromJson(jsonContent, TripDTO.class);
//
//            while (true) {
//                try {
//                    ItineraryDTO itinerary = createItinerary(trip);
//
//                    // 도착 날짜, 체크인 날짜, 체크아웃 날짜, 출발 날짜 간의 관계를 검증
//                    validateItineraryDates(itinerary.getDepartureTime(), itinerary.getArrivalTime(),
//                            itinerary.getCheckIn(), itinerary.getCheckOut());
//
//                    System.out.println("여정 정보: " + itinerary); // 여정 정보 출력
//
//
//                    addItineraryToTrip(trip, itinerary);   // TripDTO 객체의 itineraries 리스트에 새로운 여정 정보를 추가
//                    String updatedJson = JsonUtil.toJson(trip);   // TripDTO 객체를 JSON 문자열로 변환
//                    writeJsonToFile(filePath, updatedJson);  // JSON 파일에 업데이트된 정보 저장
//
//
//                    // 추가로 여정 정보를 저장 여부 묻기
//                    System.out.print("추가로 여정 정보를 저장하시겠습니까? (yes/no) ");
//                    String answer = InputUtil.getInputString("");
//                    if (!"yes".equalsIgnoreCase(answer)) {
//                        break;
//                    }
//                } catch (ItineraryException e) {
//                    System.out.println("오류: " + e.getMessage());
//                }
//            }
//        }
//    }
//
//    // 새로운 여정 정보를 생성하는 메서드
//    private static ItineraryDTO createItinerary(TripDTO trip) {
//        // 이전 itinerary_id를 가져와서 1 증가
//        int previousItineraryId = trip.getItineraries() != null ? trip.getItineraries().size() : 0;
//
//        String departurePlace = InputUtil.getInputString("출발지");
//        String destination = InputUtil.getInputString("도착지");
//        LocalDateTime departureTime = InputUtil.getInputLocalDate("출발 날짜");
//        LocalDateTime arrivalTime = InputUtil.getInputLocalDate("도착 날짜");
//        LocalDateTime checkIn = InputUtil.getInputLocalDate("체크인 날짜");
//        LocalDateTime checkOut = InputUtil.getInputLocalDate("체크아웃 날짜");
//
//        return new ItineraryDTO(previousItineraryId + 1, departurePlace, destination,
//                departureTime, arrivalTime, checkIn, checkOut);
//    }
//
//    // 여정 정보를 여행 객체에 추가하는 메서드
//    private static void addItineraryToTrip(TripDTO trip, ItineraryDTO itinerary) {
//        if (trip.getItineraries() == null) {
//            trip.setItineraries(new ArrayList<>());
//        }
//        trip.getItineraries().add(itinerary);
//    }
//
//    // 날짜 관계를 검증하는 메서드
//    private static void validateItineraryDates(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime,
//                                               LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime)
//            throws ItineraryException {
//        if (arrivalDateTime.isBefore(departureDateTime) ||
//                checkOutDateTime.isBefore(checkInDateTime) ||
//                checkInDateTime.isBefore(departureDateTime) ||
//                arrivalDateTime.isBefore(checkOutDateTime)) {
//            throw new ItineraryException("출발/도착, 체크인/체크아웃 시간을 확인해주세요. 처음부터 다시 입력해주세요.");
//        }
//    }
//
//    // JSON 파일을 읽어오는 메서드
//    private static String readJsonFile(String filePath) {
//        try {
//            File file = new File(filePath);
//            if (!file.isFile() || !file.canRead()) {
//                return null;
//            }
//
//            FileReader fr = new FileReader(file);
//            StringBuilder sb = new StringBuilder();
//            String line;
//            try (BufferedReader br = new BufferedReader(fr)) {
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//            }
//            return sb.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // JSON 파일에 업데이트된 정보를 저장하는 메서드
//    private static void writeJsonToFile(String filePath, String jsonContent) {
//        try {
//            FileWriter fileWriter = new FileWriter(filePath);
//            fileWriter.write(jsonContent);
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * CSV 파일에서 여정 정보를 읽어오는 메서드
//     *
//     * @param tripId 읽어올 여정 정보의 Trip ID
//     * @return 여정 정보 객체 리스트
//     * @throws IOException
//     */
//    public List<ItineraryDTO> getItineraryListFromCSV(int tripId) throws IOException {
//        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";
//
//        try (Reader reader = new FileReader(csvFilePath)) {
//            // CsvUtil 클래스의 fromCsv 메서드를 사용하여 CSV 파일을 객체 리스트로 역직렬화
//            List<ItineraryDTO> itineraryList = CsvUtil.fromCsv(reader, ItineraryDTO.class);
//            return itineraryList;
//        }
//    }
//
//    /**
//     * 새로운 여정 정보를 객체 리스트에 추가하고 기존 CSV 파일에 저장하는 메서드
//     *
//     * @param tripId        Trip ID
//     * @param newItinerary 새로 추가할 여정 정보 객체
//     * @throws IOException
//     * @throws CsvRequiredFieldEmptyException
//     * @throws CsvDataTypeMismatchException
//     */
//    public void addItineraryToListAndSave(int tripId, ItineraryDTO newItinerary)
//            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
//        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";
//
//        // CsvUtil 클래스의 fromCsv 메서드를 사용하여 기존 객체 리스트를 읽어옴
//        List<ItineraryDTO> itineraryList = getItineraryListFromCSV(tripId);
//
//        // 새로운 여정 정보를 객체 리스트에 추가
//        itineraryList.add(newItinerary);
//
//        // CsvUtil 클래스의 saveListToCsv 메서드를 사용하여 객체 리스트를 기존 CSV 파일에 저장
//        CsvUtil.toCsv(itineraryList, csvFilePath);
//    }
//
//}

package domain.itinerary.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryException;
import domain.trip.dto.TripDTO;
import global.dto.TripCsvDTO;
import global.util.CsvUtil;
import global.util.InputUtil;
import global.util.JsonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItineraryService {

    private static final String JSONPATH = "src/main/resources/trip/json";
    private static final String CSVPATH = "src/main/resources/trip/csv";

    public static void main(String[] args) {
        int tripId = 1; // 예시로 trip_id를 1로 하드코딩
        recordItinerary(tripId);
    }

    // 여정 정보를 입력 및 저장하는 메서드
    public static void recordItinerary(int tripId) {
        String filePath = JSONPATH + "/trip_" + tripId + ".json";
        String csvFilePath = CSVPATH + "/trip_" + tripId + ".csv";

        String jsonContent = readJsonFile(filePath);

        if (jsonContent != null) {
            // JSON 파일을 읽어와서 TripDTO 객체로 변환
            TripDTO trip = JsonUtil.fromJson(jsonContent, TripDTO.class);

            while (true) {
                try {
                    ItineraryDTO itinerary = createItinerary(trip);

                    // 도착 날짜, 체크인 날짜, 체크아웃 날짜, 출발 날짜 간의 관계를 검증
                    validateItineraryDates(itinerary.getDepartureTime(), itinerary.getArrivalTime(),
                            itinerary.getCheckIn(), itinerary.getCheckOut());

                    System.out.println("여정 정보: " + itinerary); // 여정 정보 출력

                    addItineraryToTrip(trip, itinerary);   // TripDTO 객체의 itineraries 리스트에 새로운 여정 정보를 추가
                    String updatedJson = JsonUtil.toJson(trip);   // TripDTO 객체를 JSON 문자열로 변환
                    writeJsonToFile(filePath, updatedJson);  // JSON 파일에 업데이트된 정보 저장

                    // CSV 파일에도 저장
                    addItineraryToCSV(csvFilePath, itinerary);

                    // 추가로 여정 정보를 저장 여부 묻기
                    System.out.print("추가로 여정 정보를 저장하시겠습니까? (yes/no) ");
                    String answer = InputUtil.getInputString("");
                    if (!"yes".equalsIgnoreCase(answer)) {
                        break;
                    }
                } catch (ItineraryException e) {
                    System.out.println("오류: " + e.getMessage());
                } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 새로운 여정 정보를 생성하는 메서드
    private static ItineraryDTO createItinerary(TripDTO trip) {
        // 이전 itinerary_id를 가져와서 1 증가
        int previousItineraryId = trip.getItineraries() != null ? trip.getItineraries().size() : 0;

        String departurePlace = InputUtil.getInputString("출발지");
        String destination = InputUtil.getInputString("도착지");
        LocalDateTime departureTime = InputUtil.getInputLocalDate("출발 날짜");
        LocalDateTime arrivalTime = InputUtil.getInputLocalDate("도착 날짜");
        LocalDateTime checkIn = InputUtil.getInputLocalDate("체크인 날짜");
        LocalDateTime checkOut = InputUtil.getInputLocalDate("체크아웃 날짜");

        return new ItineraryDTO(previousItineraryId + 1, departurePlace, destination,
                departureTime, arrivalTime, checkIn, checkOut);
    }

    // 여정 정보를 여행 객체에 추가하는 메서드
    private static void addItineraryToTrip(TripDTO trip, ItineraryDTO itinerary) {
        if (trip.getItineraries() == null) {
            trip.setItineraries(new ArrayList<>());
        }
        trip.getItineraries().add(itinerary);
    }

    // 날짜 관계를 검증하는 메서드
    private static void validateItineraryDates(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime,
                                               LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime)
            throws ItineraryException {
        if (arrivalDateTime.isBefore(departureDateTime) ||
                checkOutDateTime.isBefore(checkInDateTime) ||
                checkInDateTime.isBefore(departureDateTime) ||
                arrivalDateTime.isBefore(checkOutDateTime)) {
            throw new ItineraryException("출발/도착, 체크인/체크아웃 시간을 확인해주세요. 처음부터 다시 입력해주세요.");
        }
    }

    // JSON 파일을 읽어오는 메서드
    private static String readJsonFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.isFile() || !file.canRead()) {
                return null;
            }

            FileReader fr = new FileReader(file);
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader br = new BufferedReader(fr)) {
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // JSON 파일에 업데이트된 정보를 저장하는 메서드
    private static void writeJsonToFile(String filePath, String jsonContent) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//     // 여정 정보를 CSV 파일에 추가하는 메서드
//    private static void addItineraryToCSV(String csvFilePath, ItineraryDTO itinerary) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
//        List<ItineraryDTO> itineraryList = getItineraryListFromCSV(csvFilePath);
//        itineraryList.add(itinerary);
//        CsvUtil.toCsv(itineraryList, csvFilePath);
//    }
//
//     // CSV 파일에서 여정 정보를 읽어오는 메서드
//    private static List<ItineraryDTO> getItineraryListFromCSV(String csvFilePath) throws IOException {
//        try (Reader reader = new FileReader(csvFilePath)) {
//            return CsvUtil.fromCsv(reader, ItineraryDTO.class);
//        }
//    }
// CSV 파일에서 여정 정보를 읽어오는 메서드
private static List<ItineraryDTO> getItineraryListFromCSV(String csvFilePath) throws IOException {
    try (Reader reader = new FileReader(csvFilePath)) {
        CsvToBean<ItineraryDTO> csvToBean = new CsvToBeanBuilder<ItineraryDTO>(reader)
                .withType(ItineraryDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<ItineraryDTO> itineraryList = new ArrayList<>();
        // 헤더를 건너뛰고 데이터만 읽어옴
        Iterator<ItineraryDTO> iterator = csvToBean.iterator();
        iterator.next(); // 첫 번째 라인은 헤더이므로 건너뛰기

        while (iterator.hasNext()) {
            itineraryList.add(iterator.next());
        }

        return itineraryList;
    }
}

    // 여정 정보를 CSV 파일에 추가하는 메서드
    private static void addItineraryToCSV(String csvFilePath, ItineraryDTO itinerary) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<ItineraryDTO> itineraryList = getItineraryListFromCSV(csvFilePath);

        if (itineraryList.isEmpty()) {
            // CSV 파일이 비어있을 때, 헤더를 추가
            itineraryList.add(getItineraryHeader());
        }

        itineraryList.add(itinerary);
        CsvUtil.toCsv(itineraryList, csvFilePath);
    }

    // CSV 파일의 헤더 정보를 반환하는 메서드
    private static ItineraryDTO getItineraryHeader() {
        return new TripCsvDTO("trip_id", "trip_name", "start_date", "end_date", "itinerary_id", "departure", "destination", "departure_time", "arrival_time", "check_in", "check_out").toItineraryDTO();
    }
}
