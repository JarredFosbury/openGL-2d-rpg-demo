package game;

import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.rendering.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GameScene extends Entity
{
    private final MainLightSource source;
    private final SpriteLit playerSprite;

    public GameScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior,  (short) 0);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_alb.png", true, true, true), "barrel-alb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/barrel_nrm.png", true, true, true), "barrel-nrm");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/playerJumpRight_alb.png", true, true, true), "player-alb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/playerJumpRight_nrm.png", true, true, true), "player-nrm");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/frame_simple.png", true, true, true), "9sliceFrameSprite");

        Scene.mainCamera = new Camera("Main Camera", (short) 0);
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        source = new MainLightSource("mainLightSource", (short) 0, Color.WHITE, 1.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new FreeCamera();
        new SpriteLit("demoBarrelSprite-lit", (short) 0, Scene.normalMappedLit2dShader, "barrel-alb",
                "barrel-nrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        playerSprite = new SpriteLit("playerSprite-lit", (short) 0, Scene.normalMappedLit2dShader,
                "player-alb", "player-nrm", Color.WHITE, new Vector2f(0.0f, 0.857f), new Vector2f(0.1429f));
        playerSprite.initSpriteSheet("res/spriteSheetData/playerJumpRight_SheetData.ssd");
        playerSprite.scale = new Vector3f(2.0f);
        playerSprite.translate(0.0f, 1.45f, 0.0f);

        ScreenSpace9SliceSprite sprite9Slice = new ScreenSpace9SliceSprite("sprite9Slice-TEST", (short) 1000,
                "9sliceFrameSprite", Color.WHITE, true, new Vector4f(8.0f));
        sprite9Slice.locationAnchor = new Vector2i(0);
        sprite9Slice.position = new Vector3f(0.0f);
        sprite9Slice.scale = new Vector3f(sprite9Slice.mainTexture.getSize().x * 3.0f, sprite9Slice.mainTexture.getSize().y * 5.0f, 1.0f);
    }

    public void update()
    {
        source.rotate(0.0f, 0.0f, Time.deltaTime * 20.0f);
        playerSprite.animateSprite();
    }
}