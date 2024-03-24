package templates;

import engine.audio.Listener;
import engine.core.Camera;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;

public class ExampleScene extends Entity
{
    public ExampleScene()
    {
        super("Scene name here", EntityType.ScriptableBehavior, 0);
        // load all relevant assets here

        Scene.audioListener = new Listener();
        Scene.mainCamera = new Camera("Main Camera", 0);
        Scene.mainCamera.updateViewport(1.0f, -1.0f, 1.0f);

        // instantiate all instances of needed objects here
    }

    public void start()
    {
        // grab references to other objects in the scene here
    }

    public void update()
    {}

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}

    public void unloadScene()
    {
        Scene.assets.releaseAllAssetsFromPool();
        Scene.physics.resetToDefaultValues();
        Scene.entities.clear();
    }
}