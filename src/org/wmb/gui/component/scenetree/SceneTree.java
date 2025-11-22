package org.wmb.gui.component.scenetree;

import org.wmb.editor.Scene3d;
import org.wmb.editor.element.Element;

import java.util.ArrayList;
import java.util.List;

final class SceneTree {

    private final Scene3d scene;
    private final List<Element> collapsedList;
    private final List<TreeNode> treeList;

    SceneTree(Scene3d scene) {
        this.scene = scene;
        this.collapsedList = new ArrayList<>();
        this.treeList = new ArrayList<>();
        recalculateTree();
    }

    void onElementAdd() {
        recalculateTree();
    }

    void onElementRemove(Element element) {
        this.collapsedList.remove(element);
        recalculateTree();
    }

    void switchCollapsed(Element element) {
        if (this.collapsedList.contains(element))
            this.collapsedList.remove(element);
        else
            this.collapsedList.add(element);

        recalculateTree();
    }

    private void recalculateTree() {
        this.treeList.clear();
        recalculateTreeRecursive(this.scene, 0);
    }

    private void recalculateTreeRecursive(Element element, int indentLevel) {
        if (element == null)
            return;

        final List<Element> children = element.getChildren();
        final boolean collapsed = this.collapsedList.contains(element);
        treeList.add(new TreeNode(element, indentLevel, !children.isEmpty(), collapsed));

        if (collapsed)
            return;

        for (Element child : children)
            recalculateTreeRecursive(child, indentLevel + 1);
    }

    public List<TreeNode> getTree() {
        return this.treeList;
    }
}
