package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class Standard2dShader extends Shader
{
    private final int tint_LOCATION;
    private final int transform_LOCATION;
    private final int projection_LOCATION;
    private final int cameraTransform_LOCATION;
    private final int texPosOffset_LOCATION;
    private final int texPosScale_LOCATION;

    public Standard2dShader(String filepath)
    {
        super(filepath);
        tint_LOCATION = glGetUniformLocation(getShaderProgramID(), "tint");
        transform_LOCATION = glGetUniformLocation(getShaderProgramID(), "transform");
        projection_LOCATION = glGetUniformLocation(getShaderProgramID(), "projection");
        cameraTransform_LOCATION = glGetUniformLocation(getShaderProgramID(), "cameraTransform");
        texPosOffset_LOCATION = glGetUniformLocation(getShaderProgramID(), "texPosOffset");
        texPosScale_LOCATION = glGetUniformLocation(getShaderProgramID(), "texPosScale");
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Matrix4f projection, Matrix4f cameraTransform, Vector2f texPosOffset, Vector2f texPosScale)
    {
        setFloat4Uniform(tint_LOCATION, tint);
        setMatrix4Uniform(transform_LOCATION, transform);
        setMatrix4Uniform(projection_LOCATION, projection);
        setMatrix4Uniform(cameraTransform_LOCATION, cameraTransform);
        setFloat2Uniform(texPosOffset_LOCATION, texPosOffset);
        setFloat2Uniform(texPosScale_LOCATION, texPosScale);
    }
}