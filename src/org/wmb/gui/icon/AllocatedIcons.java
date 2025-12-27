package org.wmb.gui.icon;

import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.TextureFilter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public final class AllocatedIcons {

    private final AllocatedTexture[] iconTextures;

    public AllocatedIcons() throws IOException {
        final Icon[] icons = Icon.values();
        this.iconTextures = new AllocatedTexture[icons.length];

        try {
            for (Icon icon : icons) {
                final BufferedImage iconImage = icon.loadImage();
                final AllocatedTexture iconTexture = new AllocatedTexture(iconImage,
                    TextureFilter.NEAREST);
                this.iconTextures[icon.ordinal()] = iconTexture;
            }
        } catch(IOException exception) {
            // Prevent memory leak of already allocated textures
            for (AllocatedTexture iconTexture : this.iconTextures) {
                if (iconTexture != null)
                    iconTexture.delete();
            }

            throw exception;
        }
    }

    public AllocatedTexture getIconTexture(Icon icon) {
        Objects.requireNonNull(icon, "Icon is null");
        return this.iconTextures[icon.ordinal()];
    }

    public void delete() {
        for (AllocatedTexture iconTexture : this.iconTextures)
            iconTexture.delete();
    }
}
