#version 330 core

in vec3 p_color;

out vec4 o_pixel_color;

void main()
{
    o_pixel_color = vec4(p_color, 1.0);
}