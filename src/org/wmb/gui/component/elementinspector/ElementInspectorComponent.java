package org.wmb.gui.component.elementinspector;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.CompassContainerComponent;
import org.wmb.gui.component.Label;

public final class ElementInspectorComponent extends CompassContainerComponent {

    private final InspectorViewComponent inspectorViewComponent;

    public ElementInspectorComponent() {
        super();
        setBackground(Theme.BACKGROUND);
        setNorth(new Label("Element Inspector", Align.CENTER, Theme.FONT_BOLD));
        this.inspectorViewComponent = new InspectorViewComponent();
        setCenter(this.inspectorViewComponent);

        final Border border = new Border(3, Theme.BORDER);
        border.setTop(0);
        border.setRight(0);
        border.setBottom(0);
        setBorder(border);
    }

    public void setInspector(Inspector inspector) {
        this.inspectorViewComponent.setInspector(inspector);
    }
}