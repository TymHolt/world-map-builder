#version 330 core

in vec2 p_texCoord;

uniform sampler2D u_texture;

out vec4 o_color;

void main() {
    o_color = texture(u_texture, p_texCoord);
}