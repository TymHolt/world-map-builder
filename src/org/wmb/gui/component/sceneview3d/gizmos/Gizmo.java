package org.wmb.gui.component.sceneview3d.gizmos;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.wmb.WmbContext;
import org.wmb.editor.element.Object3dElement.Object3dElement;
import org.wmb.gui.icon.Icon;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.rendering.AllocatedMeshData;
import org.wmb.rendering.Camera;

public interface Gizmo {

    AllocatedMeshData getControlMesh();
    void getTransform(Object3dElement object3dElement, Matrix4f destination);
    Icon getIcon();
    void handleMouseClick(MouseClickEvent event, Vector3f lookVector, GizmoAxis hoveredAxis);
    void handleMouseMove(Vector4f mouseRay, WmbContext context, Camera camera);
}
