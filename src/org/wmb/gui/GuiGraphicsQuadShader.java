package org.wmb.gui;

import org.wmb.Log;
import org.wmb.rendering.OpenGLStateException;
import org.wmb.rendering.shader.AllocatedShaderProgram;
import org.wmb.rendering.shader.uniform.ColorUniform;
import org.wmb.rendering.shader.uniform.FloatUniform;
import org.wmb.rendering.shader.uniform.IntUniform;

import java.io.IOException;

final class GuiGraphicsQuadShader {

    private static final String TAG = "GuiGraphicsShader";
    private static final String VS_PATH = "/org/wmb/gui/GuiGraphicsQuadShaderVS.glsl";
    private static final String FS_PATH = "/org/wmb/gui/GuiGraphicsQuadShaderFS.glsl";

    private final AllocatedShaderProgram shaderProgram;
    public final ColorUniform color;
    public final IntUniform texture;
    public final FloatUniform texturedFlag;
    public final FloatUniform maskColorFactor;

    GuiGraphicsQuadShader() throws IOException {
        try {
            this.shaderProgram = AllocatedShaderProgram.fromResources(VS_PATH, FS_PATH);
        } catch (IOException exception) {
            Log.error(TAG, "Failed to compile");
            throw exception;
        }

        try {
            this.color = new ColorUniform(this.shaderProgram.getUniformLocation("u_color"));
            this.texture = new IntUniform(this.shaderProgram.getUniformLocation("u_texture"));
            this.texturedFlag = new FloatUniform(this.shaderProgram.getUniformLocation("u_textured_flag"));
            this.maskColorFactor = new FloatUniform(this.shaderProgram.getUniformLocation("u_mask_color_factor"));
        } catch (OpenGLStateException exception) {
            this.shaderProgram.delete();
            Log.error(TAG, "Failed to resolve uniform location");
            throw exception;
        }
    }

    void use() {
        this.shaderProgram.use();
    }

    void delete() {
        this.shaderProgram.delete();
    }
}
