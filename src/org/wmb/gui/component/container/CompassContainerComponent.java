package org.wmb.gui.component.container;

import org.wmb.gui.component.Component;
import org.wmb.gui.input.*;

import java.awt.Dimension;
import java.awt.Rectangle;

public class CompassContainerComponent extends ContainerComponent {

    private Component north;
    private Component south;
    private Component west;
    private Component east;
    private Component center;

    @Override
    public Dimension getRequestedSize() {
        Dimension northSize = new Dimension(0, 0);
        if (this.north != null)
            northSize = this.north.getRequestedSize();

        Dimension southSize = new Dimension(0, 0);
        if (this.south != null)
            southSize = this.south.getRequestedSize();

        Dimension westSize = new Dimension(0, 0);
        if (this.west != null)
            westSize = this.west.getRequestedSize();

        Dimension eastSize = new Dimension(0, 0);
        if (this.east != null)
            eastSize = this.east.getRequestedSize();

        Dimension centerSize = new Dimension(0, 0);
        if (this.center != null)
            centerSize = this.center.getRequestedSize();

        final int width = Math.max(Math.max(northSize.width, southSize.width),
            westSize.width + centerSize.width + eastSize.width);
        final int height = Math.max(Math.max(westSize.height, centerSize.height), eastSize.height)
            + northSize.height + southSize.height;

        return new Dimension(width, height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        final Rectangle innerBounds = getInnerBounds();
        int restWidth = innerBounds.width;
        int restHeight = innerBounds.height;

        int northHeight = 0;
        if (this.north != null) {
            northHeight = Math.min(restHeight, this.north.getRequestedSize().height);
            restHeight -= northHeight;
            this.north.setBounds(innerBounds.x, innerBounds.y, innerBounds.width, northHeight);
        }

        int southHeight = 0;
        if (this.south != null) {
            southHeight = Math.min(restHeight, this.south.getRequestedSize().height);
            restHeight -= southHeight;
            this.south.setBounds(innerBounds.x, innerBounds.y + innerBounds.height - southHeight,
                innerBounds.width, restHeight);
        }

        int westWidth = 0;
        if (this.west != null) {
            westWidth = Math.min(restWidth, this.west.getRequestedSize().width);
            restWidth -= westWidth;
            this.west.setBounds(innerBounds.x, innerBounds.y + northHeight, westWidth, restHeight);
        }

        int eastWidth = 0;
        if (this.east != null) {
            eastWidth = Math.min(restWidth, this.east.getRequestedSize().width);
            restWidth -= eastWidth;
            this.east.setBounds(innerBounds.x + innerBounds.width - eastWidth,
                innerBounds.y + northHeight, eastWidth, restHeight);
        }

        if (this.center != null)
            this.center.setBounds(innerBounds.x + westWidth, innerBounds.y + northHeight,
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
