package engine.core;

import engine.physics.AxisAlignedBoundingBox;
import engine.rendering.*;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import game.PlayerSprite;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    private static PlayerSprite player;
    private static Sprite groundPlane;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        player = new PlayerSprite();
        groundPlane = new Sprite(new Texture("res/textures/SizeBasedGrids/c2m.png", true, true, false),
                new Vector4f(new Vector3f(0.9f), 1.0f), new Vector2f(0.0f), new Vector2f(10.0f));
        groundPlane.scale = new Vector3f(20.0f);
    }

    public static void pollInput()
    {
        player.pollInput();
    }

    public static void update()
    {
        player.update();
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
    }
}