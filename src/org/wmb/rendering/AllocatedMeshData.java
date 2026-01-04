package org.wmb.rendering;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import org.wmb.loading.ResourceLoader;
import org.wmb.loading.obj.ObjFileLoader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Objects;

public final class AllocatedMeshData {

    public static AllocatedMeshData fromResource(String objPath) throws IOException {
        Objects.requireNonNull(objPath, "OBJ path is null");
        final String objSource = ResourceLoader.loadResourceText(objPath);
        return new AllocatedMeshData(ObjFileLoader.load(objSource).toMeshDataDescription());
    }

    public static AllocatedMeshData fromFile(String objPath) throws IOException {
        Objects.requireNonNull(objPath, "OBJ path is null");
        final String objSource = ResourceLoader.loadFileText(objPath);
        return new AllocatedMeshData(ObjFileLoader.load(objSource).toMeshDataDescription());
    }

    private final int vaoId;
    private final int[] vboIds;
    public final int vertexCount;

    public AllocatedMeshData(MeshDataDescription description) throws OpenGLStateException {
        description.verify();

        this.vaoId = GL30.glGenVertexArrays();
        if (this.vaoId < 1)
            throw new OpenGLStateException("VAO creation failed");

        GL30.glBindVertexArray(vaoId);

        final List<MeshDataDescription.DataArray> dataArrays = description.getDataArrays();
        this.vboIds = new int[dataArrays.size() + 1]; // +1 for EBO
        for (int index = 0; index < this.vboIds.length - 1; index++) {
            final MeshDataDescription.DataArray dataArray = dataArrays.get(index);

            try {
                this.vboIds[index] = createVbo(index, dataArray.elementsPerVertex(),
                    dataArray.data());
            } catch (OpenGLStateException exception) {
                GL30.glBindVertexArray(0);
                GL30.glDeleteVertexArrays(this.vaoId);

                for (int vboId : this.vboIds) {
                    if (vboId != 0)
                        GL30.glDeleteBuffers(vboId);
                }

                throw exception;
            }
        }

        try {
            final int[] indexArray = description.getIndexArray();
            this.vboIds[this.vboIds.length - 1] = createEbo(indexArray);
            this.vertexCount = indexArray.length;
        } catch(OpenGLStateException exception) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.vaoId);

            for (int vboId : this.vboIds) {
                if (vboId != 0)
                    GL30.glDeleteBuffers(vboId);
            }

            throw exception;
        }

        for (int index = 0; index < this.vboIds.length - 1; index++)
            GL30.glEnableVertexAttribArray(index);

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

    private static int createEbo(int[] data) throws OpenGLStateException {
        final int eboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboId);

        if (eboId < 1)
            throw new OpenGLStateException("EBO creation failed");

        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);

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
