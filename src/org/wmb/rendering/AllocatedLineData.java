package org.wmb.rendering;

import org.lwjgl.opengl.GL30;

import java.util.Objects;

public final class AllocatedLineData {

    private final int vaoId;
    private final int vboId;
    public final int vertexCount;

    public AllocatedLineData(float[] positionData) {
        Objects.requireNonNull(positionData, "Position data is null");

        this.vertexCount = positionData.length / 3;
        this.vaoId = GL30.glGenVertexArrays();
        this.vboId = GL30.glGenBuffers();

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
