package engine.core;

import engine.rendering.*;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    private static Texture linkSpriteSheet;
    private static Sprite player;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        linkSpriteSheet = new Texture("res/textures/linkSheet.png", true, false, true);
        player = new Sprite(linkSpriteSheet, Color.WHITE, new Vector2f(0.0f, 0.875f), new Vector2f(0.1f, 0.125f));
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {
        player.render();
    }
}