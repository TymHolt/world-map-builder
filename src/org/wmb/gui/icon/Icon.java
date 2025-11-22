package org.wmb.gui.icon;

import org.wmb.ResourceLoader;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.TextureFilter;

import java.io.IOException;

public enum Icon {

    EYE_SOLID("/org/wmb/gui/icon/raw/eye_solid.png"),
    EYE("/org/wmb/gui/icon/raw/eye.png"),
    INDICATE_RIGHT("/org/wmb/gui/icon/raw/indicate_right.png"),
    INDICATE_DOWN("/org/wmb/gui/icon/raw/indicate_down.png"),
    CUBE("/org/wmb/gui/icon/raw/cube.png"),
    FRAME("/org/wmb/gui/icon/raw/frame.png");

    private final String path;
    private int enumIndex;

    Icon(String path) {
        this.path = path;
    }

    public AllocatedTexture loadAllocatedTexture() throws IOException {
        return new AllocatedTexture(ResourceLoader.loadImage(this.path), TextureFilter.LINEAR);
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
