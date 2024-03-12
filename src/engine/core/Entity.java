package engine.core;

import org.joml.Vector3f;

import java.util.UUID;

public class Entity
{
    public String name;
    public final EntityType TYPE;
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;
    public boolean isVisible;
    public final UUID ID;

    public Entity(String name, EntityType type)
    {
        Scene.entities.add(this);
        this.name = name;
        this.TYPE = type;
        position = new Vector3f(0.0f);
        rotation = new Vector3f(0.0f);
        scale = new Vector3f(1.0f);
        isVisible = true;
        ID = UUID.randomUUID();

        if (TYPE == EntityType.NULL)
            System.err.println("WARNING: Class type set to NULL!");
    }

    public void start()
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

        // clamp rotation between -180 and 180 degrees to reduce floating point error
        float [] rots = {rotation.x, rotation.y, rotation.z};

        for (int i = 0; i < rots.length; i++)
        {
            if (rots[i] < -3.141593f)
                rots[i] += 6.283185f;

            if (rots[i] > 3.141593f)
                rots[i] -= 6.283185f;
        }

        this.rotation = new Vector3f(rots[0], rots[1], rots[2]);
    }

    public void rotate(Vector3f v)
    {
        rotation.add((float) Math.toRadians(v.x), (float)Math.toRadians(v.y), (float)Math.toRadians(v.z));

        // clamp rotation between -180 and 180 degrees to reduce floating point error
        float [] rots = {rotation.x, rotation.y, rotation.z};

        for (int i = 0; i < rots.length; i++)
        {
            if (rots[i] < -3.141593f)
                rots[i] += 6.283185f;

            if (rots[i] > 3.141593f)
                rots[i] -= 6.283185f;
        }

        this.rotation = new Vector3f(rots[0], rots[1], rots[2]);
    }

    public void scale(float x, float y, float z)
    {
        scale.mul(x, y, z);
    }

    public void scale(Vector3f v)
    {
        scale.mul(v.x, v.y, v.z);
    }

    public Vector3f getPositionAsScreenSpace(Vector3f inPos)
    {
        Vector3f outPos = new Vector3f(0.0f);
        outPos.y = inPos.y * -1.0f + (float)GlobalSettings.windowHeight / 2.0f;
        outPos.x = inPos.x - (float)GlobalSettings.windowWidth / 2.0f;
        return new Vector3f(outPos);
    }
}