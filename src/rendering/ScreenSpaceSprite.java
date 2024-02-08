package rendering;

import core.GlobalSettings;
import org.joml.*;
import shaders.ScreenSpace2dShader;

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

    private final int vboID, vaoID, eboID;
    private final ScreenSpace2dShader screenSpace2d_shader;
    private Texture mainTexture;
    private Matrix4f screenSpaceProjection;

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;
    public Vector2i locationAnchor;
    public boolean isLocationAnchored;

    public ScreenSpaceSprite(String mainTexture)
    {
        this.mainTexture = new Texture(mainTexture, true);
        screenSpace2d_shader = new ScreenSpace2dShader("./res/shaders/ScreenSpace2D.glsl");
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

        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        scale = new Vector3f(100.0f, 100.0f, 100.0f);
        locationAnchor = new Vector2i(0, 0);
        isLocationAnchored = false;

        // TODO: transfer this to the active scene camera
        float halfWidth = (float)GlobalSettings.WINDOW_WIDTH / 2.0f;
        float halfHeight = (float)GlobalSettings.WINDOW_HEIGHT / 2.0f;
        screenSpaceProjection = new Matrix4f().ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, -1.0f, 1.0f);
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

        screenSpace2d_shader.bind();
        mainTexture.bind(0);
        screenSpace2d_shader.updateUniforms(new Vector4f(0.812f, 0.176f, 0.22f, 1.0f), transform, screenSpaceProjection);
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        screenSpace2d_shader.unbind();
    }

    public void translate(float x, float y, float z)
    {
        position.add(x, y, z);
    }

    public void rotate(float x, float y, float z)
    {
        rotation.add(x, y, z);
    }

    public void scale(float x, float y, float z)
    {
        scale.mul(x, y, z);
    }
}