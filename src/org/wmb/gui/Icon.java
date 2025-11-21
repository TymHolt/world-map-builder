package org.wmb.gui;

import org.wmb.ResourceLoader;
import org.wmb.rendering.AllocatedTexture;

import java.io.IOException;

public enum Icon {

    EYE_SOLID("/org/wmb/icons/icon_eye_solid.png"),
    EYE("/org/wmb/icons/icon_eye.png");

    private final String path;
    private int enumIndex;

    Icon(String path) {
        this.path = path;
    }

    public AllocatedTexture loadAllocatedTexture() throws IOException {
        return new AllocatedTexture(ResourceLoader.loadImage(this.path));
    }

    public int getEnumIndex() {
        return this.enumIndex;
    }

    public static int getEnumValueCount() {
        return values().length;
    }

    static {
        // Assign indices 0 - max value for access in arrays
        int enumIndex = 0;

        for (Icon icon : Icon.values())
            icon.enumIndex = enumIndex++;
    }
}
