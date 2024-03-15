package game;

import engine.core.*;
import engine.rendering.Color;
import engine.rendering.SpriteLit;
import engine.rendering.Texture;
import org.joml.Vector2f;
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
    private final SpriteLit idleSprite;
    private final SpriteLit walkingSprite;
    private final SpriteLit runningSprite;
    private final SpriteLit jumpUpSprite;
    private final SpriteLit jumpDownSprite;
    private final SpriteLit[] sprites;
    private AnimationState animState;

    public PlayerController()
    {
        super("PlayerController", EntityType.ScriptableBehavior, 0);

        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/breathingIdle/playerBreathingIdle_alb.png",
                true, true, true), "breathingIdleAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/breathingIdle/playerBreathingIdle_nrm.png",
                true, true, true), "breathingIdleNormal");

        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/jumpingDown/playerJumpingDown_alb.png",
                true, true, true), "jumpingDownAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/jumpingDown/playerJumpingDown_nrm.png",
                true, true, true), "jumpingDownNormal");

        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/jumpingUp/playerJumpingUp_alb.png",
                true, true, true), "jumpingUpAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/jumpingUp/playerJumpingUp_nrm.png",
                true, true, true), "jumpingUpNormal");

        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/running/playerRunning_alb.png",
                true, true, true), "runningAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/running/playerRunning_nrm.png",
                true, true, true), "runningNormal");

        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/walking/playerWalking_alb.png",
                true, true, true), "walkingAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/walking/playerWalking_nrm.png",
                true, true, true), "walkingNormal");

        idleSprite = new SpriteLit("playerIdleSpriteSheet", 0, "breathingIdleAlbedo",
                "breathingIdleNormal", Color.WHITE, new Vector2f(0.0f, 0.93333334f), new Vector2f(0.06666667f));
        idleSprite.initSpriteSheet("res/textures/litSprites/player/breathingIdle/playerBreathingIdle_SheetData.ssd", true);

        walkingSprite = new SpriteLit("playerWalkingSpriteSheet", 0, "walkingAlbedo",
                "walkingNormal", Color.WHITE, new Vector2f(0.0f, 0.83333325f), new Vector2f(0.16666667f));
        walkingSprite.initSpriteSheet("res/textures/litSprites/player/walking/playerWalking_SheetData.ssd", true);

        runningSprite = new SpriteLit("playerRunningSpriteSheet", 0, "runningAlbedo",
                "runningNormal", Color.WHITE, new Vector2f(0.0f, 0.75f), new Vector2f(0.25f));
        runningSprite.initSpriteSheet("res/textures/litSprites/player/running/playerRunning_SheetData.ssd", true);

        jumpUpSprite = new SpriteLit("playerJumpUpSpriteSheet", 0, "jumpingUpAlbedo",
                "jumpingUpNormal", Color.WHITE, new Vector2f(0.0f, 0.8f), new Vector2f(0.2f));
        jumpUpSprite.initSpriteSheet("res/textures/litSprites/player/jumpingUp/playerJumpingUp_SheetData.ssd", false);

        jumpDownSprite = new SpriteLit("playerJumpDownSpriteSheet", 0, "jumpingDownAlbedo",
                "jumpingDownNormal", Color.WHITE, new Vector2f(0.0f, 0.83333325f), new Vector2f(0.16666667f));
        jumpDownSprite.initSpriteSheet("res/textures/litSprites/player/jumpingDown/playerJumpingDown_SheetData.ssd", false);

        sprites = new SpriteLit[] {idleSprite, walkingSprite, runningSprite, jumpUpSprite, jumpDownSprite};
        animState = AnimationState.IDLE;
        mainCamera = Scene.mainCamera;
    }

    public void update()
    {
        syncSpriteSheetTransforms();
        manageAnimationState();

        mainCamera.position = position;
    }

    public void fixedPhysicsUpdate()
    {}

    public void render()
    {}

    private void syncSpriteSheetTransforms()
    {
        for (SpriteLit sprite : sprites)
        {
            sprite.position = position;
            sprite.scale = scale;
        }
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
            scale = new Vector3f(1.0f);
        }

        if (KeyListener.isKeyActive(GLFW_KEY_A))
        {
            setAnimationState(AnimationState.WALKING);
            scale = new Vector3f(-1.0f, 1.0f, 1.0f);
        }
    }

    private void animationState_WALKING()
    {
        if (!walkingSprite.getPlayingState())
            walkingSprite.playAnimation();

       if (KeyListener.isKeyActive(GLFW_KEY_D) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           translate(2.0f * Time.deltaTime, 0.0f, 0.0f);
           scale = new Vector3f(1.0f);
       }
       else if (KeyListener.isKeyActive(GLFW_KEY_A) && !KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
       {
           translate(-2.0f * Time.deltaTime, 0.0f, 0.0f);
           scale = new Vector3f(-1.0f, 1.0f, 1.0f);
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
            translate(5.0f * Time.deltaTime, 0.0f, 0.0f);
            scale = new Vector3f(1.0f);
        }
        else if (KeyListener.isKeyActive(GLFW_KEY_A) && KeyListener.isKeyActive(GLFW_KEY_LEFT_SHIFT))
        {
            translate(-5.0f * Time.deltaTime, 0.0f, 0.0f);
            scale = new Vector3f(-1.0f, 1.0f, 1.0f);
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