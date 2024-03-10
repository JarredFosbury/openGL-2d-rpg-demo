package engine.core;

import engine.imGui.*;
import engine.shaders.NormalMappedLit2DShader;
import engine.shaders.ScreenSpace2dShader;
import engine.shaders.Standard2dShader;
import game.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Standard2dShader standard2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;
    public static NormalMappedLit2DShader normalMappedLit2dShader;

    public static Camera mainCamera;

    public static AssetPool assets;
    public static SceneEntityList entities;
    public static SceneImGuiComponentList imGuiComponents;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        assets = new AssetPool();
        entities = new SceneEntityList();
        imGuiComponents = new SceneImGuiComponentList();

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");
        normalMappedLit2dShader = new NormalMappedLit2DShader("res/shaders/NormalMappedLit2D.glsl");

        new ImGuiTools();
        new MainMenuBar();
        new SceneEntityViewer();
        new AssetPoolWindow();
        new EntityInspectorWindow();

        new Camera("Main Camera");
        mainCamera = (Camera) findByName("Main Camera")[0];
        mainCamera.updateViewport(4.0f, -1.0f, 1.0f);

        new MainMenuScene();

        entities.endOfInit();
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

    public static ImGuiRootComponent[] findImGuiComponents(String name)
    {
        List<ImGuiRootComponent> foundComponents = new ArrayList<>();
        for (ImGuiRootComponent component : imGuiComponents)
            if (component.NAME.equals(name))
                foundComponents.add(component);

        ImGuiRootComponent[] outComponents = new ImGuiRootComponent[foundComponents.size()];
        for (int i = 0; i < outComponents.length; i++)
            outComponents[i] = foundComponents.get(i);

        if (outComponents.length == 0)
            System.err.println("WARNING: Could not find ImGuiComponent with name " + name);

        return outComponents;
    }
}