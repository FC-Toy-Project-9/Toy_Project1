package domain.itinerary.exception;

public class ItineraryNotFoundException extends Throwable {

    public ItineraryNotFoundException() {
        System.out.println("해당 여정을 찾을 수 없습니다.");
    }

}
