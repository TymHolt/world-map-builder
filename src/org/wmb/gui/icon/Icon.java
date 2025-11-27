package org.wmb.gui.icon;

import org.wmb.ResourceLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;

public enum Icon {

    EYE_SOLID("/org/wmb/gui/icon/raw/eye_solid.png"),
    EYE("/org/wmb/gui/icon/raw/eye.png"),
    INDICATE_RIGHT("/org/wmb/gui/icon/raw/indicate_right.png"),
    INDICATE_DOWN("/org/wmb/gui/icon/raw/indicate_down.png"),
    CUBE("/org/wmb/gui/icon/raw/cube.png"),
    FRAME("/org/wmb/gui/icon/raw/frame.png");

    private final String path;

    Icon(String path) {
        this.path = path;
    }

    public BufferedImage loadImage() throws IOException {
        return ResourceLoader.loadImage(this.path);
    }

    /*public AllocatedTexture loadAllocatedTexture() throws IOException {
        return new AllocatedTexture(ResourceLoader.loadImage(this.path), TextureFilter.NEAREST);
    }*/
}
