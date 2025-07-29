package org.wmb.debug;

public final class Assert {

    public static void argNotNull(Object arg, String name) {
        if (arg == null)
            throw new NullPointerException("Argument '" + name + "' is null");
    }
}
