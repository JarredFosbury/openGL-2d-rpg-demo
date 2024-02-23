package engine.core;

import engine.fontRendering.*;
import engine.imGui.ColorPickerWindow;
import engine.physics.PhysicsContext;
import engine.rendering.*;
import engine.shaders.*;
import game.*;
import org.joml.*;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static PhysicsContext physics;

    public static DayNightCycle dayNightCycle;
    public static PlayerSprite player;
    public static IslandEnvironmentSprites island;
    public static Sprite campfirePit;
    public static Sprite fire;
    public static Sprite tree;
    public static Sprite treeStump;

    public static TextMesh time_UI;
    public static TextMesh hp_UI;
    public static TextMesh xp_UI;

    public static ColorPickerWindow colorPickerWindow;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        physics = new PhysicsContext();

        dayNightCycle = new DayNightCycle();
        dayNightCycle.totalCycleLength_MINS = 2.0f;
        dayNightCycle.setTime(8, 30);
        dayNightCycle.mainLightColor = dayNightCycle.dayLightColor;

        player = new PlayerSprite();
        player.playerPosition = new Vector3f(-1.0f, 0.25f, 0.0f);

        island = new IslandEnvironmentSprites();

        campfirePit = new Sprite(new Texture("res/textures/campfire.png", true, false, true),
                dayNightCycle.mainLightColor, new Vector2f(0.0f), new Vector2f(1.0f));

        fire = new Sprite(new Texture("res/textures/fireSheet_lowRes.png", true, false, true),
                dayNightCycle.mainLightColor, new Vector2f(0.0f, 0.75f), new Vector2f(0.125f, 0.25f));
        fire.scale = new Vector3f(1.0f, 2.0f, 1.0f);
        fire.position = new Vector3f(0.0f, 0.75f, 0.0f);
        fire.initSpriteSheet("res/spriteSheetData/fireSheetData.ssd");
        fire.animationFrameRate = 30;

        tree = new Sprite(new Texture("res/textures/tree_01.png", true, true, true),
                dayNightCycle.mainLightColor, new Vector2f(0.0f), new Vector2f(1.0f));
        tree.scale = new Vector3f(3.0f, 4.0f, 1.0f);
        tree.translate(0.0f, 4.0f, 0.0f);

        treeStump = new Sprite(new Texture("res/textures/treeStump_01.png", true, true, true),
                dayNightCycle.mainLightColor, new Vector2f(0.0f), new Vector2f(1.0f));
        treeStump.scale = new Vector3f(0.75f, 1.0f, 1.0f);
        treeStump.translate(0.0f, 2.3f, 0.0f);

        time_UI = new TextMesh(FontLoader.loadFont("res/fonts/press start/press-start_512x512_glyphMap.png",
                "res/fonts/press start/press-start_512x512_glyphMap.fnt"), true);
        time_UI.fontSize_PIXELS = 32;
        time_UI.locationAnchor = new Vector2i(1, 1);
        time_UI.position = new Vector3f(-25, -25, 0.0f);
        time_UI.textAlignment = new Vector2i(-1, 0);

        hp_UI = new TextMesh(FontLoader.loadFont("res/fonts/press start/press-start_512x512_glyphMap.png",
                "res/fonts/press start/press-start_512x512_glyphMap.fnt"), true);
        hp_UI.fontSize_PIXELS = 45;
        hp_UI.locationAnchor = new Vector2i(-1, 1);
        hp_UI.position = new Vector3f(25, -50, 0.0f);

        xp_UI = new TextMesh(FontLoader.loadFont("res/fonts/press start/press-start_512x512_glyphMap.png",
                "res/fonts/press start/press-start_512x512_glyphMap.fnt"), true);
        xp_UI.fontSize_PIXELS = 45;
        xp_UI.locationAnchor = new Vector2i(-1, 1);
        xp_UI.position = new Vector3f(25, -100, 0.0f);

        colorPickerWindow = new ColorPickerWindow();
    }

    public static void pollInput()
    {
        player.pollInput();
    }

    public static void update()
    {
        dayNightCycle.update();
        player.update();
        player.updateColor(dayNightCycle.mainLightColor);
        campfirePit.mainTextureTint = dayNightCycle.mainLightColor;
        tree.mainTextureTint = dayNightCycle.mainLightColor;
        treeStump.mainTextureTint = dayNightCycle.mainLightColor;
        fire.animateSprite();
        island.update();
    }

    public static void fixedPhysicsUpdate()
    {
        player.fixedPhysicsUpdate();
        mainCamera.position = player.playerPosition;
    }

    public static void render()
    {
        island.render();
        campfirePit.render();
        treeStump.render();

        player.render();

        fire.render();
        tree.render();

        time_UI.drawString("Time " + dayNightCycle.getTimeFormatted(), new Vector4f(0.902f, 0.902f, 0.902f, 1.0f));
        hp_UI.drawString("HP 80/80", new Vector4f(0.814f, 0.032f, 0.032f, 1.0f));
        xp_UI.drawString("XP 0/50", new Vector4f(0.637f, 0.128f, 0.532f, 1.0f));
    }

    public static void renderImGui()
    {
        colorPickerWindow.render();
    }
}