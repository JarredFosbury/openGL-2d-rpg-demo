package engine.shaders;

import engine.lighting.DirectionalLight;
import engine.lighting.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class NormalMappedLit2DShader extends Shader
{
    public NormalMappedLit2DShader(String filepath)
    {
        super(filepath);
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Matrix4f projection, Matrix4f cameraTransform,
                               Vector2f texPosOffset, Vector2f texPosScale, DirectionalLight dLight, PointLight[] pLights)
    {
        setIntegerUniform("textureMain", 0);
        setIntegerUniform("textureNormal", 1);
        setFloat4Uniform("tint", tint);
        setMatrix4Uniform("transform", transform);
        setMatrix4Uniform("projection", projection);
        setMatrix4Uniform("cameraTransform", cameraTransform);
        setFloat2Uniform("texPosOffset", texPosOffset);
        setFloat2Uniform("texPosScale", texPosScale);

        if (dLight != null)
        {
            setFloat3Uniform("dLight.direction", dLight.getDirection());
            setFloat3Uniform("dLight.base.color", dLight.getBase().getColor());
            setFloatUniform("dLight.base.intensity", dLight.getBase().getIntensity());
        }

        for (int i = 0; i < pLights.length; i++)
        {
            setFloat3Uniform("pointLights[" + i + "].base.color", pLights[i].getBase().getColor());
            setFloatUniform("pointLights[" + i + "].base.intensity", pLights[i].getBase().getIntensity());

            setFloatUniform("pointLights[" + i + "].attenuation.constant", pLights[i].getAttenuation().getConstant());
            setFloatUniform("pointLights[" + i + "].attenuation.linear", pLights[i].getAttenuation().getLinear());
            setFloatUniform("pointLights[" + i + "].attenuation.exponent", pLights[i].getAttenuation().getExponent());

            setFloat3Uniform("pointLights[" + i + "].position", pLights[i].getPosition());
            setFloatUniform("pointLights[" + i + "].range", pLights[i].getRange());
        }
    }
}