package org.wmb;

public final class Log {

    public static void info(String message) {
        if (message == null)
            message = "null";

        final String[] lines = message.split("\n");
        for (String line : lines) {
            System.out.print("[INFO] ");
            System.out.println(line);
        }
    }

    public static void error(String message) {
        if (message == null)
            message = "null";

        final String[] lines = message.split("\n");
        for (String line : lines) {
            System.err.print("[ERROR] ");
            System.err.println(line);
        }
    }

    public static void debug(String message) {
        if (!Main.isDebugEnabled())
            return;

        if (message == null)
            message = "null";

        final String[] lines = message.split("\n");
        for (String line : lines) {
            System.out.print("[DEBUG] ");
            System.out.println(line);
        }
    }

    public static void debug(Exception exception) {
        if (Main.isDebugEnabled())
            exception.printStackTrace();
    }
}
