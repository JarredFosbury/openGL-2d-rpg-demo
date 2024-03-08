package engine.core;

import engine.imGui.*;
import engine.rendering.*;
import engine.shaders.*;
import game.*;
import org.joml.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;

    public static Camera mainCamera;

    public static SceneEntityList entities;
    public static List<ImGuiRootComponent> imGuiComponents;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");

        entities = new SceneEntityList();
        imGuiComponents = new ArrayList<>();

        new Camera("Main Camera");
        mainCamera = (Camera) findByName("Main Camera")[0];
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        new Sprite("Example Sprite", "res/textures/bricks_01.jpg", Color.WHITE, new Vector2f(0.0f), new Vector2f(1.0f));
        new HeartIconTest("Script behaviour");

        new MainMenuBar();
        new SceneEntityViewer();

        entities.endOfInit();
    }

    public static void pollInput()
    {
        for (Entity entity : entities)
            entity.pollInput();
    }

    public static void update()
    {
        for (Entity entity : entities)
            entity.update();
    }

    public static void fixedPhysicsUpdate()
    {
        for (Entity entity : entities)
            entity.fixedPhysicsUpdate();
    }

    public static void render()
    {
        for (Entity entity : entities)
            entity.render();
    }

    public static void renderImGui()
    {
        for (ImGuiRootComponent window : imGuiComponents)
            window.render();
    }

    public static void endOfFrame()
    {
        entities.executeActionsInQueue();
    }

    public static Entity[] findByName(String name)
    {
        List<Entity> foundEntities = new ArrayList<>();
        for (Entity entity : entities)
            if (entity.name.equals(name))
                foundEntities.add(entity);

        Entity[] out = new Entity[foundEntities.size()];
        for (int i = 0; i < out.length; i++)
            out[i] = foundEntities.get(i);

        if (out.length == 0)
            System.err.println("WARNING: Could not find entity with name " + name);

        return out;
    }
}