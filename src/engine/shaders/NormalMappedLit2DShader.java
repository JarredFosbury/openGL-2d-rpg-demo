package engine.shaders;

import engine.lighting.DirectionalLight;
import engine.lighting.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class NormalMappedLit2DShader extends Shader
{
    private final int maxPointLights;
    private final int textureMain_LOCATION;
    private final int textureNormal_LOCATION;
    private final int tint_LOCATION;
    private final int transform_LOCATION;
    private final int projection_LOCATION;
    private final int cameraTransform_LOCATION;
    private final int texPosOffset_LOCATION;
    private final int texPosScale_LOCATION;
    private final int ambientLightColor_LOCATION;
    private final int dLightDirection_LOCATION;
    private final int dLightBaseColor_LOCATION;
    private final int dLightBaseIntensity_LOCATION;
    private final int[] pointLightBaseColor_LOCATIONS;
    private final int[] pointLightBaseIntensity_LOCATIONS;
    private final int[] pointLightAttenuationConstant_LOCATIONS;
    private final int[] pointLightAttenuationLinear_LOCATIONS;
    private final int[] pointLightAttenuationExponent_LOCATIONS;
    private final int[] pointLightPosition_LOCATIONS;
    private final int[] pointLightRange_LOCATIONS;

    public NormalMappedLit2DShader(String filepath)
    {
        super(filepath);
        maxPointLights = 16;
        textureMain_LOCATION = glGetUniformLocation(getShaderProgramID(), "textureMain");
        textureNormal_LOCATION = glGetUniformLocation(getShaderProgramID(), "textureNormal");
        tint_LOCATION = glGetUniformLocation(getShaderProgramID(), "tint");
        transform_LOCATION = glGetUniformLocation(getShaderProgramID(), "transform");
        projection_LOCATION = glGetUniformLocation(getShaderProgramID(), "projection");
        cameraTransform_LOCATION = glGetUniformLocation(getShaderProgramID(), "cameraTransform");
        texPosOffset_LOCATION = glGetUniformLocation(getShaderProgramID(), "texPosOffset");
        texPosScale_LOCATION = glGetUniformLocation(getShaderProgramID(), "texPosScale");
        ambientLightColor_LOCATION = glGetUniformLocation(getShaderProgramID(), "ambientLightColor");
        dLightDirection_LOCATION = glGetUniformLocation(getShaderProgramID(), "dLight.direction");
        dLightBaseColor_LOCATION = glGetUniformLocation(getShaderProgramID(), "dLight.base.color");
        dLightBaseIntensity_LOCATION = glGetUniformLocation(getShaderProgramID(), "dLight.base.intensity");

        pointLightBaseColor_LOCATIONS = new int[maxPointLights];
        pointLightBaseIntensity_LOCATIONS = new int[maxPointLights];
        pointLightAttenuationConstant_LOCATIONS = new int[maxPointLights];
        pointLightAttenuationLinear_LOCATIONS = new int[maxPointLights];
        pointLightAttenuationExponent_LOCATIONS = new int[maxPointLights];
        pointLightPosition_LOCATIONS = new int[maxPointLights];
        pointLightRange_LOCATIONS = new int[maxPointLights];

        for (int i = 0; i < maxPointLights; i++)
        {
            pointLightBaseColor_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].base.color");
            pointLightBaseIntensity_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].base.intensity");
            pointLightAttenuationConstant_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].attenuation.constant");
            pointLightAttenuationLinear_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].attenuation.linear");
            pointLightAttenuationExponent_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].attenuation.exponent");
            pointLightPosition_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].position");
            pointLightRange_LOCATIONS[i] = glGetUniformLocation(getShaderProgramID(), "pointLights[" + i + "].range");
        }
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Matrix4f projection, Matrix4f cameraTransform,
                               Vector2f texPosOffset, Vector2f texPosScale, DirectionalLight dLight, PointLight[] pLights, Vector4f ambientLight)
    {
        setIntegerUniform(textureMain_LOCATION, 0);
        setIntegerUniform(textureNormal_LOCATION, 1);
        setFloat4Uniform(tint_LOCATION, tint);
        setMatrix4Uniform(transform_LOCATION, transform);
        setMatrix4Uniform(projection_LOCATION, projection);
        setMatrix4Uniform(cameraTransform_LOCATION, cameraTransform);
        setFloat2Uniform(texPosOffset_LOCATION, texPosOffset);
        setFloat2Uniform(texPosScale_LOCATION, texPosScale);
        setFloat3Uniform(ambientLightColor_LOCATION, new Vector3f(ambientLight.x, ambientLight.y, ambientLight.z));

        if (dLight != null)
        {
            setFloat3Uniform(dLightDirection_LOCATION, dLight.getDirection());
            setFloat3Uniform(dLightBaseColor_LOCATION, dLight.getBase().getColor());
            setFloatUniform(dLightBaseIntensity_LOCATION, dLight.getBase().getIntensity());
        }

        for (int i = 0; i < Math.min(pLights.length, maxPointLights); i++)
        {
            setFloat3Uniform(pointLightBaseColor_LOCATIONS[i], pLights[i].getBase().getColor());
            setFloatUniform(pointLightBaseIntensity_LOCATIONS[i], pLights[i].getBase().getIntensity());

            setFloatUniform(pointLightAttenuationConstant_LOCATIONS[i], pLights[i].getAttenuation().getConstant());
            setFloatUniform(pointLightAttenuationLinear_LOCATIONS[i], pLights[i].getAttenuation().getLinear());
            setFloatUniform(pointLightAttenuationExponent_LOCATIONS[i], pLights[i].getAttenuation().getExponent());

            setFloat3Uniform(pointLightPosition_LOCATIONS[i], pLights[i].getPosition());
            setFloatUniform(pointLightRange_LOCATIONS[i], pLights[i].getRange());
        }
    }
}