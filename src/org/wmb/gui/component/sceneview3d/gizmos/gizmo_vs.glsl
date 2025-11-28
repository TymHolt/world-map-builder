#version 330 core

layout(location = 0) in vec3 i_vertex_pos;
layout(location = 1) in vec3 i_color;

uniform mat4 u_transform;
uniform mat4 u_view;
uniform mat4 u_projection;

out vec3 p_color;

void main()
{
    p_color = i_color;
    gl_Position = u_projection * u_view * u_transform * vec4(i_vertex_pos, 1.0);
}