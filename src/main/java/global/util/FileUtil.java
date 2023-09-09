package global.util;

import global.dto.TripCsvDTO;
import global.exception.TripFileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * 파라미터로 받은 경로에서 파일을 읽고 적절한 객체 타입으로 변환하여 반환하는 기능을 가진 Util 클래스
 */
public class FileUtil {

    /**
     * 특정 경로에서 JSON 파일을 읽어와 객체로 반환 하는 메서드
     *
     * @param path     파일을 읽어올 JSON 파일 경로
     * @param classOfT 변환할 객체 타입
     * @return JSON 파일에서 읽고 파라미터로 받은 객체 타입으로 변환한 객체
     */
    public <T> T readJsonFile(String path, Class<T> classOfT) {
        T t = null;
        File file = new File(path);
        // 파일이 없는 경우 TripFileNotFoundException을 던진다.
        if (!file.isFile() || !file.canRead()) {
            throw new TripFileNotFoundException();
        }
        // BufferedReader로 JSON 파일을 읽고 객체에 저장한다.
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            t = JsonUtil.fromJson(br, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 특정 경로에서 CSV 파일을 읽어와 TripCsvDto 리스트로 반환 하는 메서드
     *
     * @param path 파일을 읽어올 CSV 파일 경로
     * @return CSV 파일에서 읽은 정보를 TripCsvDto로 변환한 객체
     */
    public List<TripCsvDTO> readCsvFile(String path) {
        List<TripCsvDTO> t = null;
        File file = new File(path);
        // 파일이 없는 경우 TripFileNotFoundException을 던진다.
        if (!file.isFile() || !file.canRead()) {
            throw new TripFileNotFoundException();
        }
        // BufferedReader로 CSV 파일을 읽고 객체에 저장한다.
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            t = CsvUtil.fromCsv(br, TripCsvDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t == null) {
            throw new TripFileNotFoundException();
        }
        return t;
    }
}
