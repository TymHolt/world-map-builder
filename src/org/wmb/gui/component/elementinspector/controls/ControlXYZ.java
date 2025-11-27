package org.wmb.gui.component.elementinspector.controls;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.text.FloatField;
import org.wmb.gui.component.text.Label;
import org.wmb.gui.component.container.ContainerComponent;
import org.wmb.rendering.Color;

import java.awt.*;

public final class ControlXYZ extends ContainerComponent {

    private final Label label;
    private final Label labelX;
    private final Label labelY;
    private final Label labelZ;
    private final FloatField fieldX;
    private final FloatField fieldY;
    private final FloatField fieldZ;

    public ControlXYZ(String title) {
        super();
        this.label = new Label(title, Align.LEFT, Theme.FONT_PLAIN);
        addComponent(label);

        this.labelX = new Label("X", Align.LEFT, Theme.FONT_BOLD);
        this.labelX.setBackground(Color.RED);
        this.fieldX = new FloatField();
        addComponent(labelX);
        addComponent(fieldX);

        this.labelY = new Label("Y", Align.LEFT, Theme.FONT_BOLD);
        this.labelY.setBackground(Color.GREEN);
        this.fieldY = new FloatField();
        addComponent(labelY);
        addComponent(fieldY);

        this.labelZ = new Label("Z", Align.LEFT, Theme.FONT_BOLD);
        this.labelZ.setBackground(Color.BLUE);
        this.fieldZ = new FloatField();
        addComponent(labelZ);
        addComponent(fieldZ);
    }

    public void setX(float x) {
        this.fieldX.setValue(x);
    }

    public float getX() {
        return this.fieldX.getValue();
    }

    public void setY(float y) {
        this.fieldY.setValue(y);
    }

    public float getY() {
        return this.fieldY.getValue();
    }

    public void setZ(float z) {
        this.fieldZ.setValue(z);
    }

    public float getZ() {
        return this.fieldZ.getValue();
    }

    private static final int padding = 2;

    @Override
    public Dimension getRequestedSize() {
        final int width = padding * 7
            + this.labelX.getRequestedSize().width
            + this.fieldX.getRequestedSize().width
            + this.labelY.getRequestedSize().width
            + this.fieldY.getRequestedSize().width
            + this.labelZ.getRequestedSize().width
            + this.fieldZ.getRequestedSize().width;
        final int height = this.label.getRequestedSize().height * 2;
        return new Dimension(width, height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        final Dimension labelXSize = this.labelX.getRequestedSize();
        final Dimension labelYSize = this.labelY.getRequestedSize();
        final Dimension labelZSize = this.labelZ.getRequestedSize();
        final int labelWidth = Math.max(labelXSize.width, Math.max(labelYSize.width,
            labelZSize.width));
        final int allHeight = Math.max(labelXSize.height, Math.max(labelYSize.height,
            labelZSize.height));
        final int fieldWidth = (width - (3 * labelWidth) - (7 * padding)) / 3;

        int currentY = y;
        this.label.setBounds(x, y, width, allHeight);
        currentY += allHeight;

        int currentX = x;
        currentX += padding;
        this.labelX.setBounds(currentX, currentY, labelWidth, allHeight);
        currentX += labelWidth + padding;
        this.fieldX.setBounds(currentX, currentY, fieldWidth, allHeight);
        currentX += fieldWidth;

        currentX += padding;
        this.labelY.setBounds(currentX, currentY, labelWidth, allHeight);
        currentX += labelWidth + padding;
        this.fieldY.setBounds(currentX, currentY, fieldWidth, allHeight);
        currentX += fieldWidth;

        currentX += padding;
        this.labelZ.setBounds(currentX, currentY, labelWidth, allHeight);
        currentX += labelWidth + padding;
        this.fieldZ.setBounds(currentX, currentY, fieldWidth, allHeight);
    }
}
