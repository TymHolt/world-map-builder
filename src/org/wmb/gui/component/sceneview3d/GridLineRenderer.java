package org.wmb.gui.component.sceneview3d;

import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.rendering.AllocatedLineData;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.Camera;
import org.wmb.rendering.Color;
import org.wmb.rendering.OpenGLStateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class GridLineRenderer {

    private static final String TAG = "GridLineRenderer";

    private final AllocatedShaderProgram gridLineShaderProgram;
    private final AllocatedLineData lineData;
    private final int projectionUl;
    private final int viewUl;
    private final int colorUl;

    GridLineRenderer(int size) throws IOException {
        if (size < 1)
            throw new IllegalArgumentException("Size is less than 1");

        try {
            this.gridLineShaderProgram = AllocatedShaderProgram.fromResources(
                "/org/wmb/gui/component/sceneview3d/grid_line_vs.glsl",
                "/org/wmb/gui/component/sceneview3d/grid_line_fs.glsl"
            );
        } catch (IOException exception) {
            Log.error(TAG, "Shader program failed to load");
            throw exception;
        }

        try {
            this.projectionUl = this.gridLineShaderProgram.getUniformLocation("u_projection");
            this.viewUl = this.gridLineShaderProgram.getUniformLocation("u_view");
            this.colorUl = this.gridLineShaderProgram.getUniformLocation("u_color");
        } catch (OpenGLStateException exception) {
            this.gridLineShaderProgram.delete();
            Log.error(TAG, "Shader program failed to resolve uniform location");
            throw exception;
        }

        try {
            this.lineData = new AllocatedLineData(generateLineData(size));
        } catch (OpenGLStateException exception) {
            this.gridLineShaderProgram.delete();
            Log.error(TAG, "Line data failed to load");
            throw exception;
        }
    }

    private static float[] generateLineData(int size) {
        final List<Float> dataList = new ArrayList<>();
        for (int count = -size; count <= size; count++) {
            dataList.add((float) count);
            dataList.add(0.0f);
            dataList.add((float) size);
            dataList.add((float) count);
            dataList.add(0.0f);
            dataList.add((float) -size);

            dataList.add((float) size);
            dataList.add(0.0f);
            dataList.add((float) count);
            dataList.add((float) -size);
            dataList.add(0.0f);
            dataList.add((float) count);
        }

        final float[] data = new float[dataList.size()];
        int index = 0;
        for (Float value : dataList)
            data[index++] = value;

        return data;
    }

    void render(int x, int y, int width, int height, Camera camera, float aspect, Color color) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.gridLineShaderProgram.getId());
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);

        AllocatedShaderProgram.uniformMat4(this.viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(this.projectionUl, camera.getProjectionMatrix(aspect));
        AllocatedShaderProgram.uniformColor(this.colorUl, color);

        GL30.glBindVertexArray(this.lineData.getId());
        GL30.glDrawArrays(GL30.GL_LINES, 0, this.lineData.vertexCount);

        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    void delete() {
        this.lineData.delete();
    }
}
