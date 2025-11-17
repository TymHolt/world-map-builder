package org.wmb.common.gui.component;

import org.wmb.common.gui.input.InputHandleHelper;
import org.wmb.common.gui.input.MouseClickEvent;
import org.wmb.common.gui.input.MouseMoveEvent;
import org.wmb.core.gui.GuiGraphics;
import org.wmb.rendering.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompassContainerComponent extends Component {

    private final InputHandleHelper inputHelper;
    private Component north;
    private Component south;
    private Component west;
    private Component east;
    private Component center;

    public CompassContainerComponent() {
        setBorder(new Border(0, Color.BLACK));
        this.inputHelper = new InputHandleHelper();
    }

    private void updateInputHelper() {
        final List<Component> components = new ArrayList<>();
        components.add(this.north);
        components.add(this.south);
        components.add(this.west);
        components.add(this.east);
        components.add(this.center);
        inputHelper.setComponents(components);
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
        updateInputHelper();
    }

    public void setSouth(Component component) {
        this.south = component;
        updateInputHelper();
    }

    public void setWest(Component component) {
        this.west = component;
        updateInputHelper();
    }

    public void setEast(Component component) {
        this.east = component;
        updateInputHelper();
    }

    public void setCenter(Component component) {
        this.center = component;
        updateInputHelper();
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        this.inputHelper.handleMouseClick(event);
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        this.inputHelper.handleMouseMove(event);
    }

    @Override
    public void onGainFocus() {
        this.inputHelper.handleGainFocus();
    }

    @Override
    public void onLooseFocus() {
        this.inputHelper.handleLooseFocus();
    }
}
