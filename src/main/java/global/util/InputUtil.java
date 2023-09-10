package global.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 사용자로부터 문자열 입력을 받는 메서드
     *
     * @param prompt 사용자에게 보여질 입력 프롬프트
     * @return 입력된 문자열
     */
    public static String getInputString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * 사용자로부터 LocalDateTime 입력을 받는 메서드
     *
     * @param prompt 사용자에게 보여질 입력 프롬프트
     * @return 입력된 LocalDateTime 객체
     */
    public static LocalDateTime getInputLocalDateTime(String prompt) {
        while (true) {
            // 사용자에게 입력 프롬프트를 표시하고 형식 예시를 제공
            System.out.print(prompt + " (yyyy-MM-dd HH:mm): ");

            // 사용자 입력을 문자열로 받음
            String input = scanner.nextLine();

            // 입력된 문자열을 지정한 형식("yyyy-MM-dd HH:mm")으로 변환하기 위한 DateTimeFormatter 생성
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            try {
                // 입력된 문자열을 LocalDateTime으로 변환
                LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
                return dateTime; // 변환된 LocalDateTime 반환
            } catch (DateTimeParseException e) {
                // 형식에 맞지 않는 입력인 경우 사용자에게 오류 메시지 표시
                System.out.println("유효한 날짜 및 시간 형식이 아닙니다. 다시 입력해주세요.");
            }
        }
    }

    /**
     * 날짜정보를 문자열로 입력받고 유효한 날짜인지 검증 후 날짜타입으로 변환하는 메서드
     *
     * @param prompt 입력 전 출력되는 prompt메세지
     * @return 검증을 마친 LocalDate 타입의 날짜정보
     */
    public static LocalDate getInputLocalDate(String prompt){
        //날짜 검증 확인 변수 checkDate를 false로 초기화한다.
        boolean checkDate = false;
        String input ="";
        //checkDate이 true일 때까지 날짜 입력과 검증을 반복한다.
        while(!checkDate){
            //날짜정보를 입력받는다.
            System.out.print(prompt+" (yyyy-MM-dd): ");
            input = scanner.nextLine();
            // 정규 표현식을 사용하여 날짜 형식을 검증한다.
            Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
            Matcher matcher = pattern.matcher(input);
            if(!matcher.matches()){
                //주어진 정규표현식에 맞지 않는 경우, 다시 입력하여 검증한다.
                checkDate = false;
                System.out.println("날짜형식이 올바르지 않습니다. 다시 입력해주세요. (ex.2023-01-01)");
            }else{
                try {
                    //정규표현식에 맞는 경우, 유효한 날짜형식인지 검증한다.
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    //날짜를 엄격하게 검증하도록 설정한다.
                    dateFormat.setLenient(false);
                    //주어진 형식으로 파싱을 시도한다.
                    dateFormat.parse(input);
                    //파싱에 성공하면 검증을 완료한다.
                    checkDate = true;
                } catch (ParseException e) {
                    //파싱 오류인 경우, 다시 입력하여 검증한다.
                    checkDate = false;
                    System.out.println("날짜형식이 올바르지 않습니다. 다시 입력해주세요. (ex.2023-01-01)");
                }
            }
        }
        //입력받은 날짜정보를 문자열에서 LocalDate타입으로 변환한다.
        LocalDate inputLocalDate = LocalDate.parse(input, DateTimeFormatter.ISO_DATE);
        return inputLocalDate;
    }
}