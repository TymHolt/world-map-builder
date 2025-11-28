package org.wmb.gui.component.elementinspector;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Component;
import org.wmb.gui.component.container.ContainerComponent;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.input.KeyButton;
import org.wmb.gui.input.KeyClickEvent;
import org.wmb.gui.input.MouseClickEvent;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Objects;

public class InspectorViewComponent extends ContainerComponent {

    private Inspector inspector;

    public InspectorViewComponent() {
        super();
        setBackground(Theme.BACKGROUND);
        this.inspector = new Inspector() {

            @Override
            public void init(InspectorViewComponent inspectorView) {

            }

            @Override
            public void read() {

            }

            @Override
            public void write() {

            }
        };
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        destination.width = screenSize.width / 9;
        destination.height = 1;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int currentY = y;
        for (Component component : getComponentList()) {
            final DynamicSize requestedSize = component.getRequestedSize();
            component.setBounds(x, currentY, width, requestedSize.height);
            currentY += requestedSize.height;
        }
    }

    public void setInspector(Inspector inspector) {
        Objects.requireNonNull(inspector, "Inspector is null");
        clearComponents();

        this.inspector = inspector;
        this.inspector.init(this);
        this.inspector.read();
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        final Component focusedComponent = getFocusedComponent();
        super.onMouseClick(event);

        // Focused component changed, write
        if (getFocusedComponent() != focusedComponent)
            this.inspector.write();
    }

    @Override
    public void onKeyClick(KeyClickEvent event) {
        super.onKeyClick(event);

        // Enter, apply changes
        if (event.button == KeyButton.ENTER && event.action.isPressOrRepeat()) {
            setFocusedComponent(null);
            this.inspector.write();
            return;
        }

        // Tab, apply and go to next input component
        if (event.button == KeyButton.TAB && event.action.isPressOrRepeat())
            handleTabThrough();
    }

    public void notifyReadScene() {
        this.inspector.read();
    }
}
