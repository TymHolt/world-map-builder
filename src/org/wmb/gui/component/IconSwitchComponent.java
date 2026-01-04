package org.wmb.gui.component;

import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.data.Bounds;
import org.wmb.gui.data.DynamicSize;
import org.wmb.gui.icon.Icon;
import org.wmb.gui.input.ClickAction;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseClickEvent;
import org.wmb.rendering.Color;

import java.util.Objects;

public final class IconSwitchComponent extends Component {

    public interface SwitchListener {

        void onSwitch(IconSwitchComponent switchComponent);
    };

    private Icon unselectedIcon;
    private Icon selectedIcon;
    private Color unselectedIconColor;
    private Color selectedIconColor;
    private Color unselectedBackgroundColor;
    private Color selectedBackgroundColor;
    private boolean selected;
    private SwitchListener switchListener;
    private boolean unselectedGrayScaleMode;

    public IconSwitchComponent(Icon icon) {
        super();
        Objects.requireNonNull(icon, "Icon is null");

        this.unselectedIcon = icon;
        this.selectedIcon = icon;
        this.unselectedIconColor = Theme.FOREGROUND;
        this.selectedIconColor = Theme.FOREGROUND;
        this.unselectedBackgroundColor = Theme.BACKGROUND;
        this.selectedBackgroundColor = Theme.BACKGROUND_LIGHT;
        this.switchListener = null;
        this.unselectedGrayScaleMode = false;

        setBorder(1, 1, 1, 1);
        setPadding(2, 2, 2, 2);

        setSelected(false);
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        destination.set(25, 25);
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        super.onMouseClick(event);

        if (event.button != MouseButton.LEFT || event.action != ClickAction.PRESS)
            return;

        final Bounds bounds = getBounds();
        if (bounds.contains(event.x, event.y))
            setSelected(!selected);
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        final Bounds bounds = getBounds();
        return bounds.contains(mouseX, mouseY) ? Cursor.HAND : Cursor.DEFAULT;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);

        final Icon icon;
        final Color color;
        if (this.selected) {
            icon = this.selectedIcon;
            color = this.selectedIconColor;
        } else {
            icon = this.unselectedIcon;
            color = this.unselectedIconColor;
        }

        final Bounds innerBounds = getInnerBounds();
        if (icon.masked || (this.unselectedGrayScaleMode && !this.selected))
            graphics.fillQuadIcon(innerBounds, icon, color);
        else
            graphics.fillQuadIcon(innerBounds, icon);
    }

    public void setSwitchListener(SwitchListener listener) {
        this.switchListener = listener;
    }

    public void setUnselectedIcon(Icon icon) {
        Objects.requireNonNull(icon, "Icon is null");
        this.unselectedIcon = icon;
    }

    public Icon getUnselectedIcon() {
        return this.unselectedIcon;
    }

    public void setSelectedIcon(Icon icon) {
        Objects.requireNonNull(icon, "Icon is null");
        this.selectedIcon = icon;
    }

    public Icon getSelectedIcon() {
        return this.selectedIcon;
    }

    public void setUnselectedIconColor(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.unselectedIconColor = color;
    }

    public Color getUnselectedIconColor() {
        return this.unselectedIconColor;
    }

    public void setSelectedIconColor(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.selectedIconColor = color;
    }

    public Color getSelectedIconColor() {
        return this.selectedIconColor;
    }

    public void setUnselectedBackgroundColor(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.unselectedBackgroundColor = color;
    }

    public Color getUnselectedBackgroundColor() {
        return this.unselectedBackgroundColor;
    }

    public void setSelectedBackgroundColor(Color color) {
        Objects.requireNonNull(color, "Color is null");
        this.selectedBackgroundColor = color;
    }

    public Color getSelectedBackgroundColor() {
        return this.selectedBackgroundColor;
    }

    public void setSelected(boolean selected) {
        setSelected(selected, true);
    }

    public void setSelected(boolean selected, boolean notifyListener) {
        this.selected = selected;

        setBackground(selected ? this.selectedBackgroundColor : this.unselectedBackgroundColor);

        if (this.switchListener != null && notifyListener)
            this.switchListener.onSwitch(this);
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setUnselectedGrayScaleMode(boolean enabled) {
        this.unselectedGrayScaleMode = enabled;
    }
}
