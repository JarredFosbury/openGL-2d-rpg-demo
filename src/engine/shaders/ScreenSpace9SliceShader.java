package engine.shaders;

import engine.core.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class ScreenSpace9SliceShader extends Shader
{
    private final int tint_LOCATION;
    private final int transform_LOCATION;
    private final int projection_LOCATION;
    private final int textureMainRawSize_LOCATION;
    private final int textureMainScaledSize_LOCATION;
    private final int sliceBorders_LOCATION;

    public ScreenSpace9SliceShader(String filepath)
    {
        super(filepath);
        tint_LOCATION = glGetUniformLocation(getShaderProgramID(), "tint");
        transform_LOCATION = glGetUniformLocation(getShaderProgramID(), "transform");
        projection_LOCATION = glGetUniformLocation(getShaderProgramID(), "projection");
        textureMainRawSize_LOCATION = glGetUniformLocation(getShaderProgramID(), "textureMainRawSize");
        textureMainScaledSize_LOCATION = glGetUniformLocation(getShaderProgramID(), "textureMainScaledSize");
        sliceBorders_LOCATION = glGetUniformLocation(getShaderProgramID(), "sliceBorders");
    }

    public void updateUniforms(Vector4f tint, Matrix4f transform, Vector2f textureMainRawSize, Vector2f textureMainScaledSize, Vector4f sliceBorders)
    {
        setFloat4Uniform(tint_LOCATION, tint);
        setMatrix4Uniform(transform_LOCATION, transform);
        setMatrix4Uniform(projection_LOCATION, Scene.mainCamera.screenSpaceProjection);
        setFloat2Uniform(textureMainRawSize_LOCATION, textureMainRawSize);
        setFloat2Uniform(textureMainScaledSize_LOCATION, textureMainScaledSize);
        setFloat4Uniform(sliceBorders_LOCATION, sliceBorders);
    }
}