package global.util;

import com.opencsv.bean.CsvBindByName;
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
                "name,age,birth_date,birth_time,birth_date_time\n" +
                    "John,30,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Jane,25,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Michael,35,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Emily,28,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "David,40,2020-08-07,10:10:10,2020-08-07T10:10:10\n";
            List<Person> people = CsvUtil.fromCsv(new StringReader(csv), Person.class);
            String filePath = "./src/test/java/global/util/a.csv";

            try {
                // CSV 파일 저장
                CsvUtil.toCsv(people,filePath);

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
                    "name,age,birth_date,birth_time,birth_date_time\n" +
                    "John,30,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Jane,25,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Michael,35,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Emily,28,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "David,40,2020-08-07,10:10:10,2020-08-07T10:10:10\n";

            List<Person> people = CsvUtil.fromCsv(new StringReader(csv), Person.class);

            Assertions.assertEquals(true, people.get(0).getName() != null);
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 숫자 필드는 0이면 안된다.")
        void numberTypeField_isNotZero() {
            String csv =
                    "name,age,birth_date,birth_time,birth_date_time\n" +
                    "John,30,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Jane,25,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Michael,35,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Emily,28,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "David,40,2020-08-07,10:10:10,2020-08-07T10:10:10\n";

            List<Person> people = CsvUtil.fromCsv(new StringReader(csv), Person.class);

            Assertions.assertEquals(true, people.get(0).getAge() != 0);
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 LocalDate 타입의 값은 input 값과 동일하다")
        void checkLocalDateTypeField() {
            String csv =
                    "name,age,birth_date,birth_time,birth_date_time\n" +
                    "John,30,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Jane,25,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Michael,35,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Emily,28,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "David,40,2020-08-07,10:10:10,2020-08-07T10:10:10\n";

            List<Person> people = CsvUtil.fromCsv(new StringReader(csv), Person.class);

            Assertions.assertEquals(
                LocalDate.of(2020,8,7), people.get(0).getBirthDate());
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 LocalTime 타입의 값은 input 값과 동일하다.")
        void checkLocalTimeTypeField() {
            String csv =
                "name,age,birth_date,birth_time,birth_date_time\n" +
                    "John,30,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Jane,25,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Michael,35,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Emily,28,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "David,40,2020-08-07,10:10:10,2020-08-07T10:10:10\n";

            List<Person> people = CsvUtil.fromCsv(new StringReader(csv), Person.class);

            Assertions.assertEquals(
                LocalTime.of(10,10,10), people.get(0).getBirthTime());
        }

        @Test
        @DisplayName("csv를 파싱 하여 객체로 변환했을 경우 LocalDateTime 타입의 값은 input 값과 동일하다.")
        void checkLocalDateTimeTypeField() {
            String csv =
                "name,age,birth_date,birth_time,birth_date_time\n" +
                    "John,30,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Jane,25,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Michael,35,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "Emily,28,2020-08-07,10:10:10,2020-08-07T10:10:10\n" +
                    "David,40,2020-08-07,10:10:10,2020-08-07T10:10:10\n";

            List<Person> people = CsvUtil.fromCsv(new StringReader(csv), Person.class);

            Assertions.assertEquals(
                LocalDateTime.of(2020,8,7,10,10,10),
                people.get(0).getBirthDateTime());
        }
    }

    static class Person {

        @CsvBindByName(column = "name", required = true)
        private String name;
        @CsvBindByName(column = "age", required = true)
        private int age;
        @CsvBindByName(column = "birth_date", required = true)
        @CsvDate("yyyy-MM-dd")
        private LocalDate birthDate;
        @CsvBindByName(column = "birth_time", required = true)
        @CsvDate("HH:mm:ss")
        private LocalTime birthTime;
        @CsvBindByName(column = "birth_date_time", required = true)
        @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime birthDateTime;

        public Person() {
        }

        public Person(String name, int age, LocalDate birthDate, LocalTime birthTime,
            LocalDateTime birthDateTime) {
            this.name = name;
            this.age = age;
            this.birthDate = birthDate;
            this.birthTime = birthTime;
            this.birthDateTime = birthDateTime;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public LocalTime getBirthTime() {
            return birthTime;
        }

        public LocalDateTime getBirthDateTime() {
            return birthDateTime;
        }

        @Override
        public String toString() {
            return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthDate=" + birthDate +
                ", birthTime=" + birthTime +
                ", birthDateTime=" + birthDateTime +
                '}';
        }
    }
}