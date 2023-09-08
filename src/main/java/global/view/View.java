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

public class View {

    private static final TripService tripService = new TripService();
    private static final ItineraryService itineraryService = new ItineraryService();
    private Scanner scanner = new Scanner(System.in);

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

    public void searchTrip()
        throws IOException, ItineraryException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

            while (true) {
                System.out.println("여행 정보를 불러올 파일 유형");
                System.out.println("1. JSON");
                System.out.println("2. CSV");
                System.out.print("번호를 입력하세요: ");
                int fileNumber = scanner.nextInt();
                if (fileNumber == 1) {
                    printTrip(tripService.getTripListFromJson());
                    System.out.print("해당 여행의 여정을 조회하시겠습니까?(y/n) ");
                    searchItinerariesFromJson();
                    break;
                } else if (fileNumber == 2) {
                    printTrip(tripService.getTripListFromCsv());
                    System.out.print("해당 여행의 여정을 조회하시겠습니까?(y/n) ");
                    searchItinerariesFromCsv();
                    break;
                } else {
                    System.out.println("잘못 입력하셨습니다.");
                }
            }
    }

    public void deleteTrip(int id) throws IOException {
        if (tripService.deleteTripFromCsv(id)) {
            System.out.println("CSV 파일이 삭제됐습니다.");
        } else {
            System.out.println("CSV 파일 삭제를 실패했습니다.");
        }
        if (tripService.deleteTripFromJson(id)) {
            System.out.println("JSON 파일이 삭제됐습니다.");
        } else {
            System.out.println("JSON 파일 삭제를 실패했습니다.");
        }
    }

    public void deleteItinerary(int tripId, int itineraryId) throws ItineraryNotFoundException {

            if (itineraryService.deleteItineraryFromCSV(tripId, itineraryId)) {
                System.out.println("CSV 파일에서 해당 여정이 삭제됐습니다.");
            } else {
                System.out.println("CSV 파일에서 해당 여정 삭제를 실패했습니다.");
            }

            if (itineraryService.deleteItineraryFromJson(tripId, itineraryId)) {
                System.out.println("JSON 파일에서 해당 여정이 삭제됐습니다.");
            } else {
                System.out.println("JSON 파일에서 해당 여정 삭제를 실패했습니다.");
            }
    }

    public void searchItinerariesFromCsv() throws FileNotFoundException {
        String answer = scanner.next();
        if (answer.equals("y") | answer.equals("Y")) {
            System.out.print("여정을 조회할 여행 ID를 입력해주세요: ");
            printItineraries(
                itineraryService.getItineraryListFromCSV(scanner.nextInt()));
        }
    }

    public void searchItinerariesFromJson()
        throws IOException, ItineraryException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String answer = scanner.next();
        if (answer.equals("y") | answer.equals("Y")) {
            System.out.print("여정을 조회할 여행 ID를 입력해주세요: ");
            printItineraries(
                itineraryService.getItineraryListFromJson(scanner.nextInt()));
        }
    }

    public void printItineraries(List<ItineraryDTO> itineraries) {
        System.out.println("------------------------------");
        System.out.println("         [여정  목록]");
        for (ItineraryDTO itinerary : itineraries) {
            System.out.println(itinerary);
        }
    }

    public void printTrip(List<TripDTO> trips) {
        System.out.println("\n------------------------------");
        System.out.println("         [여행  목록]");
        System.out.println("------------------------------");
        for (TripDTO trip : trips) {
            System.out.println(trip);
        }
    }

    public Scanner getScanner() {
        return scanner;
    }
}
