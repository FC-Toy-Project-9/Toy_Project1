package domain.trip.service;

import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import global.util.JsonUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TripService {

    private static final String JSONPATH = "src/main/resources/trip/json";

    /**
     * json 에서 전체 여행 기록을 조회 하는 메서드
     *
     * @return src/main/resources/trip/json Directory 내 json 에서 읽어온 TripDTO List 객체
     */
    public List<TripDTO> getTripListFromJson() {
        List<TripDTO> tripList = new ArrayList<>();
        File[] files = new File(JSONPATH).listFiles();
        if (files == null) {
            throw new TripFileNotFoundException();
        }
        for (File file : files) {
            if (!file.isFile() || !file.canRead()) {
                continue;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file));) {
                TripDTO trip = JsonUtil.fromJson(br, TripDTO.class);
                tripList.add(trip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tripList;
    }

    /**
     * json 에서 특정 여행 기록을 조회 하는 메서드
     *
     * @param id 조회할 특정 여행 기록 trip_id 값
     * @return src/main/resources/trip/json Directory 내 여행 기록 id에 해당 하는 json 에서 읽어온 TripDTO 객체
     */
    public TripDTO getTripFromJson(int id) {
        TripDTO trip = null;
        File file = new File(JSONPATH + "/trip_" + id + ".json");
        if (!file.isFile() || !file.canRead()) {
            throw new TripFileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            trip = JsonUtil.fromJson(br, TripDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (trip == null) {
            throw new TripFileNotFoundException();
        }
        return trip;
    }

    /**
     * 특정 여행 기록 json 을 삭제 하는 메서드
     *
     * @param id 삭제할 특정 여행 기록 trip_id 값
     * @return src/main/resources/trip/json Directory 내 여행 기록 id에 해당 하는 json 삭제 성공 여부(삭제 성공: true,
     * 삭제 실패: false)
     */
    public boolean deleteTripFromJson(int id) {
        Path filePath = Paths.get(JSONPATH + "/trip_" + id + ".json");
        try {
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // TODO csv 파일에서 전체 여행 기록 조회 메서드 구현
    // TODO csv 파일에서 특정 여행 기록 조회 메서드 구현
    // TODO 특정 여행 기록 csv 삭제 메서드 구현
}
