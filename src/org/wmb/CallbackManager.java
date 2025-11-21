package org.wmb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CallbackManager {

    private final List<Runnable> callbackList = new ArrayList<>();

    public void register(Runnable callback) {
        Objects.requireNonNull(callback, "Callback must not be null");

        synchronized (this.callbackList) {
            this.callbackList.add(callback);
        }
    }

    public void executeAll() {
        synchronized (this.callbackList) {
            for (Runnable callback : this.callbackList) {
                try {
                    callback.run();
                } catch(Exception exception) {
                    Log.error("Callback exception: " + exception.getMessage());
                    Log.debug(exception);
                }
            }
        }
    }
}
