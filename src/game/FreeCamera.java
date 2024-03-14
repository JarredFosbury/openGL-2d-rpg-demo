package game;

import engine.core.*;
import engine.rendering.Color;
import engine.rendering.ScreenSpace9SliceSprite;
import engine.rendering.TextMesh;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.text.NumberFormat;

import static org.lwjgl.glfw.GLFW.*;

public class FreeCamera extends Entity
{
    private final Camera mainCamera;
    private final TextMesh debugPositionText;
    private final TextMesh debugViewportText;
    private final TextMesh debugStateText;
    private final ScreenSpace9SliceSprite sprite9Slice;

    private float moveSpeed;
    private float shiftSpeedMultiplier;
    private float scrollSpeed;

    public FreeCamera()
    {
        super("freeCamController-script", EntityType.ScriptableBehavior, 0);

        mainCamera = Scene.mainCamera;

        debugPositionText = new TextMesh("cameraPosDebug-text", 1000, "consolas", true);
        debugPositionText.fontSize_PIXELS = 24.0f;
        debugPositionText.locationAnchor = new Vector2i(-1, 1);
        debugPositionText.position = new Vector3f(25.0f, -60.0f, 0.0f);
        debugPositionText.textAlignment = new Vector2i(1, 0);

        debugViewportText = new TextMesh("cameraViewportDebug-text", 1000, "consolas", true);
        debugViewportText.fontSize_PIXELS = 24.0f;
        debugViewportText.locationAnchor = new Vector2i(-1, 1);
        debugViewportText.position = new Vector3f(25.0f, -100.0f, 0.0f);
        debugViewportText.textAlignment = new Vector2i(1, 0);

        debugStateText = new TextMesh("cameraStateDebug-text", 1000, "consolas", true);
        debugStateText.fontSize_PIXELS = 24.0f;
        debugStateText.locationAnchor = new Vector2i(-1, 1);
        debugStateText.position = new Vector3f(25.0f, -140.0f, 0.0f);
        debugStateText.textAlignment = new Vector2i(1, 0);
        debugStateText.text = "Free Camera Mode";

        sprite9Slice = new ScreenSpace9SliceSprite("sprite9Slice-TEST", 1000,
                "9sliceFrameSprite", Color.WHITE, true, new Vector4f(8.0f));
        sprite9Slice.locationAnchor = new Vector2i(-1, 1);
        sprite9Slice.position = new Vector3f(266.0f, -74.0f, 0.0f);
        sprite9Slice.scale = new Vector3f(512.0f, 128.0f, 1.0f);
    }

    public void start()
    {
        moveSpeed = 2.0f;
        shiftSpeedMultiplier = 3.5f;
        scrollSpeed = 65.0f;
    }

    public void update()
    {
        int horizontalAxis = 0;
        int verticalAxis = 0;

        if (KeyListener.isKeyActive(GLFW_KEY_W))
            verticalAxis = 1;
        else if (KeyListener.isKeyActive(GLFW_KEY_S))
            verticalAxis = -1;

        if (KeyListener.isKeyActive(GLFW_KEY_D))
            horizontalAxis = 1;
        else if (KeyListener.isKeyActive(GLFW_KEY_A))
            horizontalAxis = -1;

        float speed = (KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT) ? shiftSpeedMultiplier : 1.0f) * moveSpeed * Time.deltaTime;
        mainCamera.translate(horizontalAxis * speed, verticalAxis * speed, 0.0f);

        float planeDelta = -MouseListener.getScrollY() * scrollSpeed * Time.deltaTime;
        mainCamera.updateViewport(mainCamera.viewPlaneScale + planeDelta, -1.0f, 1.0f);

        debugPositionText.text = "position " + mainCamera.position.toString(NumberFormat.getNumberInstance());
        debugViewportText.text = "view plane " + Utils.round(mainCamera.viewPlaneScale, 3);
    }

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}
}