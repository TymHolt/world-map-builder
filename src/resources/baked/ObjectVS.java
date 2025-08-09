package resources.baked;

public class ObjectVS {

    public static final String content =
        "#version 330 core\n" +
        "\n" +
        "layout(location = 0) in vec3 i_pos;\n" +
        "layout(location = 1) in vec2 i_texCoord;\n" +
        "\n" +
        "uniform mat4 u_transform;\n" +
        "uniform mat4 u_view;\n" +
        "uniform mat4 u_projection;\n" +
        "\n" +
        "out vec2 p_texCoord;\n" +
        "\n" +
        "void main() {\n" +
        "    p_texCoord = i_texCoord;\n" +
        "    gl_Position = u_projection * u_view * u_transform * vec4(i_pos, 1.0);\n" +
        "}";
}
