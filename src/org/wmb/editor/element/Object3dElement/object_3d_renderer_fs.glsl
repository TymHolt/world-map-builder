#version 330 core

in vec2 p_texture_pos;

uniform sampler2D u_texture;

out vec4 o_pixel_color;

void main()
{
    o_pixel_color = texture(u_texture, p_texture_pos);
}