package engine.core;

import engine.audio.Listener;
import engine.imGui.*;
import engine.physics.PhysicsContext;
import engine.rendering.Debug;
import engine.shaders.*;
import game.*;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.opengl.GL15.*;

public class Scene
{
    public static Standard2dShader standard2dShader;
    public static Debug2dShader debug2dShader;
    public static ScreenSpace2dShader screenSpace2dShader;
    public static NormalMappedLit2DShader normalMappedLit2dShader;
    public static ScreenSpace9SliceShader screenSpace9sliceShader;
    public static NormalMappedActorLit2DShader normalMappedActorLit2dShader;

    public static Camera mainCamera;
    public static Listener audioListener;

    public static Vector4f ambientLight;
    public static AssetPool assets;
    public static SceneEntityList entities;
    public static ImGuiComponentList imGuiComponents;
    public static LightSourceList lightSources;
    public static PhysicsContext physics;

    public static void initialize()
    {
        glPolygonMode(GL_FRONT_FACE, GL_FILL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ambientLight = new Vector4f(0.1f);
        assets = new AssetPool();
        entities = new SceneEntityList();
        imGuiComponents = new ImGuiComponentList();
        lightSources = new LightSourceList();
        physics = new PhysicsContext();

        standard2dShader = new Standard2dShader("res/shaders/Standard2D.glsl");
        debug2dShader = new Debug2dShader("res/shaders/Debug2D.glsl");
        screenSpace2dShader = new ScreenSpace2dShader("res/shaders/ScreenSpace2D.glsl");
        normalMappedLit2dShader = new NormalMappedLit2DShader("res/shaders/NormalMappedLit2D.glsl");
        screenSpace9sliceShader = new ScreenSpace9SliceShader("res/shaders/ScreenSpace9SliceSprite2D.glsl");
        normalMappedActorLit2dShader = new NormalMappedActorLit2DShader("res/shaders/NormalMappedActorLit2D.glsl");

        // Adding editor windows and components
        new ImGuiTools();
        new MainMenuBar();
        new SceneEntityViewer();
        new AssetPoolWindow();
        new EntityInspectorWindow();
        new ColorPickerWindow();
        new PerformanceGraphWindow();

        // Loads first scene
        new TombOfTheDamnedScene();

        entities.endOfInit();
    }

    public static void update()
    {
        for (Entity entity : entities)
            entity.update();

        if (KeyListener.isKeyPressed(GLFW_KEY_F3))
        {
            GlobalSettings.renderDebug = !GlobalSettings.renderDebug;
            ImGuiWindow statsWindow = (ImGuiWindow) findImGuiComponents("performance_metrics_window")[0];
            statsWindow.isActive = GlobalSettings.renderDebug;
        }
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

        if (GlobalSettings.renderDebug)
        {
            for (Entity entity : entities)
                entity.renderDebug();

            Debug.render();
        }
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

    // TODO: move these into their respective classes (SceneEntityList, and SceneImGuiComponentList)
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

    public static void unload()
    {
        assets.releaseAllAssetsFromPool();
        physics.resetToDefaultValues();
        entities.clear();
    }
}