package org.wmb.gui.component.elementinspector;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.CompassContainerComponent;
import org.wmb.gui.component.Label;

import java.awt.*;

public final class ElementInspectorComponent extends CompassContainerComponent {

    public ElementInspectorComponent() {
        super();
        setBackground(Theme.BACKGROUND);
        setNorth(new Label("Element Inspector", Align.CENTER, true));
        setCenter(new InspectorViewComponent());

        final Border border = new Border(3, Theme.BORDER);
        border.setTop(0);
        border.setRight(0);
        border.setBottom(0);
        setBorder(border);
    }
}
