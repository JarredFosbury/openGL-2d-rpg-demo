package engine.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends Entity
{
    public Matrix4f projection;
    public Matrix4f screenSpaceProjection;
    public float viewPlaneScale;
    public float nearPlane;
    public float farPlane;

    public Camera()
    {
        updateViewport(1.0f, -1.0f, 1.0f);
    }

    public void updateViewport(float viewPlaneScale, float nearPlane, float farPlane)
    {
        this.viewPlaneScale = viewPlaneScale;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;

        float halfWidth = (float)GlobalSettings.WINDOW_WIDTH / 2.0f;
        float halfHeight = (float)GlobalSettings.WINDOW_HEIGHT / 2.0f;
        float aspectRatio = (halfWidth / halfHeight) * viewPlaneScale;
        projection = new Matrix4f().ortho(-aspectRatio, aspectRatio, -1.0f * viewPlaneScale, viewPlaneScale, nearPlane, farPlane);
        screenSpaceProjection = new Matrix4f().ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 1.0f);
    }

    public Matrix4f getTransformation()
    {
        Matrix4f transformation = new Matrix4f().identity();
        transformation.translate(new Vector3f(position).mul(-1.0f));
        transformation.rotateXYZ(rotation);
        return transformation;
    }
}