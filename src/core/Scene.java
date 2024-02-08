package core;

import org.joml.Vector3f;
import rendering.Color;
import rendering.Sprite;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    private static Sprite brickSprite;
    private static int horizontalAxis;
    private static int verticalAxis;
    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        brickSprite = new Sprite("./res/textures/bricks_01.jpg", Color.WHITE);
    }

    public static void pollInput()
    {
        if (KeyListener.isKeyActive(GLFW_KEY_W))
            verticalAxis = 1;
        else if (KeyListener.isKeyActive(GLFW_KEY_S))
            verticalAxis = -1;
        else
            verticalAxis = 0;

        if (KeyListener.isKeyActive(GLFW_KEY_D))
            horizontalAxis = 1;
        else if (KeyListener.isKeyActive(GLFW_KEY_A))
            horizontalAxis = -1;
        else
            horizontalAxis = 0;
    }

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {
        brickSprite.rotate(0.0f, 0.0f, 45.0f * Time.fixedPhysicsTimeStep);
        mainCamera.translate(horizontalAxis * 2.5f * Time.fixedPhysicsTimeStep, verticalAxis * 2.5f * Time.fixedPhysicsTimeStep, 0.0f);
    }

    public static void render()
    {
        brickSprite.render();
    }
}