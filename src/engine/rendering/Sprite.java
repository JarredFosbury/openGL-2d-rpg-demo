package engine.rendering;

import engine.core.Entity;
import engine.core.EntityType;
import engine.core.Scene;
import engine.core.Time;
import engine.shaders.Standard2dShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Sprite extends Entity
{
    private final float[] vertexData = {
            // positions            // texture coordinates
            0.5f,  0.5f, 0.0f,      1.0f, 1.0f,     // top right
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f,     // bottom right
            -0.5f, -0.5f, 0.0f,     0.0f, 0.0f,     // bottom left
            -0.5f,  0.5f, 0.0f,     0.0f, 1.0f      // top left
    };

    private final int[] indices = {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    private int vboID, vaoID, eboID;

    public Texture mainTexture;
    public Vector4f mainTextureTint;
    public Vector2f mainTextureOffset;
    public Vector2f mainTextureScale;
    public int spriteSheetFrame;
    public int animationFrameRate;

    private final Standard2dShader shader;
    private float timeSinceLastFrame;
    private Vector2f[] spriteSheetFrameOffsets;

    public Sprite(String name, int HIERARCHY_INDEX, String textureAssetKey, Vector4f mainTextureTint, Vector2f mainTextureOffset, Vector2f mainTextureScale)
    {
        super(name, EntityType.Sprite, HIERARCHY_INDEX);
        this.shader = Scene.standard2dShader;
        this.mainTexture = (Texture) Scene.assets.getAssetFromPool(textureAssetKey);
        this.mainTextureTint = mainTextureTint;
        this.mainTextureOffset = mainTextureOffset;
        this.mainTextureScale = mainTextureScale;
        initMeshData();
        initVariables();
    }

    private void initMeshData()
    {
        vboID = glGenBuffers();
        vaoID = glGenVertexArrays();
        eboID = glGenBuffers();

        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    private void initVariables()
    {
        spriteSheetFrame = 0;
        animationFrameRate = 24;
        timeSinceLastFrame = 0.0f;
    }

    public void initSpriteSheet(String frameOffsetDataFilepath)
    {
        this.spriteSheetFrameOffsets = SpriteSheetDataLoader.loadSheetDataFromPath(frameOffsetDataFilepath);
    }

    public void render()
    {
        if (!isVisible)
            return;

        Matrix4f transform = new Matrix4f().identity();
        transform.translate(position);
        transform.rotateXYZ(rotation);
        transform.scale(scale);

        shader.bind();
        mainTexture.bind(0);
        shader.updateUniforms(mainTextureTint, transform, Scene.mainCamera.projection, Scene.mainCamera.getTransformation(),
                mainTextureOffset, mainTextureScale);
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        shader.unbind();
    }

    public void animateSprite()
    {
        timeSinceLastFrame += Time.deltaTime;
        if (timeSinceLastFrame >= 1.0f / (float) animationFrameRate)
        {
            timeSinceLastFrame -= 1.0f / (float) animationFrameRate;
            nextSpriteSheetFrame();
        }
    }

    public void nextSpriteSheetFrame()
    {
        if (spriteSheetFrameOffsets == null)
        {
            System.err.println("ERROR: CANNOT ANIMATE SPRITE WITHOUT SHEET FRAME DATA!\n" +
                    "No sprite sheet data has been provided");
            return;
        }

        spriteSheetFrame ++;
        if (spriteSheetFrame >= spriteSheetFrameOffsets.length)
            spriteSheetFrame = 0;

        mainTextureOffset = spriteSheetFrameOffsets[spriteSheetFrame];
    }
}