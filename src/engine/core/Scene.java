package engine.core;

import engine.fontRendering.FontLoader;
import engine.physics.AxisAlignedBoundingBox;
import engine.rendering.*;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import game.PlayerSprite;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static Vector4f lightColor;
    public static Vector4f dayColor;
    public static Vector4f nightColor;

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

        dayColor = new Vector4f(0.96f, 0.9f, 0.86f, 1.0f);
        nightColor = new Vector4f(0.46f, 0.58f, 0.7f, 1.0f);
        lightColor = dayColor;

        player = new PlayerSprite();
        groundPlane = new Sprite(new Texture("res/textures/SizeBasedGrids/c2m.png", true, true, false),
                lightColor, new Vector2f(0.0f), new Vector2f(10.0f));
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
        player.update();
        player.updateColor(lightColor);
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

        xp_UI.drawString("XP 0/50", new Vector4f(0.46f, 0.12f, 0.61f, 1.0f));
    }
}