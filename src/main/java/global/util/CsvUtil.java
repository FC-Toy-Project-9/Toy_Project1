package global.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

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
     * @param objects csv파일로 저장할 Object List
     * @param filePath 파일을 저장할 경로 ex) "./src/main/resources/trip/output.csv"
     * @throws IOException
     * @throws CsvRequiredFieldEmptyException
     * @throws CsvDataTypeMismatchException
     */
    public static <T> void toCsv(List<T> objects, String filePath)
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        beanToCsv.write(objects);
        writer.close();
    }

    /**
     * csv파일을 읽어 객체로 역직렬화하는 메서드
     *
     * @param reader csv 파일을 읽을 Reader
     * @param classOfT csv 파일을 객체화 시킬 클래스
     * @param <T> csv 파일을 객체화 시킬 클래스 타입
     * @return 역직렬화 된 객체 리스트
     */
    public static <T> List<T> fromCsv(Reader reader, Class<T> classOfT) {
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
            .withType(classOfT)
            .withIgnoreLeadingWhiteSpace(true) // 헤더 및 데이터에서 앞쪽의 공백 문자를 무시하도록 설정
            .build();

        return csvToBean.parse();
    }
}
