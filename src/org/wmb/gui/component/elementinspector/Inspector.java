package org.wmb.gui.component.elementinspector;

public interface Inspector {

    void init(InspectorViewComponent inspectorView);
    void read();
    void write();
}
