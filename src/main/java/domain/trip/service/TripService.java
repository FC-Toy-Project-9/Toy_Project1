package domain.trip.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import global.dto.TripCsvDTO;
import global.util.CsvUtil;
import global.util.FileUtil;
import global.util.InputUtil;
import global.util.JsonUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripService {

    private static final String JSONPATH = "src/main/resources/trip/json";
    private static final String CSVPATH = "src/main/resources/trip/csv";
    private static final FileUtil fileUtil = new FileUtil();

    /**
     * 사용자로부터 여행 정보 입력받아 json파일 및 CSV파일에 저장하는 메서드
     */
    public void postTrip()
        throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        //사용자로부터 여행 정보 입력받아 TripDto 객체를 생성한다.
        TripDTO tripDTO = createTrip();
        //TripDTO를 통해 여행정보를 json파일, CSV파일에 저장한다.
        saveTripToJson(tripDTO);
        saveTripToCSV(tripDTO);
        System.out.println("여행정보가 등록되었습니다.");
    }

    /**
     * 사용자로부터 여행 정보를 입력받아 TripDTO 객체를 생성하는 메서드
     *
     * @return 입력받은 정보로 구성된 새 TripDTO 객체
     */
    private TripDTO createTrip() throws IOException {
        //여행 이름을 입력받는다.
        String tripName = InputUtil.getInputString("여행 이름");
        LocalDate startDate, endDate;
        //여행 시작 날짜와 여행 종료 날짜를 입력받고 유효한 날짜인지 검증한다.
        while (true) {
            startDate = InputUtil.getInputLocalDate("여행 시작 날짜");
            endDate = InputUtil.getInputLocalDate("여행 종료 날짜");
            if (!checkDateScope(startDate, endDate)) {
                System.out.println("시작날짜가 종료날짜보다 빨라야 합니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }
        //새로운 TripDTO 객체에 고유 식별자 ID값을 부여한다.
        int tripId = TripIDGenerator.getId();
        //빈 ItineraryDTO LIST 객체를 생성한다.
        List<ItineraryDTO> itineraryDTOList = new ArrayList<>();
        //여행 정보를 담은 TripDto 객체를 만들어 반환한다.
        return TripDTO.builder().id(tripId).name(tripName).startDate(startDate).endDate(endDate).itineraries(itineraryDTOList).build();
    }

    /**
     * TripDTO 객체를 json파일에 저장하는 메서드
     *
     * @param tripDTO 여행 정보를 담은 TripDTO 객체
     */
    private void saveTripToJson(TripDTO tripDTO) throws IOException {
        //TripDTO 객체를 json형식으로 바꿔 문자열로 저장한다.
        String tripJson = JsonUtil.toJson(tripDTO);
        //해당 내용을 json파일에 저장한다.
        BufferedWriter bw = new BufferedWriter(
            new FileWriter(JSONPATH + "/trip_" + tripDTO.getId() + ".json"));
        bw.write(tripJson);
        bw.close();
    }

    /**
     * TripDTO 객체를 TripCSVDTO 객체로 변환 후 CSV파일에 저장하는 메서드
     *
     * @param tripDTO 여행 정보를 담은 TripDTO 객체
     */
    private void saveTripToCSV(TripDTO tripDTO)
        throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        //새로운 여행 정보를 저장할 CSV파일 경로를 만든다.
        String csvFilePath = CSVPATH + "/trip_" + tripDTO.getId() + ".csv";
        //빈 TripCsvDTO List 객체를 만든 후, 여행 정보를 담은 TripDTO 객체의 내용을 CSV파일에 저장한다.
        List<TripCsvDTO> tripCsvDTOList = new ArrayList<>();
        tripCsvDTOList.add(
            TripCsvDTO.builder().tripId(tripDTO.getId()).tripName(tripDTO.getName()).startDate(
                tripDTO.getStartDate()).endDate(tripDTO.getEndDate()).build());
        CsvUtil.toCsv(tripCsvDTOList, csvFilePath);
    }

    /**
     * 여행 시작 날짜와 종료 날짜의 범위를 검증하는 메서드
     *
     * @param startDate 여행 시작 날짜
     * @param endDate   여행 종료 날짜
     * @return 시작 날짜가 종료 날짜보다 빠르면 true, 늦으면 false
     */
    private boolean checkDateScope(LocalDate startDate, LocalDate endDate) {
        int compare = startDate.compareTo(endDate);
        if (compare > 0) {
            return false;
        } else {
            return true;
        }
    }

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
            tripList.add(fileUtil.readJsonFile(JSONPATH + "/" + file.getName(), TripDTO.class));
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
        return fileUtil.readJsonFile(JSONPATH + "/trip_" + id + ".json", TripDTO.class);
    }

    /**
     * 특정 여행 기록 json 을 삭제 하는 메서드
     *
     * @param id 삭제할 특정 여행 기록 trip_id 값
     * @return src/main/resources/trip/json Directory 내 여행 기록 id에 해당 하는 json 삭제 성공 여부(삭제 성공: true,
     * 삭제 실패: false)
     */
    public boolean deleteTripFromJson(int id) throws IOException {
        Path filePath = Paths.get(JSONPATH + "/trip_" + id + ".json");
        return Files.deleteIfExists(filePath);
    }

    /**
     * csv 에서 전체 여행 기록을 조회 하는 메서드
     *
     * @return src/main/resources/trip/csv Directory 내 csv 에서 읽어온 TripDTO List 객체
     */
    public List<TripDTO> getTripListFromCsv() {
        List<TripDTO> tripList = new ArrayList<>();
        File[] files = new File(CSVPATH).listFiles();
        if (files == null) {
            throw new TripFileNotFoundException();
        }
        for (File file : files) {
            if (!file.isFile() || !file.canRead()) {
                continue;
            }
            List<TripCsvDTO> csv = fileUtil.readCsvFile(CSVPATH + "/" + file.getName());
            List<ItineraryDTO> itineraries = getItineraries(csv);
            tripList.add(TripDTO.builder().id(csv.get(0).getTripId()).name(csv.get(0).getTripName())
                .startDate(csv.get(0).getStartDate()).endDate(csv.get(0).getEndDate())
                .itineraries(itineraries).build());
        }
        return tripList;
    }

    /**
     * csv 에서 특정 여행 기록을 조회 하는 메서드
     *
     * @param id 조회할 특정 여행 기록 trip_id 값
     * @return src/main/resources/trip/csv Directory 내 여행 기록 id에 해당 하는 csv 에서 읽어온 TripDTO 객체
     */
    public TripDTO getTripFromCsv(int id) {
        List<TripCsvDTO> csv = fileUtil.readCsvFile(CSVPATH + "/trip_" + id + ".csv");
        List<ItineraryDTO> itineraries = getItineraries(csv);
        return TripDTO.builder().id(csv.get(0).getTripId()).name(csv.get(0).getTripName())
            .startDate(csv.get(0).getStartDate()).endDate(csv.get(0).getEndDate())
            .itineraries(itineraries).build();
    }

    /**
     * 특정 여행 기록 csv 을 삭제 하는 메서드
     *
     * @param id 삭제할 특정 여행 기록 trip_id 값
     * @return src/main/resources/trip/csv Directory 내 여행 기록 id에 해당 하는 csv 삭제 성공 여부(삭제 성공: true, 삭제
     * 실패: false)
     */
    public boolean deleteTripFromCsv(int id) throws IOException {
        Path filePath = Paths.get(CSVPATH + "/trip_" + id + ".csv");
        return Files.deleteIfExists(filePath);
    }

    /**
     * TripCsvDTO 에서 Itinerary 리스트를 추출하는 메서드
     *
     * @param csv
     * @return TripCsvDTO 에서 추출한 Itinerary 리스트
     */
    private List<ItineraryDTO> getItineraries(List<TripCsvDTO> csv) {
        List<ItineraryDTO> itineraries = new ArrayList<>();
        for (TripCsvDTO tripCsvDTO : csv) {
            itineraries.add(tripCsvDTO.toItineraryDTO());
        }
        return itineraries;
    }
}
