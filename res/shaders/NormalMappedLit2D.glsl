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
uniform sampler2D textureNormal;
uniform vec2 texPosOffset;
uniform vec2 texPosScale;

float colorToAxis(float colCompIn)
{
    return (colCompIn * 2.0) - 1.0;
}

void main()
{
    vec4 mainSample = texture(textureMain, (TexPos * texPosScale) + texPosOffset) * tint;
    vec4 normalSample = texture(textureNormal, (TexPos * texPosScale) + texPosOffset);

    vec3 normalDir = normalize(vec3(colorToAxis(normalSample.x), colorToAxis(normalSample.y), colorToAxis(normalSample.z)));
    vec3 forward = vec3(0.0, 0.0, 1.0);
    float normalFactor = clamp(dot(normalDir, forward), 0.0, 1.0);

    FragColor = mainSample * normalFactor;
}