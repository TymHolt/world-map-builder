package org.wmb.core.gui;

import org.lwjgl.opengl.GL30;
import org.wmb.ResourceLoader;
import org.wmb.core.WmbContext;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.Color;
import org.wmb.rendering.ITexture;

import java.awt.*;
import java.io.IOException;

public final class GuiGraphics {

    private final WmbContext context;
    private final AllocatedVertexData quadVertexData;
    private final AllocatedShaderProgram quadShaderProgram;
    private final int colorUl, textureUl, texturedFlagUl, maskColorFlagUl;

    GuiGraphics(WmbContext context) throws IOException {
        this.context = context;

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
            ResourceLoader.loadText("/org/wmb/core/gui/wmb_graphics_quad_vs.glsl"),
            ResourceLoader.loadText("/org/wmb/core/gui/wmb_graphics_quad_fs.glsl"));

        this.colorUl = quadShaderProgram.getUniformLocation("u_color");
        this.textureUl = quadShaderProgram.getUniformLocation("u_texture");
        this.texturedFlagUl = quadShaderProgram.getUniformLocation("u_texturedFlag");
        this.maskColorFlagUl = quadShaderProgram.getUniformLocation("u_maskColorFlag");
    }

    void preparePipeline() {
        GL30.glUseProgram(this.quadShaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glBindVertexArray(this.quadVertexData.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
    }

    void resetPipeline() {
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    void deleteResources() {
        this.quadVertexData.delete();
        this.quadShaderProgram.delete();
    }

    public void clear() {
        GL30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);
    }

    public void fillQuadColor(int x, int y, int width, int height, Color color) {
        correctViewport(x, y, width, height);
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadColor(Rectangle bounds, Color color) {
        correctViewport(bounds.x, bounds.y, bounds.width, bounds.height);
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadTexture(int x, int y, int width, int height, ITexture texture) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadTexture(Rectangle bounds, ITexture texture) {
        correctViewport(bounds.x, bounds.y, bounds.width, bounds.height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadMask(int x, int y, int width, int height, ITexture texture, Color color) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadMask(Rectangle bounds, ITexture texture, Color color) {
        correctViewport(bounds.x, bounds.y, bounds.width, bounds.height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    private void correctViewport(int x, int y, int width, int height) {
        final int windowHeight = this.context.getWindow().getSize().height;
        GL30.glViewport(x, windowHeight - (y + height), width, height);
    }
}
