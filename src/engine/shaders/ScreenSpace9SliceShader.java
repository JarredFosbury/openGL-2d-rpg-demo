package engine.shaders;

import engine.core.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ScreenSpace9SliceShader extends Shader
{
        public ScreenSpace9SliceShader(String filepath)
        {
            super(filepath);
        }

        public void updateUniforms(Vector4f tint, Matrix4f transform, Vector2f textureMainRawSize, Vector2f textureMainScaledSize, Vector4f sliceBorders)
        {
            setFloat4Uniform("tint", tint);
            setMatrix4Uniform("transform", transform);
            setMatrix4Uniform("projection", Scene.mainCamera.screenSpaceProjection);
            setFloat2Uniform("textureMainRawSize", textureMainRawSize);
            setFloat2Uniform("textureMainScaledSize", textureMainScaledSize);
            setFloat4Uniform("sliceBorders", sliceBorders);
        }
}