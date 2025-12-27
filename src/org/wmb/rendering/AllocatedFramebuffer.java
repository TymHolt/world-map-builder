package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

import java.util.Objects;

public final class AllocatedFramebuffer implements ITexture {

    private final int fboId;
    private final int textureId;
    private final int rboId;

    public AllocatedFramebuffer(int width, int height) throws OpenGLStateException {
        this(width, height, TextureFilter.LINEAR);
    }

    public AllocatedFramebuffer(int width, int height, TextureFilter filter)
        throws OpenGLStateException {
        Objects.requireNonNull(filter, "Filter is null");

        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Illegal dimension: " + width + "x" + height);

        this.fboId = GL30.glGenFramebuffers();
        if (this.fboId < 1)
            throw new OpenGLStateException("FBO creation failed");

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        this.textureId = GL30.glGenTextures();
        if (this.textureId < 1) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL30.glDeleteFramebuffers(this.fboId);
            throw new OpenGLStateException("FBO Texture creation failed");
        }

        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureId);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, width, height, 0, GL30.GL_RGBA,
            GL30.GL_UNSIGNED_BYTE, 0);

        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, filter.glHandle);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, filter.glHandle);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
            GL30.GL_TEXTURE_2D, this.textureId, 0);

        this.rboId = GL30.glGenRenderbuffers();
        if (this.rboId < 1) {
            GL30.glDeleteTextures(this.textureId);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL30.glDeleteFramebuffers(this.fboId);
            throw new OpenGLStateException("RBO creation failed");
        }

        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.rboId);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT,
            GL30.GL_RENDERBUFFER, this.rboId);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            GL30.glDeleteTextures(this.textureId);
            GL30.glDeleteRenderbuffers(this.rboId);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL30.glDeleteFramebuffers(this.fboId);
            throw new OpenGLStateException("Framebuffer not be complete");
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public int getFboId() {
        return this.fboId;
    }

    @Override
    public int getId() {
        return this.textureId;
    }

    public void delete() {
        GL30.glDeleteTextures(this.textureId);
        GL30.glDeleteRenderbuffers(this.rboId);
        GL30.glDeleteFramebuffers(this.fboId);
    }
}
