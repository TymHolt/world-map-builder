package org.wmb.gui.component.scenetree;

import org.wmb.WmbContext;
import org.wmb.gui.Theme;
import org.wmb.gui.component.Align;
import org.wmb.gui.component.container.CompassContainerComponent;
import org.wmb.gui.component.text.Label;

public final class SceneTreeComponent extends CompassContainerComponent {

    public SceneTreeComponent(WmbContext context) {
        super();
        setBorder(0, 0, 0, 3);
        final Label label = new Label("Scene Tree", Align.CENTER, Theme.FONT_BOLD);
        label.setBackground(Theme.BACKGROUND);
        setNorth(label);
        setCenter(new TreeViewComponent(context));
    }
}
