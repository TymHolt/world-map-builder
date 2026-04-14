package org.wmb.rendering.shader.uniform;

import org.lwjgl.opengl.GL30;
import org.wmb.rendering.Color;

public final class ColorUniform {

    private final int location;

    public ColorUniform(int location) {
        this.location = location;
    }

    public void uniform(Color color) {
        GL30.glUniform4f(this.location, color.getRed(), color.getGreen(), color.getBlue(),
            color.getAlpha());
    }
}
