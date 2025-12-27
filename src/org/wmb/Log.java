package org.wmb;

import java.io.PrintStream;

public final class Log {

    public static void info(String message) {
        info("", message);
    }

    public static void info(String tag, String message) {
        print("INFO", tag, message, System.out);
    }

    public static void error(String message) {
        error("", message);
    }

    public static void error(String tag, String message) {
        print("ERROR", tag, message, System.err);
    }

    public static void debug(String message) {
        debug("", message);
    }

    public static void debug(String tag, String message) {
        if (!Main.isDebugEnabled())
            return;

        print("DEBUG", tag, message, System.out);
    }

    private static void print(String type, String tag, String message, PrintStream stream) {
        if (tag == null)
            tag = "null";

        if (!tag.isEmpty())
            tag = "[" + tag + "] ";

        if (message == null)
            message = "null";

        final String[] lines = message.split("\n");
        for (String line : lines)
            stream.println("[" + type + "] " + tag + line);
    }

    public static void debug(Exception exception) {
        if (Main.isDebugEnabled())
            exception.printStackTrace();
    }
}
