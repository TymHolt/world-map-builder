package org.wmb.gui.component.scenetree;

import org.wmb.WmbContext;
import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;
import org.wmb.gui.Theme;
import org.wmb.gui.GuiGraphics;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.Component;
import org.wmb.gui.icon.Icon;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseButtonAction;
import org.wmb.gui.input.MouseClickEvent;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public final class SceneTreeComponent extends Component {

    private static final int childIndent = 20;

    private final WmbContext context;
    private final SceneTree sceneTree;
    private int elementHeight;

    public SceneTreeComponent(WmbContext context) {
        setBackground(Theme.BACKGROUND);

        this.context = context;
        this.sceneTree = new SceneTree(this.context.getScene());
        this.elementHeight = 1;

        final Border border = new Border(3, Theme.BORDER);
        border.setTop(0);
        border.setLeft(0);
        border.setBottom(0);
        setBorder(border);
    }

    public void onElementAdd() {
        this.sceneTree.onElementAdd();
    }

    public void onElementRemove(Element element) {
        Objects.requireNonNull(element, "Element is null");
        this.sceneTree.onElementRemove(element);
    }

    @Override
    public Dimension getRequestedSize() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension(screenSize.width / 9, 1);
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        super.onMouseClick(event);

        if (event.action != MouseButtonAction.PRESS || event.button != MouseButton.LEFT)
            return;

        final Rectangle innerBounds = getBorder().getInner(getBounds());
        final int innerY = event.y - innerBounds.y;
        final int elementClickedIndex = innerY / this.elementHeight;
        final List<TreeNode> tree = this.sceneTree.getTree();

        if (elementClickedIndex >= tree.size())
            return;

        final TreeNode treeNode = tree.get(elementClickedIndex);
        final int iconSize = this.elementHeight;
        final int collapseIconX = innerBounds.x + (treeNode.indentLevel * childIndent);

        final boolean collapseAction = event.x >= collapseIconX &&
            event.x < collapseIconX + iconSize && treeNode.hasChildren;

        if (collapseAction)
            this.sceneTree.switchCollapsed(treeNode.element);
        else
            this.context.setSelectedElement(treeNode.element);
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        final Rectangle innerBounds = getBorder().getInner(getBounds());
        final int innerY = mouseY - innerBounds.y;
        final int elementClickedIndex = innerY / this.elementHeight;
        final List<TreeNode> tree = this.sceneTree.getTree();

        if (elementClickedIndex >= tree.size())
            return Cursor.DEFAULT;

        return Cursor.HAND;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final Point mouse = this.context.getWindow().getMousePosition();
        final Rectangle innerBounds = getBorder().getInner(getBounds());
        int mouseY = -1;

        if (innerBounds.contains(mouse))
            mouseY = mouse.y;

        // TODO This is a hack, should be more robust to get max text height
        this.elementHeight = graphics.getTextSize(" ").height;

        int y = innerBounds.y;
        for (TreeNode treeNode : this.sceneTree.getTree()) {
            final Rectangle lineBounds = new Rectangle(innerBounds.x, y, innerBounds.width,
                elementHeight);

            final int nextY = y + this.elementHeight;
            if (mouseY >= y && mouseY < nextY)
                graphics.fillQuadColor(lineBounds, Theme.BACKGROUND_LIGHT);

            if (treeNode.element == this.context.getSelectedElement())
                graphics.fillQuadColor(lineBounds, Theme.HIGHLIGHT);

            int x = innerBounds.x + (treeNode.indentLevel * childIndent);
            final int iconSize = this.elementHeight;
            final int iconSize2 = iconSize / 2;
            final int iconSize4 = iconSize / 4;

            if (treeNode.hasChildren) {
                final Icon icon = treeNode.collapsed ? Icon.INDICATE_RIGHT : Icon.INDICATE_DOWN;
                graphics.fillQuadIcon(x + iconSize4, y + iconSize4, iconSize2, iconSize2, icon,
                    Theme.FOREGROUND);
            }
            x += iconSize;

            final int iconSize_8_10 = (iconSize * 8) / 10;
            final int iconSize_10 = iconSize / 10;
            graphics.fillQuadIcon(x, y + iconSize_10, iconSize_8_10, iconSize_8_10,
                treeNode.element.getIcon(), Theme.ELEMENT_ICON);
            x += iconSize;

            graphics.fillText(treeNode.element.getName(), x, y, Theme.FOREGROUND);
            y = nextY;
        }
    }
}
