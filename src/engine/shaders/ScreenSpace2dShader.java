package engine.shaders;

import engine.core.Scene;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ScreenSpace2dShader extends Shader
{
    public ScreenSpace2dShader(String filepath)
    {
        super(filepath);
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform)
    {
        setFloat4Uniform("tint", tint);
        setMatrix4Uniform("transform", transform);
        setMatrix4Uniform("projection", Scene.mainCamera.screenSpaceProjection);
    }
}