#version 330 core

in vec2 p_texture_pos;

uniform vec4 u_color;
uniform sampler2D u_texture;
uniform float u_textured_flag;
uniform float u_mask_color_factor;

out vec4 o_pixel_color;

void main()
{
    vec4 texture_color_raw = texture(u_texture, p_texture_pos);
    vec4 texture_color_factored = texture_color_raw * (1.0 - u_mask_color_factor);
    vec4 mask_color_factored = vec4(u_color.rgb, texture_color_raw.r) * u_mask_color_factor;
    vec4 texture_color_masked = (texture_color_factored + mask_color_factored);

    vec4 texture_color_flagged = texture_color_masked * u_textured_flag;
    vec4 color_flagged = u_color * (1.0 - u_textured_flag);
    o_pixel_color = texture_color_flagged + color_flagged;
}