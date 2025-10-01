package org.wmb.rendering.gui;

import org.lwjgl.opengl.GL30;
import org.wmb.ResourceLoader;
import org.wmb.WmbInstance;
import org.wmb.rendering.*;

import java.io.IOException;

public final class GuiRenderer {

    private final WmbInstance instance;
    private final AllocatedVertexData quadVertexData;
    private final AllocatedShaderProgram quadShaderProgram;
    private final int colorUl, textureUl, texturedFlagUl, maskColorFlagUl;

    public GuiRenderer(WmbInstance instance) throws IOException {
        this.instance = instance;

        this.quadVertexData = new AllocatedVertexData(new float[] {
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f
        }, new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        }, new short[] {
                0, 1, 2,
                2, 3, 0
        });

        this.quadShaderProgram = new AllocatedShaderProgram(
            ResourceLoader.loadText("/org/wmb/rendering/gui/gui_renderer_vs.glsl"),
            ResourceLoader.loadText("/org/wmb/rendering/gui/gui_renderer_fs.glsl"));

        this.colorUl = quadShaderProgram.getUniformLocation("u_color");
        this.textureUl = quadShaderProgram.getUniformLocation("u_texture");
        this.texturedFlagUl = quadShaderProgram.getUniformLocation("u_texturedFlag");
        this.maskColorFlagUl = quadShaderProgram.getUniformLocation("u_maskColorFlag");
    }

    public void delete() {
        this.quadVertexData.delete();
        this.quadShaderProgram.delete();
    }

    public void begin() {
        GL30.glUseProgram(this.quadShaderProgram.getId());
        GL30.glUseProgram(this.quadShaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glBindVertexArray(this.quadVertexData.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
    }

    public void fillQuad(int x, int y, int width, int height, Color color) {
        correctViewport(x, y, width, height);
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuad(int x, int y, int width, int height, ITexture texture) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuad(int x, int y, int width, int height, ITexture texture, Color color) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void end() {
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    private void correctViewport(int x, int y, int width, int height) {
        GL30.glViewport(x, this.instance.getWindowSize().height - (y + height), width, height);
    }
}
