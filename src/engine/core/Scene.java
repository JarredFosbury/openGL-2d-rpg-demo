package engine.core;

import engine.fontRendering.FontLoader;
import engine.rendering.Color;
import engine.rendering.ScreenSpaceSprite;
import engine.rendering.Sprite;
import engine.rendering.TextMesh;
import engine.shaders.*;
import game.HeartIconTest;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static Camera mainCamera;

    public static List<Entity> entityList;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        entityList = new ArrayList<>();

        mainCamera = new Camera("Main Camera");
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        new Sprite("Example Sprite", "res/textures/bricks_01.jpg", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
    }

    public static void pollInput()
    {
        for (Entity entity : entityList)
            entity.pollInput();
    }

    public static void update()
    {
        for (Entity entity : entityList)
            entity.update();
    }

    public static void fixedPhysicsUpdate()
    {
        for (Entity entity : entityList)
            entity.fixedPhysicsUpdate();
    }

    public static void render()
    {
        for (Entity entity : entityList)
            entity.render();
    }

    public static void renderImGui()
    {}

    public static Entity[] findByName(String name)
    {
        List<Entity> foundEntities = new ArrayList<>();
        for (Entity entity : entityList)
            if (entity.name.equals(name))
                foundEntities.add(entity);

        Entity[] out = new Entity[foundEntities.size()];
        for (int i = 0; i < out.length; i++)
            out[i] = foundEntities.get(i);

        return out;
    }
}