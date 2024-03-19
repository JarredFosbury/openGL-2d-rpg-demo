package engine.shaders;

import engine.core.Scene;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class ScreenSpace2dShader extends Shader
{
    private final int tint_LOCATION;
    private final int transform_LOCATION;
    private final int projection_LOCATION;

    public ScreenSpace2dShader(String filepath)
    {
        super(filepath);
        tint_LOCATION = glGetUniformLocation(getShaderProgramID(), "tint");
        transform_LOCATION = glGetUniformLocation(getShaderProgramID(), "transform");
        projection_LOCATION = glGetUniformLocation(getShaderProgramID(), "projection");
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform)
    {
        setFloat4Uniform(tint_LOCATION, tint);
        setMatrix4Uniform(transform_LOCATION, transform);
        setMatrix4Uniform(projection_LOCATION, Scene.mainCamera.screenSpaceProjection);
    }
}