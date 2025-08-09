package resources.baked;

public class ObjectFS {

    public static final String content =
        "#version 330 core\n" +
        "\n" +
        "in vec2 p_texCoord;\n" +
        "\n" +
        "uniform sampler2D u_texture;\n" +
        "\n" +
        "out vec4 o_color;\n" +
        "\n" +
        "void main() {\n" +
        "    o_color = texture(u_texture, p_texCoord);\n" +
        "}";
}
