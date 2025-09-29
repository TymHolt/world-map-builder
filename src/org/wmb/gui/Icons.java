package org.wmb.gui;

import org.wmb.rendering.AllocatedTexture;

import java.io.IOException;

public final class Icons {

    private final AllocatedTexture[] allocatedTextures;

    public Icons() throws IOException {
        this.allocatedTextures = new AllocatedTexture[Icon.getEnumValueCount()];

        try {
            for (Icon icon : Icon.values()) {
                this.allocatedTextures[icon.getEnumIndex()] = icon.loadAllocatedTexture();
            }
        } catch(IOException exception) {
            // Prevent memory leak
            deleteAll();
            throw exception;
        }
    }

    public AllocatedTexture getTexture(Icon icon) {
        return this.allocatedTextures[icon.getEnumIndex()];
    }

    public void deleteAll() {
        for (AllocatedTexture allocatedTexture : this.allocatedTextures) {
            if (allocatedTexture != null)
                allocatedTexture.delete();
        }
    }
}
