package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.wmb.Log;

import java.util.Objects;

public final class AllocatedLineData {

    private final int vaoId;
    private final int vboId;
    public final int vertexCount;

    public AllocatedLineData(float[] positionData) throws OpenGLStateException {
        Objects.requireNonNull(positionData, "Position data is null");

        // Position data needs 3 float elements per vertex
        if (positionData.length % 3 != 0 || positionData.length == 0) {
            Log.debug("Position data length: " + positionData.length);
            throw new IllegalArgumentException("Position data element/vertex mismatch");
        }

        // Data needs 2 vertices per line
        this.vertexCount = positionData.length / 3;
        if (this.vertexCount % 2 != 0) {
            Log.debug("Vertex count: " + this.vertexCount);
            throw new IllegalArgumentException("Data vertex/line mismatch");
        }

        this.vaoId = GL30.glGenVertexArrays();
        if (this.vaoId < 1)
            throw new OpenGLStateException("VAO creation failed");

        this.vboId = GL30.glGenBuffers();
        if (this.vboId < 1) {
            GL30.glDeleteVertexArrays(vaoId);
            throw new OpenGLStateException("VBO creation failed");
        }

        GL30.glBindVertexArray(this.vaoId);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.vboId);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, positionData, GL30.GL_STATIC_DRAW);
        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 3 * 4, 0);
        GL30.glBindVertexArray(0);
    }

    public int getId() {
        return this.vaoId;
    }

    public void delete() {
        GL30.glDeleteVertexArrays(vaoId);
        GL30.glDeleteBuffers(vboId);
    }
}
