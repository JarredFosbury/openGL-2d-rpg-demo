package engine.audio;

import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

public class Listener
{
    private Vector3f position;

    public Listener()
    {
        setPosition(0.0f, 0.0f, 0.0f);
    }

    public void translate(float x, float y, float z)
    {
        position.add(x, y, z);
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public void setPosition(float x, float y, float z)
    {
        position = new Vector3f(x, y, z);
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public Vector3f getPosition()
    {
        return position;
    }
}