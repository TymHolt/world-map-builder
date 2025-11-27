package org.wmb.rendering;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.wmb.Log;
import org.wmb.ResourceLoader;

import java.io.IOException;
import java.util.Objects;

public final class AllocatedShaderProgram {

    private final int programId;

    public static AllocatedShaderProgram fromResources(String vertexShaderPath,
        String fragmentShaderPath) throws IOException {
        Objects.requireNonNull(vertexShaderPath, "Vertex shader path is null");
        Objects.requireNonNull(fragmentShaderPath, "Fragment shader path is null");

        final String vertexShaderSource;
        try {
            vertexShaderSource = ResourceLoader.loadText(vertexShaderPath);
        } catch (IOException exception) {
            throw new IOException("(Vertex Shader) " + exception.getMessage());
        }

        final String fragmentShaderSource;
        try {
            fragmentShaderSource = ResourceLoader.loadText(fragmentShaderPath);
        } catch (IOException exception) {
            throw new IOException("(Fragment shader) " + exception.getMessage());
        }

        return new AllocatedShaderProgram(vertexShaderSource, fragmentShaderSource);
    }

    public AllocatedShaderProgram(String vertexShaderSource, String fragmentShaderSource)
        throws OpenGLStateException {
        Objects.requireNonNull(vertexShaderSource, "Vertex shader source is null");
        Objects.requireNonNull(fragmentShaderSource, "Fragment shader source is null");

        this.programId = GL30.glCreateProgram();
        if (this.programId == 0)
            throw new OpenGLStateException("Program creation failed");

        final int vertexShaderId;
        try {
            vertexShaderId = loadShader(GL30.GL_VERTEX_SHADER, vertexShaderSource);
        } catch(OpenGLStateException exception) {
            GL30.glDeleteProgram(this.programId);
            throw new OpenGLStateException("(Vertex Shader) " + exception.getMessage());
        }
        GL30.glAttachShader(this.programId, vertexShaderId);

        final int fragmentShaderId;
        try {
            fragmentShaderId = loadShader(GL30.GL_FRAGMENT_SHADER, fragmentShaderSource);
        } catch(OpenGLStateException exception) {
            GL30.glDetachShader(this.programId, vertexShaderId);
            GL30.glDeleteShader(vertexShaderId);
            GL30.glDeleteProgram(this.programId);
            throw new OpenGLStateException("(Fragment Shader) " + exception.getMessage());
        }
        GL30.glAttachShader(this.programId, fragmentShaderId);

        GL30.glLinkProgram(this.programId);
        if (GL30.glGetProgrami(this.programId, GL30.GL_LINK_STATUS) == 0) {
            Log.debug("Program info log:\n" + GL30.glGetProgramInfoLog(this.programId));
            GL30.glDetachShader(this.programId, vertexShaderId);
            GL30.glDetachShader(this.programId, fragmentShaderId);
            GL30.glDeleteShader(vertexShaderId);
            GL30.glDeleteShader(fragmentShaderId);
            GL30.glDeleteProgram(this.programId);
            throw new OpenGLStateException("Program linking failed");
        }

        // Shaders are not needed after linking
        GL30.glDetachShader(this.programId, vertexShaderId);
        GL30.glDetachShader(this.programId, fragmentShaderId);
        GL30.glDeleteShader(vertexShaderId);
        GL30.glDeleteShader(fragmentShaderId);

        GL30.glValidateProgram(this.programId);
        if (GL30.glGetProgrami(this.programId, GL30.GL_VALIDATE_STATUS) == 0) {
            Log.debug("Program info log:\n" + GL30.glGetProgramInfoLog(this.programId));
            GL30.glDeleteProgram(this.programId);
            throw new OpenGLStateException("Program validation failed");
        }
    }

    private static int loadShader(int type, String source) throws OpenGLStateException {
        final int shaderId = GL30.glCreateShader(type);
        if (shaderId == 0)
            throw new OpenGLStateException("Shader creation failed");

        GL30.glShaderSource(shaderId, source);
        GL30.glCompileShader(shaderId);
        if (GL30.glGetShaderi(shaderId, GL30.GL_COMPILE_STATUS) == 0) {
            Log.debug("Shader info log:\n" + GL30.glGetShaderInfoLog(shaderId));
            GL30.glDeleteShader(shaderId);
            throw new OpenGLStateException("Shader compilation failed");
        }

        return shaderId;
    }

    public int getUniformLocation(String name) throws OpenGLStateException {
        Objects.requireNonNull(name, "Name is null");

        final int uniformLocation = GL30.glGetUniformLocation(this.programId, name);
        if (uniformLocation == -1)
            throw new OpenGLStateException("No uniform location for id " + name);

        return uniformLocation;
    }

    public int getId() {
        return this.programId;
    }

    public void delete() {
        GL30.glDeleteProgram(this.programId);
    }

    public static void uniformColor(int location, Color color) {
        GL30.glUniform4f(location, color.getRed(), color.getGreen(), color.getBlue(),
            color.getAlpha());
    }

    public static void uniformMat4(int location, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL30.glUniformMatrix4fv(location, false, matrix.get(stack.mallocFloat(16)));
        }
    }
}
