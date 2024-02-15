package engine.rendering;

import engine.core.GlobalSettings;
import engine.core.Scene;
import org.joml.*;

import java.lang.Math;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ScreenSpaceSprite
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

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;
    public Vector2i locationAnchor;
    public boolean isLocationAnchored;
    public Texture mainTexture;
    public Vector4f mainTextureTint;

    public ScreenSpaceSprite(String mainTexture, Vector4f mainTextureTint, boolean isLocationAnchored)
    {
        this.mainTexture = new Texture(mainTexture, true, false, false);
        this.mainTextureTint = mainTextureTint;
        this.isLocationAnchored = isLocationAnchored;
        initMeshData();
        initValues();
    }

    public ScreenSpaceSprite(Texture mainTexture, Vector4f mainTextureTint, boolean isLocationAnchored)
    {
        this.mainTexture = mainTexture;
        this.mainTextureTint = mainTextureTint;
        this.isLocationAnchored = isLocationAnchored;
        initMeshData();
        initValues();
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

    private void initValues()
    {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        scale = new Vector3f(100.0f, 100.0f, 100.0f);
        locationAnchor = new Vector2i(0, 0);
    }

    public void render()
    {
        Vector3f finalPosition;
        if (isLocationAnchored)
        {
            float halfWidth = (float)GlobalSettings.WINDOW_WIDTH / 2.0f;
            float halfHeight = (float)GlobalSettings.WINDOW_HEIGHT / 2.0f;
            float x = halfWidth * (float)locationAnchor.x;
            float y = halfHeight * (float)locationAnchor.y;
            finalPosition = new Vector3f(x + position.x, y + position.y, position.z);
        }
        else
        {
            finalPosition = position;
        }

        Matrix4f transform = new Matrix4f().identity();
        transform.translate(finalPosition);
        transform.rotateXYZ(rotation);
        transform.scale(scale);

        Scene.screenSpace2dShader.bind();
        mainTexture.bind(0);
        Scene.screenSpace2dShader.updateUniforms(mainTextureTint, transform, Scene.mainCamera.screenSpaceProjection);
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        Scene.screenSpace2dShader.unbind();
    }

    public void translate(float x, float y, float z)
    {
        position.add(x, y, z);
    }

    public void rotate(float x, float y, float z)
    {
        rotation.add((float) Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
    }

    public void scale(float x, float y, float z)
    {
        scale.mul(x, y, z);
    }
}