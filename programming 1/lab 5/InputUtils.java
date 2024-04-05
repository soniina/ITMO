package utils;
import java.util.Scanner;

public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static String promptString(String prompt) {
        System.out.println(prompt);
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) throw new NullPointerException();
        return line;
    }

    public static Double promptDouble(String prompt) {
        System.out.println(prompt);
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) throw new NullPointerException();
        return Double.parseDouble(line);

    }

    public static Float promptFloat(String prompt) {
        System.out.println(prompt);
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) throw new NullPointerException();
        return Float.parseFloat(line);
    }

    public static Long promptLong(String prompt) {
        System.out.println(prompt);
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) throw new NullPointerException();
        return Long.parseLong(line);
    }

    public static Integer promptInt(String prompt) {
        System.out.println(prompt);
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) throw new NullPointerException();
        return Integer.parseInt(line);
    }
}
