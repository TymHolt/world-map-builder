package org.wmb.editor;

import org.wmb.WmbContext;
import org.wmb.editor.element.Element;
import org.wmb.editor.element.ElementAddAction;
import org.wmb.gui.component.elementinspector.BasicInspector;
import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.component.menu.Menu;
import org.wmb.gui.component.menu.MenuItem;

public class Scene3d extends Element {

    public Scene3d(WmbContext context) {
        super("Scene3d", context);
    }

    @Override
    public Inspector getInspector() {
        return new BasicInspector(this);
    }

    @Override
    public void addMenuActions(Menu menu) {
        final MenuItem deleteItem = new MenuItem("Add", getContext());
        deleteItem.setMenuAction(new ElementAddAction(this, getContext()));
        menu.addComponent(deleteItem);
        super.addMenuActions(menu);
    }
}
