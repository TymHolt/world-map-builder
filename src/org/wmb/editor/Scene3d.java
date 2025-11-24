package org.wmb.editor;

import org.wmb.editor.element.Element;
import org.wmb.gui.component.elementinspector.Inspector;
import org.wmb.gui.component.elementinspector.InspectorViewComponent;

public class Scene3d extends Element {

    public Scene3d() {
        super("Scene3d", null);
    }

    @Override
    public Inspector getInspector() {
        return new Inspector() {

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
}
