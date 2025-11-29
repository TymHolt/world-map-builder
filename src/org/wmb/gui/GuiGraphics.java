package org.wmb.gui;

import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.WmbContext;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.Size;
import org.wmb.gui.font.AllocatedFont;
import org.wmb.gui.font.AllocatedGlyph;
import org.wmb.gui.font.FontDefinition;
import org.wmb.gui.icon.AllocatedIcons;
import org.wmb.gui.icon.Icon;
import org.wmb.rendering.*;
import org.wmb.rendering.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class GuiGraphics {

    private static class CachedFont {

        final AllocatedFont allocatedFont;
        private long lastUsedTime;

        CachedFont(AllocatedFont allocatedFont) {
            this.allocatedFont = allocatedFont;
            rememberUse();
        }

        void rememberUse() {
            this.lastUsedTime = System.currentTimeMillis();
        }

        long timeSinceUse() {
            return System.currentTimeMillis() - this.lastUsedTime;
        }
    }

    private final WmbContext context;
    private final int antiAliasLevel;
    private AllocatedFramebuffer framebuffer;
    private int lastWidth;
    private int lastHeight;
    private final HashMap<FontDefinition, CachedFont> cachedFonts;
    private final List<FontDefinition> fontAllocationQueue;
    private final AllocatedMeshData quadMeshData;
    private final AllocatedShaderProgram quadShaderProgram;
    private final AllocatedIcons icons;
    private final int colorUl;
    private final int textureUl;
    private final int texturedFlagUl;
    private final int maskColorFactorUl;

    GuiGraphics(WmbContext context, int antiAliasLevel) throws IOException {
        Objects.requireNonNull(context, "Context is null");
        this.context = context;

        if (antiAliasLevel < 1)
            throw new IllegalArgumentException("Antialias level must be at leas 1");
        this.antiAliasLevel = antiAliasLevel;

        try {
            this.quadShaderProgram = AllocatedShaderProgram.fromResources(
                "/org/wmb/gui/gui_graphics_quad_vs.glsl",
                "/org/wmb/gui/gui_graphics_quad_fs.glsl"
            );
        } catch (IOException exception) {
            throw new IOException("(QuadShaderProgram) " + exception.getMessage());
        }

        try {
            this.colorUl = quadShaderProgram.getUniformLocation("u_color");
            this.textureUl = quadShaderProgram.getUniformLocation("u_texture");
            this.texturedFlagUl = quadShaderProgram.getUniformLocation("u_textured_flag");
            this.maskColorFactorUl = quadShaderProgram.getUniformLocation("u_mask_color_factor");
        } catch (OpenGLStateException exception) {
            this.quadShaderProgram.delete();
            throw new OpenGLStateException("(QuadShaderProgram) " + exception.getMessage());
        }

        try {
            this.icons = new AllocatedIcons();
        } catch (IOException exception) {
            this.quadShaderProgram.delete();
            throw new IOException("(Icons) " + exception.getMessage());
        }

        try {
            final MeshDataDescription meshDataDescription = new MeshDataDescription();
            meshDataDescription.addDataArray(3, new float[] {
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f
            });
            meshDataDescription.addDataArray(2, new float[] {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
            });
            meshDataDescription.setIndexArray(new short[] {
                0, 1, 2,
                2, 3, 0
            });
            this.quadMeshData = new AllocatedMeshData(meshDataDescription);
        } catch(OpenGLStateException exception) {
            this.quadShaderProgram.delete();
            this.icons.delete();
            throw new OpenGLStateException("(QuadMeshData) " + exception.getMessage());
        }

        try {
            this.framebuffer = new AllocatedFramebuffer(2, 2);
        } catch (OpenGLStateException exception) {
            this.quadShaderProgram.delete();
            this.icons.delete();
            this.quadMeshData.delete();
            throw new OpenGLStateException("(Framebuffer) " + exception.getMessage());
        }

        this.cachedFonts = new HashMap<>();
        this.fontAllocationQueue = new ArrayList<>();
        this.lastWidth = -1;
        this.lastHeight = -1;
    }

    void preparePipeline() throws OpenGLStateException {
        final Size windowSize = this.context.getWindow().getSize();
        final int windowWidth = windowSize.getWidth();
        final int windowHeight = windowSize.getHeight();
        if (windowWidth != this.lastWidth || windowHeight != this.lastHeight) {
            final int framebufferWidth = windowWidth * this.antiAliasLevel;
            final int framebufferHeight = windowHeight * this.antiAliasLevel;
            try {
                resizeFramebuffer(framebufferWidth, framebufferHeight);
            } catch (OpenGLStateException exception) {
                Log.debug("New framebuffer size: " + framebufferWidth + "x" + framebufferHeight);
                throw new OpenGLStateException("(Framebuffer resize)" + exception);
            }

            this.lastWidth = windowWidth;
            this.lastHeight = windowHeight;
        }

        try {
            for (FontDefinition fontDefinition : this.fontAllocationQueue) {
                final AllocatedFont allocatedFont = fontDefinition.allocate();
                this.cachedFonts.put(fontDefinition, new CachedFont(allocatedFont));
            }
        } catch (OpenGLStateException exception) {
            throw new OpenGLStateException("(Font allocation)" + exception);
        }

        this.fontAllocationQueue.clear();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebuffer.getFboId());

        GL30.glUseProgram(this.quadShaderProgram.getId());
        GL30.glUniform1i(this.textureUl, 0);
        GL30.glBindVertexArray(this.quadMeshData.getId());
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glEnable(GL30.GL_MULTISAMPLE);
    }

    private static final long resourceKeepTime = 60L * 1000L; // 1 minute

    void resetPipeline() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        final Size windowSize = this.context.getWindow().getSize();
        GL30.glViewport(0, 0, windowSize.getWidth(), windowSize.getHeight());
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.framebuffer.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisable(GL30.GL_BLEND);
        GL30.glDisable(GL30.GL_MULTISAMPLE);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);

        for (FontDefinition definition : this.cachedFonts.keySet()) {
            final CachedFont font = this.cachedFonts.get(definition);

            if (font.timeSinceUse() > resourceKeepTime) {
                this.cachedFonts.remove(definition);
                font.allocatedFont.delete();
                break; // Only deallocate one resource per frame
            }
        }
    }

    private void resizeFramebuffer(int width, int height) throws OpenGLStateException {
        // In case the allocation fails we delete the old framebuffer afterward
        final AllocatedFramebuffer newFramebuffer = new AllocatedFramebuffer(width, height);
        this.framebuffer.delete();
        this.framebuffer = newFramebuffer;
    }

    void delete() {
        this.quadShaderProgram.delete();
        this.icons.delete();
        this.quadMeshData.delete();
        this.framebuffer.delete();

        for (FontDefinition definition : this.cachedFonts.keySet())
            this.cachedFonts.get(definition).allocatedFont.delete();
        this.cachedFonts.clear();
    }

    public void clear() {
        GL30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);
    }

    public void fillQuadColor(int x, int y, int width, int height, Color color) {
        correctViewport(x, y, width, height);
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadColor(Bounds bounds, Color color) {
        fillQuadColor(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), color);
    }

    public void fillQuadTexture(int x, int y, int width, int height, ITexture texture) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadTexture(Bounds bounds, ITexture texture) {
        fillQuadTexture(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(),
            texture);
    }

    public void fillQuadMask(int x, int y, int width, int height, ITexture texture, Color color) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadMask(Bounds bounds, ITexture texture, Color color) {
        fillQuadMask(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(),
            texture, color);
    }

    public void fillQuadIcon(int x, int y, int width, int height, Icon icon, Color color) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.icons.getIconTexture(icon).getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadIcon(Bounds bounds, Icon icon, Color color) {
        correctViewport(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.icons.getIconTexture(icon).getId());
        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 1.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadIcon(int x, int y, int width, int height, Icon icon) {
        correctViewport(x, y, width, height);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.icons.getIconTexture(icon).getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillQuadIcon(Bounds bounds, Icon icon) {
        correctViewport(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.icons.getIconTexture(icon).getId());
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 0.0f);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
            GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void fillText(String text, int x, int y, Color color, FontDefinition fontDefinition) {
        Objects.requireNonNull(color, "Color is null");
        Objects.requireNonNull(fontDefinition, "Font is null");

        if (text == null)
            text = "null";

        AllocatedShaderProgram.uniformColor(this.colorUl, color);
        GL30.glUniform1f(this.texturedFlagUl, 1.0f);
        GL30.glUniform1f(this.maskColorFactorUl, 1.0f);

        final AllocatedFont allocatedFont;
        if (this.cachedFonts.containsKey(fontDefinition)) {
            final CachedFont cachedFont = this.cachedFonts.get(fontDefinition);
            allocatedFont = cachedFont.allocatedFont;
            cachedFont.rememberUse();
        } else {
            if (!this.fontAllocationQueue.contains(fontDefinition))
                this.fontAllocationQueue.add(fontDefinition);
            return;
        }

        int currentX = x;
        final int textLength = text.length();
        for (int index = 0; index < textLength; index++) {
            final char c = text.charAt(index);
            final AllocatedGlyph glyph = allocatedFont.getGlyph(c);
            if (glyph == null)
                continue;

            correctViewport(currentX, y, glyph.width, glyph.height);
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, glyph.getId());
            GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadMeshData.vertexCount,
                GL30.GL_UNSIGNED_SHORT, 0);

            currentX += glyph.width + allocatedFont.getLeading();
        }
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
