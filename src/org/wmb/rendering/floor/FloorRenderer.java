package org.wmb.rendering.floor;

import org.lwjgl.opengl.GL30;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.Camera;

public final class FloorRenderer {

    private final AllocatedVertexData floorVertexData;
    private final AllocatedShaderProgram shaderProgram;
    private final int worldPosUl, viewUl, projectionUl, colorUl, lineWidthUl;

    public FloorRenderer(float floorSize) {
        this.floorVertexData = new AllocatedVertexData(new float[] {
            -floorSize, 0.0f, -floorSize,
            -floorSize, 0.0f, floorSize,
            floorSize, 0.0f, floorSize,
            floorSize, 0.0f, -floorSize,
        }, new float[] {
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
        }, new short[] {
            0, 1, 2,
            2, 3, 0
        });

        this.shaderProgram = new AllocatedShaderProgram(
            "#version 330 core\n" +
                    "layout(location=0) in vec3 i_pos;\n" +
                    "uniform vec3 u_worldPos;\n" +
                    "uniform mat4 u_view;\n" +
                    "uniform mat4 u_projection;\n" +
                    "out vec3 p_worldPos;\n" +
                    "void main() {\n" +
                    "    p_worldPos = i_pos + u_worldPos;\n" +
                    "    gl_Position = u_projection * u_view * vec4(i_pos + u_worldPos, 1.0);\n" +
                    "}",
            "#version 330 core\n" +
                    "in vec3 p_worldPos;\n" +
                    "uniform vec3 u_color;\n" +
                    "uniform float u_lineWidth;\n" +
                    "out vec4 o_color;\n" +
                    "float getLocal(float value) {\n" +
                    "    return value - floor(value);\n" +
                    "}\n" +
                    "bool isOuter(float value, float range) {\n" +
                    "    return value <= range || value >= 1.0 - range;\n" +
                    "}\n" +
                    "void main() {\n" +
                    "    float localX = getLocal(p_worldPos.x);\n" +
                    "    float localZ = getLocal(p_worldPos.z);\n" +
                    "    if (!isOuter(localX, u_lineWidth) && !isOuter(localZ, u_lineWidth))\n" +
                    "        discard;" +
                    "    if (abs(p_worldPos.x) <= u_lineWidth)\n" +
                    "        o_color = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "    else if (abs(p_worldPos.z) <= u_lineWidth)\n" +
                    "        o_color = vec4(0.0, 1.0, 0.0, 1.0);\n" +
                    "    else\n" +
                    "        o_color = vec4(u_color, 1.0);\n" +
                    "}"
        );

        this.worldPosUl = shaderProgram.getUniformLocation("u_worldPos");
        this.viewUl = shaderProgram.getUniformLocation("u_view");
        this.projectionUl = shaderProgram.getUniformLocation("u_projection");
        this.colorUl = shaderProgram.getUniformLocation("u_color");
        this.lineWidthUl = shaderProgram.getUniformLocation("u_lineWidth");
    }

    public void delete() {
        this.floorVertexData.delete();
        this.shaderProgram.delete();
    }

    public void begin(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(shaderProgram.getId());
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

    public void renderGuideLines(Camera camera, float aspect, float lineWidth, float red, float green, float blue) {
        AllocatedShaderProgram.uniformMat4(viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(projectionUl, camera.getProjectionMatrix(aspect));

        GL30.glBindVertexArray(this.floorVertexData.getId());
        GL30.glEnableVertexAttribArray(0);

        GL30.glUniform3f(this.colorUl, red, green, blue);
        GL30.glUniform1f(this.lineWidthUl, lineWidth);
        GL30.glUniform3f(this.worldPosUl, camera.getX(), 0.0f, camera.getZ());
        GL30.glDrawElements(GL30.GL_TRIANGLES, this.floorVertexData.getVertexCount(),
                GL30.GL_UNSIGNED_SHORT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void end() {
        GL30.glUseProgram(0);
    }
}
