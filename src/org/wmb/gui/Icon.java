package org.wmb.gui;

import org.wmb.ResourceLoader;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.ITexture;

import java.io.IOException;

public enum Icon implements ITexture {

    // TODO This is only for one context
    EYE_SOLID("/org/wmb/icons/icon_eye_solid.png"),
    EYE("/org/wmb/icons/icon_eye.png");

    private final String path;
    private AllocatedTexture texture;

    Icon(String path) {
        this.path = path;
    }

    @Override
    public int getId() {
        return texture != null ? texture.getId() : 0;
    }

    public static void loadAll() {
        for (Icon icon : Icon.values()) {
            try {
                icon.texture = new AllocatedTexture(ResourceLoader.loadImage(icon.path));
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
