package engine.rendering;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.lighting.Attenuation;
import engine.lighting.BaseLight;
import engine.lighting.PointLight;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointLightSource extends Entity
{
    public Vector4f color;
    public float intensity;
    public float range;
    public float constant;
    public float linear;
    public float exponent;

    private final PointLight source;

    public PointLightSource(String name, int HIERARCHY_INDEX, Vector4f color, float intensity, float range, float constant, float linear, float exponent)
    {
        super(name, EntityType.PointLightSource, HIERARCHY_INDEX);
        this.color = color;
        this.intensity = intensity;
        this.range = range;
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
        source = new PointLight(
                new BaseLight(new Vector3f(color.x, color.y, color.z), intensity),
                position,
                new Attenuation(constant, linear, exponent), range);
        Scene.lightSources.addSource(source);
    }

    public void update()
    {
        source.getBase().setColor(new Vector3f(color.x, color.y, color.z));
        source.getBase().setIntensity(intensity);
        source.setPosition(position);
        source.setRange(range);
        source.getAttenuation().setConstant(constant);
        source.getAttenuation().setLinear(linear);
        source.getAttenuation().setExponent(exponent);
    }
}
