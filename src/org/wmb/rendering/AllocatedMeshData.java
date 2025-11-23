package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;

public final class AllocatedMeshData {

    private final int vaoId;
    private final int[] vboIds;
    public final int vertexCount;

    public AllocatedMeshData(float[] positionData, float[] texturePositionData,
        short[] indexData) {
        Objects.requireNonNull(positionData, "Position data is null");
        Objects.requireNonNull(texturePositionData, "Texture position data is null");
        Objects.requireNonNull(indexData, "Index data is null");

        this.vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        this.vboIds = new int[] {
            createVbo(0, 3, positionData),
            createVbo(1, 2, texturePositionData),
            createEbo(indexData)
        };

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        this.vertexCount = indexData.length;

        GL30.glBindVertexArray(0);
    }

    private static int createVbo(int attribIndex, int vertexSize, float[] data) {
        final int vboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);

        FloatBuffer dataBuffer = MemoryUtil.memAllocFloat(data.length).put(data).flip();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, dataBuffer, GL30.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attribIndex, vertexSize, GL30.GL_FLOAT, false, 0, 0);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        return vboId;
    }

    private static int createEbo(short[] data) {
        int eboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboId);

        ShortBuffer dataBuffer = MemoryUtil.memAllocShort(data.length).put(data).flip();
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL30.GL_STATIC_DRAW);

        // EBO does not get unbound!
        return eboId;
    }

    public int getId() {
        return this.vaoId;
    }

    public void delete() {
        GL30.glDeleteVertexArrays(this.vaoId);
        GL30.glDeleteBuffers(this.vboIds);
    }
}
