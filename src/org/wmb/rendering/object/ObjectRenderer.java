package org.wmb.rendering.object;

import org.lwjgl.opengl.GL30;
import org.wmb.ResourceLoader;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.Camera;

import java.io.IOException;

public final class ObjectRenderer {

    private final AllocatedShaderProgram shaderProgram;
    private final int textureUl, transformUl, viewUl, projectionUl;

    public ObjectRenderer() throws IOException {
        this.shaderProgram = new AllocatedShaderProgram(
            ResourceLoader.loadText("/org/wmb/rendering/object/object_renderer_vs.glsl"),
            ResourceLoader.loadText("/org/wmb/rendering/object/object_renderer_fs.glsl"));

        this.textureUl = shaderProgram.getUniformLocation("u_texture");
        this.transformUl = shaderProgram.getUniformLocation("u_transform");
        this.viewUl = shaderProgram.getUniformLocation("u_view");
        this.projectionUl = shaderProgram.getUniformLocation("u_projection");
    }

    public void delete() {
        this.shaderProgram.delete();
    }

    public void begin(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.shaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

    public void uniformCamera(Camera camera, float aspect) {
        AllocatedShaderProgram.uniformMat4(viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(projectionUl, camera.getProjectionMatrix(aspect));
    }

    public void render(IRenderObject renderObject) {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, renderObject.getTexture().getId());
        GL30.glBindVertexArray(renderObject.getVertexData().getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        AllocatedShaderProgram.uniformMat4(this.transformUl, renderObject.getTransformMatrix());
        GL30.glDrawElements(GL30.GL_TRIANGLES, renderObject.getVertexData().getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void end() {
        GL30.glUseProgram(0);
    }
}
