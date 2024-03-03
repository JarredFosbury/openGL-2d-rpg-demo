package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.core.Time;
import engine.rendering.ScreenSpaceSprite;

public class HeartIconTest extends Entity
{
    private ScreenSpaceSprite heart;

    public HeartIconTest(String name)
    {
        super(name, EntityType.ScriptableBehavior);
        heart = (ScreenSpaceSprite) Scene.findByName("Heart icon")[0];
    }

    public void update()
    {
        heart.rotate(0.0f, 0.0f, 45.0f * Time.deltaTime);
    }
}