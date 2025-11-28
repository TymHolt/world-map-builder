package org.wmb.gui.component.elementinspector;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.container.CompassContainerComponent;
import org.wmb.gui.component.text.Label;

public final class ElementInspectorComponent extends CompassContainerComponent {

    private final InspectorViewComponent inspectorViewComponent;

    public ElementInspectorComponent() {
        super();
        setBorder(0, 0, 3, 0);
        final Label label = new Label("Element Inspector", Align.CENTER, Theme.FONT_BOLD);
        label.setBackground(Theme.BACKGROUND);
        setNorth(label);
        this.inspectorViewComponent = new InspectorViewComponent();
        setCenter(this.inspectorViewComponent);
    }

    public void setInspector(Inspector inspector) {
        this.inspectorViewComponent.setInspector(inspector);
    }

    public void notifyReadScene() {
        this.inspectorViewComponent.notifyReadScene();
    }
}