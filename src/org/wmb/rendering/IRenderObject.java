package org.wmb.rendering;

import org.joml.Matrix4f;

public interface IRenderObject {

    AllocatedVertexData getVertexData();
    AllocatedTexture getTexture();
    Matrix4f getRotationMatrix();
    Matrix4f getTransformMatrix();
}
