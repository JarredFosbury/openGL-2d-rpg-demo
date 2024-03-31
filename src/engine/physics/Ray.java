package engine.physics;

import org.joml.Vector3f;

public class Ray
{
    public final Vector3f position;
    public final Vector3f direction;
    public final Vector3f inverseDirection;

    public Ray(Vector3f position, Vector3f direction)
    {
        this.position = position;
        this.direction = direction;
        this.inverseDirection = new Vector3f(1.0f).div(new Vector3f(direction));
    }
}