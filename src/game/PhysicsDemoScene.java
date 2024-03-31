package game;

import engine.audio.Listener;
import engine.core.*;
import engine.fontRendering.FontLoader;
import engine.physics.ColliderAABB;
import engine.rendering.Color;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PhysicsDemoScene extends Entity
{
    private final ColliderAABB staticCollider1;
    private final ColliderAABB staticCollider2;
    private final ColliderAABB staticCollider3;
    private final ColliderAABB staticCollider4;
    private final ColliderAABB dynamicCollider;

    private int axisHorizontal;
    private float verticalVelocity;

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

        Scene.physics.layerMasks.add("InteractiveColliders");
        staticCollider1.position = new Vector3f(8.0f, 0.5f, 0.0f);
        staticCollider1.scale = new Vector3f(8.0f, 1.0f, 1.0f);
        staticCollider1.layerMaskIndex = 1;
        staticCollider2.position = new Vector3f(0.0f, 1.5f, 0.0f);
        staticCollider2.scale = new Vector3f(8.0f, 1.0f, 1.0f);
        staticCollider2.layerMaskIndex = 1;
        staticCollider3.position = new Vector3f(-4.5f, 4.0f, 0.0f);
        staticCollider3.scale = new Vector3f(1.0f, 4.0f, 1.0f);
        staticCollider3.layerMaskIndex = 1;
        staticCollider4.position = new Vector3f(12.5f, 3.0f, 0.0f);
        staticCollider4.scale = new Vector3f(1.0f, 4.0f, 1.0f);
        staticCollider4.layerMaskIndex = 1;
        dynamicCollider.position = new Vector3f(1.2999961f, 3.6333323f, 0.0f);
        dynamicCollider.layerMaskIndex = 1;
    }

    public void start()
    {}

    public void update()
    {
        if (KeyListener.isKeyPressed(GLFW_KEY_UP) && Math.abs(verticalVelocity) <= 0.01f)
            verticalVelocity = 5.0f;

        if (KeyListener.isKeyActive(GLFW_KEY_RIGHT))
            axisHorizontal = 1;
        else if (KeyListener.isKeyActive(GLFW_KEY_LEFT))
            axisHorizontal = -1;
        else
            axisHorizontal = 0;
    }

    public void fixedPhysicsUpdate()
    {
        verticalVelocity -= 9.81f * Time.fixedPhysicsTimeStep;
        dynamicCollider.translate(axisHorizontal * Time.fixedPhysicsTimeStep * 3.5f, verticalVelocity * Time.fixedPhysicsTimeStep, 0.0f);
        Vector3f collisionDelta = Scene.physics.collideWithLayer(dynamicCollider, 1);
        dynamicCollider.translate(collisionDelta);
        if (collisionDelta.y > 0.0f)
            verticalVelocity = 0.0f;
    }

    public void render()
    {}
}