package org.wmb.rendering.shader.uniform;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public final class Matrix4fUniform {

    private final int location;

    public Matrix4fUniform(int location) {
        this.location = location;
    }

    public void uniform(Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL30.glUniformMatrix4fv(this.location, false, matrix.get(stack.mallocFloat(16)));
        }
    }
}
