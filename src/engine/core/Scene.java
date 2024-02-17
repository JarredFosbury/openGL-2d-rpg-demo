package engine.core;

import engine.fontRendering.*;
import engine.physics.AxisAlignedBoundingBox;
import engine.physics.PhysicsContext;
import engine.rendering.*;
import engine.shaders.*;
import game.*;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static PhysicsContext physics;

    public static DayNightCycle dayNightCycle;
    public static PlayerSprite player;
    public static Sprite groundPlane;
    public static Sprite campfirePit;
    public static Sprite fire;

    public static TextMesh time_UI;

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
        dayNightCycle.totalCycleLength_MINS = 20.0f;
        dayNightCycle.setTime(19, 25);
        dayNightCycle.mainLightColor = dayNightCycle.dayLightColor;

        player = new PlayerSprite();

        groundPlane = new Sprite(new Texture("res/textures/SizeBasedGrids/c2m.png", true, true, false),
                dayNightCycle.mainLightColor, new Vector2f(0.0f), new Vector2f(10.0f));
        groundPlane.scale = new Vector3f(20.0f);

        campfirePit = new Sprite(new Texture("res/textures/Tilesets/001-Grassland01.png", true, false, true),
                dayNightCycle.mainLightColor, new Vector2f(0.6328125f, 0.78298611f), new Vector2f(0.1f, 0.05f));

        fire = new Sprite(new Texture("res/textures/fireSheet_lowRes.png", true, false, true),
                dayNightCycle.mainLightColor, new Vector2f(0.0f, 0.75f), new Vector2f(0.125f, 0.25f));
        fire.scale = new Vector3f(1.0f, 2.0f, 1.0f);
        fire.position = new Vector3f(0.0f, 0.65f, 0.0f);
        fire.initSpriteSheet("res/spriteSheetData/fireSheetData.ssd");
        fire.animationFrameRate = 30;

        time_UI = new TextMesh(FontLoader.loadFont("res/fonts/liberation mono/LiberationMono_512x512_glyphMap.png",
                "res/fonts/liberation mono/LiberationMono_512x512_glyphMap.fnt"), true);
        time_UI.fontSize_PIXELS = 24;
        time_UI.locationAnchor = new Vector2i(1, 1);
        time_UI.position = new Vector3f(-25, -25, 0.0f);
        time_UI.textAlignment = new Vector2i(-1, 0);
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
        groundPlane.mainTextureTint = dayNightCycle.mainLightColor;
        campfirePit.mainTextureTint = dayNightCycle.mainLightColor;
        fire.animateSprite();
    }

    public static void fixedPhysicsUpdate()
    {
        player.fixedPhysicsUpdate();
        mainCamera.position = player.playerPosition;
    }

    public static void render()
    {
        groundPlane.render();
        campfirePit.render();
        fire.render();
        player.render();

        time_UI.drawString("Time: " + (int) dayNightCycle.timeInGame_HOURS + " hrs, " + (int) dayNightCycle.timeInGame_MINS + " mins", Color.WHITE);
    }
}