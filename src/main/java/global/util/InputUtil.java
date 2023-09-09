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

    public static String getInputString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public static LocalDateTime getInputLocalDateTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            try {
                LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
                return dateTime;
            } catch (DateTimeParseException e) {
                System.out.println("유효한 날짜 및 시간 형식이 아닙니다. 다시 입력해주세요.");
            }
        }
    }

    public static LocalDate getInputLocalDate(String prompt){
        boolean checkDate = false;
        String input ="";
        while(!checkDate){
            System.out.print(prompt+" (yyyy-MM-dd): ");
            input = scanner.nextLine();
            // 정규 표현식을 사용하여 날짜 형식 검사
            Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
            Matcher matcher = pattern.matcher(input);
            if(!matcher.matches()){
                checkDate = false;
                System.out.println("날짜형식이 올바르지 않습니다. 다시 입력해주세요. (ex.2023-01-01)");
            }else{
                try {
                    // 날짜 형식이 "yyyy-MM-dd"와 일치하면 유효한 날짜로 간주
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setLenient(false); //날짜를 엄격하게 검증하도록 설정한다.
                    dateFormat.parse(input); //주어진 형식으로 파싱을 시도한다.
                    checkDate = true;
                } catch (ParseException e) {
                    checkDate = false;
                    System.out.println("날짜형식이 올바르지 않습니다. 다시 입력해주세요. (ex.2023-01-01)");
                }
            }
        }
        //날짜입력값을 문자열에서 LocalDate타입으로 변환한다.
        LocalDate inputLocalDate = LocalDate.parse(input, DateTimeFormatter.ISO_DATE);
        return inputLocalDate;
    }
}