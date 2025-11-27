package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import org.wmb.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;

public final class AllocatedMeshData {

    private final int vaoId;
    private final int[] vboIds;
    public final int vertexCount;

    public AllocatedMeshData(float[] positionData, float[] texturePositionData,
        short[] indexData) throws OpenGLStateException {
        Objects.requireNonNull(positionData, "Position data is null");
        Objects.requireNonNull(texturePositionData, "Texture position data is null");
        Objects.requireNonNull(indexData, "Index data is null");

        // ---------- Check data for possible errors ----------------------------------------------

        // Position data needs 3 float elements per vertex
        if (positionData.length % 3 != 0 || positionData.length == 0) {
            Log.debug("Position data length: " + positionData.length);
            throw new IllegalArgumentException("Position data element/vertex mismatch");
        }

        // Texture position data needs 2 float elements per vertex
        if (texturePositionData.length % 2 != 0 || texturePositionData.length == 0) {
            Log.debug("Texture position data length: " + texturePositionData.length);
            throw new IllegalArgumentException("Texture position data element/vertex mismatch");
        }

        // Index data needs 3 vertex indices per face (Triangles)
        if (indexData.length % 3 != 0 || indexData.length == 0) {
            Log.debug("Index data length: " + texturePositionData.length);
            throw new IllegalArgumentException("Index data vertex/face mismatch");
        }

        // All data arrays (except indexData) need to have matching vertex count
        final int positionDataVertexCount = positionData.length / 3;
        final int texturePositionDataVertexCount = texturePositionData.length / 2;
        if (positionDataVertexCount != texturePositionDataVertexCount) {
            Log.debug("Position data vertex count: " + positionDataVertexCount);
            Log.debug("Texture position data vertex count: " + texturePositionDataVertexCount);
            throw new IllegalArgumentException("Data vertex count mismatch");
        }

        // Indices must be in bounds to the given vertex counts
        short minIndex = Short.MAX_VALUE;
        short maxIndex = Short.MIN_VALUE;
        for (short index : indexData) {
            if (index < minIndex)
                minIndex = index;
            if (index > maxIndex)
                maxIndex = index;
        }

        if (minIndex < 0 || maxIndex >= positionDataVertexCount) {
            Log.debug("Index data bounds: min=" + minIndex + " max=" + maxIndex);
            throw new IllegalArgumentException("Index data out of bounds");
        }

        // ---------- End checking ----------------------------------------------------------------

        this.vaoId = GL30.glGenVertexArrays();
        if (this.vaoId < 1)
            throw new OpenGLStateException("VAO creation failed");

        GL30.glBindVertexArray(vaoId);

        final int positionDataVboId;
        try {
            positionDataVboId = createVbo(0, 3, positionData);
        } catch(OpenGLStateException exception) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.vaoId);
            throw new OpenGLStateException("(Position data) " + exception.getMessage());
        }

        final int texturePositionDataVboId;
        try {
            texturePositionDataVboId = createVbo(1, 2, texturePositionData);
        } catch(OpenGLStateException exception) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.vaoId);
            GL30.glDeleteBuffers(positionDataVboId);
            throw new OpenGLStateException("(Texture position data) " + exception.getMessage());
        }

        final int indexDataEboId;
        try {
            indexDataEboId = createEbo(indexData);
        } catch(OpenGLStateException exception) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.vaoId);
            GL30.glDeleteBuffers(positionDataVboId);
            GL30.glDeleteBuffers(texturePositionDataVboId);
            throw new OpenGLStateException("(Index data) " + exception.getMessage());
        }

        this.vboIds = new int[] {
            positionDataVboId,
            texturePositionDataVboId,
            indexDataEboId
        };

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        this.vertexCount = indexData.length;

        GL30.glBindVertexArray(0);
    }

    private static int createVbo(int attribIndex, int vertexSize, float[] data)
        throws OpenGLStateException {
        final int vboId = GL30.glGenBuffers();

        if (vboId < 1)
            throw new OpenGLStateException("VBO creation failed");

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);

        final FloatBuffer dataBuffer = MemoryUtil.memAllocFloat(data.length).put(data).flip();
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, dataBuffer, GL30.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attribIndex, vertexSize, GL30.GL_FLOAT, false, 0, 0);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        return vboId;
    }

    private static int createEbo(short[] data) throws OpenGLStateException {
        final int eboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboId);

        if (eboId < 1)
            throw new OpenGLStateException("EBO creation failed");

        final ShortBuffer dataBuffer = MemoryUtil.memAllocShort(data.length).put(data).flip();
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
