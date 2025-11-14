package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

public final class AllocatedFramebuffer implements ITexture, AllocatedData {

    private final int fboId;
    private final int textureId;
    private final int rboId;

    public AllocatedFramebuffer(int width, int height) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Illegal dimension: " + width + "x" + height);

        AllocatedDataGuard.watch(this);

        this.fboId = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        this.textureId = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureId);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, width, height, 0, GL30.GL_RGBA,
            GL30.GL_UNSIGNED_BYTE, 0);

        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
            GL30.GL_TEXTURE_2D, this.textureId, 0);

        this.rboId = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.rboId);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT,
            GL30.GL_RENDERBUFFER, this.rboId);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("Framebuffer could not be completed");

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public int getFboId() {
        return this.fboId;
    }

    @Override
    public int getId() {
        return this.textureId;
    }

    @Override
    public void delete() {
        GL30.glDeleteTextures(this.textureId);
        GL30.glDeleteRenderbuffers(this.rboId);
        GL30.glDeleteFramebuffers(this.fboId);

        AllocatedDataGuard.forget(this);
    }
}
