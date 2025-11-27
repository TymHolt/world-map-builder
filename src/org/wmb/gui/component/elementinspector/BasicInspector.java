package org.wmb.gui.component.elementinspector;

import org.wmb.editor.element.Element;
import org.wmb.gui.component.VerticalPadding;
import org.wmb.gui.component.elementinspector.controls.TextControl;

import java.util.Objects;

public class BasicInspector implements Inspector {

    private final Element element;
    private final TextControl nameControl;

    public BasicInspector(Element element) {
        Objects.requireNonNull(element, "Element is null");
        this.element = element;
        this.nameControl = new TextControl("Name");
    }

    @Override
    public void init(InspectorViewComponent inspectorView) {
        inspectorView.addComponent(new VerticalPadding(5));
        inspectorView.addComponent(this.nameControl);
    }

    @Override
    public void read() {
        this.nameControl.setText(this.element.getName());
    }

    @Override
    public void write() {
        this.element.setName(this.nameControl.getText());
    }
}
