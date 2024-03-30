package engine.physics;

import org.joml.Vector3f;

public class Ray
{
    public Vector3f position;
    public Vector3f direction;

    public Ray(Vector3f position, Vector3f direction)
    {
        this.position = position;
        this.direction = direction;
    }
}