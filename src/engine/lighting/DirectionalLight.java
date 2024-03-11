package engine.lighting;

import org.joml.Vector3f;

public class DirectionalLight
{
    private BaseLight base;
    private Vector3f direction;

    public BaseLight getBase()
    {
        return base;
    }

    public void setBase(BaseLight base)
    {
        this.base = base;
    }

    public Vector3f getDirection()
    {
        return direction;
    }

    public void setDirection(Vector3f direction)
    {
        this.direction = new Vector3f(direction).normalize();
    }

    public DirectionalLight(BaseLight base, Vector3f direction)
    {
        this.base = base;
        this.direction = new Vector3f(direction).normalize();
    }
}