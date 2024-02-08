package core;

import org.joml.Vector2i;
import org.joml.Vector3f;
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
        heartSprite.isLocationAnchored = true;
        heartSprite.locationAnchor = new Vector2i(0, 0);
        heartSprite.position = new Vector3f(0.0f, 0.0f, 0.0f);
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