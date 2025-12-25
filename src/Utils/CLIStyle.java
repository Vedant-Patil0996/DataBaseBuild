package Utils;

public class CLIStyle {
    public static final String RESET = "\033[0m";

    public static final String RED = "\033[0;31m";     // For Errors
    public static final String GREEN = "\033[0;32m";   // For Success
    public static final String YELLOW = "\033[0;33m";  // For Warnings
    public static final String BLUE = "\033[0;34m";    // For Information
    public static final String CYAN = "\033[0;36m";    // For Borders/Table Lines

    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String WHITE_BOLD = "\033[1;37m";
    public static void typeWriter(String text, int speed) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) { }
        }
        System.out.println();
    }
}
