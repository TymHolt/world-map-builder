#version 330 core

uniform vec4 u_color;

out vec4 o_pixel_color;

void main()
{
    o_pixel_color = u_color;
}