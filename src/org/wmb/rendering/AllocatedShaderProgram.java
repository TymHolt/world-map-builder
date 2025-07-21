package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

public final class AllocatedShaderProgram implements AllocatedData {

    private final int programId;

    public AllocatedShaderProgram(String vsSource, String fsSource) {
        this.programId = GL30.glCreateProgram();

        if (this.programId == 0)
            throw new IllegalStateException("Program could not be created by OpenGL");

        int vsId = loadShader(GL30.GL_VERTEX_SHADER, vsSource);
        GL30.glAttachShader(programId, vsId);
        int fsId = loadShader(GL30.GL_FRAGMENT_SHADER, fsSource);
        GL30.glAttachShader(programId, fsId);

        GL30.glLinkProgram(programId);
        if (GL30.glGetProgrami(programId, GL30.GL_LINK_STATUS) == 0)
            throw new IllegalStateException("Program could not link: " +
                    GL30.glGetProgramInfoLog(programId));

        // Shaders are not needed after linking
        GL30.glDetachShader(programId, vsId);
        GL30.glDetachShader(programId, fsId);
        GL30.glDeleteShader(vsId);
        GL30.glDeleteShader(fsId);

        GL30.glValidateProgram(programId);
        if (GL30.glGetProgrami(programId, GL30.GL_VALIDATE_STATUS) == 0)
            throw new IllegalStateException("Program could not validate: " +
                    GL30.glGetProgramInfoLog(programId));

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

    @Override
    public int getId() {
        return this.programId;
    }

    @Override
    public void delete() {
        GL30.glDeleteProgram(this.programId);
    }
}
