package engine.rendering;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.lighting.BaseLight;
import engine.lighting.DirectionalLight;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MainLightSource extends Entity
{
    public Vector4f color;
    public float intensity;

    private final DirectionalLight light;

    public MainLightSource(String name, short HIERARCHY_INDEX, Vector4f color, float intensity)
    {
        super(name, EntityType.MainLightSource, HIERARCHY_INDEX);
        this.color = color;
        this.intensity = intensity;
        light = new DirectionalLight(new BaseLight(new Vector3f(color.x, color.y, color.z), intensity), directionFromRotation());
        Scene.lightSources.addSource(light);
    }

    private Vector3f directionFromRotation()
    {
        Vector3f fwd = new Vector3f(0.0f, 0.0f, 1.0f);
        fwd.rotateX(rotation.x);
        fwd.rotateY(rotation.y);
        fwd.rotateZ(rotation.z);
        return fwd.normalize();
    }

    public void update()
    {
        light.getBase().setColor(new Vector3f(color.x, color.y, color.z));
        light.getBase().setIntensity(intensity);
        light.setDirection(directionFromRotation());
    }
}