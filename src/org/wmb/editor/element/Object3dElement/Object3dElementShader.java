package org.wmb.editor.element.Object3dElement;

import org.wmb.Log;
import org.wmb.rendering.OpenGLStateException;
import org.wmb.rendering.shader.AllocatedShaderProgram;
import org.wmb.rendering.shader.uniform.CameraUniform;
import org.wmb.rendering.shader.uniform.ColorUniform;
import org.wmb.rendering.shader.uniform.FloatUniform;
import org.wmb.rendering.shader.uniform.IntUniform;
import org.wmb.rendering.shader.uniform.Matrix4fUniform;

import java.io.IOException;

class Object3dElementShader {

    private static final String TAG = "Object3dElementShader";
    private static final String VS_PATH = "/org/wmb/editor/element/Object3dElement/Object3dElementShaderVS.glsl";
    private static final String FS_PATH = "/org/wmb/editor/element/Object3dElement/Object3dElementShaderFS.glsl";

    private final AllocatedShaderProgram shaderProgram;
    public final IntUniform texture;
    public final Matrix4fUniform transformMatrix;
    public final CameraUniform camera;
    public final ColorUniform highlightColor;
    public final FloatUniform highlightFactor;

    Object3dElementShader() throws IOException {
        try {
            this.shaderProgram = AllocatedShaderProgram.fromResources(VS_PATH, FS_PATH);
        } catch (IOException exception) {
            Log.error(TAG, "Failed to compile");
            throw exception;
        }

        try {
            this.texture = new IntUniform(this.shaderProgram.getUniformLocation("u_texture"));
            this.transformMatrix = new Matrix4fUniform(this.shaderProgram.getUniformLocation("u_transform"));
            this.camera = new CameraUniform(this.shaderProgram.getUniformLocation("u_view"),
                this.shaderProgram.getUniformLocation("u_projection"));
            this.highlightColor = new ColorUniform(this.shaderProgram.getUniformLocation("u_highlight_color"));
            this.highlightFactor = new FloatUniform(this.shaderProgram.getUniformLocation("u_highlight_factor"));
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
