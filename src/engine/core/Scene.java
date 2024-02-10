package engine.core;

import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Camera mainCamera;

    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mainCamera = new Camera();
        mainCamera.updateViewport(152.0f, -1.0f, 1.0f);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {}
}