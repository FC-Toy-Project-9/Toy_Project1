package global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getInputString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public static LocalDateTime getInputLocalDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String input = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try {
                LocalDate date = LocalDate.parse(input, formatter);
                LocalDateTime dateTime = date.atStartOfDay();
                return dateTime;
            } catch (DateTimeParseException e) {
                System.out.println("유효한 날짜 형식이 아닙니다. 다시 입력해주세요.");
            }
        }
    }

}
