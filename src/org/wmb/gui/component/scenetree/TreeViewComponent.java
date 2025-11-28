package org.wmb.gui.component.scenetree;

import org.wmb.WmbContext;
import org.wmb.editor.element.Element;
import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Component;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.DynamicBounds;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.data.Position;
import org.wmb.gui.icon.Icon;
import org.wmb.gui.input.ClickAction;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseClickEvent;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Objects;

final class TreeViewComponent extends Component {

    private static final int childIndent = 20;

    private final WmbContext context;
    private final SceneTree sceneTree;
    private int elementHeight;

    public TreeViewComponent(WmbContext context) {
        super();
        setBackground(Theme.BACKGROUND);

        this.context = context;
        this.sceneTree = new SceneTree(this.context.getScene());
        this.elementHeight = 1;
    }

    public void onElementAdd() {
        this.sceneTree.onElementAdd();
    }

    public void onElementRemove(Element element) {
        Objects.requireNonNull(element, "Element is null");
        this.sceneTree.onElementRemove(element);
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        destination.width = screenSize.width / 9;
        destination.height = 1;
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        super.onMouseClick(event);

        if (event.action != ClickAction.PRESS || event.button != MouseButton.LEFT)
            return;

        final Bounds innerBounds = getInnerBounds();
        final int innerY = event.y - innerBounds.getY();
        final int elementClickedIndex = innerY / this.elementHeight;
        final java.util.List<TreeNode> tree = this.sceneTree.getTree();

        if (innerY < 0 || elementClickedIndex >= tree.size())
            return;

        final TreeNode treeNode = tree.get(elementClickedIndex);
        final int iconSize = this.elementHeight;
        final int collapseIconX = innerBounds.getX() + (treeNode.indentLevel * childIndent);

        final boolean collapseAction = event.x >= collapseIconX &&
            event.x < collapseIconX + iconSize && treeNode.hasChildren;

        if (collapseAction)
            this.sceneTree.switchCollapsed(treeNode.element);
        else
            this.context.setSelectedElement(treeNode.element);
    }

    @Override
    public org.wmb.gui.input.Cursor getCursor(int mouseX, int mouseY) {
        final Bounds innerBounds = getInnerBounds();
        final int innerY = mouseY - innerBounds.getY();
        final int elementClickedIndex = innerY / this.elementHeight;
        final List<TreeNode> tree = this.sceneTree.getTree();

        if (innerY < 0 || elementClickedIndex >= tree.size())
            return org.wmb.gui.input.Cursor.DEFAULT;

        return Cursor.HAND;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final Bounds innerBounds = getInnerBounds();
        final int innerBoundsX = innerBounds.getX();
        int y = innerBounds.getY();

        final Position mouse = this.context.getWindow().getMousePosition();
        int mouseY = -1;

        if (innerBounds.contains(mouse))
            mouseY = mouse.getY();

        this.elementHeight = Theme.FONT_PLAIN.textHeight;
        for (TreeNode treeNode : this.sceneTree.getTree()) {
            final DynamicBounds lineBounds = new DynamicBounds(innerBoundsX, y,
                innerBounds.getWidth(), elementHeight);

            final int nextY = y + this.elementHeight;
            if (mouseY >= y && mouseY < nextY)
                graphics.fillQuadColor(lineBounds, Theme.BACKGROUND_LIGHT);

            if (treeNode.element == this.context.getSelectedElement())
                graphics.fillQuadColor(lineBounds, Theme.HIGHLIGHT);

            int x = innerBoundsX + (treeNode.indentLevel * childIndent);
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

            graphics.fillText(treeNode.element.getName(), x, y, Theme.FOREGROUND, Theme.FONT_PLAIN);
            y = nextY;
        }
    }
}
