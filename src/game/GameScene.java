package game;

import engine.core.Camera;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.LitSprite;
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
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/lamp_alb.png", true, false, true), "sprite-alb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/lamp_nrm.png", true, false, true), "sprite-nrm");

        Scene.mainCamera = new Camera("Main Camera");
        Scene.mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        new FreeCamera();
        new LitSprite("demoSprite-lit", Scene.normalMappedLit2dShader, "sprite-alb",
                "sprite-nrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
    }
}