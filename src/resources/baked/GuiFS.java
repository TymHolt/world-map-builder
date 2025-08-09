package resources.baked;

public class GuiFS {

    public static final String content =
        "#version 330 core\n" +
        "\n" +
        "uniform vec4 u_color;\n" +
        "\n" +
        "out vec4 o_color;\n" +
        "\n" +
        "void main() {\n" +
        "    o_color = u_color;\n" +
        "}";
}
