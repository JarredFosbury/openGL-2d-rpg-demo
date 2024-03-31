package game;

import engine.core.*;
import engine.physics.ColliderAABB;
import engine.physics.Ray;
import engine.rendering.Color;
import engine.rendering.Debug;
import engine.rendering.PointLightSource;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
    private final PointLightSource spiritLight;
    private final ColliderAABB collider;
    private AnimationState animState;

    private float walkSpeed;
    private float runSpeed;
    private float spiritLightAwakeTimer;
    private float verticalVelocity;
    private int inputAxisHorizontal;
    private boolean inputJumpQueued;
    private boolean isRunning;
    private float jumpSpeed;
    private boolean isGrounded;
    private float timeSinceLastAnimationStateChange;

    public PlayerController()
    {
        super("PlayerController", EntityType.ScriptableBehavior, 0);
        idleSprite = new SpriteLitActor("playerIdleSpriteSheet", 0, "breathingIdleAlbedo",
                "breathingIdleNormal", Color.WHITE, new Vector2f(0.0f, 0.93333334f), new Vector2f(0.06666667f));
        idleSprite.initSpriteSheet("res/textures/litSprites/player/breathingIdle/playerBreathingIdle_SheetData.ssd",
                true, false);
        idleSprite.isVisible = false;

        walkingSprite = new SpriteLitActor("playerWalkingSpriteSheet", 0, "walkingAlbedo",
                "walkingNormal", Color.WHITE, new Vector2f(0.0f, 0.83333325f), new Vector2f(0.16666667f));
        walkingSprite.initSpriteSheet("res/textures/litSprites/player/walking/playerWalking_SheetData.ssd",
                true, false);
        walkingSprite.isVisible = false;

        runningSprite = new SpriteLitActor("playerRunningSpriteSheet", 0, "runningAlbedo",
                "runningNormal", Color.WHITE, new Vector2f(0.0f, 0.75f), new Vector2f(0.25f));
        runningSprite.initSpriteSheet("res/textures/litSprites/player/running/playerRunning_SheetData.ssd",
                true, false);
        runningSprite.isVisible = false;

        jumpUpSprite = new SpriteLitActor("playerJumpUpSpriteSheet", 0, "jumpingUpAlbedo",
                "jumpingUpNormal", Color.WHITE, new Vector2f(0.0f, 0.8f), new Vector2f(0.2f));
        jumpUpSprite.initSpriteSheet("res/textures/litSprites/player/jumpingUp/playerJumpingUp_SheetData.ssd",
                false, true);
        jumpUpSprite.isVisible = false;

        jumpDownSprite = new SpriteLitActor("playerJumpDownSpriteSheet", 0, "jumpingDownAlbedo",
                "jumpingDownNormal", Color.WHITE, new Vector2f(0.0f, 0.83333325f), new Vector2f(0.16666667f));
        jumpDownSprite.initSpriteSheet("res/textures/litSprites/player/jumpingDown/playerJumpingDown_SheetData.ssd",
                false, true);
        jumpDownSprite.isVisible = false;

        spiritLight = new PointLightSource("playerSpiritLight", 0, Color.WHITE, 0.0f, 25.0f, 0.0f, 0.0f, 1.5f);
        spiritLight.color = new Vector4f(0.561f, 0.773f, 1.0f, 1.0f);

        collider = new ColliderAABB("playerCollider", 0, Color.DEBUG_DEFAULT_COLOR);
        collider.scale = new Vector3f(0.5f, 1.5f, 1.0f);
        collider.layerMaskIndex = 1;

        sprites = new SpriteLitActor[] {idleSprite, walkingSprite, runningSprite, jumpUpSprite, jumpDownSprite};
        animState = AnimationState.IDLE;
        mainCamera = Scene.mainCamera;
        scale = new Vector3f(2.0f);
        walkSpeed = 1.5f;
        runSpeed = 4.0f;
        spiritLightAwakeTimer = 2.0f;
        verticalVelocity = 0.0f;
        inputAxisHorizontal = 0;
        inputJumpQueued = false;
        isRunning = false;
        jumpSpeed = 5.0f;
        isGrounded = false;
        timeSinceLastAnimationStateChange = 0.0f;
    }

    public void start()
    {
        setAnimationState(AnimationState.IDLE);
    }

    public void update()
    {
        if (!inputJumpQueued && KeyListener.isKeyPressed(GLFW_KEY_SPACE))
            inputJumpQueued = true;

        syncTransforms();
        manageAnimationState();
        updateSpiritLightTimer();
    }

    public void fixedPhysicsUpdate()
    {
        Ray leftGroundTestRay = new Ray(new Vector3f(collider.position).add(-0.24f, 0.0f, 0.0f), new Vector3f(0.0f, -0.75f, 0.0f));
        Ray rightGroundTestRay = new Ray(new Vector3f(collider.position).add(0.24f, 0.0f, 0.0f), new Vector3f(0.0f, -0.75f, 0.0f));
        isGrounded = Scene.physics.rayLayerIntersection(2, leftGroundTestRay)
                || Scene.physics.rayLayerIntersection(2, rightGroundTestRay);
        Debug.drawRay(leftGroundTestRay, isGrounded ? Color.GREEN : Color.RED);
        Debug.drawRay(rightGroundTestRay, isGrounded ? Color.GREEN : Color.RED);

        if (isGrounded)
        {
            if (inputJumpQueued)
            {
                verticalVelocity = jumpSpeed;
                inputJumpQueued = false;
            }
            else
            {
                verticalVelocity = 0.0f;
            }
        }
        else
        {
            verticalVelocity -= 9.81f * Time.fixedPhysicsTimeStep;
        }

        collider.translate(
                inputAxisHorizontal * (isRunning ? runSpeed : walkSpeed) * Time.fixedPhysicsTimeStep,
                verticalVelocity * Time.fixedPhysicsTimeStep,
                0.0f);

        collider.translate(Scene.physics.collideWithLayer(collider, 2));
    }

    private void syncTransforms()
    {
        position = new Vector3f(collider.position).add(0.0f, 0.25f, 0.0f);
        for (SpriteLitActor sprite : sprites)
        {
            sprite.position = position;
            sprite.scale = scale;
        }

        mainCamera.position = position;
        spiritLight.position = new Vector3f(position.x, position.y + 0.1f, position.z + 0.5f);
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

    private void updateSpiritLightTimer()
    {
        if (spiritLightAwakeTimer <= 0.0f)
            return;

        spiritLightAwakeTimer -= Time.deltaTime;
        spiritLight.intensity = (1.0f - (spiritLightAwakeTimer / 2.0f)) * 0.5f;

        if (spiritLight.intensity > 0.5f)
            spiritLight.intensity = 0.5f;
    }

    private void manageAnimationState()
    {
        switch (animState)
        {
            case IDLE -> animationState_IDLE();
            case WALKING -> animationState_WALKING();
            case RUNNING -> animationState_RUNNING();
            case JUMP_UP -> animationState_JUMP_UP();
            case JUMP_DOWN -> animationState_JUMP_DOWN();
        }

        timeSinceLastAnimationStateChange += Time.deltaTime;
    }

    private void setAnimationState(AnimationState state)
    {
        this.animState = state;
        timeSinceLastAnimationStateChange = 0.0f;
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

        inputAxisHorizontal = 0;
        isRunning = false;

        if (KeyListener.isKeyActive(GLFW_KEY_D))
            setAnimationState(AnimationState.WALKING);

        if (KeyListener.isKeyActive(GLFW_KEY_A))
            setAnimationState(AnimationState.WALKING);

        if (!isGrounded)
        {
            setAnimationState(AnimationState.JUMP_UP);
            jumpUpSprite.playAnimation();
        }
    }

    private void animationState_WALKING()
    {
        if (!walkingSprite.getPlayingState())
            walkingSprite.playAnimation();

       if (KeyListener.isKeyActive(GLFW_KEY_D) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           setHorizontalDirection(1.0f);
           inputAxisHorizontal = 1;
           isRunning = false;
       }
       else if (KeyListener.isKeyActive(GLFW_KEY_A) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           setHorizontalDirection(-1.0f);
           inputAxisHorizontal = -1;
           isRunning = false;
       }
       else if ((KeyListener.isKeyActive(GLFW_KEY_A) || KeyListener.isKeyActive(GLFW_KEY_D)) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           setAnimationState(AnimationState.RUNNING);
       }
       else
       {
           setAnimationState(AnimationState.IDLE);
       }

        if (!isGrounded)
        {
            setAnimationState(AnimationState.JUMP_UP);
            jumpUpSprite.playAnimation();
        }
    }

    private void animationState_RUNNING()
    {
        if (!runningSprite.getPlayingState())
            runningSprite.playAnimation();

        if (KeyListener.isKeyActive(GLFW_KEY_D) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            setHorizontalDirection(1.0f);
            inputAxisHorizontal = 1;
            isRunning = true;
        }
        else if (KeyListener.isKeyActive(GLFW_KEY_A) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            setHorizontalDirection(-1.0f);
            inputAxisHorizontal = -1;
            isRunning = true;
        }
        else if ((KeyListener.isKeyActive(GLFW_KEY_A) || KeyListener.isKeyActive(GLFW_KEY_D)) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            setAnimationState(AnimationState.WALKING);
        }
        else
        {
            setAnimationState(AnimationState.IDLE);
        }

        if (!isGrounded)
        {
            setAnimationState(AnimationState.JUMP_UP);
            jumpUpSprite.playAnimation();
        }
    }

    private void animationState_JUMP_UP()
    {
        if (isGrounded)
        {
            jumpDownSprite.playAnimation();
            setAnimationState(AnimationState.JUMP_DOWN);
        }
    }

    private void animationState_JUMP_DOWN()
    {
        if (timeSinceLastAnimationStateChange > 0.8f)
            setAnimationState(AnimationState.IDLE);
    }
}