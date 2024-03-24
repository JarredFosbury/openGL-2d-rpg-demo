#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 cameraTransform;

void main()
{
    gl_Position = projection * cameraTransform * transform * vec4(aPos.xyz, 1.0);
}

#type fragment
#version 330 core
out vec4 FragColor;

uniform vec4 tint;

void main()
{
    FragColor = tint;
}