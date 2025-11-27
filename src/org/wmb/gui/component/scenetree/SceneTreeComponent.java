package org.wmb.gui.component.scenetree;

import org.wmb.WmbContext;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.Border;
import org.wmb.gui.component.container.CompassContainerComponent;
import org.wmb.gui.component.text.Label;

public final class SceneTreeComponent extends CompassContainerComponent {

    public SceneTreeComponent(WmbContext context) {
        super();
        setBackground(Theme.BACKGROUND);
        setNorth(new Label("Scene Tree", Align.CENTER, Theme.FONT_BOLD));
        setCenter(new TreeViewComponent(context));

        final Border border = new Border(3, Theme.BORDER);
        border.setTop(0);
        border.setLeft(0);
        border.setBottom(0);
        setBorder(border);
    }
}
