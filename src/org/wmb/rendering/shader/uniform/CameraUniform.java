package org.wmb.rendering.shader.uniform;

import org.wmb.rendering.Camera;

public final class CameraUniform {

    private final Matrix4fUniform viewMatrixUniform;
    private final Matrix4fUniform projectionMatrixUniform;

    public CameraUniform(int viewMatrixLocation, int projectionMatrixLocation) {
        this.viewMatrixUniform = new Matrix4fUniform(viewMatrixLocation);
        this.projectionMatrixUniform = new Matrix4fUniform(projectionMatrixLocation);
    }

    public void uniform(Camera camera, float aspect) {
        this.viewMatrixUniform.uniform(camera.getViewMatrix());
        this.projectionMatrixUniform.uniform(camera.getProjectionMatrix(aspect));
    }
}
