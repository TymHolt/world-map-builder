package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;

import java.awt.*;

public final class ElementInspectorComponent extends Component {

    public ElementInspectorComponent() {
        setBackground(Theme.BACKGROUND);

        final Border border = new Border(3, Theme.BORDER);
        border.setTop(0);
        border.setRight(0);
        border.setBottom(0);
        setBorder(border);
    }

    @Override
    public Dimension getRequestedSize() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension(screenSize.width / 9, 1);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
    }
}
