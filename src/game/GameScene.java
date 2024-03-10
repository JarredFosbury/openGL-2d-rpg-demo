package game;

import engine.core.Camera;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.fontRendering.FontLoader;
import engine.rendering.Texture;

public class GameScene extends Entity
{
    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/fireSheet_lowRes.png", true, false, true), "fireSheet");
        Scene.assets.addAssetToPool(new Texture("res/textures/campfire.png", true, false, true), "firewood");

        Scene.mainCamera = new Camera("Main Camera");
        Scene.mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        new FreeCamera();
    }
}