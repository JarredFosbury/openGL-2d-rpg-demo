package engine.audio;

import engine.core.Camera;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

public class Listener extends Entity
{
    private Camera mainCamera;

    public Listener()
    {
        super("AudioListener", EntityType.AudioListener, 0);
    }

    public void start()
    {
        mainCamera = Scene.mainCamera;
    }

    public void update()
    {
        if (mainCamera != null)
            position = mainCamera.position;

        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }
}