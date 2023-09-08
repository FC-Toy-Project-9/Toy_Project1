package domain.itinerary.exception;

public class ItineraryNotFoundException extends Exception {

    public ItineraryNotFoundException() {
        super("해당 여정을 찾을 수 없습니다.");
    }

}
