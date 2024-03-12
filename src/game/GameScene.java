package game;

import engine.core.Camera;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.MainLightSource;
import engine.rendering.SpriteLit;
import engine.rendering.Texture;
import org.joml.Vector2f;

public class GameScene extends Entity
{
    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_alb.png", true, false, true), "spriteAlb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_nrm.png", true, false, true), "spriteNrm");

        Scene.mainCamera = new Camera("Main Camera");
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        MainLightSource source = new MainLightSource("mainLightSource", Color.WHITE, 1.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new FreeCamera();
        new SpriteLit("demoSprite-lit", Scene.normalMappedLit2dShader, "spriteAlb",
                "spriteNrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
    }
}