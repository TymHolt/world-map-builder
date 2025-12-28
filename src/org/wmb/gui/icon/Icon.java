package org.wmb.gui.icon;

import org.wmb.loading.ResourceLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;

public enum Icon {

    EYE_SOLID("/org/wmb/gui/icon/raw/eye_solid.png", true),
    EYE("/org/wmb/gui/icon/raw/eye.png", true),
    INDICATE_RIGHT("/org/wmb/gui/icon/raw/indicate_right.png", true),
    INDICATE_DOWN("/org/wmb/gui/icon/raw/indicate_down.png", true),
    CUBE("/org/wmb/gui/icon/raw/cube.png", true),
    FRAME("/org/wmb/gui/icon/raw/frame.png", true),
    GIZMO_TRANSLATE("/org/wmb/gui/icon/raw/gizmo_translate.png", false),
    GIZMO_ROTATE("/org/wmb/gui/icon/raw/gizmo_rotate.png", false),
    GIZMO_SCALE("/org/wmb/gui/icon/raw/gizmo_scale.png", false);

    private final String path;
    public final boolean masked;

    Icon(String path, boolean masked) {
        this.path = path;
        this.masked = masked;
    }

    public BufferedImage loadImage() throws IOException {
        return ResourceLoader.loadImage(this.path);
    }
}
