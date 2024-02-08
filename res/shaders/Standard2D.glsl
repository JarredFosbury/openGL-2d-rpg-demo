#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexPos;

out vec2 TexPos;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 cameraTransform;

void main()
{
    gl_Position = projection * cameraTransform * transform * vec4(aPos.xyz, 1.0);
    TexPos = aTexPos;
}

#type fragment
#version 330 core
out vec4 FragColor;

in vec2 TexPos;

uniform vec4 tint;
uniform sampler2D textureMain;

void main()
{
    FragColor = texture(textureMain, TexPos) * tint;
}