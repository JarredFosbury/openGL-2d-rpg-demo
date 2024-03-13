#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexPos;

out vec2 TexPos;
out vec3 camPos;
out vec3 fragPos;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 cameraTransform;

void main()
{
    gl_Position = projection * cameraTransform * transform * vec4(aPos.xyz, 1.0);
    TexPos = aTexPos;
    camPos = vec3(cameraTransform[0][3], cameraTransform[1][3], cameraTransform[2][3]);
    // I have no idea if this is correct or not. To get the value I wanted on CPU side I needed to take the transpose
    //  of this matrix, grab the same 0,3, 1,3, and 2,3 values, and then takte the inverse of the resulting vector.
    // The output of that is the camera position in world space.

    fragPos = vec3(transform * vec4(aPos.xyz, 1.0));
}

#type fragment
#version 330 core
out vec4 FragColor;

in vec2 TexPos;
in vec3 camPos;
in vec3 fragPos;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    BaseLight base;
    Attenuation attenuation;
    vec3 position;
    float range;
};

uniform vec3 ambientLightColor;
uniform vec4 tint;
uniform sampler2D textureMain;
uniform sampler2D textureNormal;
uniform vec2 texPosOffset;
uniform vec2 texPosScale;

uniform DirectionalLight dLight;
uniform PointLight pointLights[16];

vec3 calculateMainLightFactor(vec3 normalIn)
{
    return max(dot(normalIn, dLight.direction) * dLight.base.intensity, 0.0) * dLight.base.color;
}

void main()
{
    vec2 uv = (TexPos * texPosScale) + texPosOffset;
    vec4 mainSample = texture(textureMain, uv) * tint;
    vec4 normalSample = texture(textureNormal, uv);

    vec3 normal = normalize(normalSample.rgb * 2.0 - 1.0);

    vec4 finalColor = vec4(mainSample.rgb * calculateMainLightFactor(normal), 1.0);

    if (mainSample.a <= 0.001)
        discard;

    FragColor = finalColor;
}