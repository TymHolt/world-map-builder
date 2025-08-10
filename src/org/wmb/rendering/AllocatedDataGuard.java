package org.wmb.rendering;

import java.util.ArrayList;

public final class AllocatedDataGuard {

    // TODO This is only for a single context
    private static final ArrayList<AllocatedData> watchedData = new ArrayList<>();

    public static void watch(AllocatedData data) {
        AllocatedDataGuard.watchedData.add(data);
    }

    public static void forget(AllocatedData data) {
        AllocatedDataGuard.watchedData.remove(data);
    }

    public static void deleteAll() {
        // Copy list to prevent forget() calls while iterating
        for (AllocatedData data : new ArrayList<>(AllocatedDataGuard.watchedData)) {
            data.delete();
        }

        AllocatedDataGuard.watchedData.clear();
    }
}
