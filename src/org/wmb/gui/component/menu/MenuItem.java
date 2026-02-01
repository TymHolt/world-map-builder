package org.wmb.gui.component.menu;

import org.wmb.WmbContext;
import org.wmb.gui.GuiGraphics;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.text.Label;
import org.wmb.gui.input.ClickAction;
import org.wmb.gui.input.Cursor;
import org.wmb.gui.input.MouseButton;
import org.wmb.gui.input.MouseClickEvent;

import java.util.Objects;

public final class MenuItem extends Label {

    private final WmbContext context;
    private MenuAction menuAction;

    public MenuItem(String text, WmbContext context) {
        super(text, Align.LEFT);
        Objects.requireNonNull(context, "Context is null");
        this.context = context;
        setBackground(Theme.BACKGROUND_LIGHT);
    }

    public void setMenuAction(MenuAction menuAction) {
        this.menuAction = menuAction;
    }

    @Override
    public void draw(GuiGraphics graphics) {
        if (contains(this.context.getWindow().getMousePosition()))
            setBackground(Theme.HIGHLIGHT);
        else
            setBackground(Theme.BACKGROUND_LIGHT);

        super.draw(graphics);
    }

    @Override
    public void onMouseClick(MouseClickEvent event) {
        super.onMouseClick(event);

        if (event.button == MouseButton.LEFT && event.action == ClickAction.PRESS &&
            this.menuAction != null) {
            this.context.getGui().setOpenedMenu(null);
            this.menuAction.execute();
        }
    }

    @Override
    public Cursor getCursor(int mouseX, int mouseY) {
        if (getInnerBounds().contains(mouseX, mouseY))
            return Cursor.HAND;
        else
            return super.getCursor(mouseX, mouseY);
    }
}
