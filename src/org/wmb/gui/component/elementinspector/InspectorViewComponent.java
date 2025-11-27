package org.wmb.gui.component.elementinspector;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Component;
import org.wmb.gui.component.container.ContainerComponent;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Objects;

public class InspectorViewComponent extends ContainerComponent {

    public InspectorViewComponent() {
        setBackground(Theme.BACKGROUND);
    }

    @Override
    public Dimension getRequestedSize() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension(screenSize.width / 9, 1);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int currentY = y;
        for (Component component : getComponentList()) {
            final Dimension requestedSize = component.getRequestedSize();
            component.setBounds(x, currentY, width, requestedSize.height);
            currentY += requestedSize.height;
        }
    }

    public void setInspector(Inspector inspector) {
        Objects.requireNonNull(inspector, "Inspector is null");
        clearComponents();
        inspector.init(this);
    }
}
