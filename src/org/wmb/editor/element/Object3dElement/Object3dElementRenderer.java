package org.wmb.editor.element.Object3dElement;

import org.lwjgl.opengl.GL30;
import org.wmb.ResourceLoader;
import org.wmb.rendering.*;

import java.io.IOException;

public class Object3dElementRenderer {

    private final AllocatedShaderProgram shaderProgram;
    private final int textureUl, transformUl, viewUl, projectionUl;

    private final AllocatedVertexData testVertexData;
    private final AllocatedTexture testTexture;

    public Object3dElementRenderer() throws IOException {
        this.shaderProgram = new AllocatedShaderProgram(
            ResourceLoader.loadText("/org/wmb/editor/element/Object3dElement/object_3d_renderer_vs.glsl"),
            ResourceLoader.loadText("/org/wmb/editor/element/Object3dElement/object_3d_renderer_fs.glsl"));

        this.textureUl = shaderProgram.getUniformLocation("u_texture");
        this.transformUl = shaderProgram.getUniformLocation("u_transform");
        this.viewUl = shaderProgram.getUniformLocation("u_view");
        this.projectionUl = shaderProgram.getUniformLocation("u_projection");

        this.testVertexData = new AllocatedVertexData(new float[] {
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

        this.testTexture = new AllocatedTexture(TextureUtil.getDebugBufferedImage());
    }

    public void delete() {
        this.shaderProgram.delete();
        this.testVertexData.delete();
        this.testTexture.delete();
    }

    public void preparePipeline(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.shaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
    }

    public void uniformCamera(Camera camera, float aspect) {
        AllocatedShaderProgram.uniformMat4(viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(projectionUl, camera.getProjectionMatrix(aspect));
    }

    public void render(Object3dElement element) {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.testTexture.getId());
        GL30.glBindVertexArray(this.testVertexData.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        AllocatedShaderProgram.uniformMat4(this.transformUl, element.getTransform().getAsMatrix());
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.testVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void resetPipeline() {
        GL30.glUseProgram(0);
    }
}
