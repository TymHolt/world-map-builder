#version 330 core

layout(location = 0) in vec3 i_pos;

out vec2 p_texCoord;

void main() {
    p_texCoord = vec2((i_pos.x + 1.0) / 2.0, 1.0 - ((i_pos.y + 1.0) / 2.0));
    gl_Position = vec4(i_pos, 1.0);
}