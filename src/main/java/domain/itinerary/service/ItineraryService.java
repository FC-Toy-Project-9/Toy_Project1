

package domain.itinerary.service;

import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryException;
import domain.trip.dto.TripDTO;
import global.util.InputUtil;
import global.util.JsonUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItineraryService {

    private static final String JSONPATH = "src/main/resources/trip/json";

    public static void main(String[] args) {
        int tripId = 1; // 예시로 trip_id를 1로 하드코딩
        recordItinerary(tripId);
    }

    public static void recordItinerary(int tripId) {
        String filePath = JSONPATH + "/trip_" + tripId + ".json";
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

                    // 추가로 여정 정보를 저장 여부 묻기
                    System.out.print("추가로 여정 정보를 저장하시겠습니까? (yes/no): ");
                    String answer = InputUtil.getInputString("");
                    if (!"yes".equalsIgnoreCase(answer)) {
                        break;
                    }
                } catch (ItineraryException e) {
                    System.out.println("오류: " + e.getMessage());
                }
            }
        }
    }


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

    private static void addItineraryToTrip(TripDTO trip, ItineraryDTO itinerary) {
        if (trip.getItineraries() == null) {
            trip.setItineraries(new ArrayList<>());
        }
        trip.getItineraries().add(itinerary);
    }

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

    private static void writeJsonToFile(String filePath, String jsonContent) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

