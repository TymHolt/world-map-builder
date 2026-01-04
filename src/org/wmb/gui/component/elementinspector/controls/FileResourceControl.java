package org.wmb.gui.component.elementinspector.controls;

import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.IconSwitchComponent;
import org.wmb.gui.component.container.ContainerComponent;
import org.wmb.gui.component.text.Label;
import org.wmb.gui.component.text.TextField;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.icon.Icon;

import javax.swing.JFileChooser;

public final class FileResourceControl extends ContainerComponent {

    private static final String NONE_TEXT = "SELECT";

    private final Label label;
    private final TextField pathTextField;
    private final IconSwitchComponent deleteComponent;

    public FileResourceControl(String title) {
        super();
        this.label = new Label(title, Align.LEFT);
        addComponent(this.label);

        this.pathTextField = new TextField(NONE_TEXT, Align.CENTER);
        this.pathTextField.setEditAllowed(false);
        addComponent(this.pathTextField);

        this.deleteComponent = new IconSwitchComponent(Icon.CROSS);
        this.deleteComponent.setSelectedIconColor(Theme.FOREGROUND);
        this.deleteComponent.setSelectedIcon(Icon.CROSS);
        this.deleteComponent.setUnselectedIconColor(Theme.FOREGROUND);
        this.deleteComponent.setUnselectedIcon(Icon.FOLDER);
        this.deleteComponent.setSwitchListener(switchComponent -> {
            if (switchComponent.isSelected()) {
                final JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showDialog(null, "Load") == JFileChooser.APPROVE_OPTION)
                    this.pathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                else
                    switchComponent.setSelected(false, false);
            } else
                this.pathTextField.setText(NONE_TEXT);
        });
        addComponent(this.deleteComponent);
    }

    public void setSelectedPath(String path) {
        if (path == null) {
            this.pathTextField.setText(NONE_TEXT);
            this.deleteComponent.setSelected(false, false);
        } else {
            this.pathTextField.setText(path);
            this.deleteComponent.setSelected(true, false);
        }
    }

    public String getSelectedPath() {
        return this.pathTextField.getText();
    }

    public boolean hasSelectedPath() {
        return this.deleteComponent.isSelected();
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        final DynamicSize sizeBuffer = new DynamicSize(0, 0);

        this.label.getRequestedSize(sizeBuffer);
        final int labelWidth = sizeBuffer.width;
        final int labelHeight = sizeBuffer.height;

        this.pathTextField.getRequestedSize(sizeBuffer);
        final int pathTextFieldWidth = sizeBuffer.width;

        this.deleteComponent.getRequestedSize(sizeBuffer);
        final int deleteComponentWidth = sizeBuffer.width;

        destination.width = Math.max(labelWidth, pathTextFieldWidth + deleteComponentWidth);
        destination.height = 2 * labelHeight;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        final DynamicSize labelSize = this.label.getRequestedSize();

        int currentY = y;
        this.label.setBounds(x, currentY, width, labelSize.height);
        currentY += labelSize.height;

        int currentX = x;
        final int textFieldWidth = width - labelSize.height;
        this.pathTextField.setBounds(currentX, currentY, textFieldWidth, labelSize.height);
        currentX += textFieldWidth;

        this.deleteComponent.setBounds(currentX, currentY, labelSize.height, labelSize.height);
    }
}
