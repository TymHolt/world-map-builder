package org.wmb.gui.component;

import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.gui.Theme;
import org.wmb.gui.GuiGraphics;
import org.wmb.rendering.Color;

import java.awt.*;

public final class SceneTreeComponent extends Component {

    private final Scene3d scene;

    public SceneTreeComponent(Scene3d scene) {
        setBackground(Theme.BACKGROUND);

        this.scene = scene;

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

        final Point mouse = graphics.getContext().getWindow().getMousePosition();
        this.innerBounds = getBorder().getInner(getBounds());

        if (this.innerBounds.contains(mouse))
            this.mouse = mouse;
        else
            this.mouse = null;

        renderChildRecursive(this.innerBounds.x, this.innerBounds.y, this.scene, graphics);
    }

    private static final int childIndent = 20;
    private Point mouse;
    private Rectangle innerBounds;

    private void renderChildRecursive(int x, int y, Element element, GuiGraphics graphics) {
        if (element == null)
            return;

        final Dimension textSize = graphics.getTextSize(element.getName());
        final int newX = x + childIndent;
        final int newY = y + textSize.height;

        if (this.mouse != null && this.mouse.y >= y && this.mouse.y < newY)
            graphics.fillQuadColor(this.innerBounds.x, y, this.innerBounds.width,
                textSize.height, Theme.HIGHLIGHT);

        graphics.fillText(element.getName(), x, y, Theme.FOREGROUND);

        for (Element child : element.getChildren())
            renderChildRecursive(newX, newY, child, graphics);
    }
}
