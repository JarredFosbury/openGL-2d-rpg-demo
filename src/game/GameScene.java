package game;

import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.MainLightSource;
import engine.rendering.SpriteLit;
import engine.rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameScene extends Entity
{
    private final MainLightSource source;
    private final SpriteLit playerSprite;

    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior, 0);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_alb.png", true, true, true), "barrel-alb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_nrm.png", true, true, true), "barrel-nrm");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/playerSittingIdle_alb.png", true, true, true), "playerSittingAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/playerSittingIdle_nrm.png", true, true, true), "playerSittingNormal");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/frameRoundedSimple.png", true, true, true), "9sliceFrameSprite");

        Scene.mainCamera = new Camera("Main Camera", 0);
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        source = new MainLightSource("mainLightSource", 0, Color.WHITE, 1.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new FreeCamera();
        new SpriteLit("demoBarrelSprite-lit", 0, Scene.normalMappedLit2dShader, "barrel-alb",
                "barrel-nrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        playerSprite = new SpriteLit("playerSprite-lit", 0, Scene.normalMappedLit2dShader,
                "playerSittingAlbedo", "playerSittingNormal", Color.WHITE, new Vector2f(0.0f, 0.9411765f), new Vector2f(0.05882353f));
        playerSprite.initSpriteSheet("res/spriteSheetData/playerSittingIdle_SheetData.ssd");
        playerSprite.scale = new Vector3f(2.0f);
        playerSprite.translate(-0.2f, 1.48f, 0.0f);
    }

    public void update()
    {
        source.rotate(0.0f, 0.0f, Time.deltaTime * 20.0f);
        playerSprite.animateSprite();
    }
}