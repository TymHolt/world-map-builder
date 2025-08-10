package org.wmb.rendering.object;

import org.joml.Matrix4f;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.ITexture;

public interface IRenderObject {

    AllocatedVertexData getVertexData();
    ITexture getTexture();
    Matrix4f getRotationMatrix();
    Matrix4f getTransformMatrix();
}
