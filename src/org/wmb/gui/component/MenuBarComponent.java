package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.rendering.Color;

import java.awt.*;

public class MenuBarComponent extends Component {

    public MenuBarComponent() {
        final Border border = new Border(3, Color.BLACK);
        border.setTop(0);
        border.setRight(0);
        border.setLeft(0);
        setBorder(border);
    }

    @Override
    public Dimension getRequestedSize() {
        return new Dimension(1, 20);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
    }
}
