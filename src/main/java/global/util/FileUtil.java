package global.util;

import domain.trip.exception.TripFileNotFoundException;
import global.dto.TripCsvDTO;

import java.io.*;
import java.util.List;

public class FileUtil {

    /**
     * 특정 path 에서 json 파일을 읽어와 개체로 반환 하는 메서드
     *
     * @param path     파일을 읽어올 json 파일 경로
     * @param classOfT 변환할 Object type의 class
     * @return json 파일에서 읽고 파라미터로 받은 객체 타입으로 변환한 객체
     */
    public <T> T readJsonFile(String path, Class<T> classOfT) {
        T t = null;
        File file = new File(path);
        if (!file.isFile() || !file.canRead()) {
            throw new TripFileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            t = JsonUtil.fromJson(br, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t == null) {
            throw new TripFileNotFoundException();
        }
        return t;
    }

    /**
     * 특정 path 에서 csv 파일을 읽어와 개체로 반환 하는 메서드
     *
     * @param path     파일을 읽어올 csv 파일 경로
     * @return json 파일에서 읽고 파라미터로 받은 객체 타입으로 변환한 객체
     */
    public List<TripCsvDTO> readCsvFile(String path) {
        List<TripCsvDTO> t = null;
        File file = new File(path);
        if (!file.isFile() || !file.canRead()) {
            throw new TripFileNotFoundException();
        }
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