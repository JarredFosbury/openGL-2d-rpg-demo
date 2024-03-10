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

bool isBlack(vec3 sampleColor)
{
    if (sampleColor.r <= 0 && sampleColor.g <= 0 && sampleColor.b <= 0)
        return true;
    else
        return false;
}

void main()
{
    vec4 mainSample = texture(textureMain, (TexPos * texPosScale) + texPosOffset) * tint;
    vec3 normalSample = texture(textureNormal, (TexPos * texPosScale) + texPosOffset).xyz;

    vec3 exampleLightDir = vec3(0.0, 0.0, 1.0);
    vec3 normalDir = normalize(vec3(colorToAxis(normalSample.r), colorToAxis(normalSample.g), colorToAxis(normalSample.b)));
    float normalFactor = clamp(dot(normalDir, exampleLightDir), 0.1, 1.0);

    if (isBlack(mainSample.xyz))
        discard;

    FragColor = vec4(mainSample.xyz * normalFactor, 1.0);
}