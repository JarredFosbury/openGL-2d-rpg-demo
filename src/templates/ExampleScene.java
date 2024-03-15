package templates;

import engine.core.Entity;
import engine.core.EntityType;

public class ExampleScene extends Entity
{
    public ExampleScene()
    {
        super("Scene name here", EntityType.ScriptableBehavior, 0);
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
    {}
}