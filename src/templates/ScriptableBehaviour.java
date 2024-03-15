package templates;

import engine.core.Entity;
import engine.core.EntityType;

public class ScriptableBehaviour extends Entity
{
    public ScriptableBehaviour()
    {
        super("Entity name here", EntityType.ScriptableBehavior, 0);
    }

    public void start()
    {}

    public void update()
    {}

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}
}