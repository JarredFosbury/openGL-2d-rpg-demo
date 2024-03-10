package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class NormalMappedLit2DShader extends Shader
{
    public NormalMappedLit2DShader(String filepath)
    {
        super(filepath);
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Matrix4f projection, Matrix4f cameraTransform, Vector2f texPosOffset, Vector2f texPosScale)
    {
        setIntegerUniform("textureMain", 0);
        setIntegerUniform("textureNormal", 1);
        setFloat4Uniform("tint", tint);
        setMatrix4Uniform("transform", transform);
        setMatrix4Uniform("projection", projection);
        setMatrix4Uniform("cameraTransform", cameraTransform);
        setFloat2Uniform("texPosOffset", texPosOffset);
        setFloat2Uniform("texPosScale", texPosScale);
    }
}