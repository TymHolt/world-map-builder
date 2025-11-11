package org.wmb;

public final class Log {

    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }

    public static void debug(String message) {
        if (Main.isDebugEnabled())
            System.out.println("[DEBUG] " + message);
    }

    public static void debug(Exception exception) {
        if (Main.isDebugEnabled())
            exception.printStackTrace();
    }
}
