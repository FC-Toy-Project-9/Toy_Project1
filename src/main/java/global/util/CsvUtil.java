package global.util;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CSV 파싱 유틸 클래스
 *
 * @author byungsang jo
 * @since 2023-09-06
 */
public class CsvUtil {

    /**
     * 객체를 직렬화 하여 csv파일을 만들어 filePath 위치에 저장하는 메서드
     *
     * @param objects  csv파일로 저장할 Object List
     * @param filePath 파일을 저장할 경로 ex) "./src/main/resources/trip/output.csv"
     * @throws IOException
     * @throws CsvRequiredFieldEmptyException
     * @throws CsvDataTypeMismatchException
     */
    public static <T> void toCsv(List<T> objects, String filePath)
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        CSVWriter writer = new CSVWriter(new FileWriter(filePath));

        // 클래스의 필드 배열을 가져옵니다.
        Field[] fields = objects.get(0).getClass().getDeclaredFields();

        // 필드를 @CsvBindByPosition의 position 값으로 정렬합니다.
        List<Field> sortedFields =
            Arrays.stream(fields)
                  .filter(field -> field.isAnnotationPresent(CsvBindByPosition.class))
                  .sorted(Comparator.comparingInt(
                      f -> f.getAnnotation(CsvBindByPosition.class).position()))
                  .collect(Collectors.toList());

        // 정렬된 필드를 기반으로 헤더를 생성합니다.
        String[] header = sortedFields.stream()
                                      .map(field -> field.getAnnotation(CsvBindByName.class)
                                                         .column())
                                      .toArray(String[]::new);
        // StatefulBeanToCsv 설정
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
            .withApplyQuotesToAll(false)
            .build();

        // header 저장
        writer.writeNext(header,false);

        // body 저장
        beanToCsv.write(objects);

        // 파일 닫기
        writer.close();
    }

    /**
     * csv파일을 읽어 객체로 역직렬화하는 메서드
     *
     * @param reader   csv 파일을 읽을 Reader
     * @param classOfT csv 파일을 객체화 시킬 클래스
     * @param <T>      csv 파일을 객체화 시킬 클래스 타입
     * @return 역직렬화 된 객체 리스트
     */
    public static <T> List<T> fromCsv(Reader reader, Class<T> classOfT) {
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
            .withType(classOfT)
            .withIgnoreLeadingWhiteSpace(true) // 헤더 및 데이터에서 앞쪽의 공백 문자를 무시하도록 설정
            .withSkipLines(1)
            .build();

        return csvToBean.parse();
    }
}
