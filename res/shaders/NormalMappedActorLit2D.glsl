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
const int MAX_POINT_LIGHTS = 16;

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
uniform float spriteDirection; // used to flip normal map sample values on x axis (expected -1.0f or 1.0f)

uniform DirectionalLight dLight;
uniform PointLight pointLights[16];

vec4 calculateMainLight(vec3 normalIn)
{
    float diffuseFactor = max(dot(normalIn, dLight.direction) * dLight.base.intensity, 0.0);
    return diffuseFactor * vec4(dLight.base.color, 1.0);
}

vec4 calculateLight(BaseLight base, vec3 direction, vec3 normal)
{
    float diffuseFactor = dot(normal, -direction);
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    if(diffuseFactor > 0)
    {
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
    }
    return diffuseColor;
}

vec4 calculatePointLight(PointLight light, vec3 normal)
{
    vec3 lightDirection = fragPos - light.position;
    float distanceToLight = length(lightDirection);

    if(distanceToLight > light.range)
    {
        return vec4(0, 0, 0, 0);
    }

    lightDirection = normalize(lightDirection);
    vec4 color = calculateLight(light.base, lightDirection, normal);
    float attenuation = light.attenuation.constant + light.attenuation.linear * distanceToLight + light.attenuation.exponent * distanceToLight * distanceToLight + 0.0001;

    return color / attenuation;
}

void main()
{
    vec4 totalLight = vec4(ambientLightColor, 1.0);
    vec2 uv = (TexPos * texPosScale) + texPosOffset;
    vec4 mainSample = texture(textureMain, uv) * tint;
    if (mainSample.a <= 0.001)
    {
        discard;
    }

    vec3 normal = normalize(texture(textureNormal, uv).xyz * 2.0 - 1.0);
    normal.x *= spriteDirection;
    totalLight += calculateMainLight(normal);
    for (int i = 0; i < MAX_POINT_LIGHTS; i++)
    {
        totalLight += calculatePointLight(pointLights[i], normal);
    }

    FragColor = mainSample * totalLight;
}