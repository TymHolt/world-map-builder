package org.wmb.gui.component.menu;

import org.wmb.gui.component.Component;
import org.wmb.gui.component.container.ContainerComponent;
import org.wmb.gui.data.DynamicSize;

public final class Menu extends ContainerComponent {

    @Override
    public void getRequestedSize(DynamicSize destination) {
        int maxWidth = 0;
        int totalHeight = 0;

        final DynamicSize componentSize = new DynamicSize(0, 0);
        for (Component component : this.getComponentList()) {
            component.getRequestedSize(componentSize);
            maxWidth = Math.max(maxWidth, componentSize.width);
            totalHeight += componentSize.height;
        }

        destination.width = maxWidth;
        destination.height = totalHeight;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int currentY = y;
        final DynamicSize requestedSize = getRequestedSize();
        final DynamicSize componentSize = new DynamicSize(0, 0);
        for (Component component : this.getComponentList()) {
            component.getRequestedSize(componentSize);
            component.setBounds(x, currentY, requestedSize.width, componentSize.height);
            currentY += componentSize.height;
        }
    }

    public void popAt(int x, int y) {
        final DynamicSize requestedSize = getRequestedSize();
        setBounds(x, y, requestedSize.width, requestedSize.height);
    }
}
