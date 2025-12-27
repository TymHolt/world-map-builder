package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.data.DynamicSize;

public final class VerticalPadding extends Component {

    private final int padding;

    public VerticalPadding(int padding) {
        super();
        this.padding = padding;
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        destination.width = 1;
        destination.height = this.padding;
    }

    @Override
    public void draw(GuiGraphics graphics) {

    }
}
