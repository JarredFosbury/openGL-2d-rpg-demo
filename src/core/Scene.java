package core;

import rendering.ScreenSpaceSprite;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    private static ScreenSpaceSprite heartSprite;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        heartSprite = new ScreenSpaceSprite("./res/textures/UI/heart.png");
    }

    public static void pollInput()
    {}

    public static void update()
    {}

    public static void fixedPhysicsUpdate()
    {}

    public static void render()
    {
        heartSprite.render();
    }
}