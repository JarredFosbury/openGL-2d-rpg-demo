package engine.core;

import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.ScreenSpaceSprite;
import engine.rendering.Sprite;
import engine.rendering.TextMesh;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    private static Sprite brickSprite;
    private static TextMesh exampleText;
    private static ScreenSpaceSprite heartSprite;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        brickSprite = new Sprite("./res/textures/bricks_01.jpg", Color.WHITE);
        exampleText = new TextMesh(FontLoader.loadFont("./res/fonts/liberation mono/LiberationMono_512x512_glyphMap.png",
                "./res/fonts/liberation mono/LiberationMono_512x512_glyphMap.fnt"), true);
        exampleText.locationAnchor = new Vector2i(0, 1);
        exampleText.position = new Vector3f(0.0f, -50.0f, 0.0f);
        exampleText.fontSize_PIXELS = 72;
        heartSprite = new ScreenSpaceSprite("./res/textures/UI/heart.png", Color.WHITE, true);
        heartSprite.locationAnchor = new Vector2i(0, 1);
        heartSprite.position = new Vector3f(0.0f, -50.0f, 0.0f);
        heartSprite.scale = new Vector3f(15.0f);
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {
        brickSprite.render();
        exampleText.drawString("Hello world!", Color.GREEN);
        heartSprite.render();
    }
}