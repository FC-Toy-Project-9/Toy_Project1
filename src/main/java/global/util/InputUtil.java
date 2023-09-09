package global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
}