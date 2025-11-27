package org.wmb.editor.element.Object3dElement;

import org.lwjgl.opengl.GL30;
import org.wmb.rendering.*;

import java.io.IOException;

public class Object3dElementRenderer {

    private final AllocatedShaderProgram object3dShaderProgram;
    private final int textureUl;
    private final int transformUl;
    private final int viewUl;
    private final int projectionUl;
    private final int highlightColorUl;
    private final int highlightFactorUl;

    private final AllocatedMeshData testMeshData;
    private final AllocatedTexture testTexture;

    public Object3dElementRenderer() throws IOException {
        try {
            this.object3dShaderProgram = AllocatedShaderProgram.fromResources(
                "/org/wmb/editor/element/Object3dElement/object_3d_renderer_vs.glsl",
                "/org/wmb/editor/element/Object3dElement/object_3d_renderer_fs.glsl"
            );
        } catch (IOException exception) {
            throw new IOException("(Object3dShaderProgram) " + exception.getMessage());
        }

        try {
            this.textureUl = object3dShaderProgram.getUniformLocation("u_texture");
            this.transformUl = object3dShaderProgram.getUniformLocation("u_transform");
            this.viewUl = object3dShaderProgram.getUniformLocation("u_view");
            this.projectionUl = object3dShaderProgram.getUniformLocation("u_projection");
            this.highlightColorUl = object3dShaderProgram.getUniformLocation("u_highlight_color");
            this.highlightFactorUl = object3dShaderProgram.getUniformLocation(
                "u_highlight_factor");
        } catch (OpenGLStateException exception) {
            this.object3dShaderProgram.delete();
            throw new OpenGLStateException("(Object3dShaderProgram) " + exception.getMessage());
        }

        try {
            this.testMeshData = new AllocatedMeshData(new float[] {
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
            }, new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
            }, new short[] {
                0, 1, 2,
                2, 3, 0
            });
        } catch(OpenGLStateException exception) {
            this.object3dShaderProgram.delete();
            throw new OpenGLStateException("(TestMeshData) " + exception.getMessage());
        }

        try {
            this.testTexture = new AllocatedTexture(TextureUtil.getDebugBufferedImage());
        } catch (OpenGLStateException exception) {
            this.object3dShaderProgram.delete();
            this.testMeshData.delete();
            throw new OpenGLStateException("(TestTexture) " + exception.getMessage());
        }
    }

    public void delete() {
        this.object3dShaderProgram.delete();
        this.testMeshData.delete();
        this.testTexture.delete();
    }

    public void preparePipeline(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.object3dShaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
    }

    public void uniformCamera(Camera camera, float aspect) {
        AllocatedShaderProgram.uniformMat4(this.viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(this.projectionUl, camera.getProjectionMatrix(aspect));
    }

    public void render(Object3dElement element, Color highlight, float factor) {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.testTexture.getId());
        GL30.glBindVertexArray(this.testMeshData.getId());

        AllocatedShaderProgram.uniformMat4(this.transformUl, element.getTransform().getAsMatrix());
        AllocatedShaderProgram.uniformColor(this.highlightColorUl, highlight);
        GL30.glUniform1f(this.highlightFactorUl, factor);

        GL30.glDrawElements(GL30.GL_TRIANGLES, this.testMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glBindVertexArray(0);
    }

    public void resetPipeline() {
        GL30.glUseProgram(0);
    }
}
