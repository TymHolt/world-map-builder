package org.wmb.gui.component.sceneview3d.gizmos;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.wmb.rendering.Camera;

abstract class GizmoAAPlane {

    public static final GizmoAAPlane XZ = new GizmoAAPlane() {

        @Override
        public Vector3f intersect(Vector4f ray, Vector3f planePosition, Camera camera) {
            final Vector3f intersection = new Vector3f();

            final float yDiff = planePosition.y - camera.getY();
            final float ySteps = yDiff / ray.y;
            intersection.x = camera.getX() + ray.x * ySteps;
            intersection.y = planePosition.y;
            intersection.z = camera.getZ() + ray.z * ySteps;

            return intersection;
        }
    };

    public static final GizmoAAPlane XY = new GizmoAAPlane() {

        @Override
        public Vector3f intersect(Vector4f ray, Vector3f planePosition, Camera camera) {
            final Vector3f intersection = new Vector3f();

            final float zDiff = planePosition.z - camera.getZ();
            final float zSteps = zDiff / ray.z;
            intersection.x = camera.getX() + ray.x * zSteps;
            intersection.y = camera.getY() + ray.y * zSteps;
            intersection.z = planePosition.z;

            return intersection;
        }
    };

    public static final GizmoAAPlane YZ = new GizmoAAPlane() {

        @Override
        public Vector3f intersect(Vector4f ray, Vector3f planePosition, Camera camera) {
            final Vector3f intersection = new Vector3f();

            final float xDiff = planePosition.x - camera.getX();
            final float xSteps = xDiff / ray.x;
            intersection.x = planePosition.x;
            intersection.y = camera.getY() + ray.y * xSteps;
            intersection.z = camera.getZ() + ray.z * xSteps;

            return intersection;
        }
    };

    private GizmoAAPlane() {

    }

    public abstract Vector3f intersect(Vector4f ray, Vector3f planePosition, Camera camera);

    public static GizmoAAPlane getBestPlane(GizmoAxis axis, Vector3f lookVector) {
        final float xzFactor = Math.max(lookVector.dot(0.0f, 1.0f, 0.0f),
            lookVector.dot(0.0f, -1.0f, 0.0f));
        final float xyFactor = Math.max(lookVector.dot(0.0f, 0.0f, 1.0f),
            lookVector.dot(0.0f, 0.0f, -1.0f));
        final float yzFactor = Math.max(lookVector.dot(1.0f, 0.0f, 0.0f),
            lookVector.dot(-1.0f, 0.0f, 0.0f));

        return switch (axis) {
            case X -> xzFactor > xyFactor ? XZ : XY;
            case Y -> xyFactor > yzFactor ? XY : YZ;
            case Z -> xzFactor > yzFactor ? XZ : YZ;
        };
    }
}
