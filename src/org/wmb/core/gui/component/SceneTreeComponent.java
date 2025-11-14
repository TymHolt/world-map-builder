package org.wmb.core.gui.component;

import org.wmb.core.gui.Theme;
import org.wmb.core.gui.GuiGraphics;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.Component;

import java.awt.*;

public final class SceneTreeComponent extends Component {

    public SceneTreeComponent() {
        setBackground(Theme.BACKGROUND);

        final Border border = new Border(3, Theme.BORDER);
        border.setTop(0);
        border.setLeft(0);
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
