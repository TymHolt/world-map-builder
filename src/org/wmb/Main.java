package org.wmb;

import javax.swing.JOptionPane;

public final class Main {

    private static boolean mainCalled = false;

    public static void main(String[] args) {
        if (Main.mainCalled)
            throw new IllegalStateException("Main called again");

        mainCalled = true;

        try {
            handleArguments(args);

            if (isDebugEnabled()) {
                Log.debug("Debug enabled");
                Log.debug("Running max UPS: " + Main.maxUps);
            }

            // TODO Multiple context lead to JVM natives crash
            new WmbContext();
            while (WmbContext.activeContextExists()) {
                WmbContext.updateAll();
                enforceMaxUps();
            }
        } catch(Exception exception) {
            Log.error("(Internal) " + exception.getMessage());
            Log.debug(exception);
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Internal Exception",
                JOptionPane.ERROR_MESSAGE);
        }

        Main.onExitCallbacks.executeAll();
    }

    private static void handleArguments(String[] arguments) {
        for (String argument : arguments) {
            switch (argument) {
                case "-debug":
                case "--debug":
                case "-d":
                    debugEnabled = true;
                    break;

                default:
                    if (argument.startsWith("-ups")) {
                        final String upsString = argument.substring("-ups".length());
                        final Integer ups = stringToInt(upsString);

                        if (ups == null || ups <= 0)
                            throw new IllegalArgumentException("Not a valid UPS value: " + ups);

                        Main.maxUps = ups;
                        break;
                    }

                    Log.error("Unknown argument: " + argument);
                    throw new IllegalArgumentException("Unknown argument");
            }
        }
    }

    private static boolean debugEnabled = false;

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    private static final CallbackManager onExitCallbacks = new CallbackManager();

    public static void executeOnExit(Runnable runnable) {
        Main.onExitCallbacks.register(runnable);
    }

    private static int maxUps = 30;

    private static void enforceMaxUps() throws InterruptedException {
        Thread.sleep(1000 / Main.maxUps);
    }

    private static Integer stringToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(Exception exception) {
            return null;
        }
    }
}
