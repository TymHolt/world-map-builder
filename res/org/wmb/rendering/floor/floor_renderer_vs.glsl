#version 330 core

layout(location = 0) in vec3 i_pos;

uniform vec3 u_worldPos;
uniform mat4 u_view;
uniform mat4 u_projection;

out vec3 p_worldPos;

void main() {
    p_worldPos = i_pos + u_worldPos;
    gl_Position = u_projection * u_view * vec4(i_pos + u_worldPos, 1.0);
}