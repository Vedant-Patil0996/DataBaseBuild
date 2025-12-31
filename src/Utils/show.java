package Utils;

public class show {
    public static void showLoadingAnimation(String message) {
        String[] spinner = {"|", "/", "-", "\\"};

        System.out.print(message + "   ");

        try {
            for (int i = 0; i < 20; i++) {
                String frame = spinner[i % spinner.length];
                System.out.print("\r" + message + " " + CLIStyle.YELLOW + frame + CLIStyle.RESET);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            // Handle interruption
        }
        System.out.println("\r" + message + " " + CLIStyle.GREEN + "DONE" + CLIStyle.RESET);
    }
}
