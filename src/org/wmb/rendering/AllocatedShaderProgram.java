package org.wmb.rendering;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public final class AllocatedShaderProgram implements AllocatedData {

    private final int programId;

    public AllocatedShaderProgram(String vsSource, String fsSource) {
        AllocatedDataGuard.watch(this);

        this.programId = GL30.glCreateProgram();

        if (this.programId == 0)
            throw new IllegalStateException("Program could not be created by OpenGL");

        int vsId = loadShader(GL30.GL_VERTEX_SHADER, vsSource);
        GL30.glAttachShader(this.programId, vsId);
        int fsId = loadShader(GL30.GL_FRAGMENT_SHADER, fsSource);
        GL30.glAttachShader(this.programId, fsId);

        GL30.glLinkProgram(this.programId);
        if (GL30.glGetProgrami(this.programId, GL30.GL_LINK_STATUS) == 0)
            throw new IllegalStateException("Program could not link: " +
                    GL30.glGetProgramInfoLog(this.programId));

        // Shaders are not needed after linking
        GL30.glDetachShader(this.programId, vsId);
        GL30.glDetachShader(this.programId, fsId);
        GL30.glDeleteShader(vsId);
        GL30.glDeleteShader(fsId);

        GL30.glValidateProgram(this.programId);
        if (GL30.glGetProgrami(this.programId, GL30.GL_VALIDATE_STATUS) == 0)
            throw new IllegalStateException("Program could not validate: " +
                    GL30.glGetProgramInfoLog(this.programId));

    }

    private static int loadShader(int type, String source) {
        int shaderId = GL30.glCreateShader(type);

        if (shaderId == 0)
            throw new IllegalStateException("Shader could not be created by OpenGL");

        GL30.glShaderSource(shaderId, source);
        GL30.glCompileShader(shaderId);

        if (GL30.glGetShaderi(shaderId, GL30.GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException("Shader could not compile: " +
                GL30.glGetShaderInfoLog(shaderId));

        return shaderId;
    }

    public int getUniformLocation(String name) {
        return GL30.glGetUniformLocation(this.programId, name);
    }

    @Override
    public int getId() {
        return this.programId;
    }

    @Override
    public void delete() {
        GL30.glDeleteProgram(this.programId);

        AllocatedDataGuard.forget(this);
    }

    public static void uniformColor(int location, Color color) {
        GL30.glUniform4f(location, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void uniformMat4(int location, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // 4x4 Matrix -> 16 values
            GL30.glUniformMatrix4fv(location, false, matrix.get(stack.mallocFloat(16)));
        }
    }
}
