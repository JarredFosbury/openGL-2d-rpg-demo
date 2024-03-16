package game;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.rendering.Color;
import engine.rendering.SpriteLit;
import engine.rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PlayerStartingAnimationController extends Entity
{
    private final SpriteLit animationSheet;
    private boolean triggered;

    public PlayerStartingAnimationController()
    {
        super("startingAnimationController", EntityType.ScriptableBehavior, 0);
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/standUp/playerStandingUp_alb.png",
                true, true, true), "standingUpAlbedo");
        Scene.assets.addAssetToPool(new Texture("res/textures/litSprites/player/standUp/playerStandingUp_nrm.png",
                true, true, true), "standingUpNormal");
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

        animationSheet = new SpriteLit("playerStandingSpriteSheet", 0, "standingUpAlbedo",
                "standingUpNormal", Color.WHITE, new Vector2f(0.0f, 0.923077f), new Vector2f(0.07692308f));
        animationSheet.initSpriteSheet("res/textures/litSprites/player/standUp/playerStandingUp_SheetData.ssd", false, true);
        animationSheet.scale = new Vector3f(2.0f);
        triggered = false;
        Scene.mainCamera.updateViewport(4.0f, -1.0f, 1.0f);
    }

    public void update()
    {
        if (!triggered && !animationSheet.getPlayingState())
        {
            triggered = true;
            animationSheet.playAnimation();
        }

        if (triggered && !animationSheet.getPlayingState())
        {
            animationSheet.isVisible = false;
            Scene.entities.remove(this);
            Scene.entities.remove(animationSheet);
            Scene.assets.releaseTextureFromPool("standingUpAlbedo");
            Scene.assets.releaseTextureFromPool("standingUpNormal");
            new PlayerController();
        }
    }
}