#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexPos;

out vec2 uv;

uniform mat4 transform;
uniform mat4 projection;

void main()
{
    gl_Position = projection * transform * vec4(aPos.xyz, 1.0);
    uv = aTexPos;
}

#type fragment
#version 330 core
// code for frag shader borrowed and modified from
// https://gamedev.stackexchange.com/questions/208178/glsl-9-slice-or-9-patch-working-with-single-texture-but-not-with-atlas-textur
// gave up trying to write this myself after 3 days. I suck ass at math lmao

out vec4 FragColor;

in vec2 uv;

uniform vec4 tint;
uniform sampler2D textureMain;
uniform vec2 textureMainRawSize;        // texture size in pixels
uniform vec2 textureMainScaledSize;     // actual displayed image size in pixels
uniform vec4 sliceBorders;              // pixels from each edge

vec2 uv9slice(vec2 uv, vec2 s, vec4 b)
{
    vec2 t = clamp((s * uv - b.xy) / (s - b.xy - b.zw), 0., 1.);
    return mix(uv * s, 1. - s * (1. - uv), t);
}

vec4 draw9Slice()
{
    vec2 _s = textureMainScaledSize.xy / textureMainRawSize;
    vec4 _b = sliceBorders / textureMainRawSize.xxyy;
    vec2 _uv = uv9slice(uv, _s, _b);
    return vec4(texture(textureMain, _uv));
}

void main()
{
    FragColor = draw9Slice() * tint;
}