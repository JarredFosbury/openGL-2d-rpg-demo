package engine.core;

import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.Sprite;
import engine.rendering.TextMesh;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    private static Sprite sprite;
    private static TextMesh text;
    private static TextMesh text2;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        mainCamera.updateViewport(1.0f, -1.0f, 1.0f);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        sprite = new Sprite("res/textures/fireSheet.png", Color.WHITE, new Vector2f(0.0f, 0.75f), new Vector2f(0.125f, 0.25f));
        sprite.scale = new Vector3f(0.5f, 1.0f, 1.0f);
        sprite.initSpriteSheet("res/spriteSheetData/fireSheetData.ssd");

        text = new TextMesh(FontLoader.loadFont("res/fonts/liberation mono/LiberationMono_512x512_glyphMap.png",
                "res/fonts/liberation mono/LiberationMono_512x512_glyphMap.fnt"), true);
        text.fontSize_PIXELS = 24;
        text.locationAnchor = new Vector2i(-1, 1);
        text.position = new Vector3f(20.0f, -40.0f, 0.0f);

        text2 = new TextMesh(FontLoader.loadFont("res/fonts/liberation mono/LiberationMono_512x512_glyphMap.png",
                "res/fonts/liberation mono/LiberationMono_512x512_glyphMap.fnt"), true);
        text2.fontSize_PIXELS = 24;
        text2.locationAnchor = new Vector2i(-1, 1);
        text2.position = new Vector3f(20.0f, -80.0f, 0.0f);
    }

    public static void pollInput()
    {
        if (KeyListener.isKeyPressed(GLFW_KEY_UP))
            sprite.mainTextureOffset.y += 0.25f;

        if (KeyListener.isKeyPressed(GLFW_KEY_DOWN))
            sprite.mainTextureOffset.y -= 0.25f;

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT))
            sprite.mainTextureOffset.x -= 0.125f;

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT))
            sprite.mainTextureOffset.x += 0.125f;

        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE))
            sprite.nextSpriteSheetFrame();
    }

    public static void update()
    {
        sprite.animateSprite();
    }

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {
        sprite.render();

        text.drawString("Animation frame: " + sprite.spriteSheetFrame, Color.WHITE);
        text2.drawString("Offset: " + sprite.mainTextureOffset.x + ", " + sprite.mainTextureOffset.y, Color.WHITE);
    }
}