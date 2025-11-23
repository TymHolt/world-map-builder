#version 330 core

in vec2 p_texCoord;

uniform vec4 u_color;
uniform sampler2D u_texture;
uniform vec4 u_subTextureCoord;
uniform float u_texturedFlag;
uniform float u_maskColorFlag;

out vec4 o_color;

void main() {
    vec2 texCoord = vec2(u_subTextureCoord.x, u_subTextureCoord.y) + (p_texCoord * vec2(u_subTextureCoord.z, u_subTextureCoord.w));
    vec4 textureColor = texture(u_texture, texCoord);
    textureColor = textureColor * (1.0 - u_maskColorFlag) + vec4(u_color.rgb, textureColor.r) * u_maskColorFlag;

    o_color = textureColor * u_texturedFlag + u_color * (1.0 - u_texturedFlag);
}