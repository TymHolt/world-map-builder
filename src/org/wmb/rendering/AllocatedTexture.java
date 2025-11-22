package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class AllocatedTexture implements ITexture, AllocatedData {

    private final int textureId;

    public AllocatedTexture(BufferedImage image) {
        this(image, TextureFilter.NEAREST);
    }

    public AllocatedTexture(BufferedImage image, TextureFilter filter) {
        Objects.requireNonNull(image, "Image is null");
        Objects.requireNonNull(filter, "Filter is null");

        AllocatedDataGuard.watch(this);

        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelRgbData = image.getRGB(0, 0, width, height, null, 0, width);
        ByteBuffer pixelDataBuffer = MemoryUtil.memAlloc(pixelRgbData.length * 4);

        for (int pixelRgb : pixelRgbData) {
            pixelDataBuffer.put((byte) ((pixelRgb >> 16) & 0xFF));
            pixelDataBuffer.put((byte) ((pixelRgb >> 8) & 0xFF));
            pixelDataBuffer.put((byte) (pixelRgb & 0xFF));
            pixelDataBuffer.put((byte) ((pixelRgb >> 24) & 0xFF));
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

    @Override
    public void delete() {
        GL30.glDeleteTextures(this.textureId);

        AllocatedDataGuard.forget(this);
    }
}
