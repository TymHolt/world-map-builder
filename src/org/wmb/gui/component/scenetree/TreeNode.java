package org.wmb.gui.component.scenetree;

import org.wmb.editor.element.Element;

final class TreeNode {

    public final Element element;
    public final int indentLevel;
    public final boolean hasChildren;
    public final boolean collapsed;

    TreeNode(Element element, int indentLevel, boolean hasChildren, boolean collapsed) {
        this.element = element;
        this.indentLevel = indentLevel;
        this.hasChildren = hasChildren;
        this.collapsed = collapsed;
    }
}
