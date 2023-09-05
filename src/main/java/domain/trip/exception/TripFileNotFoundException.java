package domain.trip.exception;

public class TripFileNotFoundException extends RuntimeException {

    public TripFileNotFoundException() {
        super("여행 파일을 찾을 수 없습니다.");
    }
}
