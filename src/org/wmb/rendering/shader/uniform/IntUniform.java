package org.wmb.rendering.shader.uniform;

import org.lwjgl.opengl.GL30;

public final class IntUniform {

    private final int location;

    public IntUniform(int location) {
        this.location = location;
    }

    public void uniform(int value) {
        GL30.glUniform1i(this.location, value);
    }
}
