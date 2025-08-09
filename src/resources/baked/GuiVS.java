package resources.baked;

public class GuiVS {

    public static final String content =
        "#version 330 core\n" +
        "\n" +
        "layout(location = 0) in vec3 i_pos;\n" +
        "\n" +
        "void main() {\n" +
        "    gl_Position = vec4(i_pos, 1.0);\n" +
        "}";
}
