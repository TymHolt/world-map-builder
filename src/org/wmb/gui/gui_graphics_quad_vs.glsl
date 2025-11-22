#version 330 core

layout(location = 0) in vec3 i_pos;
layout(location = 01) in vec2 i_texCoord;

out vec2 p_texCoord;

void main() {
    p_texCoord = i_texCoord;
    gl_Position = vec4(i_pos, 1.0);
}