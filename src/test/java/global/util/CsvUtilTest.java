package global.util;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import java.io.File;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * CSV 파싱 유틸 테스트 클래스
 *
 * @author byungsang jo
 * @since 2023-09-06
 */
class CsvUtilTest {

    @Nested
    @DisplayName("toCsv()는")
    class toCsv {

        @Test
        @DisplayName("객체를 받아서 csv파일로 저장할 수 있다.")
        void ObjectToSave() {
            String csv =
                "trip_id,trip_name,start_date,end_date,time,datetime\n" +
                    "1,Family Vacation1,2023-07-15,2023-07-20,08:00:00,2023-07-20T08:00:00\n" +
                    "2,Family Vacation2,2023-07-16,2023-07-21,09:00:00,2023-07-21T09:00:00\n" +
                    "3,Family Vacation3,2023-07-17,2023-07-22,15:00:00,2023-07-22T15:00:00\n" +
                    "4,Family Vacation4,2023-07-18,2023-07-23,16:00:00,2023-07-15T16:00:00\n";
            List<TripTestDTO> trips = CsvUtil.fromCsv(new StringReader(csv), TripTestDTO.class);
            String filePath = "src/test/java/global/util/a.csv";

            try {
                // CSV 파일 저장
                CsvUtil.toCsv(trips, filePath);

                // 파일이 정상적으로 생성되었는지 확인
                Assertions.assertTrue(new File(filePath).exists());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    @DisplayName("fromCSV()는")
    class fromCsv {

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 문자열 필드는 null이면 안된다.")
        void stringTypeField_isNotNull() {
            String csv =
                "trip_id,trip_name,start_date,end_date,time,datetime\n" +
                    "1,Family Vacation1,2023-07-15,2023-07-20,08:00:00,2023-07-20T08:00:00\n" +
                    "2,Family Vacation2,2023-07-16,2023-07-21,09:00:00,2023-07-21T09:00:00\n" +
                    "3,Family Vacation3,2023-07-17,2023-07-22,15:00:00,2023-07-22T15:00:00\n" +
                    "4,Family Vacation4,2023-07-18,2023-07-23,16:00:00,2023-07-15T16:00:00\n";

            List<TripTestDTO> trips = CsvUtil.fromCsv(new StringReader(csv), TripTestDTO.class);

            Assertions.assertEquals(true, trips.get(0).getTripName() != null);
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 숫자 필드는 0이면 안된다.")
        void numberTypeField_isNotZero() {
            String csv =
                "trip_id,trip_name,start_date,end_date,time,datetime\n" +
                    "1,Family Vacation1,2023-07-15,2023-07-20,08:00:00,2023-07-20T08:00:00\n" +
                    "2,Family Vacation2,2023-07-16,2023-07-21,09:00:00,2023-07-21T09:00:00\n" +
                    "3,Family Vacation3,2023-07-17,2023-07-22,15:00:00,2023-07-22T15:00:00\n" +
                    "4,Family Vacation4,2023-07-18,2023-07-23,16:00:00,2023-07-15T16:00:00\n";

            List<TripTestDTO> trips = CsvUtil.fromCsv(new StringReader(csv), TripTestDTO.class);

            Assertions.assertEquals(true, trips.get(0).getTripId() != 0);
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 LocalDate 타입의 값은 input 값과 동일하다")
        void checkLocalDateTypeField() {
            String csv =
                "trip_id,trip_name,start_date,end_date,time,datetime\n" +
                    "1,Family Vacation1,2023-07-15,2023-07-20,08:00:00,2023-07-20T08:00:00\n" +
                    "2,Family Vacation2,2023-07-16,2023-07-21,09:00:00,2023-07-21T09:00:00\n" +
                    "3,Family Vacation3,2023-07-17,2023-07-22,15:00:00,2023-07-22T15:00:00\n" +
                    "4,Family Vacation4,2023-07-18,2023-07-23,16:00:00,2023-07-15T16:00:00\n";

            List<TripTestDTO> trips = CsvUtil.fromCsv(new StringReader(csv), TripTestDTO.class);

            Assertions.assertEquals(
                LocalDate.of(2023, 7, 20), trips.get(0).getEndDate());
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 LocalTime 타입의 값은 input 값과 동일하다.")
        void checkLocalTimeTypeField() {
            String csv =
                "trip_id,trip_name,start_date,end_date,time,datetime\n" +
                    "1,Family Vacation1,2023-07-15,2023-07-20,08:00:00,2023-07-20T08:00:00\n" +
                    "2,Family Vacation2,2023-07-16,2023-07-21,09:00:00,2023-07-21T09:00:00\n" +
                    "3,Family Vacation3,2023-07-17,2023-07-22,15:00:00,2023-07-22T15:00:00\n" +
                    "4,Family Vacation4,2023-07-18,2023-07-23,16:00:00,2023-07-15T16:00:00\n";

            List<TripTestDTO> trips = CsvUtil.fromCsv(new StringReader(csv), TripTestDTO.class);

            Assertions.assertEquals(
                LocalTime.of(8, 0, 0), trips.get(0).getTime());
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 LocalDateTime 타입의 값은 input 값과 동일하다.")
        void checkLocalDateTimeTypeField() {
            String csv =
                "trip_id,trip_name,start_date,end_date,time,datetime\n" +
                    "1,Family Vacation1,2023-07-15,2023-07-20,08:00:00,2023-07-20T08:00:00\n" +
                    "2,Family Vacation2,2023-07-16,2023-07-21,09:00:00,2023-07-21T09:00:00\n" +
                    "3,Family Vacation3,2023-07-17,2023-07-22,15:00:00,2023-07-22T15:00:00\n" +
                    "4,Family Vacation4,2023-07-18,2023-07-23,16:00:00,2023-07-15T16:00:00\n";

            List<TripTestDTO> trips = CsvUtil.fromCsv(new StringReader(csv), TripTestDTO.class);

            Assertions.assertEquals(
                LocalDateTime.of(2023, 7, 20, 8, 0, 0),
                trips.get(0).getDatetime());
        }
    }


    public static class TripTestDTO {

        @CsvBindByName(column = "trip_id", required = true)
        @CsvBindByPosition(position = 0)
        private Long tripId;
        @CsvBindByName(column = "trip_name", required = true)
        @CsvBindByPosition(position = 1)
        private String tripName;
        @CsvBindByName(column = "start_date", required = true)
        @CsvBindByPosition(position = 2)
        @CsvDate("yyyy-MM-dd")
        private LocalDate startDate;
        @CsvBindByName(column = "end_date", required = true)
        @CsvBindByPosition(position = 3)
        @CsvDate("yyyy-MM-dd")
        private LocalDate endDate;
        @CsvBindByName(column = "time", required = true)
        @CsvBindByPosition(position = 4)
        @CsvDate("HH:mm:ss")
        private LocalTime time;
        @CsvBindByName(column = "datetime", required = true)
        @CsvBindByPosition(position = 5)
        @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime datetime;

        public TripTestDTO() {
        }

        public TripTestDTO(Long tripId, String tripName, LocalDate startDate, LocalDate endDate,
            LocalTime time, LocalDateTime datetime) {
            this.tripId = tripId;
            this.tripName = tripName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.datetime = datetime;
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

        public LocalTime getTime() {
            return time;
        }

        public LocalDateTime getDatetime() {
            return datetime;
        }

        @Override
        public String toString() {
            return "TripTestDTO{" +
                "tripId=" + tripId +
                ", tripName='" + tripName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", time=" + time +
                ", datetime=" + datetime +
                '}';
        }
    }
}