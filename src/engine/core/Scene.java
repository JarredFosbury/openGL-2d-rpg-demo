package engine.core;

import engine.fontRendering.FontLoader;
import engine.rendering.*;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import game.DayNightCycle;
import game.PlayerSprite;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static DayNightCycle dayNightCycle;
    public static PlayerSprite player;
    public static Sprite groundPlane;
    public static TextMesh xp_UI;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        dayNightCycle = new DayNightCycle();
        dayNightCycle.totalCycleLength_MINS = 20.0f;
        dayNightCycle.setTime(9, 0);
        dayNightCycle.mainLightColor = dayNightCycle.dayLightColor;

        player = new PlayerSprite();
        groundPlane = new Sprite(new Texture("res/textures/SizeBasedGrids/c2m.png", true, true, false),
                dayNightCycle.mainLightColor, new Vector2f(0.0f), new Vector2f(10.0f));
        groundPlane.scale = new Vector3f(20.0f);

        xp_UI = new TextMesh(FontLoader.loadFont("res/fonts/liberation mono/LiberationMono_512x512_glyphMap.png",
                "res/fonts/liberation mono/LiberationMono_512x512_glyphMap.fnt"), true);
        xp_UI.fontSize_PIXELS = 32;
        xp_UI.locationAnchor = new Vector2i(-1, 1);
        xp_UI.position = new Vector3f(20.0f, -50.0f, 0.0f);
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
    }

    public static void fixedPhysicsUpdate()
    {
        player.fixedPhysicsUpdate();
        mainCamera.position = player.playerPosition;
    }

    public static void render()
    {
        groundPlane.render();
        player.render();

        xp_UI.drawString((int) dayNightCycle.timeInGame_HOURS + " HRS, " + (int) dayNightCycle.timeInGame_MINS + " MINS", Color.BLUE);
    }
}