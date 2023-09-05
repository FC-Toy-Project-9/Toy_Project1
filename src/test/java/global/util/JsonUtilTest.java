package global.util;

import com.google.gson.annotations.SerializedName;
import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
/**
 * JSON 파싱 유틸 테스트 클래스
 *
 * @author byungsang jo
 * @since 2023-09-05
 */
class JsonUtilTest {


    /**
     * TC1: 직렬화 했을 경우 문자열의 길이가 0보다 커야 한다.
     * public static String toJson(Object obj): Object -> String
     */
    @Test
    void toJsonTC1() {
        TestTrip vacation =
            new TestTrip(1L, "Vacation",
                LocalDate.of(2023,9,9),
                LocalDate.of(2023,10,9));

        String jsonStr = JsonUtil.toJson(vacation);

//        System.out.println(jsonStr.length());
        Assertions.assertEquals(true, jsonStr.length()>0);
    }

    /**
     * TC2: 직렬화 했을 경우 문자열이 비어있으면 안된다.
     * public static String toJson(Object obj): Object -> String
     */
    @Test
    void toJsonTC2() {
        TestTrip vacation =
            new TestTrip(1L, "Vacation",
                LocalDate.of(2023,9,9),
                LocalDate.of(2023,10,9));

        String jsonStr = JsonUtil.toJson(vacation);

//        System.out.println(jsonStr.length());
        Assertions.assertEquals(false, jsonStr.isBlank());
    }

    /**
     * TC3: 직렬화 했을 경우 문자열은 null이면 안된다.
     * public static String toJson(Object obj): Object -> String
     */
    @Test
    void toJsonTC3() {
        TestTrip vacation =
            new TestTrip(1L, "Vacation",
                LocalDate.of(2023,9,9),
                LocalDate.of(2023,10,9));

        String jsonStr = JsonUtil.toJson(vacation);

//        System.out.println(jsonStr.length());
        Assertions.assertEquals(true, jsonStr!=null);
    }

    /**
     * TC1: 역직렬화 했을 경우 json 문자열에 있는 trip_id 값과 인스턴스의 tripId 필드의 값이 같아야 한다.
     * public static <T> T fromJson(String json, Class<T> classOfT)
     */
    @Test
    void fromJsonTC1() {
        String jsonStr = "{\n"
            + "\"trip_id\": 1,\n"
            + "\"trip_name\": \"Family Vacation\",\n"
            + "\"start_date\": \"2023-07-15\",\n"
            + "\"end_date\": \"2023-07-20\"}";

        TestTrip testTrip = JsonUtil.fromJson(jsonStr,TestTrip.class);

        Assertions.assertEquals(1, testTrip.getTripId());
    }

    /**
     * TC2: 역직렬화 했을 경우 json 문자열에 있는 trip_name 값과 인스턴스의 tripName 필드의 값이 같아야 한다.
     * public static <T> T fromJson(String json, Class<T> classOfT)
     */
    @Test
    void fromJsonTC2() {
        String jsonStr = "{\n"
            + "\"trip_id\": 1,\n"
            + "\"trip_name\": \"Family Vacation\",\n"
            + "\"start_date\": \"2023-07-15\",\n"
            + "\"end_date\": \"2023-07-20\"}";

        TestTrip testTrip = JsonUtil.fromJson(jsonStr,TestTrip.class);

        Assertions.assertEquals("Family Vacation", testTrip.getTripName());
    }
    /**
     * TC3: 역직렬화 했을 경우 json 문자열에 있는 start_date 값과 인스턴스의 startDate 필드의 값이 같아야 한다.
     * public static <T> T fromJson(String json, Class<T> classOfT)
     */
    @Test
    void fromJsonTC3() {
        String jsonStr = "{\n"
            + "\"trip_id\": 1,\n"
            + "\"trip_name\": \"Family Vacation\",\n"
            + "\"start_date\": \"2023-07-15\",\n"
            + "\"end_date\": \"2023-07-20\"}";

        TestTrip testTrip = JsonUtil.fromJson(jsonStr,TestTrip.class);

        Assertions.assertEquals(LocalDate.of(2023,7,15), testTrip.getStartDate());
    }
    /**
     * TC4: 역직렬화 했을 경우 json 문자열에 있는 end_date 값과 인스턴스의 endDate 필드의 값이 같아야 한다.
     * public static <T> T fromJson(String json, Class<T> classOfT)
     */
    @Test
    void fromJsonTC4() {
        String jsonStr = "{\n"
            + "\"trip_id\": 1,\n"
            + "\"trip_name\": \"Family Vacation\",\n"
            + "\"start_date\": \"2023-07-15\",\n"
            + "\"end_date\": \"2023-07-20\"}";

        TestTrip testTrip = JsonUtil.fromJson(jsonStr,TestTrip.class);

        Assertions.assertEquals(LocalDate.of(2023,7,20), testTrip.getEndDate());
    }

    /**
     * TC5: 역직렬화 했을 경우 BufferedReader로 읽은 json의 end_date 값과 인스턴스의 endDate 필드의 값이 같아야 한다.
     * public static <T> T fromJson(BufferedReader reader, Class<T> classOfT)
     */
    @Test
    void fromJsonTC5() {
        String jsonStr = "{\n"
            + "\"trip_id\": 1,\n"
            + "\"trip_name\": \"Family Vacation\",\n"
            + "\"start_date\": \"2023-07-15\",\n"
            + "\"end_date\": \"2023-07-20\"}";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(jsonStr));

        TestTrip testTrip = JsonUtil.fromJson(bufferedReader,TestTrip.class);

        Assertions.assertEquals(LocalDate.of(2023,7,20), testTrip.getEndDate());
    }


    /**
     * 테스트를 위해 임의로 만든 테스트 클래스
     */
    class TestTrip{
        @SerializedName("trip_id")
        private Long tripId;
        @SerializedName("trip_name")
        private String tripName;
        @SerializedName("start_date")
        private LocalDate startDate;
        @SerializedName("end_date")
        private LocalDate endDate;

        public TestTrip(Long tripId, String tripName, LocalDate startDate, LocalDate endDate) {
            this.tripId = tripId;
            this.tripName = tripName;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Long getTripId() {
            return tripId;
        }

        public String getTripName() {
            return tripName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        @Override
        public String toString() {
            return "TestTrip{" +
                "tripId=" + tripId +
                ", tripName='" + tripName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
        }
    }
}