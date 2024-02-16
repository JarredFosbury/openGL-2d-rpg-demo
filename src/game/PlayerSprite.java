package game;

import engine.core.KeyListener;
import engine.core.Scene;
import engine.core.Time;
import engine.physics.AxisAlignedBoundingBox;
import engine.rendering.Sprite;
import engine.rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class PlayerSprite
{
    public Texture linkSpriteSheet;
    public Sprite playerIdle;
    public Sprite playerMoveUp;
    public Sprite playerMoveDown;
    public Sprite playerMoveLeft;
    public Sprite playerMoveRight;

    public Vector3f playerPosition;
    public int inputDirection;
    public int lastInputDirection;
    public float playerMovementSpeed;
    public AxisAlignedBoundingBox playerCollider;

    public PlayerSprite()
    {
        linkSpriteSheet = new Texture("res/textures/linkSheet.png", true, false, true);
        playerIdle = new Sprite(linkSpriteSheet, Scene.lightColor, new Vector2f(0.0f, 0.875f), new Vector2f(0.1f, 0.125f));

        playerMoveUp = new Sprite(linkSpriteSheet, Scene.lightColor, new Vector2f(0.0f, 0.125f), new Vector2f(0.1f, 0.125f));
        playerMoveUp.initSpriteSheet("res/spriteSheetData/linkSheetWalkUpData.ssd");
        playerMoveUp.animationFrameRate = 16;

        playerMoveDown = new Sprite(linkSpriteSheet, Scene.lightColor, new Vector2f(0.0f, 0.125f), new Vector2f(0.1f, 0.125f));
        playerMoveDown.initSpriteSheet("res/spriteSheetData/linkSheetWalkDownData.ssd");
        playerMoveDown.animationFrameRate = 16;

        playerMoveLeft = new Sprite(linkSpriteSheet, Scene.lightColor, new Vector2f(0.0f, 0.125f), new Vector2f(0.1f, 0.125f));
        playerMoveLeft.initSpriteSheet("res/spriteSheetData/linkSheetWalkLeftData.ssd");
        playerMoveLeft.animationFrameRate = 16;

        playerMoveRight = new Sprite(linkSpriteSheet, Scene.lightColor, new Vector2f(0.0f, 0.125f), new Vector2f(0.1f, 0.125f));
        playerMoveRight.initSpriteSheet("res/spriteSheetData/linkSheetWalkRightData.ssd");
        playerMoveRight.animationFrameRate = 16;

        playerPosition = new Vector3f(0.0f);
        inputDirection = 0;
        lastInputDirection = 0;
        playerMovementSpeed = 3.5f;
        playerCollider = new AxisAlignedBoundingBox();
    }

    public void updateColor(Vector4f color)
    {
        playerIdle.mainTextureTint = color;
        playerMoveUp.mainTextureTint = color;
        playerMoveDown.mainTextureTint = color;
        playerMoveLeft.mainTextureTint = color;
        playerMoveRight.mainTextureTint = color;
    }

    public void pollInput()
    {
        if (KeyListener.isKeyActive(GLFW_KEY_W))
        {
            inputDirection = 1;
            lastInputDirection = 1;

            playerIdle.visible = false;
            playerMoveUp.visible = true;
            playerMoveDown.visible = false;
            playerMoveLeft.visible = false;
            playerMoveRight.visible = false;
        }
        else if (KeyListener.isKeyActive(GLFW_KEY_A))
        {
            inputDirection = 2;
            lastInputDirection = 2;

            playerIdle.visible = false;
            playerMoveUp.visible = false;
            playerMoveDown.visible = false;
            playerMoveLeft.visible = true;
            playerMoveRight.visible = false;
        }
        else if (KeyListener.isKeyActive(GLFW_KEY_S))
        {
            inputDirection = 3;
            lastInputDirection = 3;

            playerIdle.visible = false;
            playerMoveUp.visible = false;
            playerMoveDown.visible = true;
            playerMoveLeft.visible = false;
            playerMoveRight.visible = false;
        }
        else if (KeyListener.isKeyActive(GLFW_KEY_D))
        {
            inputDirection = 4;
            lastInputDirection = 4;

            playerIdle.visible = false;
            playerMoveUp.visible = false;
            playerMoveDown.visible = false;
            playerMoveLeft.visible = false;
            playerMoveRight.visible = true;
        }
        else
        {
            inputDirection = 0;

            playerIdle.visible = true;
            playerMoveUp.visible = false;
            playerMoveDown.visible = false;
            playerMoveLeft.visible = false;
            playerMoveRight.visible = false;
        }
    }

    public void update()
    {
        if (lastInputDirection == 1)
            playerIdle.mainTextureOffset = new Vector2f(0.0f, 0.625f);
        else if (lastInputDirection == 2)
            playerIdle.mainTextureOffset = new Vector2f(0.0f, 0.75f);
        else if (lastInputDirection == 3)
            playerIdle.mainTextureOffset = new Vector2f(0.0f, 0.875f);
        else if (lastInputDirection == 4)
            playerIdle.mainTextureOffset = new Vector2f(0.0f, 0.5f);

        playerMoveUp.animateSprite();
        playerMoveDown.animateSprite();
        playerMoveLeft.animateSprite();
        playerMoveRight.animateSprite();
    }

    public void fixedPhysicsUpdate()
    {
        Vector2f move = new Vector2f(0.0f);
        if (inputDirection == 1)
            move.y = 1.0f * playerMovementSpeed * Time.fixedPhysicsTimeStep;
        else if (inputDirection == 2)
            move.x = -1.0f * playerMovementSpeed * Time.fixedPhysicsTimeStep;
        else if (inputDirection == 3)
            move.y = -1.0f * playerMovementSpeed * Time.fixedPhysicsTimeStep;
        else if (inputDirection == 4)
            move.x = 1.0f * playerMovementSpeed * Time.fixedPhysicsTimeStep;

        playerPosition.add(new Vector3f(move, 0.0f));
        playerCollider.position = playerPosition;
        playerIdle.position = playerPosition;
        playerMoveUp.position = playerPosition;
        playerMoveDown.position = playerPosition;
        playerMoveLeft.position = playerPosition;
        playerMoveRight.position = playerPosition;
    }

    public void render()
    {
        playerIdle.render();
        playerMoveUp.render();
        playerMoveDown.render();
        playerMoveLeft.render();
        playerMoveRight.render();

        //playerCollider.renderColliderSprite();
    }
}