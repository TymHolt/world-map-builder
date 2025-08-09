#version 330 core

in vec3 p_worldPos;

uniform vec3 u_color;
uniform float u_lineWidth;

out vec4 o_color;

float getLocal(float value) {
    return value - floor(value);
}

bool isOuter(float value, float range) {
    return value <= range || value >= 1.0 - range;
}

void main() {
    float localX = getLocal(p_worldPos.x);
    float localZ = getLocal(p_worldPos.z);

    if (!isOuter(localX, u_lineWidth) && !isOuter(localZ, u_lineWidth))
        discard;

    if (abs(p_worldPos.x) <= u_lineWidth)
        o_color = vec4(1.0, 0.0, 0.0, 1.0);
    else if (abs(p_worldPos.z) <= u_lineWidth)
        o_color = vec4(0.0, 1.0, 0.0, 1.0);
    else
        o_color = vec4(u_color, 1.0);
}