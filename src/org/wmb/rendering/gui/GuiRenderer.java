package org.wmb.rendering.gui;

import org.lwjgl.opengl.GL30;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.AllocatedVertexData;

public final class GuiRenderer {

    private final AllocatedVertexData quadVertexData;
    private final AllocatedShaderProgram quadShaderProgram;
    private final int colorUl;

    public GuiRenderer() {
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
                "#version 330 core\n" +
                        "layout(location=0) in vec3 i_pos;\n" +
                        "void main() {\n" +
                        "    gl_Position = vec4(i_pos, 1.0);\n" +
                        "}",
                "#version 330 core\n" +
                        "uniform vec4 u_color;\n" +
                        "out vec4 o_color;\n" +
                        "void main() {\n" +
                        "    o_color = u_color;\n" +
                        "}"
        );


        this.colorUl = quadShaderProgram.getUniformLocation("u_color");
    }

    public void delete() {
        this.quadVertexData.delete();
        this.quadShaderProgram.delete();
    }

    public void begin() {
        GL30.glUseProgram(quadShaderProgram.getId());
        GL30.glBindVertexArray(this.quadVertexData.getId());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
    }

    public void fillQuad(int x, int y, int width, int height, float r, float g, float b, float a) {
        GL30.glViewport(x, y, width, height);
        GL30.glUniform4f(colorUl, r, g, b, a);
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.quadVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);
    }

    public void end() {
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }
}
