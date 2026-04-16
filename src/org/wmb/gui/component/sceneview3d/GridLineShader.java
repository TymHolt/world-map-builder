package org.wmb.gui.component.sceneview3d;

import org.wmb.Log;
import org.wmb.rendering.OpenGLStateException;
import org.wmb.rendering.shader.AllocatedShaderProgram;
import org.wmb.rendering.shader.uniform.CameraUniform;
import org.wmb.rendering.shader.uniform.ColorUniform;

import java.io.IOException;

final class GridLineShader {

    private static final String TAG = "GridLineShader";
    private static final String VS_PATH = "/org/wmb/gui/component/sceneview3d/GridLineShaderVS.glsl";
    private static final String FS_PATH = "/org/wmb/gui/component/sceneview3d/GridLineShaderFS.glsl";

    private final AllocatedShaderProgram shaderProgram;
    public final CameraUniform camera;
    public final ColorUniform color;

    GridLineShader() throws IOException {
        try {
            this.shaderProgram = AllocatedShaderProgram.fromResources(VS_PATH, FS_PATH);
        } catch (IOException exception) {
            Log.error(TAG, "Failed to compile");
            throw exception;
        }

        try {
            this.camera = new CameraUniform(this.shaderProgram.getUniformLocation("u_view"),
                this.shaderProgram.getUniformLocation("u_projection"));
            this.color = new ColorUniform(this.shaderProgram.getUniformLocation("u_color"));
        } catch (OpenGLStateException exception) {
            this.shaderProgram.delete();
            Log.error(TAG, "Failed to resolve uniform location");
            throw exception;
        }
    }

    void use() {
        this.shaderProgram.use();
    }

    void delete() {
        this.shaderProgram.delete();
    }
}
