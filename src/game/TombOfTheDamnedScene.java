package game;

import engine.audio.Listener;
import engine.audio.SoundClip;
import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.rendering.*;
import engine.vfx.ParticleSystem;
import engine.vfx.ParticleSystemConfiguration;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class TombOfTheDamnedScene extends Entity
{
    private final ParticleSystem demoSystem;

    public TombOfTheDamnedScene()
    {
        super("Game Scene Handler", EntityType.ScriptableBehavior, 0);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/morris roman/morrisRoman.png",
                "res/fonts/morris roman/morrisRoman.fnt"), "morrisRoman");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/materials/cobblestoneWall_alb.png",
                true, true, true), "cobblestoneWallAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/materials/cobblestoneWall_nrm.png",
                true, true, true), "cobblestoneWallNormal");
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
        Scene.assets.addAssetToPool(new Texture("res/textures/particles/defaultParticleWhite128px.png", true,
                true, false), "defaultParticle");

        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx01.ogg"), "caveDrip01");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx02.ogg"), "caveDrip02");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx03.ogg"), "caveDrip03");
        Scene.assets.addAssetToPool(new SoundClip("res/audio/sfx/waterDripSfx04.ogg"), "caveDrip04");

        Scene.audioListener = new Listener();
        Scene.mainCamera = new Camera("Main Camera", 0);
        Scene.mainCamera.updateViewport(1.5f, -1.0f, 1.0f);

        MainLightSource source = new MainLightSource("mainLightSource", 0, Color.WHITE, 0.0f);
        source.rotate(-55.0f, 0.0f, -45.0f);

        new PlayerStartingAnimationController();
        new DrippingWaterSfxHandler();
        new PlayerGameHUD(1000);

        SpriteLit wall = new SpriteLit("cobblestoneBackgroundWall", -1, "cobblestoneWallAlbedo",
                "cobblestoneWallNormal", Color.WHITE, new Vector2f(0.0f), new Vector2f(10.0f, 2.0f));
        wall.scale = new Vector3f(20.0f, 4.0f, 1.0f);
        wall.translate(0.0f, 1.0f, -1.0f);

        ScreenSpaceSprite swatchBg = new ScreenSpaceSprite("background", -1000, "whiteSwatch32px", Color.BLACK, true);
        int[] wSize = Window.get().getWindowSize();
        swatchBg.scale = new Vector3f(wSize[0], wSize[1], 1.0f);

        ParticleSystemConfiguration config = new ParticleSystemConfiguration();
        config.maxParticleCount = 100;
        config.looping = true;
        config.size = new Vector3f(0.1f);
        config.textureKey = "defaultParticle";
        config.emissionModule.use = true;
        config.emissionModule.emitsPerSecond = 20.0f;
        config.startWithRandomizedRotation = true;
        config.shapeModule.use = true;
        config.shapeModule.shape = ParticleSystemConfiguration.Shape.Circle;
        config.shapeModule.radius = 0.5f;
        demoSystem = new ParticleSystem("demoParticleSystem", 50, config);
    }

    public void update()
    {
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE))
            demoSystem.play();
    }
}