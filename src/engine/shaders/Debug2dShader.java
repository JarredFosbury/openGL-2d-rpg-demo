package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class Debug2dShader extends Shader
{
    private final int tint_LOCATION;
    private final int transform_LOCATION;
    private final int projection_LOCATION;
    private final int cameraTransform_LOCATION;

    public Debug2dShader(String filepath)
    {
        super(filepath);
        tint_LOCATION = glGetUniformLocation(getShaderProgramID(), "tint");
        transform_LOCATION = glGetUniformLocation(getShaderProgramID(), "transform");
        projection_LOCATION = glGetUniformLocation(getShaderProgramID(), "projection");
        cameraTransform_LOCATION = glGetUniformLocation(getShaderProgramID(), "cameraTransform");
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Matrix4f projection, Matrix4f cameraTransform)
    {
        setFloat4Uniform(tint_LOCATION, tint);
        setMatrix4Uniform(transform_LOCATION, transform);
        setMatrix4Uniform(projection_LOCATION, projection);
        setMatrix4Uniform(cameraTransform_LOCATION, cameraTransform);
    }
}