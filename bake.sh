#! /bin/bash

cd src/resources/baked
bake java-text ../../../src/org/wmb/rendering/floor/floor_renderer_vs.glsl FloorVS.java
bake java-text ../../../src/org/wmb/rendering/floor/floor_renderer_fs.glsl FloorFS.java

bake java-text ../../../src/org/wmb/rendering/gui/gui_renderer_vs.glsl GuiVS.java
bake java-text ../../../src/org/wmb/rendering/gui/gui_renderer_fs.glsl GuiFS.java

bake java-text ../../../src/org/wmb/rendering/object/object_renderer_vs.glsl ObjectVS.java
bake java-text ../../../src/org/wmb/rendering/object/object_renderer_fs.glsl ObjectFS.java
