package game;

import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.rendering.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameScene extends Entity
{
    private final MainLightSource source;
    private final SpriteLit playerSprite;

    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_alb.png", true, true, true), "spriteAlb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_nrm.png", true, true, true), "spriteNrm");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/playerJumpRight_alb.png", true, true, true), "playerAlb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/playerJumpRight_nrm.png", true, true, true), "playerNrm");

        Scene.mainCamera = new Camera("Main Camera");
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        source = new MainLightSource("mainLightSource", Color.WHITE, 1.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new FreeCamera();
        SpriteLit barrel = new SpriteLit("demoSprite-lit", Scene.normalMappedLit2dShader, "spriteAlb",
                "spriteNrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        barrel.scale = new Vector3f(2.0f);

        playerSprite = new SpriteLit("playerSprite-lit", Scene.normalMappedLit2dShader,
                "playerAlb", "playerNrm", Color.WHITE, new Vector2f(0.0f, 0.857f), new Vector2f(0.1429f));
        playerSprite.initSpriteSheet("res/spriteSheetData/playerJumpRight_SheetData.ssd");
        playerSprite.scale = new Vector3f(2.0f);
        playerSprite.translate(0.0f, 1.95f, 0.0f);
    }

    public void update()
    {
        source.rotate(0.0f, 0.0f, Time.deltaTime * 45.0f);
        playerSprite.animateSprite();
    }
}