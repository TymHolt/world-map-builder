package resources.baked;

public class FloorVS {

    public static final String content =
        "#version 330 core\n" +
        "\n" +
        "layout(location = 0) in vec3 i_pos;\n" +
        "\n" +
        "uniform vec3 u_worldPos;\n" +
        "uniform mat4 u_view;\n" +
        "uniform mat4 u_projection;\n" +
        "\n" +
        "out vec3 p_worldPos;\n" +
        "\n" +
        "void main() {\n" +
        "    p_worldPos = i_pos + u_worldPos;\n" +
        "    gl_Position = u_projection * u_view * vec4(i_pos + u_worldPos, 1.0);\n" +
        "}";
}
