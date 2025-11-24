package org.wmb.gui.component.elementinspector;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InspectorViewComponent extends Component {

    private final List<Component> controlsList;

    public InspectorViewComponent() {
        setBackground(Theme.BACKGROUND);
        this.controlsList = new ArrayList<>();
    }

    @Override
    public Dimension getRequestedSize() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension(screenSize.width / 9, 1);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        for (Component component : this.controlsList)
            component.draw(graphics);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int currentY = y;
        for (Component component : this.controlsList) {
            final Dimension requestedSize = component.getRequestedSize();
            component.setBounds(x, currentY, width, requestedSize.height);
            currentY += requestedSize.height;
        }
    }

    public void setInspector(Inspector inspector) {
        Objects.requireNonNull(inspector, "Inspector is null");
        this.controlsList.clear();
        inspector.init(this);
    }

    public void addControl(Component component) {
        Objects.requireNonNull(component, "Component is null");
        this.controlsList.add(component);
    }
}
