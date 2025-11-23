#version 330 core

layout(location = 0) in vec3 i_vertex_pos;

uniform mat4 u_view;
uniform mat4 u_projection;

void main()
{
    gl_Position = u_projection * u_view * vec4(i_vertex_pos, 1.0);
}