package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class AllocatedTexture implements ITexture {

    private final int textureId;

    public AllocatedTexture(BufferedImage image) {
        this(image, TextureFilter.NEAREST);
    }

    public AllocatedTexture(BufferedImage image, TextureFilter filter) {
        Objects.requireNonNull(image, "Image is null");
        Objects.requireNonNull(filter, "Filter is null");

        final int width = image.getWidth();
        final int height = image.getHeight();
        final ByteBuffer pixelDataBuffer = MemoryUtil.memAlloc(width * height * 4);

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                final int pixelRgba = image.getRGB(x, y);
                pixelDataBuffer.put((byte) ((pixelRgba >> 16) & 0xFF));
                pixelDataBuffer.put((byte) ((pixelRgba >> 8) & 0xFF));
                pixelDataBuffer.put((byte) (pixelRgba & 0xFF));
                pixelDataBuffer.put((byte) ((pixelRgba >> 24) & 0xFF));
            }
        }

        pixelDataBuffer.flip();

        this.textureId = GL30.glGenTextures();
        if (this.textureId == 0)
            throw new IllegalStateException("OpenGL texture could not be created");

        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureId);
        GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, filter.glHandle);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, filter.glHandle);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width, height, 0, GL30.GL_RGBA,
                GL30.GL_UNSIGNED_BYTE, pixelDataBuffer);
    }

    @Override
    public int getId() {
        return this.textureId;
    }

    public void delete() {
        GL30.glDeleteTextures(this.textureId);
    }
}
