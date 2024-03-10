package templates;

import engine.core.Entity;
import engine.core.EntityType;

public class ScriptableBehaviour extends Entity
{
    public ScriptableBehaviour()
    {
        super("Entity name here", EntityType.ScriptableBehavior);
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
}