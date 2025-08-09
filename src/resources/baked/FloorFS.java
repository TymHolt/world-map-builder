package resources.baked;

public class FloorFS {

    public static final String content =
        "#version 330 core\n" +
        "\n" +
        "in vec3 p_worldPos;\n" +
        "\n" +
        "uniform vec3 u_color;\n" +
        "uniform float u_lineWidth;\n" +
        "\n" +
        "out vec4 o_color;\n" +
        "\n" +
        "float getLocal(float value) {\n" +
        "    return value - floor(value);\n" +
        "}\n" +
        "\n" +
        "bool isOuter(float value, float range) {\n" +
        "    return value <= range || value >= 1.0 - range;\n" +
        "}\n" +
        "\n" +
        "void main() {\n" +
        "    float localX = getLocal(p_worldPos.x);\n" +
        "    float localZ = getLocal(p_worldPos.z);\n" +
        "\n" +
        "    if (!isOuter(localX, u_lineWidth) && !isOuter(localZ, u_lineWidth))\n" +
        "        discard;\n" +
        "\n" +
        "    if (abs(p_worldPos.x) <= u_lineWidth)\n" +
        "        o_color = vec4(1.0, 0.0, 0.0, 1.0);\n" +
        "    else if (abs(p_worldPos.z) <= u_lineWidth)\n" +
        "        o_color = vec4(0.0, 1.0, 0.0, 1.0);\n" +
        "    else\n" +
        "        o_color = vec4(u_color, 1.0);\n" +
        "}";
}
