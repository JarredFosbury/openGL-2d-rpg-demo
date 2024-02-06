package shaders;

import org.joml.Vector4f;

public class ScreenSpace2dShader extends Shader
{
    public ScreenSpace2dShader(String filepath)
    {
        super(filepath);
    }

    public void updateUniforms(Vector4f tint)
    {
        setFloat4Uniform("tint", tint);
    }
}