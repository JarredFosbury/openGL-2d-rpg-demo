#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;

void main()
{
    gl_Position = vec4(aPos.xyz, 1.0);
}

#type fragment
#version 330 core
out vec4 FragColor;

uniform vec4 tint;

void main()
{
    FragColor = vec4(1.0, 1.0, 1.0, 1.0) * tint;
}