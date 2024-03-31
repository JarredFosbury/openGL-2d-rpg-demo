package game;

import engine.audio.Listener;
import engine.audio.SoundClip;
import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.physics.ColliderAABB;
import engine.rendering.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TombOfTheDamnedScene extends Entity
{
    public TombOfTheDamnedScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior, 0);
        Scene.ambientLight = new Vector4f(0.1f);

        Scene.physics.layerMasks.add("Player");
        Scene.physics.layerMasks.add("StaticGeometry");

        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/materials/cobblestoneWall_alb.png",
                true, true, true), "cobblestoneWallAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/materials/cobblestoneWall_nrm.png",
                true, true, true), "cobblestoneWallNormal");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/props/torch_alb.png",
                true, true, true), "torchAlb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/props/torch_nrm.png",
                true, true, true), "torchNrm");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/whiteSwatch32px.png",
                true, true, true), "whiteSwatch32px");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/centerLitBarSwatch32px.png", false,
                true, false), "barSwatch32px");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/frameRoundedSimple.png", false,
                true, false), "roundedFrame");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/itemSlot.png", false,
                true, false), "itemSlot");
        Scene.assets.addAssetToPool(new Texture("res/textures/UI/button.png", false,
                true, false), "button");
        Scene.assets.addAssetToPool(new Texture("res/textures/fireSheet_lowRes.png", true,
                true, false), "fireSheet");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/props/doorReinforced_alb.png",
                true, true, true), "doorReinforcedAlb");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/props/doorReinforced_nrm.png",
                true, true, true), "doorReinforcedNrm");

        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx01.ogg"), "caveDrip01");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx02.ogg"), "caveDrip02");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx03.ogg"), "caveDrip03");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx04.ogg"), "caveDrip04");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/fireSmall.ogg"), "fireSmall");

        Scene.audioListener = new Listener();
        Scene.mainCamera = new Camera("Main Camera", 0);
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        MainLightSource source = new MainLightSource("mainLightSource", 0, Color.WHITE, 0.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new PlayerStartingAnimationController();
        new DrippingWaterSfxHandler();
        new PlayerGameHUD(1000);

        SpriteLit wall = new SpriteLit("cobblestoneBackgroundWall", -10, "cobblestoneWallAlbedo",
                "cobblestoneWallNormal", Color.WHITE, new Vector2f(0.0f), new Vector2f(10.0f, 2.0f));
        wall.scale = new Vector3f(20.0f, 4.0f, 1.0f);
        wall.translate(0.0f, 1.0f, -1.0f);

        ScreenSpaceSprite swatchBg = new ScreenSpaceSprite("background", -1000,
                "whiteSwatch32px", Color.BLACK, true);
        int[] wSize = Window.get().getWindowSize();
        swatchBg.scale = new Vector3f(wSize[0], wSize[1], 1.0f);

        WallTorch torch1 = new WallTorch("torch1", -9);
        torch1.position = new Vector3f(0.0f, 1.0f, -0.9f);

        WallTorch torch2 = new WallTorch("torch2", -9);
        torch2.position = new Vector3f(5.0f, 1.0f, -0.9f);

        WallTorch torch3 = new WallTorch("torch3", -9);
        torch3.position = new Vector3f(-5.0f, 1.0f, -0.9f);

        SpriteLit doorProp1 = new SpriteLit("doorProp1", -9, "doorReinforcedAlb",
                "doorReinforcedNrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        doorProp1.position = new Vector3f(2.5f, 0.0f, -1.0f);
        doorProp1.scale = new Vector3f(2.0f, 2.0f, 2.0f);

        SpriteLit doorProp2 = new SpriteLit("doorProp2", -9, "doorReinforcedAlb",
                "doorReinforcedNrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        doorProp2.position = new Vector3f(-2.5f, 0.0f, -1.0f);
        doorProp2.scale = new Vector3f(2.0f, 2.0f, 2.0f);

        SpriteLit doorProp3 = new SpriteLit("doorProp3", -9, "doorReinforcedAlb",
                "doorReinforcedNrm", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        doorProp3.position = new Vector3f(7.5f, 0.0f, -1.0f);
        doorProp3.scale = new Vector3f(2.0f, 2.0f, 2.0f);

        ColliderAABB floorCollider = new ColliderAABB("floorCollider", 0, Color.DEBUG_DEFAULT_COLOR);
        floorCollider.layerMaskIndex = 2;
        floorCollider.position = new Vector3f(0.0f, -1.25f, 0.0f);
        floorCollider.scale = new Vector3f(20.0f, 0.5f, 1.0f);

        ColliderAABB wallColliderRight = new ColliderAABB("wallCollider", 0, Color.DEBUG_DEFAULT_COLOR);
        wallColliderRight.layerMaskIndex = 2;
        wallColliderRight.position = new Vector3f(11.0f, 1.0f, 0.0f);
        wallColliderRight.scale = new Vector3f(2.0f, 4.0f, 1.0f);
    }
}