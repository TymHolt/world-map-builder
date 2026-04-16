package org.wmb.editor.element.Object3dElement;

import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.ResourceManager;
import org.wmb.rendering.*;

import java.io.IOException;

public class Object3dElementRenderer {

    private static final String TAG = "Object3dElementRenderer";

    private final Object3dElementShader shader;

    public Object3dElementRenderer() throws IOException {
        try {
            this.shader = new Object3dElementShader();
        } catch (IOException exception) {
            Log.error(TAG, "Shader failed to load");
            throw exception;
        }
    }

    public void delete() {
        this.shader.delete();
    }

    public void preparePipeline(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        this.shader.use();
        this.shader.texture.uniform(0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
    }

    public void uniformCamera(Camera camera, float aspect) {
        this.shader.camera.uniform(camera, aspect);
    }

    public void render(Object3dElement element, Color highlight, float factor, ResourceManager resourceManager) {
        final AllocatedMeshData model = resourceManager.getModel(element.modelPath);
        final AllocatedTexture texture = resourceManager.getTexture(element.texturePath);

        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glBindVertexArray(model.getId());

        this.shader.transformMatrix.uniform(element.getTransform().getAsMatrix());
        this.shader.highlightColor.uniform(highlight);
        this.shader.highlightFactor.uniform(factor);

        GL30.glDrawElements(GL30.GL_TRIANGLES, model.vertexCount,
            GL30.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    public void resetPipeline() {
        GL30.glUseProgram(0);
    }
}
