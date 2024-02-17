package game;

import engine.core.Scene;
import engine.core.Utils;
import engine.rendering.Sprite;
import engine.rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class IslandEnvironmentSprites
{
    public Sprite groundPlane;
    public Texture terrainDetailsSpriteSheet;
    public Sprite[] terrainDetailSprites;

    public IslandEnvironmentSprites()
    {
        groundPlane = new Sprite(new Texture("res/textures/grassTile.png", true, true, true),
                Scene.dayNightCycle.mainLightColor, new Vector2f(0.0f), new Vector2f(21.0f));
        groundPlane.scale = new Vector3f(21.0f);

        terrainDetailsSpriteSheet = new Texture("res/textures/detailsSpriteSheet.png", true, false, true);
        terrainDetailSprites = new Sprite[12];
        for (int i = 0; i < terrainDetailSprites.length; i++)
        {
            int x = Utils.randomRangeInteger(0, 3);
            int y = Utils.randomRangeInteger(0, 3);
            int xPos = Utils.randomRangeInteger(-10, 10);
            int yPos = Utils.randomRangeInteger(-10, 10);
            terrainDetailSprites[i] = new Sprite(terrainDetailsSpriteSheet, Scene.dayNightCycle.mainLightColor, new Vector2f(0.25f * x, 0.25f * y), new Vector2f(0.25f));
            terrainDetailSprites[i].position = new Vector3f(xPos, yPos, 0.0f);
        }
    }

    public void update()
    {
        groundPlane.mainTextureTint = Scene.dayNightCycle.mainLightColor;
        for (Sprite detail : terrainDetailSprites)
            detail.mainTextureTint = Scene.dayNightCycle.mainLightColor;
    }

    public void render()
    {
        groundPlane.render();
        for (Sprite detail : terrainDetailSprites)
            detail.render();
    }
}