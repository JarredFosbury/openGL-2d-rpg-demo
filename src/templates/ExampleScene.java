package templates;

import engine.core.Entity;
import engine.core.EntityType;

public class ExampleScene extends Entity
{
    public ExampleScene()
    {
        super("Scene name here", EntityType.ScriptableBehavior, 0);
    }

    public void start()
    {}

    public void update()
    {}

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}

    public void unloadScene()
    {}
}