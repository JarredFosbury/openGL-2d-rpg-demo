#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 vertColor;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 cameraTransform;

void main()
{
    gl_Position = projection * cameraTransform * transform * vec4(aPos.xyz, 1.0);
    vertColor = aColor;
}

#type fragment
#version 330 core
in vec4 vertColor;

out vec4 FragColor;

uniform vec4 tint;

void main()
{
    FragColor = tint * vertColor;
}