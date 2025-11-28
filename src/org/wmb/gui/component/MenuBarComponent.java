package org.wmb.gui.component;

import org.wmb.gui.data.DynamicSize;

public class MenuBarComponent extends Component {

    public MenuBarComponent() {
        super();
        setBorder(0, 3, 0, 0);
    }

    @Override
    public void getRequestedSize(DynamicSize destination) {
        destination.width = 1;
        destination.height = 20;
    }
}
