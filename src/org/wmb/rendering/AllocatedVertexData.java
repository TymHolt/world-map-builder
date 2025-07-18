package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public final class AllocatedVertexData implements AllocatedData {

    private final int vaoId;
    private final int[] vboIds;
    private final int vertexCount;

    public AllocatedVertexData(float[] positionData) {
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        vboIds = new int[] {
            createVbo(0, 3, positionData)
        };

        this.vertexCount = positionData.length / 3;

        GL30.glBindVertexArray(0);
    }

    private static int createVbo(int attribIndex, int vertexSize, float[] data) {
        int vboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);

        FloatBuffer dataBuffer = MemoryUtil.memAllocFloat(data.length).put(data).flip();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, dataBuffer, GL30.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attribIndex, vertexSize, GL30.GL_FLOAT, false, 0, 0);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        return vboId;
    }

    @Override
    public int getId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void delete() {
        GL30.glDeleteVertexArrays(vaoId);
        GL30.glDeleteBuffers(vboIds);
    }
}
