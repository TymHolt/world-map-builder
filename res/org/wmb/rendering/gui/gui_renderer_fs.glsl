#version 330 core

in vec2 p_texCoord;

uniform vec4 u_color;
uniform sampler2D u_texture;
uniform float u_texturedFlag;

out vec4 o_color;

void main() {
    vec4 textureColor = texture(u_texture, p_texCoord);
    o_color = textureColor * u_texturedFlag + u_color * (1.0 - u_texturedFlag);
}