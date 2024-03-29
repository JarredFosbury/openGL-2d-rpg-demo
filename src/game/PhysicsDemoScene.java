package game;

import engine.audio.Listener;
import engine.core.Camera;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.fontRendering.FontLoader;
import engine.physics.ColliderAABB;
import engine.rendering.Color;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PhysicsDemoScene extends Entity
{
    private final ColliderAABB staticCollider1;
    private final ColliderAABB staticCollider2;
    private final ColliderAABB staticCollider3;
    private final ColliderAABB staticCollider4;
    private final ColliderAABB dynamicCollider;
    private final ColliderAABB ghostCollider;

    public PhysicsDemoScene()
    {
        super("Physics demo scene", EntityType.ScriptableBehavior, 0);
        Scene.assets.addAssetToPool(FontLoader.loadFont("res/fonts/consolas/consolas.png",
                "res/fonts/consolas/consolas.fnt"), "consolas");

        Scene.audioListener = new Listener();
        Scene.mainCamera = new Camera("Main Camera", 0);
        Scene.mainCamera.updateViewport(1.0f, -1.0f, 1.0f);
        new FreeCamera();

        staticCollider1 = new ColliderAABB("staticCollider1", 0, Color.DEBUG_DEFAULT_COLOR);
        staticCollider2 = new ColliderAABB("staticCollider2", 0, Color.DEBUG_DEFAULT_COLOR);
        staticCollider3 = new ColliderAABB("staticCollider3", 0, Color.DEBUG_DEFAULT_COLOR);
        staticCollider4 = new ColliderAABB("staticCollider4", 0, Color.DEBUG_DEFAULT_COLOR);
        dynamicCollider = new ColliderAABB("dynamicCollider", 0, Color.GREEN);
        ghostCollider = new ColliderAABB("ghostCollider", 0, new Vector4f(0.953f, 1.0f, 0.0f, 1.0f));

        staticCollider1.position = new Vector3f(0.0f, -1.5f, 0.0f);
        staticCollider1.scale = new Vector3f(8.0f, 1.0f, 1.0f);
        staticCollider2.position = new Vector3f(0.0f, 1.5f, 0.0f);
        staticCollider2.scale = new Vector3f(8.0f, 1.0f, 1.0f);
        staticCollider3.position = new Vector3f(3.5f, 0.0f, 0.0f);
        staticCollider3.scale = new Vector3f(1.0f, 2.0f, 1.0f);
        staticCollider4.position = new Vector3f(-1.1f, 0.0f, 0.0f);
        staticCollider4.scale = new Vector3f(4.0f, 2.0f, 1.0f);
    }

    public void start()
    {}

    public void update()
    {}

    public void fixedPhysicsUpdate()
    {
        ghostCollider.position = new Vector3f(dynamicCollider.position)
                .add(dynamicCollider.collide(staticCollider1))
                .add(dynamicCollider.collide(staticCollider2))
                .add(dynamicCollider.collide(staticCollider3))
                .add(dynamicCollider.collide(staticCollider4));
    }

    public void render()
    {}

    public void unloadScene()
    {
        Scene.assets.releaseAllAssetsFromPool();
        Scene.physics.resetToDefaultValues();
        Scene.entities.clear();
    }
}