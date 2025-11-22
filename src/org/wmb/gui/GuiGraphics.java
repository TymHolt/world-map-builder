package org.wmb.gui;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.wmb.ResourceLoader;
import org.wmb.WmbContext;
import org.wmb.gui.icon.AllocatedIcons;
import org.wmb.gui.icon.Icon;
import org.wmb.rendering.*;
import org.wmb.rendering.Color;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class GuiGraphics {

    private final WmbContext context;
    private AllocatedFramebuffer framebuffer;
    private int lastWidth;
    private int lastHeight;
    private final AllocatedVertexData quadVertexData;
    private final AllocatedShaderProgram quadShaderProgram;
    private final AllocatedFont fontPlain;
    private final AllocatedFont fontBold;
    private final AllocatedIcons icons;
    private final int colorUl, textureUl, texturedFlagUl, maskColorFlagUl, subTextureCoordUl;
    private final int antiAliasLevel;

    GuiGraphics(WmbContext context, int antiAliasLevel) throws IOException {
        Objects.requireNonNull(context, "Context is null");
        this.context = context;

        if (antiAliasLevel < 1)
            throw new IllegalArgumentException("Antialias level must be at leas 1");
        this.antiAliasLevel = antiAliasLevel;

        this.framebuffer = new AllocatedFramebuffer(2, 2);
        this.lastWidth = -1;
        this.lastHeight = -1;

        this.quadVertexData = new AllocatedVertexData(new float[] {
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
        }, new float[] {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
        }, new short[] {
            0, 1, 2,
            2, 3, 0
        });

        this.quadShaderProgram = new AllocatedShaderProgram(
            ResourceLoader.loadText("/org/wmb/gui/gui_graphics_quad_vs.glsl"),
            ResourceLoader.loadText("/org/wmb/gui/gui_graphics_quad_fs.glsl"));

        this.fontPlain = new AllocatedFont(Theme.FONT_PLAIN, (char) 256);
        this.fontBold = new AllocatedFont(Theme.FONT_BOLD, (char) 256);
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
        GL30.glEnableVertexAttribArray(1);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glEnable(GL30.GL_MULTISAMPLE);

        final Dimension windowSize = this.context.getWindow().getSize();
        if (windowSize.width != this.lastWidth || windowSize.height != this.lastHeight) {
            resizeFramebuffer(windowSize.width * this.antiAliasLevel,
                windowSize.height * this.antiAliasLevel);
            this.lastWidth = windowSize.width;
            this.lastHeight = windowSize.height;
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebuffer.getFboId());
    }

    void resetPipeline() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        final Dimension windowSize = this.context.getWindow().getSize();
        GL30.glViewport(0, 0, windowSize.width, windowSize.height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.framebuffer.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 0.0f);
        GL30.glUniform4f(this.subTextureCoordUl, 0.0f, 0.0f, 1.0f, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisable(GL30.GL_BLEND);
        GL30.glDisable(GL30.GL_MULTISAMPLE);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    private void resizeFramebuffer(int width, int height) {
        this.framebuffer.delete();
        this.framebuffer = new AllocatedFramebuffer(width, height);
    }

    void deleteResources() {
        this.quadVertexData.delete();
        this.quadShaderProgram.delete();
        this.fontPlain.delete();
        this.fontBold.delete();
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
        fillText(text, x, y, color, false);
    }

    public void fillText(String text, int x, int y, Color color, boolean bold) {
        Objects.requireNonNull(color, "Color is null");

        if (text == null)
            text = "null";

        final AllocatedFont font = bold ? this.fontBold : this.fontPlain;

        GL30.glBindTexture(GL30.GL_TEXTURE_2D, font.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFlagUl, 1.0f);

        int currentX = x;
        for (int index = 0; index < text.length(); index++) {
            final char c = text.charAt(index);
            final Dimension bounds = font.getCharSize(c);
            correctViewport(currentX, y, bounds.width, bounds.height);

            final Vector4f subTexCoords = font.getCharTexturePosition(c);
            GL30.glUniform4f(this.subTextureCoordUl, subTexCoords.x, subTexCoords.y,
                subTexCoords.z, subTexCoords.w);

            GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);

            currentX += bounds.width + font.getLeading();
        }
    }

    public Dimension getTextSize(String text) {
        return getTextSize(text, false);
    }

    public Dimension getTextSize(String text, boolean bold) {
        if (bold)
            return this.fontBold.getStringSize(text);
        else
            return this.fontPlain.getStringSize(text);
    }

    private void correctViewport(int x, int y, int width, int height) {
        x *= this.antiAliasLevel;
        y *= this.antiAliasLevel;
        width *= this.antiAliasLevel;
        height *= this.antiAliasLevel;
        final int windowHeight = this.lastHeight * this.antiAliasLevel;
        GL30.glViewport(x, windowHeight - (y + height), width, height);
    }
}
