package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.rendering.Color;
import engine.rendering.Sprite;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CampfirePrefab extends Entity
{
    private final Sprite fire;

    public CampfirePrefab()
    {
        super("campfire-sprite", EntityType.ScriptableBehavior);

        new Sprite("campfireBase-sprite", Scene.standard2dShader, "firewood",
                Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));

        fire = new Sprite("fire-animated", Scene.standard2dShader, "fireSheet",
                Color.WHITE, new Vector2f(0.0f, 0.75f), new Vector2f(0.125f,0.25f));
        fire.scale = new Vector3f(1.0f, 2.0f, 1.0f);
        fire.initSpriteSheet("res/spriteSheetData/fireSheetData.ssd");
        fire.animationFrameRate = 24;
    }

    public void update()
    {
        fire.animateSprite();
    }
}