package engine.core;

import org.joml.Vector3f;

public class Entity
{
    public String name;
    public final EntityType TYPE;
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public Entity(String name, EntityType type)
    {
        Scene.entityList.add(this);
        this.name = name;
        this.TYPE = type;
        position = new Vector3f(0.0f);
        rotation = new Vector3f(0.0f);
        scale = new Vector3f(1.0f);

        if (TYPE == EntityType.NULL)
            System.err.println("WARNING: Class type set to NULL!");
    }

    public void pollInput()
    {}

    public void update()
    {}

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}

    public void translate(float x, float y, float z)
    {
        position.add(x, y, z);
    }

    public void translate(Vector3f v)
    {
        position.add(v.x, v.y, v.z);
    }

    public void rotate(float x, float y, float z)
    {
        rotation.add((float) Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
    }

    public void rotate(Vector3f v)
    {
        rotation.add((float) Math.toRadians(v.x), (float)Math.toRadians(v.y), (float)Math.toRadians(v.z));
    }

    public void scale(float x, float y, float z)
    {
        scale.mul(x, y, z);
    }

    public void scale(Vector3f v)
    {
        scale.mul(v.x, v.y, v.z);
    }
}