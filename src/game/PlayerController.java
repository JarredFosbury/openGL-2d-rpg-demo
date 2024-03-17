package game;

import engine.core.*;
import engine.rendering.Color;
import engine.rendering.TextMesh;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Entity
{
    private enum AnimationState
    {
        IDLE,
        WALKING,
        RUNNING,
        JUMP_UP,
        JUMP_DOWN;
    }

    private final Camera mainCamera;
    private final SpriteLitActor idleSprite;
    private final SpriteLitActor walkingSprite;
    private final SpriteLitActor runningSprite;
    private final SpriteLitActor jumpUpSprite;
    private final SpriteLitActor jumpDownSprite;
    private final SpriteLitActor[] sprites;
    private AnimationState animState;

    private final TextMesh debugDirectionText;

    private float walkSpeed;
    private float runSpeed;

    public PlayerController()
    {
        super("PlayerController", EntityType.ScriptableBehavior, 0);
        idleSprite = new SpriteLitActor("playerIdleSpriteSheet", 0, "breathingIdleAlbedo",
                "breathingIdleNormal", Color.WHITE, new Vector2f(0.0f, 0.93333334f), new Vector2f(0.06666667f));
        idleSprite.initSpriteSheet("res/textures/litSprites/player/breathingIdle/playerBreathingIdle_SheetData.ssd", true, false);
        idleSprite.isVisible = false;

        walkingSprite = new SpriteLitActor("playerWalkingSpriteSheet", 0, "walkingAlbedo",
                "walkingNormal", Color.WHITE, new Vector2f(0.0f, 0.83333325f), new Vector2f(0.16666667f));
        walkingSprite.initSpriteSheet("res/textures/litSprites/player/walking/playerWalking_SheetData.ssd", true, false);
        walkingSprite.isVisible = false;

        runningSprite = new SpriteLitActor("playerRunningSpriteSheet", 0, "runningAlbedo",
                "runningNormal", Color.WHITE, new Vector2f(0.0f, 0.75f), new Vector2f(0.25f));
        runningSprite.initSpriteSheet("res/textures/litSprites/player/running/playerRunning_SheetData.ssd", true, false);
        runningSprite.isVisible = false;

        jumpUpSprite = new SpriteLitActor("playerJumpUpSpriteSheet", 0, "jumpingUpAlbedo",
                "jumpingUpNormal", Color.WHITE, new Vector2f(0.0f, 0.8f), new Vector2f(0.2f));
        jumpUpSprite.initSpriteSheet("res/textures/litSprites/player/jumpingUp/playerJumpingUp_SheetData.ssd", false, true);
        jumpUpSprite.isVisible = false;

        jumpDownSprite = new SpriteLitActor("playerJumpDownSpriteSheet", 0, "jumpingDownAlbedo",
                "jumpingDownNormal", Color.WHITE, new Vector2f(0.0f, 0.83333325f), new Vector2f(0.16666667f));
        jumpDownSprite.initSpriteSheet("res/textures/litSprites/player/jumpingDown/playerJumpingDown_SheetData.ssd", false, true);
        jumpDownSprite.isVisible = false;

        sprites = new SpriteLitActor[] {idleSprite, walkingSprite, runningSprite, jumpUpSprite, jumpDownSprite};
        animState = AnimationState.IDLE;
        mainCamera = Scene.mainCamera;
        scale = new Vector3f(2.0f);
        walkSpeed = 1.5f;
        runSpeed = 4.0f;

        debugDirectionText = new TextMesh("debugPlayerDirection-text", 1000, "consolas", true);
        debugDirectionText.locationAnchor = new Vector2i(-1, 1);
        debugDirectionText.position = new Vector3f(20.0f, -55.0f, 0.0f);
        debugDirectionText.fontSize_PIXELS = 24.0f;
    }

    public void start()
    {
        setAnimationState(AnimationState.IDLE);
    }

    public void update()
    {
        syncTransforms();
        manageAnimationState();

        debugDirectionText.text = "Horizontal direction: " + idleSprite.horizontalDirection;
    }

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}

    private void syncTransforms()
    {
        for (SpriteLitActor sprite : sprites)
        {
            sprite.position = position;
            sprite.scale = scale;
        }

        mainCamera.position = position;
    }

    private void setHorizontalDirection(float dir)
    {
        float scaleFactor = scale.y;
        scale = new Vector3f(scaleFactor * dir, scaleFactor, 1.0f);
        idleSprite.horizontalDirection = dir;
        walkingSprite.horizontalDirection = dir;
        runningSprite.horizontalDirection = dir;
        jumpUpSprite.horizontalDirection = dir;
        jumpDownSprite.horizontalDirection = dir;
    }

    private void manageAnimationState()
    {
        switch (animState)
        {
            case IDLE -> animationState_IDLE();
            case WALKING -> animationState_WALKING();
            case RUNNING -> animationState_RUNNING();
        }
    }

    private void setAnimationState(AnimationState state)
    {
        this.animState = state;
        idleSprite.isVisible = animState == AnimationState.IDLE;
        walkingSprite.isVisible = animState == AnimationState.WALKING;
        runningSprite.isVisible = animState == AnimationState.RUNNING;
        jumpUpSprite.isVisible = animState == AnimationState.JUMP_UP;
        jumpDownSprite.isVisible = animState == AnimationState.JUMP_DOWN;
    }

    private void animationState_IDLE()
    {
        if (!idleSprite.getPlayingState())
            idleSprite.playAnimation();

        if (KeyListener.isKeyActive(GLFW_KEY_D))
        {
            setAnimationState(AnimationState.WALKING);
        }

        if (KeyListener.isKeyActive(GLFW_KEY_A))
        {
            setAnimationState(AnimationState.WALKING);
        }
    }

    private void animationState_WALKING()
    {
        if (!walkingSprite.getPlayingState())
            walkingSprite.playAnimation();

       if (KeyListener.isKeyActive(GLFW_KEY_D) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           translate(walkSpeed * Time.deltaTime, 0.0f, 0.0f);
           setHorizontalDirection(1.0f);
       }
       else if (KeyListener.isKeyActive(GLFW_KEY_A) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           translate(-walkSpeed * Time.deltaTime, 0.0f, 0.0f);
           setHorizontalDirection(-1.0f);
       }
       else if ((KeyListener.isKeyActive(GLFW_KEY_A) || KeyListener.isKeyActive(GLFW_KEY_D)) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           setAnimationState(AnimationState.RUNNING);
       }
       else
       {
           setAnimationState(AnimationState.IDLE);
       }
    }

    private void animationState_RUNNING()
    {
        if (!runningSprite.getPlayingState())
            runningSprite.playAnimation();

        if (KeyListener.isKeyActive(GLFW_KEY_D) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            translate(runSpeed * Time.deltaTime, 0.0f, 0.0f);
            setHorizontalDirection(1.0f);
        }
        else if (KeyListener.isKeyActive(GLFW_KEY_A) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            translate(-runSpeed * Time.deltaTime, 0.0f, 0.0f);
            setHorizontalDirection(-1.0f);
        }
        else if ((KeyListener.isKeyActive(GLFW_KEY_A) || KeyListener.isKeyActive(GLFW_KEY_D)) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            setAnimationState(AnimationState.WALKING);
        }
        else
        {
            setAnimationState(AnimationState.IDLE);
        }
    }
}