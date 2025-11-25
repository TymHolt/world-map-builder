#version 330 core

in vec2 p_texture_pos;

uniform sampler2D u_texture;
uniform vec4 u_highlight_color;
uniform float u_highlight_factor;

out vec4 o_pixel_color;

void main()
{
    vec4 texture_color_factored = texture(u_texture, p_texture_pos) * (1.0 - u_highlight_factor);
    vec4 highlight_color_factored = u_highlight_color * u_highlight_factor;
    o_pixel_color = texture_color_factored + highlight_color_factored;
}