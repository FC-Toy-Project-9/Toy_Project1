package global.exception;

/**
 * 여행 파일을 찾을 수 없는 경우 던질 예외 클래스
 */
public class TripFileNotFoundException extends RuntimeException {

    public TripFileNotFoundException() {
        super("여행 파일을 찾을 수 없습니다.");
    }
}
