package global.util;

import com.google.gson.*;
import com.google.gson.stream.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * JSON 파싱 유틸 클래스
 *
 * @author byungsang jo
 * @since 2023-09-04
 */
public class JsonUtil {

    /**
     * LocalDate Type의 String Format
     */
    private static String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * LocalTime Type의 String Format
     */
    private static String PATTERN_TIME = "HH:mm:ss";

    /**
     * LocalDateTime Type의 String Format
     */
    private static String PATTERN_DATETIME = String.format("%s'T'%s", PATTERN_DATE, PATTERN_TIME);

    /**
     * 인스턴스 하나를 생성 해 놓고 필요할 때마다 사용하는 것이 편리하여 static final로 선언.
     */
    private static final Gson gson =
            new GsonBuilder().setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setDateFormat(PATTERN_DATETIME)
                    .registerTypeAdapter(LocalDateTime.class,
                            new LocalDateTimeAdapter().nullSafe())
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter().nullSafe())
                    .create();

    /**
     * 객체를 Json 문자열로 변환
     *
     * @param obj 직렬화할 object
     * @return 직렬화된 json 문자열
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Json 문자열을 객체로 변환
     *
     * @param json     json 문자열
     * @param classOfT 변환할 Object type의 class
     * @param <T> 변환할 타입
     * @return json 문자열을 객체로 변환한 T 타입의 인스턴스
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * Reader를 이용하여 Json 문자열을 읽어 객체로 반환
     *
     * @param reader 문자열 Reader
     * @param classOfT 변환할 Object type의 class
     * @param <T> 변환할 타입
     * @return json 문자열을 객체로 변환한 T 타입의 인스턴스
     */
    public static <T> T fromJson(Reader reader, Class<T> classOfT) {
        StringBuilder sb = new StringBuilder();
        String line;

        try (reader) {
            while ((line = ((BufferedReader) reader).readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gson.fromJson(sb.toString(), classOfT);
    }

    /**
     * LocalDateTime 타입으로 직렬화, 역직렬화 시 TypeAdapter 역할을 하는 클래스
     */
    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value != null) {
                out.value(value.format(format));
            }
        }
        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return LocalDateTime.parse(in.nextString(), format);
        }
    }

    /**
     * LocalDate 타입으로 직렬화, 역직렬화 시 TypeAdapter 역할을 하는 클래스
     */
    static class LocalDateAdapter extends TypeAdapter<LocalDate> {

        DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_DATE);

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value.format(format));
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString(), format);
        }
    }

    /**
     * LocalTime 타입으로 직렬화, 역직렬화 시 TypeAdapter 역할을 하는 클래스
     */
    static class LocalTimeAdapter extends TypeAdapter<LocalTime> {

        DateTimeFormatter format = DateTimeFormatter.ofPattern(PATTERN_TIME);

        @Override
        public void write(JsonWriter out, LocalTime value) throws IOException {
            out.value(value.format(format));
        }

        @Override
        public LocalTime read(JsonReader in) throws IOException {
            return LocalTime.parse(in.nextString(), format);
        }
    }
}