package org.wmb.gui.component.sceneview3d;

import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.rendering.AllocatedLineData;
import org.wmb.rendering.Camera;
import org.wmb.rendering.Color;
import org.wmb.rendering.OpenGLStateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class GridLineRenderer {

    private static final String TAG = "GridLineRenderer";

    private final GridLineShader shader;
    private final AllocatedLineData lineData;

    GridLineRenderer(int size) throws IOException {
        if (size < 1)
            throw new IllegalArgumentException("Size is less than 1");

        try {
            this.shader = new GridLineShader();
        } catch (IOException exception) {
            Log.error(TAG, "Shader failed to load");
            throw exception;
        }

        try {
            this.lineData = new AllocatedLineData(generateLineData(size));
        } catch (OpenGLStateException exception) {
            this.shader.delete();
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
        this.shader.use();
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);

        this.shader.camera.uniform(camera, aspect);
        this.shader.color.uniform(color);

        GL30.glBindVertexArray(this.lineData.getId());
        GL30.glDrawArrays(GL30.GL_LINES, 0, this.lineData.vertexCount);

        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    void delete() {
        this.lineData.delete();
    }
}
