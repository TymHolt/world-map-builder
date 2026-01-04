package org.wmb.gui.component.sceneview3d.gizmos;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.wmb.Log;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.rendering.AllocatedMeshData;
import org.wmb.rendering.AllocatedShaderProgram;
import org.wmb.rendering.Camera;
import org.wmb.rendering.OpenGLStateException;

import java.io.IOException;

public final class GizmoRenderer {

    private static final String TAG = "GizmoRenderer";

    private final AllocatedShaderProgram gizmoShaderProgram;
    private final int transformUl;
    private final int viewUl;
    private final int projectionUl;

    public GizmoRenderer() throws IOException {
        try {
            this.gizmoShaderProgram = AllocatedShaderProgram.fromResources(
                "/org/wmb/gui/component/sceneview3d/gizmos/gizmo_vs.glsl",
                "/org/wmb/gui/component/sceneview3d/gizmos/gizmo_fs.glsl"
            );
        } catch (IOException exception) {
            Log.error(TAG, "Shader program failed to load");
            throw exception;
        }

        try {
            this.transformUl = gizmoShaderProgram.getUniformLocation("u_transform");
            this.viewUl = gizmoShaderProgram.getUniformLocation("u_view");
            this.projectionUl = gizmoShaderProgram.getUniformLocation("u_projection");
        } catch (OpenGLStateException exception) {
            this.gizmoShaderProgram.delete();
            Log.error(TAG, "Shader program failed to resolve uniform location");
            throw exception;
        }
    }

    public void delete() {
        this.gizmoShaderProgram.delete();
    }

    public void preparePipeline(int x, int y, int width, int height, Camera camera, float aspect) {
        GL30.glViewport(x, y, width, height);
        GL30.glUseProgram(this.gizmoShaderProgram.getId());
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
        AllocatedShaderProgram.uniformMat4(this.viewUl, camera.getViewMatrix());
        AllocatedShaderProgram.uniformMat4(this.projectionUl, camera.getProjectionMatrix(aspect));
    }

    public void renderGizmo(Gizmo gizmo, Object3dElement object3dElement) {
        final AllocatedMeshData gizmoMesh = gizmo.getControlMesh();
        GL30.glBindVertexArray(gizmoMesh.getId());

        final Matrix4f transform = new Matrix4f();
        gizmo.getTransform(object3dElement, transform);
        AllocatedShaderProgram.uniformMat4(this.transformUl, transform);

        GL30.glDrawElements(GL30.GL_TRIANGLES, gizmoMesh.vertexCount,
            GL30.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    public void resetPipeline() {
        GL30.glUseProgram(0);
    }
}
