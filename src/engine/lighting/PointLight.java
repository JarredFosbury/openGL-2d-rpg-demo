package engine.lighting;

import org.joml.Vector3f;

public class PointLight
{
    private BaseLight base;
    private Vector3f position;
    private Attenuation attenuation;
    private float range;

    public BaseLight getBase()
    {
        return base;
    }

    public void setBase(BaseLight base)
    {
        this.base = base;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Attenuation getAttenuation()
    {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation)
    {
        this.attenuation = attenuation;
    }

    public float getRange()
    {
        return range;
    }

    public void setRange(float range)
    {
        this.range = range;
    }

    public PointLight(BaseLight base, Vector3f position, Attenuation attenuation, float range)
    {
        this.base = base;
        this.position = position;
        this.attenuation = attenuation;
        this.range = range;
    }
}