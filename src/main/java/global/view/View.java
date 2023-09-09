package global.view;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import domain.itinerary.dto.ItineraryDTO;
import domain.itinerary.exception.ItineraryException;
import domain.itinerary.exception.ItineraryNotFoundException;
import domain.itinerary.service.ItineraryService;
import domain.trip.dto.TripDTO;
import domain.trip.service.TripService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * 메인 메서드에서 사용할 View와 Service를 연결하는 View + Controller 클래스
 */
public class View {

    private static final TripService tripService = new TripService();
    private static final ItineraryService itineraryService = new ItineraryService();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 메뉴를 출력하는 메서드
     */
    public void printMenu() {
        System.out.println("----------[메뉴  선택]----------");
        System.out.println("1. 여행 등록");
        System.out.println("2. 여정 등록");
        System.out.println("3. 여행 조회");
        System.out.println("4. 여행 삭제");
        System.out.println("5. 여정 삭제");
        System.out.println("6. 종료");
        System.out.println("------------------------------");
    }

    /**
     * 여행 기록을 조회하는 메서드
     *
     * @throws Exception 메서드 수행 중 발생할 수 있는 예외
     */
    public void searchTrip() throws Exception {

        while (true) {
            System.out.println("여행 기록을 불러올 파일 유형");
            System.out.println("1. JSON");
            System.out.println("2. CSV");
            System.out.print("번호를 입력하세요: ");
            int fileNumber = scanner.nextInt();
            if (fileNumber == 1) {
                // JSON 파일에서 여행 기록을 조회하여 출력한다.
                printTrip(tripService.getTripListFromJson());
                System.out.print("해당 여행의 여정을 조회하시겠습니까?(y/n) ");
                // JSON 파일에서 여정 기록을 조회하여 출력한다.
                searchItinerariesFromJson();
                break;
            } else if (fileNumber == 2) {
                // CSV 파일에서 여행 기록을 조회하여 출력한다.
                printTrip(tripService.getTripListFromCsv());
                System.out.print("해당 여행의 여정 기록을 조회하시겠습니까?(y/n) ");
                // CSV 파일에서 여정 기록을 조회하여 출력한다.
                searchItinerariesFromCsv();
                break;
            } else {
                System.out.println("잘못 입력하셨습니다.");
            }
        }
    }

    /**
     * 여행 기록을 삭제하는 메서드
     *
     * @param id 삭제할 여행 ID
     * @throws IOException 메서드 수행 중 발생할 수 있는 예외
     */
    public void deleteTrip(int id) throws IOException {
        if (tripService.deleteTripFromJson(id)) {
            System.out.println("JSON 파일이 삭제됐습니다.");
        } else {
            System.out.println("JSON 파일 삭제를 실패했습니다.");
        }
        if (tripService.deleteTripFromCsv(id)) {
            System.out.println("CSV 파일이 삭제됐습니다.");
        } else {
            System.out.println("CSV 파일 삭제를 실패했습니다.");
        }
    }

    /**
     * JSON 파일에서 여정 기록을 조회하는 메서드
     *
     * @throws Exception 메서드 수행 중 발생할 수 있는 예외
     */
    public void searchItinerariesFromJson() throws Exception {
        String answer = scanner.next();
        if (answer.equals("y") | answer.equals("Y")) {
            System.out.print("여정을 조회할 여행 ID를 입력해주세요: ");
            printItineraries(itineraryService.getItineraryListFromJson(scanner.nextInt()));
        }
    }

    /**
     * CSV 파일에서 여정 기록을 조회하는 메서드
     *
     * @throws FileNotFoundException 파일을 못 찾을 경우 발생하는 예외
     */
    public void searchItinerariesFromCsv()
        throws IOException, ItineraryException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String answer = scanner.next();
        if (answer.equals("y") | answer.equals("Y")) {
            System.out.print("여정을 조회할 여행 ID를 입력해주세요: ");
            printItineraries(itineraryService.getItineraryListFromCSV(scanner.nextInt()));
        }
    }

    /**
     * 여정 기록을 삭제하는 메서드
     *
     * @param tripId      삭제할 여정이 속한 여행 ID
     * @param itineraryId 삭제할 여정 ID
     * @throws ItineraryNotFoundException 여정 파일을 못 찾을 때 발생하는 예외
     */
    public void deleteItinerary(int tripId, int itineraryId) throws ItineraryNotFoundException {

        if (itineraryService.deleteItineraryFromCSV(tripId, itineraryId)) {
            System.out.println("CSV 파일에서 해당 여정 기록이 삭제됐습니다.");
        } else {
            System.out.println("CSV 파일에서 해당 여정 기록 삭제를 실패했습니다.");
        }

        if (itineraryService.deleteItineraryFromJson(tripId, itineraryId)) {
            System.out.println("JSON 파일에서 해당 여정 기록이 삭제됐습니다.");
        } else {
            System.out.println("JSON 파일에서 해당 여정 기록 삭제를 실패했습니다.");
        }
    }

    /**
     * 여행 기록 리스트를 출력하는 메서드
     *
     * @param trips 여행 기록 리스트
     */
    public void printTrip(List<TripDTO> trips) {
        System.out.println("\n------------------------------");
        System.out.println("         [여행  목록]");
        System.out.println("------------------------------");
        for (TripDTO trip : trips) {
            System.out.println(trip);
        }
    }

    /**
     * 여정 기록 리스트를 출력하는 메서드
     *
     * @param itineraries 여정 기록 리스트
     */
    public void printItineraries(List<ItineraryDTO> itineraries) {
        System.out.println("------------------------------");
        System.out.println("         [여정  목록]");
        for (ItineraryDTO itinerary : itineraries) {
            System.out.println(itinerary);
        }
    }

    /**
     * 프로그램 종료 전 인스턴스 Scanner를 close하기 위한 getter 메서드
     *
     * @return Scanner 객체
     */
    public Scanner getScanner() {
        return scanner;
    }
}