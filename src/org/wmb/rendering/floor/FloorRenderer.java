package org.wmb.rendering.floor;

import org.lwjgl.opengl.GL30;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.AllocatedVertexData;
import org.wmb.rendering.Camera;
import resources.baked.FloorFS;
import resources.baked.FloorVS;

public final class FloorRenderer {

    private final AllocatedVertexData floorVertexData;
    private final AllocatedShaderProgram floorShaderProgram;
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
            0.0f, 0.0f
        }, new short[] {
            0, 1, 2,
            2, 3, 0
        });

        this.floorShaderProgram = new AllocatedShaderProgram(FloorVS.content, FloorFS.content);

        this.worldPosUl = floorShaderProgram.getUniformLocation("u_worldPos");
        this.viewUl = floorShaderProgram.getUniformLocation("u_view");
        this.projectionUl = floorShaderProgram.getUniformLocation("u_projection");
        this.colorUl = floorShaderProgram.getUniformLocation("u_color");
        this.lineWidthUl = floorShaderProgram.getUniformLocation("u_lineWidth");
    }

    public void delete() {
        this.floorVertexData.delete();
        this.floorShaderProgram.delete();
    }

    public void begin(int x, int y, int width, int height) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(floorShaderProgram.getId());
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
