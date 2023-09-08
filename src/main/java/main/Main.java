package main;

import domain.itinerary.service.ItineraryService;
import domain.trip.service.TripService;
import global.view.View;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TripService tripService = new TripService();
    private static final ItineraryService itineraryService = new ItineraryService();
    private static final View view = new View();

    public static void main(String[] args) {
        try {
            boolean isRunning = true;
            while (isRunning) {
                view.printMenu();
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        //1. 여행등록
                        tripService.postTrip();
                        break;
                    case 2:
                        //2. 여정등록
                        view.printTrip(tripService.getTripListFromJson());
                        System.out.print("여정을 추가할 여행 ID를 입력하세요: ");
                        itineraryService.recordItinerary(scanner.nextInt());
                        break;
                    case 3:
                        //3. 여행 조회 (여행전체조회 -> 특정 여행 및 여정 정보 조회)
                        view.searchTrip();
                        break;
                    case 4:
                        //4. 여행 삭제
                        view.printTrip(tripService.getTripListFromJson());
                        System.out.println("삭제할 여행 ID를 입력하세요.");
                        view.deleteTrip(scanner.nextInt());
                        break;
                    case 5:
                        //5. 여정 삭제
                        view.printTrip(tripService.getTripListFromJson());
                        System.out.println("삭제할 여정의 여행 ID를 입력하세요.");
                        int deleteTripId = scanner.nextInt();
                        view.printItineraries(
                            itineraryService.getItineraryListFromJson(deleteTripId));
                        System.out.println("삭제할 여정 ID를 입력하세요.");
                        int deleteItineraryId = scanner.nextInt();
                        view.deleteItinerary(deleteTripId, deleteItineraryId);
                        break;
                    case 6:
                        //6. 종료
                        System.out.println("프로그램을 종료합니다.");
                        isRunning = false;
                        scanner.close();
                        view.getScanner().close();
                        break;
                    default:
                        System.out.println("다시 선택해주세요.");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
