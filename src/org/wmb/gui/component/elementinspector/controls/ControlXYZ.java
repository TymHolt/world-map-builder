package org.wmb.gui.component.elementinspector.controls;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Component;
import org.wmb.gui.component.Label;
import org.wmb.gui.component.TextField;
import org.wmb.rendering.Color;

import java.awt.*;

public final class ControlXYZ extends Component {

    private final Label label;
    private final Label labelX;
    private final Label labelY;
    private final Label labelZ;
    private final TextField fieldX;
    private final TextField fieldY;
    private final TextField fieldZ;
    private final Component[] components;

    public ControlXYZ(String title) {
        setBackground(Color.TRANSPARENT);

        this.label = new Label(title, Align.LEFT, Theme.FONT_PLAIN);

        this.labelX = new Label("X", Align.LEFT, Theme.FONT_BOLD);
        this.labelX.setBackground(Color.RED);
        this.fieldX = new TextField();

        this.labelY = new Label("Y", Align.LEFT, Theme.FONT_BOLD);
        this.labelY.setBackground(Color.GREEN);
        this.fieldY = new TextField();

        this.labelZ = new Label("Z", Align.LEFT, Theme.FONT_BOLD);
        this.labelZ.setBackground(Color.BLUE);
        this.fieldZ = new TextField();

        this.components = new Component[] {
            this.label,
            this.labelX,
            this.fieldX,
            this.labelY,
            this.fieldY,
            this.labelZ,
            this.fieldZ
        };
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

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        for (Component component : this.components)
            component.draw(graphics);
    }
}
