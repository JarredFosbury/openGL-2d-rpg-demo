package core;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera
{
    public Matrix4f projection;
    public Matrix4f screenSpaceProjection;
    public Vector3f position;
    public Vector3f rotation;

    public Camera()
    {
        float halfWidth = (float)GlobalSettings.WINDOW_WIDTH / 2.0f;
        float halfHeight = (float)GlobalSettings.WINDOW_HEIGHT / 2.0f;
        float aspectRatio = halfWidth / halfHeight;
        projection = new Matrix4f().ortho(-aspectRatio, aspectRatio, -1.0f, 1.0f, -1.0f, 1.0f);
        screenSpaceProjection = new Matrix4f().ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 1.0f);
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public Matrix4f getTransformation()
    {
        Matrix4f transformation = new Matrix4f().identity();
        transformation.translate(new Vector3f(position).mul(-1.0f));
        transformation.rotateXYZ(rotation);
        return transformation;
    }

    public void translate(float x, float y, float z)
    {
        position.add(x, y, z);
    }

    public void rotate(float x, float y, float z)
    {
        rotation.add(x, y, z);
    }
}