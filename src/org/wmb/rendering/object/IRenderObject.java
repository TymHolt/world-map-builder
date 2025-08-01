package org.wmb.rendering.object;

import org.joml.Matrix4f;
import org.wmb.rendering.AllocatedTexture;
import org.wmb.rendering.AllocatedVertexData;

public interface IRenderObject {

    AllocatedVertexData getVertexData();
    AllocatedTexture getTexture();
    Matrix4f getRotationMatrix();
    Matrix4f getTransformMatrix();
}
