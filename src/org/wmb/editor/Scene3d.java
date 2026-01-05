package org.wmb.editor;

import org.wmb.WmbContext;
import org.wmb.editor.element.Element;
import org.wmb.gui.component.elementinspector.BasicInspector;
import org.wmb.gui.component.elementinspector.Inspector;

public class Scene3d extends Element {

    public Scene3d(WmbContext context) {
        super("Scene3d", null, context);
    }

    @Override
    public Inspector getInspector() {
        return new BasicInspector(this);
    }
}
