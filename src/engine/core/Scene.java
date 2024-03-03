package engine.core;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {}

    public static void renderImGui()
    {}
}