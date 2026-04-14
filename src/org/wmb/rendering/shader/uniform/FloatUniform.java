package org.wmb.rendering.shader.uniform;

import org.lwjgl.opengl.GL30;

public final class FloatUniform {

    private final int location;

    public FloatUniform(int location) {
        this.location = location;
    }

    public void uniform(float value) {
        GL30.glUniform1f(this.location, value);
    }
}
