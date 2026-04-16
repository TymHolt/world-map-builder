#version 330 core

layout(location = 0) in vec3 i_vertex_pos;
layout(location = 1) in vec2 i_texture_pos;

out vec2 p_texture_pos;

void main()
{
    p_texture_pos = i_texture_pos;
    gl_Position = vec4(i_vertex_pos, 1.0);
}