package domain.trip.service;

import com.opencsv.CSVWriter;
import domain.itinerary.dto.ItineraryDTO;
import domain.trip.dto.TripDTO;
import domain.trip.exception.TripFileNotFoundException;
import global.dto.TripCsvDTO;
import global.util.CsvUtil;
import global.util.FileUtil;
import global.util.JsonUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TripService {

    private static final String JSONPATH = "src/main/resources/trip/json";
    private static final String CSVPATH = "src/main/resources/trip/csv";
    private static final FileUtil fileUtil = new FileUtil();

    /**
     * 메뉴리스트 1.여행정보등록시 호출되는 메서드 사용자로부터 정보 입력받아 json파일 및 csv파일에 저장하는 기능 구현
     */
    public void postTrip() {
        //사용자로부터 정보 입력받아 TripDto객체 생성
        TripDTO tripDTO = createTrip();

        //json파일, csv파일에 저장
        saveTripToJson(tripDTO);
        saveTripToCSV(tripDTO);
    }

    /**
     * 사용자로부터 여행정보를 입력받아 TripDTO 객체를 생성하는 메서드
     *
     * @return 입력받은 정보로 구성된 새 TripDTO 객체
     */
    private TripDTO createTrip() {
        try {
            //여행정보 입력받기
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("여행 이름: ");
            String tripName = br.readLine();

            LocalDate startDate, endDate;

            while (true) {
                System.out.print("여행 시작 날짜(yyyy-MM-dd): ");
                String startDateStr = br.readLine();
                while (!checkDateFormat(startDateStr)) {
                    System.out.println("날짜형식이 올바르지 않습니다. 다시 입력해주세요.");
                    System.out.print("여행 시작 날짜(yyyy-MM-dd): ");
                    startDateStr = br.readLine();
                }

                System.out.print("여행 종료 날짜(yyyy-MM-dd): ");
                String endDateStr = br.readLine();
                while (!checkDateFormat(endDateStr)) {
                    System.out.println("날짜형식이 올바르지 않습니다. 다시 입력해주세요.");
                    System.out.print("여행 종료 날짜(yyyy-MM-dd): ");
                    endDateStr = br.readLine();
                }

                //String->LocalDate
                startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE);
                endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE);

                if (!checkDateScope(startDate, endDate)) {
                    System.out.println("시작날짜가 종료날짜보다 작아야 합니다. 다시 입력해주세요.");
                } else {
                    break;
                }
            }

            //id 설정
            int tripId = TripIDGenerator.getId();

            //빈 여정리스트 객체 생성
            List<ItineraryDTO> itineraryDTOList = new ArrayList<>();

            // TripDTO 생성
            TripDTO trip = new TripDTO(tripId, tripName, startDate, endDate, itineraryDTOList);

            return trip;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * TripDTO 객체를 Json파일에 저장하는 메서드
     *
     * @param tripDTO: 저장할 정보를 담은 TripDTO객체
     */
    private void saveTripToJson(TripDTO tripDTO) {
        //TripDTO -> Json으로 변환
        String tripJson = JsonUtil.toJson(tripDTO);

        try {
            //json파일에 저장
            BufferedWriter bw = new BufferedWriter(
                new FileWriter(JSONPATH + "/trip_" + tripDTO.getId() + ".json")); //파일열기
            bw.write(tripJson); //json문자열 파일에 쓰기

            //파일 닫기
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TripDTO객체를 TripCSVDto로 변환 후 CSV파일에 저장하는 메서드
     *
     * @param tripDTO 여행정보를 담은 TripDTO객체
     */
    private void saveTripToCSV(TripDTO tripDTO) {
        TripCsvDTO tripCsvDTO = new TripCsvDTO(tripDTO.getId(), tripDTO.getName(),
            tripDTO.getStartDate(), tripDTO.getEndDate());
        String csvFilePath = CSVPATH + "/trip_" + tripDTO.getId() + ".csv";
        try (CSVWriter cw = new CSVWriter(new FileWriter(csvFilePath))) {
            List<TripCsvDTO> tripCsvDTOList = new ArrayList<>();
            tripCsvDTOList.add(tripCsvDTO);
            CsvUtil.toCsv(tripCsvDTOList, csvFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 날짜 유효성 검증하는 메서드
     *
     * @param checkDate 검증이 필요한 날짜 문자열
     * @return 날짜가 정해진 형식과 다르거나, 불가능한 숫자가 들어간 경우 false, 올바른 날짜인 경우 true
     */
    private static boolean checkDateFormat(String checkDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);//허술하게 체크하지 않겠다
            dateFormat.parse(checkDate); //오류 발생 -> 올바르지 않은 날짜
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 여행 시작 날짜와 종료 날짜의 범위 (시작 날짜 < 종료 날자) 확인하는 메서드
     *
     * @param startDate 여행 시작 날짜
     * @param endDate   여행 종료 날짜
     * @return 시작 날짜가 종료 날짜보다 작으면 true, 크면 false
     */
    private boolean checkDateScope(LocalDate startDate, LocalDate endDate) {
        int compare = startDate.compareTo(endDate);
        if (compare > 0) { //시작날짜 > 종료날짜
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
    public boolean deleteTripFromJson(int id) {
        Path filePath = Paths.get(JSONPATH + "/trip_" + id + ".json");
        try {
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    public boolean deleteTripFromCsv(int id) {
        Path filePath = Paths.get(CSVPATH + "/trip_" + id + ".csv");
        try {
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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