package org.wmb.gui;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.wmb.ResourceLoader;
import org.wmb.WmbContext;
import org.wmb.gui.icon.AllocatedIcons;
import org.wmb.gui.icon.Icon;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.Color;
import org.wmb.rendering.ITexture;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class GuiGraphics {

    private final WmbContext context;
    private final AllocatedVertexData quadVertexData;
    private final AllocatedShaderProgram quadShaderProgram;
    private final AllocatedFont font;
    private final AllocatedIcons icons;
    private final int colorUl, textureUl, texturedFlagUl, maskColorFlagUl, subTextureCoordUl;

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
            ResourceLoader.loadText("/org/wmb/gui/gui_graphics_quad_vs.glsl"),
            ResourceLoader.loadText("/org/wmb/gui/gui_graphics_quad_fs.glsl"));

        this.font = new AllocatedFont(Theme.font, (char) 256);
        this.icons = new AllocatedIcons();

        this.colorUl = quadShaderProgram.getUniformLocation("u_color");
        this.textureUl = quadShaderProgram.getUniformLocation("u_texture");
        this.texturedFlagUl = quadShaderProgram.getUniformLocation("u_texturedFlag");
        this.maskColorFlagUl = quadShaderProgram.getUniformLocation("u_maskColorFlag");
        this.subTextureCoordUl = quadShaderProgram.getUniformLocation("u_subTextureCoord");
    }

    void preparePipeline() {
        GL30.glUseProgram(this.quadShaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glBindVertexArray(this.quadVertexData.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glEnable(GL30.GL_MULTISAMPLE);
    }

    void resetPipeline() {
        GL30.glDisable(GL30.GL_BLEND);
        GL30.glDisable(GL30.GL_MULTISAMPLE);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    void deleteResources() {
        this.quadVertexData.delete();
        this.quadShaderProgram.delete();
        this.font.delete();
        this.icons.deleteAll();
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
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadTexture(Rectangle bounds, ITexture texture) {
        correctViewport(bounds.x, bounds.y, bounds.width, bounds.height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 0.0f);
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadMask(int x, int y, int width, int height, ITexture texture, Color color) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadMask(Rectangle bounds, ITexture texture, Color color) {
        correctViewport(bounds.x, bounds.y, bounds.width, bounds.height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadIcon(int x, int y, int width, int height, Icon icon, Color color) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.icons.getTexture(icon).getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadIcon(Rectangle bounds, Icon icon, Color color) {
        correctViewport(bounds.x, bounds.y, bounds.width, bounds.height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.icons.getTexture(icon).getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillText(String text, int x, int y, Color color) {
        Objects.requireNonNull(color, "Color is null");

        if (text == null)
            text = "null";

        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.font.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);

        int currentX = x;
        for (int index = 0; index < text.length(); index++) {
            final char c = text.charAt(index);
            final Dimension bounds = this.font.getCharSize(c);
            correctViewport(currentX, y, bounds.width, bounds.height);

            final Vector4f subTexCoords = this.font.getCharTexturePosition(c);
            GL30.glUniform4f(this.subTextureCoordUl, subTexCoords.x, subTexCoords.y,
                subTexCoords.z, subTexCoords.w);

            GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);

            currentX += bounds.width + this.font.getLeading();
        }
    }

    public Dimension getTextSize(String text) {
        return this.font.getStringSize(text);
    }

    public WmbContext getContext() {
        return this.context;
    }

    private void correctViewport(int x, int y, int width, int height) {
        final int windowHeight = this.context.getWindow().getSize().height;
        GL30.glViewport(x, windowHeight - (y + height), width, height);
    }
}
