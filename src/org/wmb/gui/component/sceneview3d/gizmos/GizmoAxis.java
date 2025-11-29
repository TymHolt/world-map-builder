package org.wmb.gui.component.sceneview3d.gizmos;

import org.wmb.rendering.Color;

public enum GizmoAxis {

    X,
    Y,
    Z;

    public static GizmoAxis fromColor(Color color) {
        if (color.getRed() > 0)
            return X;

        if (color.getGreen() > 0)
            return Y;

        if(color.getBlue() > 0)
            return Z;

        return null;
    }
}
