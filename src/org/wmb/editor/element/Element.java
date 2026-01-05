package org.wmb.editor.element;

import org.wmb.WmbContext;
import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.icon.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Element {

    private final WmbContext context;
    private String name;
    private Element parent;
    private final List<Element> children;

    public Element(String name, Element parent, WmbContext context) {
        Objects.requireNonNull(context);
        this.context = context;
        setName(name);
        setParent(parent);
        this.children = new ArrayList<>();
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "Name is null");
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setParent(Element element) {
        this.parent = element;
    }

    public Element getParent() {
        return this.parent;
    }

    public List<Element> getChildren() {
        return this.children;
    }

    public Icon getIcon() {
        return Icon.FRAME;
    }

    public WmbContext getContext() {
        return this.context;
    }

    public abstract Inspector getInspector();
}
