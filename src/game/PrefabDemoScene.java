package game;

import engine.core.Entity;
import engine.core.EntityType;

public class PrefabDemoScene extends Entity
{
    public PrefabDemoScene()
    {
        super("PrefabDemoSceneManager", EntityType.ScriptableBehavior, 0);
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
    {}
}