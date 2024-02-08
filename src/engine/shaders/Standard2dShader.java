package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Standard2dShader extends Shader
{
    public Standard2dShader(String filepath)
    {
        super(filepath);
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Matrix4f projection, Matrix4f cameraTransform)
    {
        setFloat4Uniform("tint", tint);
        setMatrix4Uniform("transform", transform);
        setMatrix4Uniform("projection", projection);
        setMatrix4Uniform("cameraTransform", cameraTransform);
    }
}