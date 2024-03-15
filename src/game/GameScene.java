package game;

import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.MainLightSource;
import engine.rendering.Texture;

public class GameScene extends Entity
{
    private final MainLightSource source;

    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior, 0);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/frameRoundedSimple.png", true, true, true), "9sliceFrameSprite");

        Scene.mainCamera = new Camera("Main Camera", 0);
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        source = new MainLightSource("mainLightSource", 0, Color.WHITE, 1.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new FreeCamera();
        new PlayerController();
    }

    public void update()
    {
        source.rotate(0.0f, 0.0f, Time.deltaTime * 20.0f);
    }
}