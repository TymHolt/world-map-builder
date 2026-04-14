package org.wmb.editor.element.Object3dElement;

import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.ResourceManager;
import org.wmb.rendering.*;
import org.wmb.rendering.shader.AllocatedShaderProgram;
import org.wmb.rendering.shader.uniform.CameraUniform;
import org.wmb.rendering.shader.uniform.ColorUniform;
import org.wmb.rendering.shader.uniform.FloatUniform;
import org.wmb.rendering.shader.uniform.IntUniform;
import org.wmb.rendering.shader.uniform.Matrix4fUniform;

import java.io.IOException;

public class Object3dElementRenderer {

    private static final String TAG = "Object3dElementRenderer";

    private final AllocatedShaderProgram object3dShaderProgram;
    private final IntUniform textureUniform;
    private final Matrix4fUniform transformMatrixUniform;
    private final CameraUniform cameraUniform;
    private final ColorUniform highlightColorUniform;
    private final FloatUniform highlightFactorUniform;

    public Object3dElementRenderer() throws IOException {
        try {
            this.object3dShaderProgram = AllocatedShaderProgram.fromResources(
                "/org/wmb/editor/element/Object3dElement/object_3d_renderer_vs.glsl",
                "/org/wmb/editor/element/Object3dElement/object_3d_renderer_fs.glsl"
            );
        } catch (IOException exception) {
            Log.error(TAG, "Shader program failed to load");
            throw exception;
        }

        try {
            this.textureUniform = new IntUniform(this.object3dShaderProgram.getUniformLocation("u_texture"));
            this.transformMatrixUniform = new Matrix4fUniform(
                this.object3dShaderProgram.getUniformLocation("u_transform"));
            this.cameraUniform = new CameraUniform(this.object3dShaderProgram.getUniformLocation("u_view"),
                this.object3dShaderProgram.getUniformLocation("u_projection"));
            this.highlightColorUniform = new ColorUniform(
                this.object3dShaderProgram.getUniformLocation("u_highlight_color"));
            this.highlightFactorUniform = new FloatUniform(
                this.object3dShaderProgram.getUniformLocation("u_highlight_factor"));
        } catch (OpenGLStateException exception) {
            this.object3dShaderProgram.delete();
            Log.error(TAG, "Shader program failed to resolve uniform location");
            throw exception;
        }
    }

    public void delete() {
        this.object3dShaderProgram.delete();
    }

    public void preparePipeline(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.object3dShaderProgram.getId());
        this.textureUniform.uniform(0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
    }

    public void uniformCamera(Camera camera, float aspect) {
        this.cameraUniform.uniform(camera, aspect);
    }

    public void render(Object3dElement element, Color highlight, float factor, ResourceManager resourceManager) {
        final AllocatedMeshData model = resourceManager.getModel(element.modelPath);
        final AllocatedTexture texture = resourceManager.getTexture(element.texturePath);

        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glBindVertexArray(model.getId());

        this.transformMatrixUniform.uniform(element.getTransform().getAsMatrix());
        this.highlightColorUniform.uniform(highlight);
        this.highlightFactorUniform.uniform(factor);

        GL30.glDrawElements(GL30.GL_TRIANGLES, model.vertexCount,
            GL30.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    public void resetPipeline() {
        GL30.glUseProgram(0);
    }
}
