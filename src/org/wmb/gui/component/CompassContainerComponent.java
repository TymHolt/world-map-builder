package org.wmb.gui.component;

import org.wmb.core.gui.GuiGraphics;
import org.wmb.rendering.Color;

import java.awt.*;

public class CompassContainerComponent extends Component {

    private Component north;
    private Component south;
    private Component west;
    private Component east;
    private Component center;

    public CompassContainerComponent() {
        setBorder(new Border(0, Color.BLACK));
    }

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
        recalculateBounds(x, y, width, height);
    }

    @Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        recalculateBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    private void recalculateBounds(int x, int y, int width, int height) {
        int restWidth = width;
        int restHeight = height;

        int northHeight = 0;
        if (this.north != null) {
            northHeight = Math.min(restHeight, this.north.getRequestedSize().height);
            restHeight -= northHeight;
            this.north.setBounds(x, y, width, northHeight);
        }

        int southHeight = 0;
        if (this.south != null) {
            southHeight = Math.min(restHeight, this.south.getRequestedSize().height);
            restHeight -= southHeight;
            this.south.setBounds(x, y + height - southHeight, width, restHeight);
        }

        int westWidth = 0;
        if (this.west != null) {
            westWidth = Math.min(restWidth, this.west.getRequestedSize().width);
            restWidth -= westWidth;
            this.west.setBounds(x, y + northHeight, westWidth, restHeight);
        }

        int eastWidth = 0;
        if (this.east != null) {
            eastWidth = Math.min(restWidth, this.east.getRequestedSize().width);
            restWidth -= eastWidth;
            this.east.setBounds(x + width - eastWidth, y + northHeight, eastWidth, restHeight);
        }

        if (this.center != null)
            this.center.setBounds(x + westWidth, y + northHeight, restWidth, restHeight);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        if (this.center != null)
            this.center.draw(graphics);

        if (this.east != null)
            this.east.draw(graphics);

        if (this.west != null)
            this.west.draw(graphics);

        if (this.south != null)
            this.south.draw(graphics);

        if (this.north != null)
            this.north.draw(graphics);
    }

    public void setNorth(Component component) {
        this.north = component;
    }

    public void setSouth(Component component) {
        this.south = component;
    }

    public void setWest(Component component) {
        this.west = component;
    }

    public void setEast(Component component) {
        this.east = component;
    }

    public void setCenter(Component component) {
        this.center = component;
    }
}
