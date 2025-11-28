package org.wmb.gui.component.elementinspector.controls;

import org.wmb.gui.component.Align;
import org.wmb.gui.component.text.Label;
import org.wmb.gui.component.container.ContainerComponent;
import org.wmb.gui.component.text.TextField;
import org.wmb.gui.data.DynamicSize;

public final class TextControl extends ContainerComponent {

    private final Label label;
    private final TextField textField;

    public TextControl(String name) {
        super();
        this.label = new Label(name, Align.LEFT);
        addComponent(this.label);
        this.textField = new TextField("", Align.CENTER);
        addComponent(this.textField);
    }

    public void setText(String text) {
        this.textField.setText(text);
    }

    public String getText() {
        return this.textField.getText();
    }

    private static final int padding = 2;

    @Override
    public void getRequestedSize(DynamicSize destination) {
        final DynamicSize labelSize = this.label.getRequestedSize();
        final DynamicSize textFieldSize = this.textField.getRequestedSize();
        destination.width = padding * 3 + labelSize.width + textFieldSize.width;
        destination.height = Math.max(labelSize.height, textFieldSize.height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        final DynamicSize labelSize = this.label.getRequestedSize();
        final DynamicSize textFieldSize = this.textField.getRequestedSize();
        final int widthToSplit = width - padding * 3;
        final int componentHeight = Math.max(labelSize.height, textFieldSize.height);

        int currentX = x + padding;
        final int labelWidth = widthToSplit / 4;
        this.label.setBounds(currentX, y, labelWidth, componentHeight);
        currentX += labelWidth + padding;

        final int textFieldWidth = (widthToSplit * 3) / 4;
        this.textField.setBounds(currentX, y, textFieldWidth, componentHeight);
    }
}
