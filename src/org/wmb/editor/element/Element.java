package org.wmb.editor.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Element {

    private String name;
    private Element parent;
    private final List<Element> children;

    public Element(String name, Element parent) {
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
}
