package org.wmb;

public final class Log {

    public static void info(String message) {
        System.out.print("[INFO] ");
        System.out.println(message);
    }

    public static void error(String message) {
        System.err.print("[ERROR] ");
        System.err.println(message);
    }

    public static void debug(String message) {
        if (Main.isDebugEnabled()) {
            System.out.print("[DEBUG] ");
            System.out.println(message);
        }
    }

    public static void debug(Exception exception) {
        if (Main.isDebugEnabled())
            exception.printStackTrace();
    }
}
