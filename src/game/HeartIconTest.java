package game;

import engine.core.*;
import engine.rendering.Sprite;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class HeartIconTest extends Entity
{
    private Sprite heart;

    public HeartIconTest(String name)
    {
        super(name, EntityType.ScriptableBehavior);
        heart = (Sprite) Scene.findByName("Example Sprite")[0];
    }

    public void pollInput()
    {
        if (KeyListener.isKeyPressed(GLFW_KEY_W))
            Scene.entities.remove(heart);
    }

    public void update()
    {
        heart.rotate(0.0f, 0.0f, 45.0f * Time.deltaTime);
    }
}