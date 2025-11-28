package org.wmb.gui.component.container;

import org.wmb.gui.component.Component;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.input.*;

public class CompassContainerComponent extends ContainerComponent {

    private Component north;
    private Component south;
    private Component west;
    private Component east;
    private Component center;

    @Override
    public void getRequestedSize(DynamicSize destination) {
        final DynamicSize northSize = new DynamicSize(0, 0);
        if (this.north != null)
            this.north.getRequestedSize(northSize);

        final DynamicSize southSize = new DynamicSize(0, 0);
        if (this.south != null)
            this.south.getRequestedSize(southSize);

        final DynamicSize westSize = new DynamicSize(0, 0);
        if (this.west != null)
            this.west.getRequestedSize(westSize);

        final DynamicSize eastSize = new DynamicSize(0, 0);
        if (this.east != null)
            this.east.getRequestedSize(eastSize);

        final DynamicSize centerSize = new DynamicSize(0, 0);
        if (this.center != null)
            this.center.getRequestedSize(centerSize);

        destination.width = Math.max(Math.max(northSize.width, southSize.width),
            westSize.width + centerSize.width + eastSize.width);
        destination.height = Math.max(Math.max(westSize.height, centerSize.height), eastSize.height)
            + northSize.height + southSize.height;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        final Bounds innerBounds = getInnerBounds();
        final int innerBoundsX = innerBounds.getX();
        final int innerBoundsY = innerBounds.getY();
        final int innerBoundsWidth = innerBounds.getWidth();
        final int innerBoundsHeight = innerBounds.getHeight();
        int restWidth = innerBoundsWidth;
        int restHeight = innerBoundsHeight;

        int northHeight = 0;
        if (this.north != null) {
            northHeight = Math.min(restHeight, this.north.getRequestedSize().height);
            restHeight -= northHeight;
            this.north.setBounds(innerBoundsX, innerBoundsY, innerBoundsWidth, northHeight);
        }

        int southHeight = 0;
        if (this.south != null) {
            southHeight = Math.min(restHeight, this.south.getRequestedSize().height);
            restHeight -= southHeight;
            this.south.setBounds(innerBoundsX, innerBoundsY + innerBoundsHeight - southHeight,
                innerBoundsWidth, restHeight);
        }

        int westWidth = 0;
        if (this.west != null) {
            westWidth = Math.min(restWidth, this.west.getRequestedSize().width);
            restWidth -= westWidth;
            this.west.setBounds(innerBoundsX, innerBoundsY + northHeight, westWidth, restHeight);
        }

        int eastWidth = 0;
        if (this.east != null) {
            eastWidth = Math.min(restWidth, this.east.getRequestedSize().width);
            restWidth -= eastWidth;
            this.east.setBounds(innerBoundsX + innerBoundsWidth - eastWidth,
                innerBoundsY + northHeight, eastWidth, restHeight);
        }

        if (this.center != null)
            this.center.setBounds(innerBoundsX + westWidth, innerBoundsY + northHeight,
                restWidth, restHeight);
    }

    public void setNorth(Component component) {
        removeComponent(this.north);
        this.north = component;

        if (component != null)
            addComponent(component);
    }

    public void setSouth(Component component) {
        removeComponent(this.south);
        this.south = component;

        if (component != null)
            addComponent(component);
    }

    public void setWest(Component component) {
        removeComponent(this.west);
        this.west = component;

        if (component != null)
            addComponent(component);
    }

    public void setEast(Component component) {
        removeComponent(this.east);
        this.east = component;

        if (component != null)
            addComponent(component);
    }

    public void setCenter(Component component) {
        removeComponent(this.center);
        this.center = component;

        if (component != null)
            addComponent(component);
    }
}
