package org.wmb.gui.font;

import org.wmb.rendering.AllocatedData;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.ITexture;
import org.wmb.rendering.TextureFilter;

import java.awt.image.BufferedImage;
import java.util.Objects;

public final class AllocatedGlyph implements ITexture, AllocatedData {

    private final AllocatedTexture texture;
    public final int width;
    public final int height;

    AllocatedGlyph(BufferedImage sourceImage) {
        Objects.requireNonNull(sourceImage, "Source image is null");
        this.texture = new AllocatedTexture(sourceImage, TextureFilter.NEAREST);
        this.width = sourceImage.getWidth();
        this.height = sourceImage.getHeight();
    }

    @Override
    public void delete() {
        this.texture.delete();
    }

    @Override
    public int getId() {
        return this.texture.getId();
    }
}
