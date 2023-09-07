package domain.itinerary.service;

import domain.itinerary.dto.ItineraryDTO;
import global.util.InputUtil;
import global.util.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ItineraryService {

    private static final String JSONPATH = "src/main/resources/itinerary/json";

    public static void recordItinerary() {
        while (true) {
            // 사용자로부터 여정 정보 입력 받기
            ItineraryDTO itinerary = new ItineraryDTO();

            itinerary.setDeparturePlace(InputUtil.getInputString("출발지"));
            itinerary.setDestination(InputUtil.getInputString("도착지"));
            itinerary.setDepartureTime(InputUtil.getInputLocalDate("출발 날짜"));
            itinerary.setArrivalTime(InputUtil.getInputLocalDate("도착 날짜"));
            itinerary.setCheckIn(InputUtil.getInputLocalDate("체크인 날짜"));
            itinerary.setCheckOut(InputUtil.getInputLocalDate("체크아웃 날짜"));

            // 여정 정보 출력
            System.out.println("여정 정보: " + itinerary);

            // ItineraryDTO 객체를 JSON으로 직렬화
            String jsonItinerary = JsonUtil.toJson(itinerary);

            // JSON을 파일에 저장
            saveItineraryToFile(jsonItinerary);

            // 추가로 여정 정보를 저장 여부 묻기
            System.out.print("추가로 여정 정보를 저장하시겠습니까? (yes/no): ");
            String answer = InputUtil.getInputString("");
            if (!"yes".equalsIgnoreCase(answer)) {
                break;
            }
        }
    }

    private static void saveItineraryToFile(String jsonItinerary) {
        try {
            // JSON 파일을 저장할 디렉토리가 없으면 생성
            File jsonDirectory = new File(JSONPATH);
            if (!jsonDirectory.exists()) {
                jsonDirectory.mkdirs();
            }

            // 여정 정보를 파일에 저장
            String fileName = generateFileName(); // 파일명 생성
            File jsonFile = new File(jsonDirectory, fileName);

            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write(jsonItinerary);
            }

            System.out.println("여정 정보가 저장되었습니다. 파일명: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일명을 고유하게 생성하는 메서드 (예: itinerary_1.json, itinerary_2.json, ...)
    private static String generateFileName() {
        File jsonDirectory = new File(JSONPATH);
        int fileCount = jsonDirectory.list().length;
        return "itinerary_" + (fileCount + 1) + ".json";
    }

    public static void main(String[] args) {
        // 엔트리 포인트 메서드에서 recordItinerary 메서드 호출
        recordItinerary();
    }
}