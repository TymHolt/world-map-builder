package org.wmb.gui.component.elementinspector;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Component;

import java.awt.*;

public class InspectorViewComponent extends Component {

    public InspectorViewComponent() {
        setBackground(Theme.BACKGROUND);
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
