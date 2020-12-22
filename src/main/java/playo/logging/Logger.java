package playo.logging;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private final String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    public void error(String msg) {
        error(tag, msg);
    }

    public void debug(String msg) {
        debug(tag, msg);
    }

    public void info(String msg) {
        info(tag, msg);
    }

    public static void error(String tag, String msg) {
        System.out.print(ANSI_RED);
        System.out.print("[ERROR] ");
        System.out.print(tag);
        System.out.print(": ");
        System.out.println(msg);
        System.out.print(ANSI_RESET);

    }

    public static void debug(String tag, String msg) {
        System.out.print(ANSI_YELLOW);
        System.out.print("[DEBUG] ");
        System.out.print(tag);
        System.out.print(": ");
        System.out.println(msg);
        System.out.print(ANSI_RESET);

    }

    public static void info(String tag, String msg) {
        System.out.print("[INFO] ");
        System.out.print(tag);
        System.out.print(": ");
        System.out.println(msg);
    }
}
