package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

public enum TextureFilter {

    NEAREST(GL30.GL_NEAREST),
    LINEAR(GL30.GL_LINEAR);

    final int glHandle;

    TextureFilter(int glHandle) {
        this.glHandle = glHandle;
    }
}
