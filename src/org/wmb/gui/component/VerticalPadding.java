package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;

import java.awt.Dimension;

public final class VerticalPadding extends Component{

    private final int padding;

    public VerticalPadding(int padding) {
        super();
        this.padding = padding;
    }

    @Override
    public Dimension getRequestedSize() {
        return new Dimension(1, this.padding);
    }

    @Override
    public void draw(GuiGraphics graphics) {

    }
}
